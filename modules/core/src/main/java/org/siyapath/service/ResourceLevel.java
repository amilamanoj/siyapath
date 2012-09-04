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

public enum ResourceLevel implements org.apache.thrift.TEnum {
  LOW(1),
  MEDIUM(2),
  HIGH(3);

  private final int value;

  private ResourceLevel(int value) {
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
  public static ResourceLevel findByValue(int value) { 
    switch (value) {
      case 1:
        return LOW;
      case 2:
        return MEDIUM;
      case 3:
        return HIGH;
      default:
        return null;
    }
  }
}
