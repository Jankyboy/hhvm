/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#include <folly/detail/ThreadLocalDetail.h>

#include <algorithm>
#include <list>
#include <mutex>
#include <random>

#include <folly/ConstexprMath.h>
#include <folly/Utility.h>
#include <folly/detail/thread_local_globals.h>
#include <folly/lang/Hint.h>
#include <folly/memory/SanitizeLeak.h>
#include <folly/synchronization/CallOnce.h>

constexpr auto kSmallGrowthFactor = 1.1;
constexpr auto kBigGrowthFactor = 1.7;

namespace folly {
namespace threadlocal_detail {

struct rand_engine {
  using result_type = unsigned int;
  result_type operator()() { return to_unsigned(std::rand()); }
  static constexpr result_type min() { return 0; }
  static constexpr result_type max() { return RAND_MAX; }
};

SharedPtrDeleter::SharedPtrDeleter(std::shared_ptr<void> const& ts) noexcept
    : ts_{ts} {}
SharedPtrDeleter::SharedPtrDeleter(SharedPtrDeleter const& that) noexcept
    : ts_{that.ts_} {}
SharedPtrDeleter::~SharedPtrDeleter() = default;
void SharedPtrDeleter::operator()(
    void* /* ptr */, folly::TLPDestructionMode) const {
  ts_.reset();
}

uintptr_t ElementDisposeInfo::castForgetAlign(DeleterFunType* f) noexcept {
  auto const p = reinterpret_cast<char const*>(f);
  auto const q = std::launder(p);
  return reinterpret_cast<uintptr_t>(q);
}

bool ThreadEntrySet::basicSanity() const {
  if constexpr (!kIsDebug) {
    return true;
  }
  if (threadElements.empty() && entryToVectorSlot.empty()) {
    return true;
  }
  if (threadElements.size() != entryToVectorSlot.size()) {
    return false;
  }
  auto const size = threadElements.size();
  rand_engine rng;
  std::uniform_int_distribution<size_t> dist{0, size - 1};
  if (dist(rng) < constexpr_log2(size)) {
    return true;
  }
  return //
      threadElements.size() == entryToVectorSlot.size() &&
      std::all_of(
          entryToVectorSlot.begin(),
          entryToVectorSlot.end(),
          [&](auto const& kvp) {
            return kvp.second < threadElements.size() &&
                threadElements[kvp.second].threadEntry == kvp.first;
          });
}

void ThreadEntrySet::compress() {
  assert(compressible());
  // compress the vector
  threadElements.shrink_to_fit();
  // compress the index
  EntryIndex newIndex;
  newIndex.reserve(entryToVectorSlot.size());
  while (!entryToVectorSlot.empty()) {
    newIndex.insert(entryToVectorSlot.extract(entryToVectorSlot.begin()));
  }
  entryToVectorSlot = std::move(newIndex);
}

StaticMetaBase::StaticMetaBase(ThreadEntry* (*threadEntry)(), bool strict)
    : nextId_(1), threadEntry_(threadEntry), strict_(strict) {
  int ret = pthread_key_create(&pthreadKey_, &onThreadExit);
  checkPosixError(ret, "pthread_key_create failed");
  PthreadKeyUnregister::registerKey(pthreadKey_);
}

ThreadEntryList* StaticMetaBase::getThreadEntryList() {
  class PthreadKey {
   public:
    static void onThreadExit(void* ptr) {
      ThreadEntryList* list = static_cast<ThreadEntryList*>(ptr);
      StaticMetaBase::cleanupThreadEntriesAndList(list);
    }

    PthreadKey() {
      int ret = pthread_key_create(&pthreadKey_, &onThreadExit);
      checkPosixError(ret, "pthread_key_create failed");
      PthreadKeyUnregister::registerKey(pthreadKey_);
    }

    FOLLY_ALWAYS_INLINE pthread_key_t get() const { return pthreadKey_; }

   private:
    pthread_key_t pthreadKey_;
  };

  auto& instance = detail::createGlobal<PthreadKey, void>();

  ThreadEntryList* threadEntryList =
      static_cast<ThreadEntryList*>(pthread_getspecific(instance.get()));

  if (FOLLY_UNLIKELY(!threadEntryList)) {
    auto uptr = std::make_unique<ThreadEntryList>();
    int ret = pthread_setspecific(instance.get(), uptr.get());
    checkPosixError(ret, "pthread_setspecific failed");
    threadEntryList = uptr.release();
    threadEntryList->count = 1; // Pin once for own onThreadExit callback.
    lsan_ignore_object(threadEntryList);
  }

  return threadEntryList;
}

bool StaticMetaBase::dying() {
  return folly::detail::thread_is_dying();
}

void StaticMetaBase::onThreadExit(void* ptr) {
  folly::detail::thread_is_dying_mark();
  auto threadEntry = static_cast<ThreadEntry*>(ptr);

  {
    auto& meta = *threadEntry->meta;

    // Make sure this ThreadEntry is available if ThreadLocal A is accessed in
    // ThreadLocal B destructor.
    pthread_setspecific(meta.pthreadKey_, threadEntry);

    auto elementsCapacity = threadEntry->getElementsCapacity();
    auto beforeCount = meta.totalElementWrappers_.fetch_sub(elementsCapacity);
    DCHECK_GE(beforeCount, elementsCapacity);
    for (bool shouldRun = true; shouldRun;) {
      std::shared_lock forkRlock(meta.forkHandlerLock_);
      std::shared_lock rlock(meta.accessAllThreadsLock_, std::defer_lock);
      if (meta.strict_) {
        rlock.lock();
      }
      // NOTE: User-provided deleter / object dtor itself may be using
      // ThreadLocal with the same Tag, so dispose() calls below may (re)create
      // some of the elements or even increase elementsCapacity, thus multiple
      // cleanup rounds may be required.
      std::vector<ElementDisposeInfo> elements;
      shouldRun = false;
      meta.removeThreadEntryAndCollectDisposeInfos(threadEntry, elements);
      forkRlock.unlock();
      for (auto& e : elements) {
        if (e.dispose(TLPDestructionMode::THIS_THREAD)) {
          e.cleanup();
          shouldRun = true;
        }
      }
      DCHECK(
          shouldRun ||
          meta.isThreadEntryRemovedFromAllInMap(threadEntry, !meta.strict_));
    }
    pthread_setspecific(meta.pthreadKey_, nullptr);
  }

  auto threadEntryList = threadEntry->list;
  DCHECK_GT(threadEntryList->count, 0u);

  cleanupThreadEntriesAndList(threadEntryList);
}

/* static */
void StaticMetaBase::cleanupThreadEntriesAndList(
    ThreadEntryList* threadEntryList) {
  --threadEntryList->count;
  if (threadEntryList->count) {
    return;
  }

  // dispose all the elements
  for (bool shouldRunOuter = true; shouldRunOuter;) {
    shouldRunOuter = false;
    auto tmp = threadEntryList->head;
    while (tmp) {
      auto& meta = *tmp->meta;
      pthread_setspecific(meta.pthreadKey_, tmp);
      for (bool shouldRunInner = true; shouldRunInner;) {
        std::shared_lock forkRlock(meta.forkHandlerLock_);
        std::shared_lock rlock(meta.accessAllThreadsLock_, std::defer_lock);
        if (meta.strict_) {
          rlock.lock();
        }
        shouldRunInner = false;
        std::vector<ElementDisposeInfo> elements;
        meta.removeThreadEntryAndCollectDisposeInfos(tmp, elements);
        forkRlock.unlock();
        for (auto& e : elements) {
          if (e.dispose(TLPDestructionMode::THIS_THREAD)) {
            e.cleanup();
            shouldRunInner = true;
            shouldRunOuter = true;
          }
        }
      }
      pthread_setspecific(meta.pthreadKey_, nullptr);
      tmp = tmp->listNext;
    }
  }

  // free the entry list
  auto head = threadEntryList->head;
  threadEntryList->head = nullptr;
  while (head) {
    auto tmp = head;
    head = head->listNext;
    if (tmp->elements) {
      free(tmp->elements);
      tmp->elements = nullptr;
      tmp->setElementsCapacity(0);
    }

    // Fail safe check to make sure that the ThreadEntry is not present
    // before issuing a delete.
    DCHECK(tmp->meta->isThreadEntryRemovedFromAllInMap(tmp, true));

    delete tmp;
  }

  delete threadEntryList;
}

uint32_t StaticMetaBase::elementsCapacity() const {
  ThreadEntry* threadEntry = (*threadEntry_)();

  return FOLLY_LIKELY(!!threadEntry) ? threadEntry->getElementsCapacity() : 0;
}

uint32_t StaticMetaBase::allocate(EntryID* ent) {
  uint32_t id;
  auto& meta = *this;
  std::lock_guard g(meta.lock_);

  id = ent->value.load(std::memory_order_relaxed);

  if (id == kEntryIDInvalid) {
    if (!meta.freeIds_.empty()) {
      id = meta.freeIds_.back();
      meta.freeIds_.pop_back();
    } else {
      id = meta.nextId_++;
    }
    uint32_t old_id = ent->value.exchange(id, std::memory_order_release);
    DCHECK_EQ(old_id, kEntryIDInvalid);
  }
  return id;
}

ThreadEntrySet StaticMetaBase::popThreadEntrySetAndClearElementPtrs(
    uint32_t id) {
  // Lock the ThreadEntrySet for id so that no other thread can update
  // its local ptr or alter the its elements array or ThreadEntry object
  // itself, before this function is done updating them.
  auto wlocked = allId2ThreadEntrySets_[id].wlock();
  ThreadEntrySet tmp;
  std::swap(*wlocked, tmp);
  std::lock_guard g(lock_);
  for (auto& e : tmp.threadElements) {
    auto elementsCapacity = e.threadEntry->getElementsCapacity();
    if (id < elementsCapacity) {
      /*
       * Writing another thread's ThreadEntry from here is fine;
       * The TL object is being destroyed, so get(id), or reset()
       * or accessAllThreads calls on it are illegal. Only other
       * racing accesses would be from the owner thread itself
       * either a) reallocating the elements array (guarded by lock_, so safe)
       * or b) exiting and trying to clear the elements array or free the
       * elements and ThreadEntry itself. The ThreadEntrySet lock synchronizes
       * this part as the exiting thread will acquire it to remove itself from
       * the set.
       */
      e.threadEntry->elements[id].ptr = nullptr;
    }
    // Destroy should not access thread entry after this call as racing
    // exit call can make it invalid.
    e.threadEntry = nullptr;
  }
  return tmp;
}

void StaticMetaBase::destroy(EntryID* ent) {
  try {
    auto& meta = *this;

    // Elements in other threads that use this id.
    ThreadEntrySet tmpEntrySet;

    {
      std::shared_lock forkRlock(meta.forkHandlerLock_);
      std::unique_lock wlock(meta.accessAllThreadsLock_, std::defer_lock);
      if (meta.strict_) {
        /*
         * In strict mode, the logic guarantees per-thread instances are
         * destroyed by the moment ThreadLocal<> dtor returns.
         * In order to achieve that, we should wait until concurrent
         * onThreadExit() calls (that might acquire ownership over per-thread
         * instances in order to destroy them) are finished.
         */
        wlock.lock();
      }

      uint32_t id =
          ent->value.exchange(kEntryIDInvalid, std::memory_order_acquire);
      if (id == kEntryIDInvalid) {
        return;
      }
      tmpEntrySet = meta.popThreadEntrySetAndClearElementPtrs(id);
      forkRlock.unlock();

      {
        std::lock_guard g(meta.lock_);
        meta.freeIds_.push_back(id);
      }
    }
    // Delete elements outside the locks.
    for (auto& e : tmpEntrySet.threadElements) {
      if (e.wrapper.dispose(TLPDestructionMode::ALL_THREADS)) {
        e.wrapper.cleanup();
      }
    }
  } catch (...) { // Just in case we get a lock error or something anyway...
    LOG(WARNING) << "Destructor discarding an exception that was thrown.";
  }
}

ElementWrapper* StaticMetaBase::reallocate(
    ThreadEntry* threadEntry, uint32_t idval, size_t& newCapacity) {
  size_t prevCapacity = threadEntry->getElementsCapacity();

  // Growth factor < 2, see folly/docs/FBVector.md; + 5 to prevent
  // very slow start.
  auto smallCapacity = static_cast<size_t>((idval + 5) * kSmallGrowthFactor);
  auto bigCapacity = static_cast<size_t>((idval + 5) * kBigGrowthFactor);

  newCapacity =
      (threadEntry->meta && (bigCapacity <= threadEntry->meta->nextId_))
      ? bigCapacity
      : smallCapacity;

  assert(newCapacity > prevCapacity);
  ElementWrapper* reallocated = nullptr;

  // Need to grow. Note that we can't call realloc, as elements is
  // still linked in meta, so another thread might access invalid memory
  // after realloc succeeds. We'll copy by hand and update our ThreadEntry
  // under the lock.
  if (usingJEMalloc()) {
    bool success = false;
    size_t newByteSize = nallocx(newCapacity * sizeof(ElementWrapper), 0);

    // Try to grow in place.
    //
    // Note that xallocx(MALLOCX_ZERO) will only zero newly allocated memory,
    // even if a previous allocation allocated more than we requested.
    // This is fine; we always use MALLOCX_ZERO with jemalloc and we
    // always expand our allocation to the real size.
    if (prevCapacity * sizeof(ElementWrapper) >= jemallocMinInPlaceExpandable) {
      success =
          (xallocx(threadEntry->elements, newByteSize, 0, MALLOCX_ZERO) ==
           newByteSize);
    }

    // In-place growth failed.
    if (!success) {
      success =
          ((reallocated = static_cast<ElementWrapper*>(
                mallocx(newByteSize, MALLOCX_ZERO))) != nullptr);
    }

    if (success) {
      // Expand to real size
      assert(newByteSize / sizeof(ElementWrapper) >= newCapacity);
      newCapacity = newByteSize / sizeof(ElementWrapper);
    } else {
      throw_exception<std::bad_alloc>();
    }
  } else { // no jemalloc
    // calloc() is simpler than malloc() followed by memset(), and
    // potentially faster when dealing with a lot of memory, as it can get
    // already-zeroed pages from the kernel.
    reallocated = static_cast<ElementWrapper*>(
        calloc(newCapacity, sizeof(ElementWrapper)));
    if (!reallocated) {
      throw_exception<std::bad_alloc>();
    }

    // When the main thread exits, it will call functions registered with
    // 'atexit' and then call 'exit()'. However, It will NOT call any functions
    // registered via the 'TLS' feature of pthread_key_create.
    // Reference:
    // https://pubs.opengroup.org/onlinepubs/9699919799/functions/pthread_create.html
    folly::lsan_ignore_object(reallocated);
  }
  return reallocated;
}

/**
 * Reserve enough space in the ThreadEntry::elements for the item
 * @id to fit in.
 */

void StaticMetaBase::reserve(EntryID* id) {
  auto& meta = *this;
  ThreadEntry* threadEntry = (*threadEntry_)();
  size_t prevCapacity = threadEntry->getElementsCapacity();

  uint32_t idval = id->getOrAllocate(meta);
  if (prevCapacity > idval) {
    return;
  }

  size_t newCapacity;
  ElementWrapper* reallocated = reallocate(threadEntry, idval, newCapacity);

  // Success, update the entry
  {
    std::lock_guard g(meta.lock_);

    if (reallocated) {
      /*
       * Note: we need to hold the meta lock when copying data out of
       * the old vector, because some other thread might be
       * destructing a ThreadLocal and writing to the elements vector
       * of this thread.
       */
      if (prevCapacity != 0) {
        memcpy(
            reallocated,
            threadEntry->elements,
            sizeof(*reallocated) * prevCapacity);
      }
      std::swap(reallocated, threadEntry->elements);
    }

    threadEntry->setElementsCapacity(newCapacity);
  }

  meta.totalElementWrappers_ += (newCapacity - prevCapacity);
  free(reallocated);
}

FOLLY_NOINLINE void StaticMetaBase::ensureThreadEntryIsInSet(
    ThreadEntry* te,
    SynchronizedThreadEntrySet& set,
    SynchronizedThreadEntrySet::RLockedPtr& rlock) {
  rlock.unlock();
  auto wlock = set.wlock();
  wlock->insert(te);
  rlock = wlock.moveFromWriteToRead();
}

/*
 * release the element @id.
 */
void* ThreadEntry::releaseElement(uint32_t id) {
  auto rlocked = meta->allId2ThreadEntrySets_[id].rlock();
  auto slot = rlocked->getIndexFor(this);
  if (slot < 0) {
    return nullptr;
  }
  auto ptr = rlocked.asNonConstUnsafe().threadElements[slot].wrapper.release();
  DCHECK_EQ(ptr, elements[id].ptr);
  elements[id].ptr = nullptr;
  return ptr;
}

FOLLY_STATIC_CTOR_PRIORITY_MAX
PthreadKeyUnregister PthreadKeyUnregister::instance_;
#if defined(__GLIBC__)
// Invoking thread_local dtor register early to fix issue
// https://github.com/facebook/folly/issues/1252
struct GlibcThreadLocalInit {
  struct GlibcThreadLocalInitHelper {
    FOLLY_NOINLINE ~GlibcThreadLocalInitHelper() {
      compiler_must_not_elide(this);
    }
  };
  GlibcThreadLocalInit() {
    static thread_local GlibcThreadLocalInitHelper glibcThreadLocalInit;
    compiler_must_not_elide(glibcThreadLocalInit);
  }
};
__attribute__((
    __init_priority__(101))) GlibcThreadLocalInit glibcThreadLocalInit;
#endif
} // namespace threadlocal_detail
} // namespace folly
