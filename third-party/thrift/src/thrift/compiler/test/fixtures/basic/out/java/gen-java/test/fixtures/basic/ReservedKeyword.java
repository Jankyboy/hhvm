/**
 * Autogenerated by Thrift
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */

package test.fixtures.basic;

import com.facebook.swift.codec.*;
import com.facebook.swift.codec.ThriftField.Requiredness;
import com.facebook.swift.codec.ThriftField.Recursiveness;
import com.google.common.collect.*;
import java.util.*;
import javax.annotation.Nullable;
import org.apache.thrift.*;
import org.apache.thrift.TException;
import org.apache.thrift.transport.*;
import org.apache.thrift.protocol.*;
import org.apache.thrift.protocol.TProtocol;
import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.MoreObjects.ToStringHelper;

@SwiftGenerated
@com.facebook.swift.codec.ThriftStruct(value="ReservedKeyword", builder=ReservedKeyword.Builder.class)
public final class ReservedKeyword implements com.facebook.thrift.payload.ThriftSerializable {
    @ThriftConstructor
    public ReservedKeyword(
        @com.facebook.swift.codec.ThriftField(value=1, name="reserved_field", requiredness=Requiredness.NONE) final int reservedField
    ) {
        this.reservedField = reservedField;
    }
    
    @ThriftConstructor
    protected ReservedKeyword() {
      this.reservedField = 0;
    }

    public static Builder builder() {
      return new Builder();
    }

    public static Builder builder(ReservedKeyword other) {
      return new Builder(other);
    }

    public static class Builder {
        private int reservedField = 0;
    
        @com.facebook.swift.codec.ThriftField(value=1, name="reserved_field", requiredness=Requiredness.NONE)    public Builder setReservedField(int reservedField) {
            this.reservedField = reservedField;
            return this;
        }
    
        public int getReservedField() { return reservedField; }
    
        public Builder() { }
        public Builder(ReservedKeyword other) {
            this.reservedField = other.reservedField;
        }
    
        @ThriftConstructor
        public ReservedKeyword build() {
            ReservedKeyword result = new ReservedKeyword (
                this.reservedField
            );
            return result;
        }
    }
    
    public static final Map<String, Integer> NAMES_TO_IDS = new HashMap<>();
    public static final Map<String, Integer> THRIFT_NAMES_TO_IDS = new HashMap<>();
    public static final Map<Integer, TField> FIELD_METADATA = new HashMap<>();
    private static final TStruct STRUCT_DESC = new TStruct("ReservedKeyword");
    private final int reservedField;
    public static final int _RESERVED_FIELD = 1;
    private static final TField RESERVED_FIELD_FIELD_DESC = new TField("reserved_field", TType.I32, (short)1);
    static {
      NAMES_TO_IDS.put("reservedField", 1);
      THRIFT_NAMES_TO_IDS.put("reserved_field", 1);
      FIELD_METADATA.put(1, RESERVED_FIELD_FIELD_DESC);
      com.facebook.thrift.type.TypeRegistry.add(new com.facebook.thrift.type.Type(
        new com.facebook.thrift.type.UniversalName("test.dev/fixtures/basic/ReservedKeyword"),
        ReservedKeyword.class, ReservedKeyword::read0));
    }
    
    
    @com.facebook.swift.codec.ThriftField(value=1, name="reserved_field", requiredness=Requiredness.NONE)
    public int getReservedField() { return reservedField; }

    @java.lang.Override
    public String toString() {
        ToStringHelper helper = toStringHelper(this);
        helper.add("reservedField", reservedField);
        return helper.toString();
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
    
        ReservedKeyword other = (ReservedKeyword)o;
    
        return
            Objects.equals(reservedField, other.reservedField) &&
            true;
    }

    @java.lang.Override
    public int hashCode() {
        return Arrays.deepHashCode(new java.lang.Object[] {
            reservedField
        });
    }

    
    public static com.facebook.thrift.payload.Reader<ReservedKeyword> asReader() {
      return ReservedKeyword::read0;
    }
    
    public static ReservedKeyword read0(TProtocol oprot) throws TException {
      TField __field;
      oprot.readStructBegin(ReservedKeyword.NAMES_TO_IDS, ReservedKeyword.THRIFT_NAMES_TO_IDS, ReservedKeyword.FIELD_METADATA);
      ReservedKeyword.Builder builder = new ReservedKeyword.Builder();
      while (true) {
        __field = oprot.readFieldBegin();
        if (__field.type == TType.STOP) { break; }
        switch (__field.id) {
        case _RESERVED_FIELD:
          if (__field.type == TType.I32) {
            int reservedField = oprot.readI32();
            builder.setReservedField(reservedField);
          } else {
            TProtocolUtil.skip(oprot, __field.type);
          }
          break;
        default:
          TProtocolUtil.skip(oprot, __field.type);
          break;
        }
        oprot.readFieldEnd();
      }
      oprot.readStructEnd();
      return builder.build();
    }

    public void write0(TProtocol oprot) throws TException {
      oprot.writeStructBegin(STRUCT_DESC);
      oprot.writeFieldBegin(RESERVED_FIELD_FIELD_DESC);
      oprot.writeI32(this.reservedField);
      oprot.writeFieldEnd();
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

    private static class _ReservedKeywordLazy {
        private static final ReservedKeyword _DEFAULT = new ReservedKeyword.Builder().build();
    }
    
    public static ReservedKeyword defaultInstance() {
        return  _ReservedKeywordLazy._DEFAULT;
    }
}
