/*
   +----------------------------------------------------------------------+
   | HipHop for PHP                                                       |
   +----------------------------------------------------------------------+
   | Copyright (c) 2010-present Facebook, Inc. (http://www.facebook.com)  |
   | Copyright (c) 1997-2010 The PHP Group                                |
   +----------------------------------------------------------------------+
   | This source file is subject to version 3.01 of the PHP license,      |
   | that is bundled with this package in the file LICENSE, and is        |
   | available through the world-wide-web at the following url:           |
   | http://www.php.net/license/3_01.txt                                  |
   | If you did not receive a copy of the PHP license and are unable to   |
   | obtain it through the world-wide-web, please send a note to          |
   | license@php.net so we can mail you a copy immediately.               |
   +----------------------------------------------------------------------+
*/

#include "hphp/runtime/ext/thrift/adapter.h"

#include "hphp/runtime/base/execution-context.h"
#include "hphp/runtime/base/type-string.h"
#include "hphp/runtime/ext/thrift/util.h"

namespace HPHP {
namespace thrift {
///////////////////////////////////////////////////////////////////////////////

void setThriftType(Variant value, const Object& obj, const String& fieldName) {
  auto getter_name = "get_" + fieldName;
  auto field_val =
      obj->o_invoke_few_args(getter_name, RuntimeCoeffects::pure(), 0);
  if (field_val.isObject()) {
    auto wrapped_obj = field_val.toObject();
    wrapped_obj->o_invoke_few_args(
        "setValue_DO_NOT_USE_THRIFT_INTERNAL",
        RuntimeCoeffects::fixme(),
        1,
        value);
  } else {
    thrift_error(
        folly::sformat("Method {} returned a non-object.", getter_name),
        ERR_INVALID_DATA);
  }
}

Variant getThriftType(const Object& obj, const String& fieldName) {
  auto getter_name = "get_" + fieldName;
  auto field_val =
      obj->o_invoke_few_args(getter_name, RuntimeCoeffects::pure(), 0);
  if (field_val.isObject()) {
    auto wrapped_obj = field_val.toObject();
    return wrapped_obj->o_invoke_few_args(
        "getValue_DO_NOT_USE_THRIFT_INTERNAL",
        RuntimeCoeffects::pure(),
        0);
  }
  thrift_error(
      folly::sformat("Method {} returned a non-object.", getter_name),
      ERR_INVALID_DATA);
}

///////////////////////////////////////////////////////////////////////////////
} // namespace thrift
} // namespace HPHP
