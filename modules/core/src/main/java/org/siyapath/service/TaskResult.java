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

public class TaskResult implements org.apache.thrift.TBase<TaskResult, TaskResult._Fields>, java.io.Serializable, Cloneable {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("TaskResult");

  private static final org.apache.thrift.protocol.TField STATUS_FIELD_DESC = new org.apache.thrift.protocol.TField("status", org.apache.thrift.protocol.TType.I32, (short)1);
  private static final org.apache.thrift.protocol.TField RESULTS_FIELD_DESC = new org.apache.thrift.protocol.TField("results", org.apache.thrift.protocol.TType.STRING, (short)2);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new TaskResultStandardSchemeFactory());
    schemes.put(TupleScheme.class, new TaskResultTupleSchemeFactory());
  }

  /**
   * 
   * @see TaskStatus
   */
  public TaskStatus status; // required
  public String results; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    /**
     * 
     * @see TaskStatus
     */
    STATUS((short)1, "status"),
    RESULTS((short)2, "results");

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
        case 1: // STATUS
          return STATUS;
        case 2: // RESULTS
          return RESULTS;
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
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.STATUS, new org.apache.thrift.meta_data.FieldMetaData("status", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.EnumMetaData(org.apache.thrift.protocol.TType.ENUM, TaskStatus.class)));
    tmpMap.put(_Fields.RESULTS, new org.apache.thrift.meta_data.FieldMetaData("results", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(TaskResult.class, metaDataMap);
  }

  public TaskResult() {
  }

  public TaskResult(
    TaskStatus status,
    String results)
  {
    this();
    this.status = status;
    this.results = results;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public TaskResult(TaskResult other) {
    if (other.isSetStatus()) {
      this.status = other.status;
    }
    if (other.isSetResults()) {
      this.results = other.results;
    }
  }

  public TaskResult deepCopy() {
    return new TaskResult(this);
  }

  @Override
  public void clear() {
    this.status = null;
    this.results = null;
  }

  /**
   * 
   * @see TaskStatus
   */
  public TaskStatus getStatus() {
    return this.status;
  }

  /**
   * 
   * @see TaskStatus
   */
  public TaskResult setStatus(TaskStatus status) {
    this.status = status;
    return this;
  }

  public void unsetStatus() {
    this.status = null;
  }

  /** Returns true if field status is set (has been assigned a value) and false otherwise */
  public boolean isSetStatus() {
    return this.status != null;
  }

  public void setStatusIsSet(boolean value) {
    if (!value) {
      this.status = null;
    }
  }

  public String getResults() {
    return this.results;
  }

  public TaskResult setResults(String results) {
    this.results = results;
    return this;
  }

  public void unsetResults() {
    this.results = null;
  }

  /** Returns true if field results is set (has been assigned a value) and false otherwise */
  public boolean isSetResults() {
    return this.results != null;
  }

  public void setResultsIsSet(boolean value) {
    if (!value) {
      this.results = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case STATUS:
      if (value == null) {
        unsetStatus();
      } else {
        setStatus((TaskStatus)value);
      }
      break;

    case RESULTS:
      if (value == null) {
        unsetResults();
      } else {
        setResults((String)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case STATUS:
      return getStatus();

    case RESULTS:
      return getResults();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case STATUS:
      return isSetStatus();
    case RESULTS:
      return isSetResults();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof TaskResult)
      return this.equals((TaskResult)that);
    return false;
  }

  public boolean equals(TaskResult that) {
    if (that == null)
      return false;

    boolean this_present_status = true && this.isSetStatus();
    boolean that_present_status = true && that.isSetStatus();
    if (this_present_status || that_present_status) {
      if (!(this_present_status && that_present_status))
        return false;
      if (!this.status.equals(that.status))
        return false;
    }

    boolean this_present_results = true && this.isSetResults();
    boolean that_present_results = true && that.isSetResults();
    if (this_present_results || that_present_results) {
      if (!(this_present_results && that_present_results))
        return false;
      if (!this.results.equals(that.results))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  public int compareTo(TaskResult other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;
    TaskResult typedOther = (TaskResult)other;

    lastComparison = Boolean.valueOf(isSetStatus()).compareTo(typedOther.isSetStatus());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetStatus()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.status, typedOther.status);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetResults()).compareTo(typedOther.isSetResults());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetResults()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.results, typedOther.results);
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
    StringBuilder sb = new StringBuilder("TaskResult(");
    boolean first = true;

    sb.append("status:");
    if (this.status == null) {
      sb.append("null");
    } else {
      sb.append(this.status);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("results:");
    if (this.results == null) {
      sb.append("null");
    } else {
      sb.append(this.results);
    }
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
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class TaskResultStandardSchemeFactory implements SchemeFactory {
    public TaskResultStandardScheme getScheme() {
      return new TaskResultStandardScheme();
    }
  }

  private static class TaskResultStandardScheme extends StandardScheme<TaskResult> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, TaskResult struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // STATUS
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.status = TaskStatus.findByValue(iprot.readI32());
              struct.setStatusIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // RESULTS
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.results = iprot.readString();
              struct.setResultsIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, TaskResult struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.status != null) {
        oprot.writeFieldBegin(STATUS_FIELD_DESC);
        oprot.writeI32(struct.status.getValue());
        oprot.writeFieldEnd();
      }
      if (struct.results != null) {
        oprot.writeFieldBegin(RESULTS_FIELD_DESC);
        oprot.writeString(struct.results);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class TaskResultTupleSchemeFactory implements SchemeFactory {
    public TaskResultTupleScheme getScheme() {
      return new TaskResultTupleScheme();
    }
  }

  private static class TaskResultTupleScheme extends TupleScheme<TaskResult> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, TaskResult struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetStatus()) {
        optionals.set(0);
      }
      if (struct.isSetResults()) {
        optionals.set(1);
      }
      oprot.writeBitSet(optionals, 2);
      if (struct.isSetStatus()) {
        oprot.writeI32(struct.status.getValue());
      }
      if (struct.isSetResults()) {
        oprot.writeString(struct.results);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, TaskResult struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(2);
      if (incoming.get(0)) {
        struct.status = TaskStatus.findByValue(iprot.readI32());
        struct.setStatusIsSet(true);
      }
      if (incoming.get(1)) {
        struct.results = iprot.readString();
        struct.setResultsIsSet(true);
      }
    }
  }

}

