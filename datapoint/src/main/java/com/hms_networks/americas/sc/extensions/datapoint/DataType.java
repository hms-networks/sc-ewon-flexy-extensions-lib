package com.hms_networks.americas.sc.extensions.datapoint;

/**
 * Class to represent a tag data type in a similar fashion to enums in Java 1.5+
 *
 * @since 1.0.0
 * @author HMS Networks, MU Americas Solution Center
 */
public class DataType {

  /** Byte assigned to represent the boolean data type. */
  private static final byte DATA_TYPE_BOOLEAN = 0;

  /** Byte assigned to represent the float data type. */
  private static final byte DATA_TYPE_FLOAT = 1;

  /** Byte assigned to represent the integer data type. */
  private static final byte DATA_TYPE_INTEGER = 2;

  /** Byte assigned to represent the DWORD type. */
  private static final byte DATA_TYPE_DWORD = 3;

  /** Byte assigned to represent the string data type. */
  private static final byte DATA_TYPE_STRING = 4;

  /** Byte assigned to represent the integer mapped string data type. */
  private static final byte DATA_TYPE_INTEGER_MAPPED_STRING = 5;

  /** Public instance of {@link DataType} representing boolean data type. */
  public static final DataType BOOLEAN = new DataType(DATA_TYPE_BOOLEAN);

  /** Public instance of {@link DataType} representing float data type. */
  public static final DataType FLOAT = new DataType(DATA_TYPE_FLOAT);

  /** Public instance of {@link DataType} representing integer data type. */
  public static final DataType INTEGER = new DataType(DATA_TYPE_INTEGER);

  /** Public instance of {@link DataType} representing DWORD data type. */
  public static final DataType DWORD = new DataType(DATA_TYPE_DWORD);

  /** Public instance of {@link DataType} representing string data type. */
  public static final DataType STRING = new DataType(DATA_TYPE_STRING);

  /** Public instance of {@link DataType} representing integer mapped string data type. */
  public static final DataType INTEGER_MAPPED_STRING =
      new DataType(DATA_TYPE_INTEGER_MAPPED_STRING);

  /** Instance data type. */
  private final byte instanceDataType;

  /**
   * Private (internal) constructor for creating an instance of {@link DataType} with a selected
   * data type byte.
   *
   * <p>Note: Data type bytes shall be unique.
   *
   * @param instanceDataType byte to represent data type
   */
  private DataType(byte instanceDataType) {
    this.instanceDataType = instanceDataType;
  }

  /**
   * Returns the constant byte representing the data type.
   *
   * @return byte representing data type
   */
  public byte getRawDataType() {
    return instanceDataType;
  }
}
