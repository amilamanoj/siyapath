/**
 * Autogenerated by Thrift Compiler (0.8.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package org.siyapath.service;

import org.apache.commons.lang.builder.HashCodeBuilder;
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

public class Result implements org.apache.thrift.TBase<Result, Result._Fields>, java.io.Serializable, Cloneable {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("Result");

  private static final org.apache.thrift.protocol.TField JOB_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("jobID", org.apache.thrift.protocol.TType.I32, (short)1);
  private static final org.apache.thrift.protocol.TField TASK_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("taskID", org.apache.thrift.protocol.TType.I32, (short)2);
  private static final org.apache.thrift.protocol.TField STATUS_FIELD_DESC = new org.apache.thrift.protocol.TField("status", org.apache.thrift.protocol.TType.I32, (short)3);
  private static final org.apache.thrift.protocol.TField RESULTS_FIELD_DESC = new org.apache.thrift.protocol.TField("results", org.apache.thrift.protocol.TType.STRING, (short)4);
  private static final org.apache.thrift.protocol.TField PROCESSING_NODE_FIELD_DESC = new org.apache.thrift.protocol.TField("processingNode", org.apache.thrift.protocol.TType.STRUCT, (short)5);
  private static final org.apache.thrift.protocol.TField TASK_REPLICA_INDEX_FIELD_DESC = new org.apache.thrift.protocol.TField("taskReplicaIndex", org.apache.thrift.protocol.TType.I32, (short)6);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new ResultStandardSchemeFactory());
    schemes.put(TupleScheme.class, new ResultTupleSchemeFactory());
  }

  public int jobID; // required
  public int taskID; // required
  /**
   * 
   * @see TaskStatus
   */
  public TaskStatus status; // required
  public ByteBuffer results; // required
  public NodeData processingNode; // required
  public int taskReplicaIndex; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    JOB_ID((short)1, "jobID"),
    TASK_ID((short)2, "taskID"),
    /**
     * 
     * @see TaskStatus
     */
    STATUS((short)3, "status"),
    RESULTS((short)4, "results"),
    PROCESSING_NODE((short)5, "processingNode"),
    TASK_REPLICA_INDEX((short)6, "taskReplicaIndex");

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
        case 1: // JOB_ID
          return JOB_ID;
        case 2: // TASK_ID
          return TASK_ID;
        case 3: // STATUS
          return STATUS;
        case 4: // RESULTS
          return RESULTS;
        case 5: // PROCESSING_NODE
          return PROCESSING_NODE;
        case 6: // TASK_REPLICA_INDEX
          return TASK_REPLICA_INDEX;
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
  private static final int __JOBID_ISSET_ID = 0;
  private static final int __TASKID_ISSET_ID = 1;
  private static final int __TASKREPLICAINDEX_ISSET_ID = 2;
  private BitSet __isset_bit_vector = new BitSet(3);
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.JOB_ID, new org.apache.thrift.meta_data.FieldMetaData("jobID", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.TASK_ID, new org.apache.thrift.meta_data.FieldMetaData("taskID", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.STATUS, new org.apache.thrift.meta_data.FieldMetaData("status", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.EnumMetaData(org.apache.thrift.protocol.TType.ENUM, TaskStatus.class)));
    tmpMap.put(_Fields.RESULTS, new org.apache.thrift.meta_data.FieldMetaData("results", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING        , true)));
    tmpMap.put(_Fields.PROCESSING_NODE, new org.apache.thrift.meta_data.FieldMetaData("processingNode", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, NodeData.class)));
    tmpMap.put(_Fields.TASK_REPLICA_INDEX, new org.apache.thrift.meta_data.FieldMetaData("taskReplicaIndex", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(Result.class, metaDataMap);
  }

  public Result() {
  }

  public Result(
    int jobID,
    int taskID,
    TaskStatus status,
    ByteBuffer results,
    NodeData processingNode,
    int taskReplicaIndex)
  {
    this();
    this.jobID = jobID;
    setJobIDIsSet(true);
    this.taskID = taskID;
    setTaskIDIsSet(true);
    this.status = status;
    this.results = results;
    this.processingNode = processingNode;
    this.taskReplicaIndex = taskReplicaIndex;
    setTaskReplicaIndexIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public Result(Result other) {
    __isset_bit_vector.clear();
    __isset_bit_vector.or(other.__isset_bit_vector);
    this.jobID = other.jobID;
    this.taskID = other.taskID;
    if (other.isSetStatus()) {
      this.status = other.status;
    }
    if (other.isSetResults()) {
      this.results = org.apache.thrift.TBaseHelper.copyBinary(other.results);
;
    }
    if (other.isSetProcessingNode()) {
      this.processingNode = new NodeData(other.processingNode);
    }
    this.taskReplicaIndex = other.taskReplicaIndex;
  }

  public Result deepCopy() {
    return new Result(this);
  }

  @Override
  public void clear() {
    setJobIDIsSet(false);
    this.jobID = 0;
    setTaskIDIsSet(false);
    this.taskID = 0;
    this.status = null;
    this.results = null;
    this.processingNode = null;
    setTaskReplicaIndexIsSet(false);
    this.taskReplicaIndex = 0;
  }

  public int getJobID() {
    return this.jobID;
  }

  public Result setJobID(int jobID) {
    this.jobID = jobID;
    setJobIDIsSet(true);
    return this;
  }

  public void unsetJobID() {
    __isset_bit_vector.clear(__JOBID_ISSET_ID);
  }

  /** Returns true if field jobID is set (has been assigned a value) and false otherwise */
  public boolean isSetJobID() {
    return __isset_bit_vector.get(__JOBID_ISSET_ID);
  }

  public void setJobIDIsSet(boolean value) {
    __isset_bit_vector.set(__JOBID_ISSET_ID, value);
  }

  public int getTaskID() {
    return this.taskID;
  }

  public Result setTaskID(int taskID) {
    this.taskID = taskID;
    setTaskIDIsSet(true);
    return this;
  }

  public void unsetTaskID() {
    __isset_bit_vector.clear(__TASKID_ISSET_ID);
  }

  /** Returns true if field taskID is set (has been assigned a value) and false otherwise */
  public boolean isSetTaskID() {
    return __isset_bit_vector.get(__TASKID_ISSET_ID);
  }

  public void setTaskIDIsSet(boolean value) {
    __isset_bit_vector.set(__TASKID_ISSET_ID, value);
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
  public Result setStatus(TaskStatus status) {
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

  public byte[] getResults() {
    setResults(org.apache.thrift.TBaseHelper.rightSize(results));
    return results == null ? null : results.array();
  }

  public ByteBuffer bufferForResults() {
    return results;
  }

  public Result setResults(byte[] results) {
    setResults(results == null ? (ByteBuffer)null : ByteBuffer.wrap(results));
    return this;
  }

  public Result setResults(ByteBuffer results) {
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

  public NodeData getProcessingNode() {
    return this.processingNode;
  }

  public Result setProcessingNode(NodeData processingNode) {
    this.processingNode = processingNode;
    return this;
  }

  public void unsetProcessingNode() {
    this.processingNode = null;
  }

  /** Returns true if field processingNode is set (has been assigned a value) and false otherwise */
  public boolean isSetProcessingNode() {
    return this.processingNode != null;
  }

  public void setProcessingNodeIsSet(boolean value) {
    if (!value) {
      this.processingNode = null;
    }
  }

  public int getTaskReplicaIndex() {
    return this.taskReplicaIndex;
  }

  public Result setTaskReplicaIndex(int taskReplicaIndex) {
    this.taskReplicaIndex = taskReplicaIndex;
    setTaskReplicaIndexIsSet(true);
    return this;
  }

  public void unsetTaskReplicaIndex() {
    __isset_bit_vector.clear(__TASKREPLICAINDEX_ISSET_ID);
  }

  /** Returns true if field taskReplicaIndex is set (has been assigned a value) and false otherwise */
  public boolean isSetTaskReplicaIndex() {
    return __isset_bit_vector.get(__TASKREPLICAINDEX_ISSET_ID);
  }

  public void setTaskReplicaIndexIsSet(boolean value) {
    __isset_bit_vector.set(__TASKREPLICAINDEX_ISSET_ID, value);
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case JOB_ID:
      if (value == null) {
        unsetJobID();
      } else {
        setJobID((Integer)value);
      }
      break;

    case TASK_ID:
      if (value == null) {
        unsetTaskID();
      } else {
        setTaskID((Integer)value);
      }
      break;

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
        setResults((ByteBuffer)value);
      }
      break;

    case PROCESSING_NODE:
      if (value == null) {
        unsetProcessingNode();
      } else {
        setProcessingNode((NodeData)value);
      }
      break;

    case TASK_REPLICA_INDEX:
      if (value == null) {
        unsetTaskReplicaIndex();
      } else {
        setTaskReplicaIndex((Integer)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case JOB_ID:
      return Integer.valueOf(getJobID());

    case TASK_ID:
      return Integer.valueOf(getTaskID());

    case STATUS:
      return getStatus();

    case RESULTS:
      return getResults();

    case PROCESSING_NODE:
      return getProcessingNode();

    case TASK_REPLICA_INDEX:
      return Integer.valueOf(getTaskReplicaIndex());

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case JOB_ID:
      return isSetJobID();
    case TASK_ID:
      return isSetTaskID();
    case STATUS:
      return isSetStatus();
    case RESULTS:
      return isSetResults();
    case PROCESSING_NODE:
      return isSetProcessingNode();
    case TASK_REPLICA_INDEX:
      return isSetTaskReplicaIndex();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof Result)
      return this.equals((Result)that);
    return false;
  }

  public boolean equals(Result that) {
    if (that == null)
      return false;

    boolean this_present_jobID = true;
    boolean that_present_jobID = true;
    if (this_present_jobID || that_present_jobID) {
      if (!(this_present_jobID && that_present_jobID))
        return false;
      if (this.jobID != that.jobID)
        return false;
    }

    boolean this_present_taskID = true;
    boolean that_present_taskID = true;
    if (this_present_taskID || that_present_taskID) {
      if (!(this_present_taskID && that_present_taskID))
        return false;
      if (this.taskID != that.taskID)
        return false;
    }

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

    boolean this_present_processingNode = true && this.isSetProcessingNode();
    boolean that_present_processingNode = true && that.isSetProcessingNode();
    if (this_present_processingNode || that_present_processingNode) {
      if (!(this_present_processingNode && that_present_processingNode))
        return false;
      if (!this.processingNode.equals(that.processingNode))
        return false;
    }

    boolean this_present_taskReplicaIndex = true;
    boolean that_present_taskReplicaIndex = true;
    if (this_present_taskReplicaIndex || that_present_taskReplicaIndex) {
      if (!(this_present_taskReplicaIndex && that_present_taskReplicaIndex))
        return false;
      if (this.taskReplicaIndex != that.taskReplicaIndex)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();

    boolean present_jobID = true;
    builder.append(present_jobID);
    if (present_jobID)
      builder.append(jobID);

    boolean present_taskID = true;
    builder.append(present_taskID);
    if (present_taskID)
      builder.append(taskID);

    boolean present_status = true && (isSetStatus());
    builder.append(present_status);
    if (present_status)
      builder.append(status.getValue());

    boolean present_results = true && (isSetResults());
    builder.append(present_results);
    if (present_results)
      builder.append(results);

    boolean present_processingNode = true && (isSetProcessingNode());
    builder.append(present_processingNode);
    if (present_processingNode)
      builder.append(processingNode);

    boolean present_taskReplicaIndex = true;
    builder.append(present_taskReplicaIndex);
    if (present_taskReplicaIndex)
      builder.append(taskReplicaIndex);

    return builder.toHashCode();
  }

  public int compareTo(Result other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;
    Result typedOther = (Result)other;

    lastComparison = Boolean.valueOf(isSetJobID()).compareTo(typedOther.isSetJobID());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetJobID()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.jobID, typedOther.jobID);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetTaskID()).compareTo(typedOther.isSetTaskID());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetTaskID()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.taskID, typedOther.taskID);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
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
    lastComparison = Boolean.valueOf(isSetProcessingNode()).compareTo(typedOther.isSetProcessingNode());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetProcessingNode()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.processingNode, typedOther.processingNode);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetTaskReplicaIndex()).compareTo(typedOther.isSetTaskReplicaIndex());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetTaskReplicaIndex()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.taskReplicaIndex, typedOther.taskReplicaIndex);
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
    StringBuilder sb = new StringBuilder("Result(");
    boolean first = true;

    sb.append("jobID:");
    sb.append(this.jobID);
    first = false;
    if (!first) sb.append(", ");
    sb.append("taskID:");
    sb.append(this.taskID);
    first = false;
    if (!first) sb.append(", ");
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
      org.apache.thrift.TBaseHelper.toString(this.results, sb);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("processingNode:");
    if (this.processingNode == null) {
      sb.append("null");
    } else {
      sb.append(this.processingNode);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("taskReplicaIndex:");
    sb.append(this.taskReplicaIndex);
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

  private static class ResultStandardSchemeFactory implements SchemeFactory {
    public ResultStandardScheme getScheme() {
      return new ResultStandardScheme();
    }
  }

  private static class ResultStandardScheme extends StandardScheme<Result> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, Result struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // JOB_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.jobID = iprot.readI32();
              struct.setJobIDIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // TASK_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.taskID = iprot.readI32();
              struct.setTaskIDIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // STATUS
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.status = TaskStatus.findByValue(iprot.readI32());
              struct.setStatusIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // RESULTS
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.results = iprot.readBinary();
              struct.setResultsIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 5: // PROCESSING_NODE
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.processingNode = new NodeData();
              struct.processingNode.read(iprot);
              struct.setProcessingNodeIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 6: // TASK_REPLICA_INDEX
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.taskReplicaIndex = iprot.readI32();
              struct.setTaskReplicaIndexIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, Result struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      oprot.writeFieldBegin(JOB_ID_FIELD_DESC);
      oprot.writeI32(struct.jobID);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(TASK_ID_FIELD_DESC);
      oprot.writeI32(struct.taskID);
      oprot.writeFieldEnd();
      if (struct.status != null) {
        oprot.writeFieldBegin(STATUS_FIELD_DESC);
        oprot.writeI32(struct.status.getValue());
        oprot.writeFieldEnd();
      }
      if (struct.results != null) {
        oprot.writeFieldBegin(RESULTS_FIELD_DESC);
        oprot.writeBinary(struct.results);
        oprot.writeFieldEnd();
      }
      if (struct.processingNode != null) {
        oprot.writeFieldBegin(PROCESSING_NODE_FIELD_DESC);
        struct.processingNode.write(oprot);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(TASK_REPLICA_INDEX_FIELD_DESC);
      oprot.writeI32(struct.taskReplicaIndex);
      oprot.writeFieldEnd();
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class ResultTupleSchemeFactory implements SchemeFactory {
    public ResultTupleScheme getScheme() {
      return new ResultTupleScheme();
    }
  }

  private static class ResultTupleScheme extends TupleScheme<Result> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, Result struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetJobID()) {
        optionals.set(0);
      }
      if (struct.isSetTaskID()) {
        optionals.set(1);
      }
      if (struct.isSetStatus()) {
        optionals.set(2);
      }
      if (struct.isSetResults()) {
        optionals.set(3);
      }
      if (struct.isSetProcessingNode()) {
        optionals.set(4);
      }
      if (struct.isSetTaskReplicaIndex()) {
        optionals.set(5);
      }
      oprot.writeBitSet(optionals, 6);
      if (struct.isSetJobID()) {
        oprot.writeI32(struct.jobID);
      }
      if (struct.isSetTaskID()) {
        oprot.writeI32(struct.taskID);
      }
      if (struct.isSetStatus()) {
        oprot.writeI32(struct.status.getValue());
      }
      if (struct.isSetResults()) {
        oprot.writeBinary(struct.results);
      }
      if (struct.isSetProcessingNode()) {
        struct.processingNode.write(oprot);
      }
      if (struct.isSetTaskReplicaIndex()) {
        oprot.writeI32(struct.taskReplicaIndex);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, Result struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(6);
      if (incoming.get(0)) {
        struct.jobID = iprot.readI32();
        struct.setJobIDIsSet(true);
      }
      if (incoming.get(1)) {
        struct.taskID = iprot.readI32();
        struct.setTaskIDIsSet(true);
      }
      if (incoming.get(2)) {
        struct.status = TaskStatus.findByValue(iprot.readI32());
        struct.setStatusIsSet(true);
      }
      if (incoming.get(3)) {
        struct.results = iprot.readBinary();
        struct.setResultsIsSet(true);
      }
      if (incoming.get(4)) {
        struct.processingNode = new NodeData();
        struct.processingNode.read(iprot);
        struct.setProcessingNodeIsSet(true);
      }
      if (incoming.get(5)) {
        struct.taskReplicaIndex = iprot.readI32();
        struct.setTaskReplicaIndexIsSet(true);
      }
    }
  }

}

