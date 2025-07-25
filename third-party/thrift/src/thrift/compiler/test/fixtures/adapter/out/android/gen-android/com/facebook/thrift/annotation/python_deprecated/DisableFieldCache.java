/**
 * Autogenerated by Thrift
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.facebook.thrift.annotation.python_deprecated;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;
import java.util.BitSet;
import java.util.Arrays;
import com.facebook.thrift.*;
import com.facebook.thrift.annotations.*;
import com.facebook.thrift.async.*;
import com.facebook.thrift.meta_data.*;
import com.facebook.thrift.server.*;
import com.facebook.thrift.transport.*;
import com.facebook.thrift.protocol.*;

/**
 * Disable caching all fields for a struct.
 * Also available as a thrift_library compiler option:
 *     thrift_library(..., thrift_python_options = ["disable_field_cache"])
 * 
 * Has NO effect in cinder runtime (e.g., IG Django)
 * Only affects thrift-python, not older deprecated variants.
 * 
 * Usage guidelines:
 *   - Improves latency/throughput when struct fields accessed only once.
 *   - Reduces memory usage for most use cases; try this to resolve OOMs.
 *   - Worsens latency for subsequent field accesses relative to default.
 */
@SuppressWarnings({ "unused", "serial" })
public class DisableFieldCache implements TBase, java.io.Serializable, Cloneable {
  private static final TStruct STRUCT_DESC = new TStruct("DisableFieldCache");


  public DisableFieldCache() {
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public DisableFieldCache(DisableFieldCache other) {
  }

  public DisableFieldCache deepCopy() {
    return new DisableFieldCache(this);
  }

  @Override
  public boolean equals(Object _that) {
    if (_that == null)
      return false;
    if (this == _that)
      return true;
    if (!(_that instanceof DisableFieldCache))
      return false;
    DisableFieldCache that = (DisableFieldCache)_that;

    return true;
  }

  @Override
  public int hashCode() {
    return Arrays.deepHashCode(new Object[] {});
  }

  // This is required to satisfy the TBase interface, but can't be implemented on immutable struture.
  public void read(TProtocol iprot) throws TException {
    throw new TException("unimplemented in android immutable structure");
  }

  public static DisableFieldCache deserialize(TProtocol iprot) throws TException {
    TField __field;
    iprot.readStructBegin();
    while (true)
    {
      __field = iprot.readFieldBegin();
      if (__field.type == TType.STOP) {
        break;
      }
      switch (__field.id)
      {
        default:
          TProtocolUtil.skip(iprot, __field.type);
          break;
      }
      iprot.readFieldEnd();
    }
    iprot.readStructEnd();

    DisableFieldCache _that;
    _that = new DisableFieldCache(
    );
    _that.validate();
    return _that;
  }

  public void write(TProtocol oprot) throws TException {
    validate();

    oprot.writeStructBegin(STRUCT_DESC);
    oprot.writeFieldStop();
    oprot.writeStructEnd();
  }

  @Override
  public String toString() {
    return toString(1, true);
  }

  @Override
  public String toString(int indent, boolean prettyPrint) {
    return TBaseHelper.toStringHelper(this, indent, prettyPrint);
  }

  public void validate() throws TException {
    // check for required fields
  }

}

