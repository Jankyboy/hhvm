/*
   +----------------------------------------------------------------------+
   | HipHop for PHP                                                       |
   +----------------------------------------------------------------------+
   | Copyright (c) 2010-present Facebook, Inc. (http://www.facebook.com)  |
   +----------------------------------------------------------------------+
   | This source path is subject to version 3.01 of the PHP license,      |
   | that is bundled with this package in the path LICENSE, and is        |
   | available through the world-wide-web at the following url:           |
   | http://www.php.net/license/3_01.txt                                  |
   | If you did not receive a copy of the PHP license and are unable to   |
   | obtain it through the world-wide-web, please send a note to          |
   | license@php.net so we can mail you a copy immediately.               |
   +----------------------------------------------------------------------+
*/

#include "hphp/runtime/base/static-string-table.h"
#include "hphp/runtime/base/string-data.h"
#include "hphp/runtime/ext/facts/string-ptr.h"

/**
 * Implements our StringPtr class in terms of HPHP::StringData.
 *
 * By contrast, see string-data-fake.cpp, which implements this class in
 * terms of std::string so our tests don't need to link against
 * //hphp/runtime/base:runtime_base.
 */

namespace HPHP {
namespace Facts {

StringPtr::StringPtr(const StringData* impl) noexcept : m_impl{impl} {
  assertx(m_impl == nullptr || m_impl->isStatic());
}

std::string_view StringPtr::slice() const noexcept {
  if (m_impl == nullptr) {
    return std::string_view{};
  }
  return m_impl->slice();
}

int StringPtr::size() const noexcept {
  if (m_impl == nullptr) {
    return 0;
  }
  return m_impl->size();
}

bool StringPtr::empty() const noexcept {
  return m_impl == nullptr || m_impl->empty();
}

strhash_t StringPtr::hash() const noexcept {
  return m_impl->hash();
}

bool StringPtr::same(const StringPtr& s) const noexcept {
  if (m_impl == s.m_impl) {
    return true;
  }
  if (m_impl == nullptr || s.m_impl == nullptr) {
    return false;
  }
  return m_impl->same(s.m_impl);
}

bool StringPtr::tsame(const StringPtr& s) const noexcept {
  if (m_impl == s.m_impl) {
    return true;
  }
  if (m_impl == nullptr || s.m_impl == nullptr) {
    return false;
  }
  return m_impl->tsame(s.m_impl);
}

bool StringPtr::fsame(const StringPtr& s) const noexcept {
  if (m_impl == s.m_impl) {
    return true;
  }
  if (m_impl == nullptr || s.m_impl == nullptr) {
    return false;
  }
  return m_impl->fsame(s.m_impl);
}

bool StringPtr::tsame_slice(std::string_view a, std::string_view b) noexcept {
  return tstrcmp_slice(a, b) == 0;
}

bool StringPtr::fsame_slice(std::string_view a, std::string_view b) noexcept {
  return fstrcmp_slice(a, b) == 0;
}

StringPtr makeStringPtr(const StringData& s) {
  return StringPtr{makeStaticString(&s)};
}

StringPtr makeStringPtr(const std::string& s) {
  return StringPtr{makeStaticString(s)};
}

StringPtr makeStringPtr(std::string_view s) {
  return StringPtr{makeStaticString(s)};
}

std::ostream& operator<<(std::ostream& os, const StringPtr& s) {
  if (s.m_impl == nullptr) {
    return os << "<nullptr>";
  }
  return os << s.m_impl->slice();
}

} // namespace Facts
} // namespace HPHP
