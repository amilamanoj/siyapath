/**
 * Autogenerated by Thrift Compiler (0.8.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package org.siyapath.service;

import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Job implements org.apache.thrift.TBase<Job, Job._Fields>, java.io.Serializable, Cloneable {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("Job");

  private static final org.apache.thrift.protocol.TField SENDER_FIELD_DESC = new org.apache.thrift.protocol.TField("sender", org.apache.thrift.protocol.TType.I32, (short)1);
  private static final org.apache.thrift.protocol.TField RECIPIENT_FIELD_DESC = new org.apache.thrift.protocol.TField("recipient", org.apache.thrift.protocol.TType.I32, (short)2);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new JobStandardSchemeFactory());
    schemes.put(TupleScheme.class, new JobTupleSchemeFactory());
  }

  public int sender; // required
  public int recipient; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    SENDER((short)1, "sender"),
    RECIPIENT((short)2, "recipient");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // SENDER
          return SENDER;
        case 2: // RECIPIENT
          return RECIPIENT;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final int __SENDER_ISSET_ID = 0;
  private static final int __RECIPIENT_ISSET_ID = 1;
  private BitSet __isset_bit_vector = new BitSet(2);
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.SENDER, new org.apache.thrift.meta_data.FieldMetaData("sender", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.RECIPIENT, new org.apache.thrift.meta_data.FieldMetaData("recipient", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(Job.class, metaDataMap);
  }

  public Job() {
  }

  public Job(
    int sender,
    int recipient)
  {
    this();
    this.sender = sender;
    setSenderIsSet(true);
    this.recipient = recipient;
    setRecipientIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public Job(Job other) {
    __isset_bit_vector.clear();
    __isset_bit_vector.or(other.__isset_bit_vector);
    this.sender = other.sender;
    this.recipient = other.recipient;
  }

  public Job deepCopy() {
    return new Job(this);
  }

  @Override
  public void clear() {
    setSenderIsSet(false);
    this.sender = 0;
    setRecipientIsSet(false);
    this.recipient = 0;
  }

  public int getSender() {
    return this.sender;
  }

  public Job setSender(int sender) {
    this.sender = sender;
    setSenderIsSet(true);
    return this;
  }

  public void unsetSender() {
    __isset_bit_vector.clear(__SENDER_ISSET_ID);
  }

  /** Returns true if field sender is set (has been assigned a value) and false otherwise */
  public boolean isSetSender() {
    return __isset_bit_vector.get(__SENDER_ISSET_ID);
  }

  public void setSenderIsSet(boolean value) {
    __isset_bit_vector.set(__SENDER_ISSET_ID, value);
  }

  public int getRecipient() {
    return this.recipient;
  }

  public Job setRecipient(int recipient) {
    this.recipient = recipient;
    setRecipientIsSet(true);
    return this;
  }

  public void unsetRecipient() {
    __isset_bit_vector.clear(__RECIPIENT_ISSET_ID);
  }

  /** Returns true if field recipient is set (has been assigned a value) and false otherwise */
  public boolean isSetRecipient() {
    return __isset_bit_vector.get(__RECIPIENT_ISSET_ID);
  }

  public void setRecipientIsSet(boolean value) {
    __isset_bit_vector.set(__RECIPIENT_ISSET_ID, value);
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case SENDER:
      if (value == null) {
        unsetSender();
      } else {
        setSender((Integer)value);
      }
      break;

    case RECIPIENT:
      if (value == null) {
        unsetRecipient();
      } else {
        setRecipient((Integer)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case SENDER:
      return Integer.valueOf(getSender());

    case RECIPIENT:
      return Integer.valueOf(getRecipient());

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case SENDER:
      return isSetSender();
    case RECIPIENT:
      return isSetRecipient();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof Job)
      return this.equals((Job)that);
    return false;
  }

  public boolean equals(Job that) {
    if (that == null)
      return false;

    boolean this_present_sender = true;
    boolean that_present_sender = true;
    if (this_present_sender || that_present_sender) {
      if (!(this_present_sender && that_present_sender))
        return false;
      if (this.sender != that.sender)
        return false;
    }

    boolean this_present_recipient = true;
    boolean that_present_recipient = true;
    if (this_present_recipient || that_present_recipient) {
      if (!(this_present_recipient && that_present_recipient))
        return false;
      if (this.recipient != that.recipient)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  public int compareTo(Job other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;
    Job typedOther = (Job)other;

    lastComparison = Boolean.valueOf(isSetSender()).compareTo(typedOther.isSetSender());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetSender()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.sender, typedOther.sender);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetRecipient()).compareTo(typedOther.isSetRecipient());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetRecipient()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.recipient, typedOther.recipient);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("Job(");
    boolean first = true;

    sb.append("sender:");
    sb.append(this.sender);
    first = false;
    if (!first) sb.append(", ");
    sb.append("recipient:");
    sb.append(this.recipient);
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bit_vector = new BitSet(1);
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class JobStandardSchemeFactory implements SchemeFactory {
    public JobStandardScheme getScheme() {
      return new JobStandardScheme();
    }
  }

  private static class JobStandardScheme extends StandardScheme<Job> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, Job struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // SENDER
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.sender = iprot.readI32();
              struct.setSenderIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // RECIPIENT
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.recipient = iprot.readI32();
              struct.setRecipientIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, Job struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      oprot.writeFieldBegin(SENDER_FIELD_DESC);
      oprot.writeI32(struct.sender);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(RECIPIENT_FIELD_DESC);
      oprot.writeI32(struct.recipient);
      oprot.writeFieldEnd();
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class JobTupleSchemeFactory implements SchemeFactory {
    public JobTupleScheme getScheme() {
      return new JobTupleScheme();
    }
  }

  private static class JobTupleScheme extends TupleScheme<Job> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, Job struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetSender()) {
        optionals.set(0);
      }
      if (struct.isSetRecipient()) {
        optionals.set(1);
      }
      oprot.writeBitSet(optionals, 2);
      if (struct.isSetSender()) {
        oprot.writeI32(struct.sender);
      }
      if (struct.isSetRecipient()) {
        oprot.writeI32(struct.recipient);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, Job struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(2);
      if (incoming.get(0)) {
        struct.sender = iprot.readI32();
        struct.setSenderIsSet(true);
      }
      if (incoming.get(1)) {
        struct.recipient = iprot.readI32();
        struct.setRecipientIsSet(true);
      }
    }
  }

}

