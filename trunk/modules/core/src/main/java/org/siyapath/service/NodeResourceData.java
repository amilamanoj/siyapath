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

/**
 * Contains information about a particular node
 */
public class NodeResourceData implements org.apache.thrift.TBase<NodeResourceData, NodeResourceData._Fields>, java.io.Serializable, Cloneable {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("NodeResourceData");

  private static final org.apache.thrift.protocol.TField NODE_DATA_FIELD_DESC = new org.apache.thrift.protocol.TField("nodeData", org.apache.thrift.protocol.TType.STRUCT, (short)1);
  private static final org.apache.thrift.protocol.TField NODE_PROPERTIES_FIELD_DESC = new org.apache.thrift.protocol.TField("nodeProperties", org.apache.thrift.protocol.TType.MAP, (short)2);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new NodeResourceDataStandardSchemeFactory());
    schemes.put(TupleScheme.class, new NodeResourceDataTupleSchemeFactory());
  }

  public NodeData nodeData; // required
  public Map<String,String> nodeProperties; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    NODE_DATA((short)1, "nodeData"),
    NODE_PROPERTIES((short)2, "nodeProperties");

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
        case 1: // NODE_DATA
          return NODE_DATA;
        case 2: // NODE_PROPERTIES
          return NODE_PROPERTIES;
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
    tmpMap.put(_Fields.NODE_DATA, new org.apache.thrift.meta_data.FieldMetaData("nodeData", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, NodeData.class)));
    tmpMap.put(_Fields.NODE_PROPERTIES, new org.apache.thrift.meta_data.FieldMetaData("nodeProperties", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.MapMetaData(org.apache.thrift.protocol.TType.MAP, 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING), 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING))));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(NodeResourceData.class, metaDataMap);
  }

  public NodeResourceData() {
  }

  public NodeResourceData(
    NodeData nodeData,
    Map<String,String> nodeProperties)
  {
    this();
    this.nodeData = nodeData;
    this.nodeProperties = nodeProperties;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public NodeResourceData(NodeResourceData other) {
    if (other.isSetNodeData()) {
      this.nodeData = new NodeData(other.nodeData);
    }
    if (other.isSetNodeProperties()) {
      Map<String,String> __this__nodeProperties = new HashMap<String,String>();
      for (Map.Entry<String, String> other_element : other.nodeProperties.entrySet()) {

        String other_element_key = other_element.getKey();
        String other_element_value = other_element.getValue();

        String __this__nodeProperties_copy_key = other_element_key;

        String __this__nodeProperties_copy_value = other_element_value;

        __this__nodeProperties.put(__this__nodeProperties_copy_key, __this__nodeProperties_copy_value);
      }
      this.nodeProperties = __this__nodeProperties;
    }
  }

  public NodeResourceData deepCopy() {
    return new NodeResourceData(this);
  }

  @Override
  public void clear() {
    this.nodeData = null;
    this.nodeProperties = null;
  }

  public NodeData getNodeData() {
    return this.nodeData;
  }

  public NodeResourceData setNodeData(NodeData nodeData) {
    this.nodeData = nodeData;
    return this;
  }

  public void unsetNodeData() {
    this.nodeData = null;
  }

  /** Returns true if field nodeData is set (has been assigned a value) and false otherwise */
  public boolean isSetNodeData() {
    return this.nodeData != null;
  }

  public void setNodeDataIsSet(boolean value) {
    if (!value) {
      this.nodeData = null;
    }
  }

  public int getNodePropertiesSize() {
    return (this.nodeProperties == null) ? 0 : this.nodeProperties.size();
  }

  public void putToNodeProperties(String key, String val) {
    if (this.nodeProperties == null) {
      this.nodeProperties = new HashMap<String,String>();
    }
    this.nodeProperties.put(key, val);
  }

  public Map<String,String> getNodeProperties() {
    return this.nodeProperties;
  }

  public NodeResourceData setNodeProperties(Map<String,String> nodeProperties) {
    this.nodeProperties = nodeProperties;
    return this;
  }

  public void unsetNodeProperties() {
    this.nodeProperties = null;
  }

  /** Returns true if field nodeProperties is set (has been assigned a value) and false otherwise */
  public boolean isSetNodeProperties() {
    return this.nodeProperties != null;
  }

  public void setNodePropertiesIsSet(boolean value) {
    if (!value) {
      this.nodeProperties = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case NODE_DATA:
      if (value == null) {
        unsetNodeData();
      } else {
        setNodeData((NodeData)value);
      }
      break;

    case NODE_PROPERTIES:
      if (value == null) {
        unsetNodeProperties();
      } else {
        setNodeProperties((Map<String,String>)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case NODE_DATA:
      return getNodeData();

    case NODE_PROPERTIES:
      return getNodeProperties();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case NODE_DATA:
      return isSetNodeData();
    case NODE_PROPERTIES:
      return isSetNodeProperties();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof NodeResourceData)
      return this.equals((NodeResourceData)that);
    return false;
  }

  public boolean equals(NodeResourceData that) {
    if (that == null)
      return false;

    boolean this_present_nodeData = true && this.isSetNodeData();
    boolean that_present_nodeData = true && that.isSetNodeData();
    if (this_present_nodeData || that_present_nodeData) {
      if (!(this_present_nodeData && that_present_nodeData))
        return false;
      if (!this.nodeData.equals(that.nodeData))
        return false;
    }

    boolean this_present_nodeProperties = true && this.isSetNodeProperties();
    boolean that_present_nodeProperties = true && that.isSetNodeProperties();
    if (this_present_nodeProperties || that_present_nodeProperties) {
      if (!(this_present_nodeProperties && that_present_nodeProperties))
        return false;
      if (!this.nodeProperties.equals(that.nodeProperties))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();

    boolean present_nodeData = true && (isSetNodeData());
    builder.append(present_nodeData);
    if (present_nodeData)
      builder.append(nodeData);

    boolean present_nodeProperties = true && (isSetNodeProperties());
    builder.append(present_nodeProperties);
    if (present_nodeProperties)
      builder.append(nodeProperties);

    return builder.toHashCode();
  }

  public int compareTo(NodeResourceData other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;
    NodeResourceData typedOther = (NodeResourceData)other;

    lastComparison = Boolean.valueOf(isSetNodeData()).compareTo(typedOther.isSetNodeData());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetNodeData()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.nodeData, typedOther.nodeData);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetNodeProperties()).compareTo(typedOther.isSetNodeProperties());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetNodeProperties()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.nodeProperties, typedOther.nodeProperties);
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
    StringBuilder sb = new StringBuilder("NodeResourceData(");
    boolean first = true;

    sb.append("nodeData:");
    if (this.nodeData == null) {
      sb.append("null");
    } else {
      sb.append(this.nodeData);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("nodeProperties:");
    if (this.nodeProperties == null) {
      sb.append("null");
    } else {
      sb.append(this.nodeProperties);
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

  private static class NodeResourceDataStandardSchemeFactory implements SchemeFactory {
    public NodeResourceDataStandardScheme getScheme() {
      return new NodeResourceDataStandardScheme();
    }
  }

  private static class NodeResourceDataStandardScheme extends StandardScheme<NodeResourceData> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, NodeResourceData struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // NODE_DATA
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.nodeData = new NodeData();
              struct.nodeData.read(iprot);
              struct.setNodeDataIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // NODE_PROPERTIES
            if (schemeField.type == org.apache.thrift.protocol.TType.MAP) {
              {
                org.apache.thrift.protocol.TMap _map0 = iprot.readMapBegin();
                struct.nodeProperties = new HashMap<String,String>(2*_map0.size);
                for (int _i1 = 0; _i1 < _map0.size; ++_i1)
                {
                  String _key2; // required
                  String _val3; // required
                  _key2 = iprot.readString();
                  _val3 = iprot.readString();
                  struct.nodeProperties.put(_key2, _val3);
                }
                iprot.readMapEnd();
              }
              struct.setNodePropertiesIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, NodeResourceData struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.nodeData != null) {
        oprot.writeFieldBegin(NODE_DATA_FIELD_DESC);
        struct.nodeData.write(oprot);
        oprot.writeFieldEnd();
      }
      if (struct.nodeProperties != null) {
        oprot.writeFieldBegin(NODE_PROPERTIES_FIELD_DESC);
        {
          oprot.writeMapBegin(new org.apache.thrift.protocol.TMap(org.apache.thrift.protocol.TType.STRING, org.apache.thrift.protocol.TType.STRING, struct.nodeProperties.size()));
          for (Map.Entry<String, String> _iter4 : struct.nodeProperties.entrySet())
          {
            oprot.writeString(_iter4.getKey());
            oprot.writeString(_iter4.getValue());
          }
          oprot.writeMapEnd();
        }
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class NodeResourceDataTupleSchemeFactory implements SchemeFactory {
    public NodeResourceDataTupleScheme getScheme() {
      return new NodeResourceDataTupleScheme();
    }
  }

  private static class NodeResourceDataTupleScheme extends TupleScheme<NodeResourceData> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, NodeResourceData struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetNodeData()) {
        optionals.set(0);
      }
      if (struct.isSetNodeProperties()) {
        optionals.set(1);
      }
      oprot.writeBitSet(optionals, 2);
      if (struct.isSetNodeData()) {
        struct.nodeData.write(oprot);
      }
      if (struct.isSetNodeProperties()) {
        {
          oprot.writeI32(struct.nodeProperties.size());
          for (Map.Entry<String, String> _iter5 : struct.nodeProperties.entrySet())
          {
            oprot.writeString(_iter5.getKey());
            oprot.writeString(_iter5.getValue());
          }
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, NodeResourceData struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(2);
      if (incoming.get(0)) {
        struct.nodeData = new NodeData();
        struct.nodeData.read(iprot);
        struct.setNodeDataIsSet(true);
      }
      if (incoming.get(1)) {
        {
          org.apache.thrift.protocol.TMap _map6 = new org.apache.thrift.protocol.TMap(org.apache.thrift.protocol.TType.STRING, org.apache.thrift.protocol.TType.STRING, iprot.readI32());
          struct.nodeProperties = new HashMap<String,String>(2*_map6.size);
          for (int _i7 = 0; _i7 < _map6.size; ++_i7)
          {
            String _key8; // required
            String _val9; // required
            _key8 = iprot.readString();
            _val9 = iprot.readString();
            struct.nodeProperties.put(_key8, _val9);
          }
        }
        struct.setNodePropertiesIsSet(true);
      }
    }
  }

}

