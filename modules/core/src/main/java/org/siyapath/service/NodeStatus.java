/**
 * Autogenerated by Thrift Compiler (0.8.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package org.siyapath.service;


import java.util.Map;
import java.util.HashMap;
import org.apache.thrift.TEnum;

public enum NodeStatus implements org.apache.thrift.TEnum {
  CREATED(1),
  STARTING(2),
  PROCESSING_IDLE(3),
  PROCESSING_BUSY(4),
  DISTRIBUTING(5),
  BUSY(6),
  IDLE(7);

  private final int value;

  private NodeStatus(int value) {
    this.value = value;
  }

  /**
   * Get the integer value of this enum value, as defined in the Thrift IDL.
   */
  public int getValue() {
    return value;
  }

  /**
   * Find a the enum type by its integer value, as defined in the Thrift IDL.
   * @return null if the value is not found.
   */
  public static NodeStatus findByValue(int value) { 
    switch (value) {
      case 1:
        return CREATED;
      case 2:
        return STARTING;
      case 3:
        return PROCESSING_IDLE;
      case 4:
        return PROCESSING_BUSY;
      case 5:
        return DISTRIBUTING;
      case 6:
        return BUSY;
      case 7:
        return IDLE;
      default:
        return null;
    }
  }
}
