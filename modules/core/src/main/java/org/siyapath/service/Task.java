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

/**
 * Contains information about a Task
 */
public class Task implements org.apache.thrift.TBase<Task, Task._Fields>, java.io.Serializable, Cloneable {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("Task");

  private static final org.apache.thrift.protocol.TField TASK_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("taskID", org.apache.thrift.protocol.TType.I32, (short)1);
  private static final org.apache.thrift.protocol.TField JOB_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("jobID", org.apache.thrift.protocol.TType.I32, (short)2);
  private static final org.apache.thrift.protocol.TField TASK_PROGRAM_FIELD_DESC = new org.apache.thrift.protocol.TField("taskProgram", org.apache.thrift.protocol.TType.STRING, (short)3);
  private static final org.apache.thrift.protocol.TField TASK_DATA_FIELD_DESC = new org.apache.thrift.protocol.TField("taskData", org.apache.thrift.protocol.TType.STRING, (short)4);
  private static final org.apache.thrift.protocol.TField CLASS_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("className", org.apache.thrift.protocol.TType.STRING, (short)5);
  private static final org.apache.thrift.protocol.TField SENDER_FIELD_DESC = new org.apache.thrift.protocol.TField("sender", org.apache.thrift.protocol.TType.STRUCT, (short)6);
  private static final org.apache.thrift.protocol.TField BACKUP_FIELD_DESC = new org.apache.thrift.protocol.TField("backup", org.apache.thrift.protocol.TType.STRUCT, (short)7);
  private static final org.apache.thrift.protocol.TField REQUIRED_RESOURCES_FIELD_DESC = new org.apache.thrift.protocol.TField("requiredResources", org.apache.thrift.protocol.TType.STRING, (short)8);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new TaskStandardSchemeFactory());
    schemes.put(TupleScheme.class, new TaskTupleSchemeFactory());
  }

  public int taskID; // required
  public int jobID; // required
  public ByteBuffer taskProgram; // required
  public String taskData; // required
  public String className; // required
  public NodeData sender; // required
  public NodeData backup; // required
  public String requiredResources; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    TASK_ID((short)1, "taskID"),
    JOB_ID((short)2, "jobID"),
    TASK_PROGRAM((short)3, "taskProgram"),
    TASK_DATA((short)4, "taskData"),
    CLASS_NAME((short)5, "className"),
    SENDER((short)6, "sender"),
    BACKUP((short)7, "backup"),
    REQUIRED_RESOURCES((short)8, "requiredResources");

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
        case 1: // TASK_ID
          return TASK_ID;
        case 2: // JOB_ID
          return JOB_ID;
        case 3: // TASK_PROGRAM
          return TASK_PROGRAM;
        case 4: // TASK_DATA
          return TASK_DATA;
        case 5: // CLASS_NAME
          return CLASS_NAME;
        case 6: // SENDER
          return SENDER;
        case 7: // BACKUP
          return BACKUP;
        case 8: // REQUIRED_RESOURCES
          return REQUIRED_RESOURCES;
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
  private static final int __TASKID_ISSET_ID = 0;
  private static final int __JOBID_ISSET_ID = 1;
  private BitSet __isset_bit_vector = new BitSet(2);
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.TASK_ID, new org.apache.thrift.meta_data.FieldMetaData("taskID", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.JOB_ID, new org.apache.thrift.meta_data.FieldMetaData("jobID", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.TASK_PROGRAM, new org.apache.thrift.meta_data.FieldMetaData("taskProgram", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING        , true)));
    tmpMap.put(_Fields.TASK_DATA, new org.apache.thrift.meta_data.FieldMetaData("taskData", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.CLASS_NAME, new org.apache.thrift.meta_data.FieldMetaData("className", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.SENDER, new org.apache.thrift.meta_data.FieldMetaData("sender", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, NodeData.class)));
    tmpMap.put(_Fields.BACKUP, new org.apache.thrift.meta_data.FieldMetaData("backup", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, NodeData.class)));
    tmpMap.put(_Fields.REQUIRED_RESOURCES, new org.apache.thrift.meta_data.FieldMetaData("requiredResources", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(Task.class, metaDataMap);
  }

  public Task() {
  }

  public Task(
    int taskID,
    int jobID,
    ByteBuffer taskProgram,
    String taskData,
    String className,
    NodeData sender,
    NodeData backup,
    String requiredResources)
  {
    this();
    this.taskID = taskID;
    setTaskIDIsSet(true);
    this.jobID = jobID;
    setJobIDIsSet(true);
    this.taskProgram = taskProgram;
    this.taskData = taskData;
    this.className = className;
    this.sender = sender;
    this.backup = backup;
    this.requiredResources = requiredResources;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public Task(Task other) {
    __isset_bit_vector.clear();
    __isset_bit_vector.or(other.__isset_bit_vector);
    this.taskID = other.taskID;
    this.jobID = other.jobID;
    if (other.isSetTaskProgram()) {
      this.taskProgram = org.apache.thrift.TBaseHelper.copyBinary(other.taskProgram);
;
    }
    if (other.isSetTaskData()) {
      this.taskData = other.taskData;
    }
    if (other.isSetClassName()) {
      this.className = other.className;
    }
    if (other.isSetSender()) {
      this.sender = new NodeData(other.sender);
    }
    if (other.isSetBackup()) {
      this.backup = new NodeData(other.backup);
    }
    if (other.isSetRequiredResources()) {
      this.requiredResources = other.requiredResources;
    }
  }

  public Task deepCopy() {
    return new Task(this);
  }

  @Override
  public void clear() {
    setTaskIDIsSet(false);
    this.taskID = 0;
    setJobIDIsSet(false);
    this.jobID = 0;
    this.taskProgram = null;
    this.taskData = null;
    this.className = null;
    this.sender = null;
    this.backup = null;
    this.requiredResources = null;
  }

  public int getTaskID() {
    return this.taskID;
  }

  public Task setTaskID(int taskID) {
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

  public int getJobID() {
    return this.jobID;
  }

  public Task setJobID(int jobID) {
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

  public byte[] getTaskProgram() {
    setTaskProgram(org.apache.thrift.TBaseHelper.rightSize(taskProgram));
    return taskProgram == null ? null : taskProgram.array();
  }

  public ByteBuffer bufferForTaskProgram() {
    return taskProgram;
  }

  public Task setTaskProgram(byte[] taskProgram) {
    setTaskProgram(taskProgram == null ? (ByteBuffer)null : ByteBuffer.wrap(taskProgram));
    return this;
  }

  public Task setTaskProgram(ByteBuffer taskProgram) {
    this.taskProgram = taskProgram;
    return this;
  }

  public void unsetTaskProgram() {
    this.taskProgram = null;
  }

  /** Returns true if field taskProgram is set (has been assigned a value) and false otherwise */
  public boolean isSetTaskProgram() {
    return this.taskProgram != null;
  }

  public void setTaskProgramIsSet(boolean value) {
    if (!value) {
      this.taskProgram = null;
    }
  }

  public String getTaskData() {
    return this.taskData;
  }

  public Task setTaskData(String taskData) {
    this.taskData = taskData;
    return this;
  }

  public void unsetTaskData() {
    this.taskData = null;
  }

  /** Returns true if field taskData is set (has been assigned a value) and false otherwise */
  public boolean isSetTaskData() {
    return this.taskData != null;
  }

  public void setTaskDataIsSet(boolean value) {
    if (!value) {
      this.taskData = null;
    }
  }

  public String getClassName() {
    return this.className;
  }

  public Task setClassName(String className) {
    this.className = className;
    return this;
  }

  public void unsetClassName() {
    this.className = null;
  }

  /** Returns true if field className is set (has been assigned a value) and false otherwise */
  public boolean isSetClassName() {
    return this.className != null;
  }

  public void setClassNameIsSet(boolean value) {
    if (!value) {
      this.className = null;
    }
  }

  public NodeData getSender() {
    return this.sender;
  }

  public Task setSender(NodeData sender) {
    this.sender = sender;
    return this;
  }

  public void unsetSender() {
    this.sender = null;
  }

  /** Returns true if field sender is set (has been assigned a value) and false otherwise */
  public boolean isSetSender() {
    return this.sender != null;
  }

  public void setSenderIsSet(boolean value) {
    if (!value) {
      this.sender = null;
    }
  }

  public NodeData getBackup() {
    return this.backup;
  }

  public Task setBackup(NodeData backup) {
    this.backup = backup;
    return this;
  }

  public void unsetBackup() {
    this.backup = null;
  }

  /** Returns true if field backup is set (has been assigned a value) and false otherwise */
  public boolean isSetBackup() {
    return this.backup != null;
  }

  public void setBackupIsSet(boolean value) {
    if (!value) {
      this.backup = null;
    }
  }

  public String getRequiredResources() {
    return this.requiredResources;
  }

  public Task setRequiredResources(String requiredResources) {
    this.requiredResources = requiredResources;
    return this;
  }

  public void unsetRequiredResources() {
    this.requiredResources = null;
  }

  /** Returns true if field requiredResources is set (has been assigned a value) and false otherwise */
  public boolean isSetRequiredResources() {
    return this.requiredResources != null;
  }

  public void setRequiredResourcesIsSet(boolean value) {
    if (!value) {
      this.requiredResources = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case TASK_ID:
      if (value == null) {
        unsetTaskID();
      } else {
        setTaskID((Integer)value);
      }
      break;

    case JOB_ID:
      if (value == null) {
        unsetJobID();
      } else {
        setJobID((Integer)value);
      }
      break;

    case TASK_PROGRAM:
      if (value == null) {
        unsetTaskProgram();
      } else {
        setTaskProgram((ByteBuffer)value);
      }
      break;

    case TASK_DATA:
      if (value == null) {
        unsetTaskData();
      } else {
        setTaskData((String)value);
      }
      break;

    case CLASS_NAME:
      if (value == null) {
        unsetClassName();
      } else {
        setClassName((String)value);
      }
      break;

    case SENDER:
      if (value == null) {
        unsetSender();
      } else {
        setSender((NodeData)value);
      }
      break;

    case BACKUP:
      if (value == null) {
        unsetBackup();
      } else {
        setBackup((NodeData)value);
      }
      break;

    case REQUIRED_RESOURCES:
      if (value == null) {
        unsetRequiredResources();
      } else {
        setRequiredResources((String)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case TASK_ID:
      return Integer.valueOf(getTaskID());

    case JOB_ID:
      return Integer.valueOf(getJobID());

    case TASK_PROGRAM:
      return getTaskProgram();

    case TASK_DATA:
      return getTaskData();

    case CLASS_NAME:
      return getClassName();

    case SENDER:
      return getSender();

    case BACKUP:
      return getBackup();

    case REQUIRED_RESOURCES:
      return getRequiredResources();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case TASK_ID:
      return isSetTaskID();
    case JOB_ID:
      return isSetJobID();
    case TASK_PROGRAM:
      return isSetTaskProgram();
    case TASK_DATA:
      return isSetTaskData();
    case CLASS_NAME:
      return isSetClassName();
    case SENDER:
      return isSetSender();
    case BACKUP:
      return isSetBackup();
    case REQUIRED_RESOURCES:
      return isSetRequiredResources();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof Task)
      return this.equals((Task)that);
    return false;
  }

  public boolean equals(Task that) {
    if (that == null)
      return false;

    boolean this_present_taskID = true;
    boolean that_present_taskID = true;
    if (this_present_taskID || that_present_taskID) {
      if (!(this_present_taskID && that_present_taskID))
        return false;
      if (this.taskID != that.taskID)
        return false;
    }

    boolean this_present_jobID = true;
    boolean that_present_jobID = true;
    if (this_present_jobID || that_present_jobID) {
      if (!(this_present_jobID && that_present_jobID))
        return false;
      if (this.jobID != that.jobID)
        return false;
    }

    boolean this_present_taskProgram = true && this.isSetTaskProgram();
    boolean that_present_taskProgram = true && that.isSetTaskProgram();
    if (this_present_taskProgram || that_present_taskProgram) {
      if (!(this_present_taskProgram && that_present_taskProgram))
        return false;
      if (!this.taskProgram.equals(that.taskProgram))
        return false;
    }

    boolean this_present_taskData = true && this.isSetTaskData();
    boolean that_present_taskData = true && that.isSetTaskData();
    if (this_present_taskData || that_present_taskData) {
      if (!(this_present_taskData && that_present_taskData))
        return false;
      if (!this.taskData.equals(that.taskData))
        return false;
    }

    boolean this_present_className = true && this.isSetClassName();
    boolean that_present_className = true && that.isSetClassName();
    if (this_present_className || that_present_className) {
      if (!(this_present_className && that_present_className))
        return false;
      if (!this.className.equals(that.className))
        return false;
    }

    boolean this_present_sender = true && this.isSetSender();
    boolean that_present_sender = true && that.isSetSender();
    if (this_present_sender || that_present_sender) {
      if (!(this_present_sender && that_present_sender))
        return false;
      if (!this.sender.equals(that.sender))
        return false;
    }

    boolean this_present_backup = true && this.isSetBackup();
    boolean that_present_backup = true && that.isSetBackup();
    if (this_present_backup || that_present_backup) {
      if (!(this_present_backup && that_present_backup))
        return false;
      if (!this.backup.equals(that.backup))
        return false;
    }

    boolean this_present_requiredResources = true && this.isSetRequiredResources();
    boolean that_present_requiredResources = true && that.isSetRequiredResources();
    if (this_present_requiredResources || that_present_requiredResources) {
      if (!(this_present_requiredResources && that_present_requiredResources))
        return false;
      if (!this.requiredResources.equals(that.requiredResources))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  public int compareTo(Task other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;
    Task typedOther = (Task)other;

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
    lastComparison = Boolean.valueOf(isSetTaskProgram()).compareTo(typedOther.isSetTaskProgram());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetTaskProgram()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.taskProgram, typedOther.taskProgram);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetTaskData()).compareTo(typedOther.isSetTaskData());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetTaskData()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.taskData, typedOther.taskData);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetClassName()).compareTo(typedOther.isSetClassName());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetClassName()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.className, typedOther.className);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
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
    lastComparison = Boolean.valueOf(isSetBackup()).compareTo(typedOther.isSetBackup());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetBackup()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.backup, typedOther.backup);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetRequiredResources()).compareTo(typedOther.isSetRequiredResources());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetRequiredResources()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.requiredResources, typedOther.requiredResources);
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
    StringBuilder sb = new StringBuilder("Task(");
    boolean first = true;

    sb.append("taskID:");
    sb.append(this.taskID);
    first = false;
    if (!first) sb.append(", ");
    sb.append("jobID:");
    sb.append(this.jobID);
    first = false;
    if (!first) sb.append(", ");
    sb.append("taskProgram:");
    if (this.taskProgram == null) {
      sb.append("null");
    } else {
      org.apache.thrift.TBaseHelper.toString(this.taskProgram, sb);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("taskData:");
    if (this.taskData == null) {
      sb.append("null");
    } else {
      sb.append(this.taskData);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("className:");
    if (this.className == null) {
      sb.append("null");
    } else {
      sb.append(this.className);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("sender:");
    if (this.sender == null) {
      sb.append("null");
    } else {
      sb.append(this.sender);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("backup:");
    if (this.backup == null) {
      sb.append("null");
    } else {
      sb.append(this.backup);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("requiredResources:");
    if (this.requiredResources == null) {
      sb.append("null");
    } else {
      sb.append(this.requiredResources);
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
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bit_vector = new BitSet(1);
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class TaskStandardSchemeFactory implements SchemeFactory {
    public TaskStandardScheme getScheme() {
      return new TaskStandardScheme();
    }
  }

  private static class TaskStandardScheme extends StandardScheme<Task> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, Task struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // TASK_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.taskID = iprot.readI32();
              struct.setTaskIDIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // JOB_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.jobID = iprot.readI32();
              struct.setJobIDIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // TASK_PROGRAM
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.taskProgram = iprot.readBinary();
              struct.setTaskProgramIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // TASK_DATA
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.taskData = iprot.readString();
              struct.setTaskDataIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 5: // CLASS_NAME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.className = iprot.readString();
              struct.setClassNameIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 6: // SENDER
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.sender = new NodeData();
              struct.sender.read(iprot);
              struct.setSenderIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 7: // BACKUP
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.backup = new NodeData();
              struct.backup.read(iprot);
              struct.setBackupIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 8: // REQUIRED_RESOURCES
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.requiredResources = iprot.readString();
              struct.setRequiredResourcesIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, Task struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      oprot.writeFieldBegin(TASK_ID_FIELD_DESC);
      oprot.writeI32(struct.taskID);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(JOB_ID_FIELD_DESC);
      oprot.writeI32(struct.jobID);
      oprot.writeFieldEnd();
      if (struct.taskProgram != null) {
        oprot.writeFieldBegin(TASK_PROGRAM_FIELD_DESC);
        oprot.writeBinary(struct.taskProgram);
        oprot.writeFieldEnd();
      }
      if (struct.taskData != null) {
        oprot.writeFieldBegin(TASK_DATA_FIELD_DESC);
        oprot.writeString(struct.taskData);
        oprot.writeFieldEnd();
      }
      if (struct.className != null) {
        oprot.writeFieldBegin(CLASS_NAME_FIELD_DESC);
        oprot.writeString(struct.className);
        oprot.writeFieldEnd();
      }
      if (struct.sender != null) {
        oprot.writeFieldBegin(SENDER_FIELD_DESC);
        struct.sender.write(oprot);
        oprot.writeFieldEnd();
      }
      if (struct.backup != null) {
        oprot.writeFieldBegin(BACKUP_FIELD_DESC);
        struct.backup.write(oprot);
        oprot.writeFieldEnd();
      }
      if (struct.requiredResources != null) {
        oprot.writeFieldBegin(REQUIRED_RESOURCES_FIELD_DESC);
        oprot.writeString(struct.requiredResources);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class TaskTupleSchemeFactory implements SchemeFactory {
    public TaskTupleScheme getScheme() {
      return new TaskTupleScheme();
    }
  }

  private static class TaskTupleScheme extends TupleScheme<Task> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, Task struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetTaskID()) {
        optionals.set(0);
      }
      if (struct.isSetJobID()) {
        optionals.set(1);
      }
      if (struct.isSetTaskProgram()) {
        optionals.set(2);
      }
      if (struct.isSetTaskData()) {
        optionals.set(3);
      }
      if (struct.isSetClassName()) {
        optionals.set(4);
      }
      if (struct.isSetSender()) {
        optionals.set(5);
      }
      if (struct.isSetBackup()) {
        optionals.set(6);
      }
      if (struct.isSetRequiredResources()) {
        optionals.set(7);
      }
      oprot.writeBitSet(optionals, 8);
      if (struct.isSetTaskID()) {
        oprot.writeI32(struct.taskID);
      }
      if (struct.isSetJobID()) {
        oprot.writeI32(struct.jobID);
      }
      if (struct.isSetTaskProgram()) {
        oprot.writeBinary(struct.taskProgram);
      }
      if (struct.isSetTaskData()) {
        oprot.writeString(struct.taskData);
      }
      if (struct.isSetClassName()) {
        oprot.writeString(struct.className);
      }
      if (struct.isSetSender()) {
        struct.sender.write(oprot);
      }
      if (struct.isSetBackup()) {
        struct.backup.write(oprot);
      }
      if (struct.isSetRequiredResources()) {
        oprot.writeString(struct.requiredResources);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, Task struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(8);
      if (incoming.get(0)) {
        struct.taskID = iprot.readI32();
        struct.setTaskIDIsSet(true);
      }
      if (incoming.get(1)) {
        struct.jobID = iprot.readI32();
        struct.setJobIDIsSet(true);
      }
      if (incoming.get(2)) {
        struct.taskProgram = iprot.readBinary();
        struct.setTaskProgramIsSet(true);
      }
      if (incoming.get(3)) {
        struct.taskData = iprot.readString();
        struct.setTaskDataIsSet(true);
      }
      if (incoming.get(4)) {
        struct.className = iprot.readString();
        struct.setClassNameIsSet(true);
      }
      if (incoming.get(5)) {
        struct.sender = new NodeData();
        struct.sender.read(iprot);
        struct.setSenderIsSet(true);
      }
      if (incoming.get(6)) {
        struct.backup = new NodeData();
        struct.backup.read(iprot);
        struct.setBackupIsSet(true);
      }
      if (incoming.get(7)) {
        struct.requiredResources = iprot.readString();
        struct.setRequiredResourcesIsSet(true);
      }
    }
  }

}
