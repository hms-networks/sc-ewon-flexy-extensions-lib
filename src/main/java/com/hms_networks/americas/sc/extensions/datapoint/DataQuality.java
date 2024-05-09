package com.hms_networks.americas.sc.extensions.datapoint;

/**
 * Class to represent tag data quality in a similar fashion to enums in Java 1.5+
 *
 * @since 1.0.0
 * @version 1.1.0
 * @author HMS Networks, MU Americas Solution Center
 */
public class DataQuality {

  /**
   * The data quality string representation for bad data quality.
   *
   * @since 1.1.0
   */
  private static final String DATA_QUALITY_BAD_STRING = "bad";

  /**
   * The data quality string representation for uncertain data quality.
   *
   * @since 1.1.0
   */
  private static final String DATA_QUALITY_UNCERTAIN_STRING = "uncertain";

  /**
   * The data quality string representation for good data quality.
   *
   * @since 1.1.0
   */
  private static final String DATA_QUALITY_GOOD_STRING = "good";

  /**
   * The data quality string representation for unknown data quality.
   *
   * @since 1.1.0
   */
  private static final String DATA_QUALITY_UNKNOWN_STRING = "unknown";

  /**
   * Byte assigned to represent bad data quality.
   *
   * @since 1.0.0
   */
  private static final byte DATA_QUALITY_BAD = 0;

  /**
   * Byte assigned to represent unknown data quality.
   *
   * @since 1.0.0
   */
  private static final byte DATA_QUALITY_UNCERTAIN = 1;

  /**
   * Byte assigned to represent good data quality.
   *
   * @since 1.0.0
   */
  private static final byte DATA_QUALITY_GOOD = 3;

  /**
   * Public instance of {@link DataQuality} representing bad data quality
   *
   * @since 1.0.0
   */
  public static final DataQuality BAD = new DataQuality(DATA_QUALITY_BAD);

  /**
   * Public instance of {@link DataQuality} representing uncertain data quality.
   *
   * @since 1.0.0
   */
  public static final DataQuality UNCERTAIN = new DataQuality(DATA_QUALITY_UNCERTAIN);

  /**
   * Public instance of {@link DataQuality} representing good data quality.
   *
   * @since 1.0.0
   */
  public static final DataQuality GOOD = new DataQuality(DATA_QUALITY_GOOD);

  /**
   * Instance data quality.
   *
   * @since 1.0.0
   */
  private final byte instanceDataQuality;

  /**
   * Gets the {@link DataQuality} object corresponding to the specified raw data quality, or {@link
   * DataQuality#UNCERTAIN} if the raw data quality is unknown.
   *
   * @param rawDataQuality data quality integer
   * @return corresponding {@link DataQuality} object
   * @since 1.0.0
   */
  public static DataQuality fromRawDataQuality(int rawDataQuality) {
    DataQuality correspondingDataQualityObject = UNCERTAIN;
    if (rawDataQuality == DATA_QUALITY_BAD) {
      correspondingDataQualityObject = BAD;
    } else if (rawDataQuality == DATA_QUALITY_GOOD) {
      correspondingDataQualityObject = GOOD;
    }
    return correspondingDataQualityObject;
  }

  /**
   * Gets the {@link DataQuality} object corresponding to the specified OPCUA quality value, or
   * {@link DataQuality#UNCERTAIN} if the OPCUA quality value is unknown.
   *
   * <p>OPCUA quality values are 16-bit values with the following layout:
   *
   * <pre>
   *   Bits 15-8: Vendor specific
   *   Bits 7-6: Major quality (0-bad, 1-uncertain, 3-good)
   *   Bits 5-2: Sub status code
   *   Bits 1-0: Limit status
   * </pre>
   *
   * <p>This method extracts the major quality bits and returns the corresponding {@link
   * DataQuality} object.
   *
   * @param opcuaQuality OPCUA quality value
   * @return corresponding {@link DataQuality} object
   * @since 1.1.0
   */
  public static DataQuality fromOpcuaQuality(int opcuaQuality) {
    final int majorQualityBitIndex = 6;
    final int majorQualityBitMask = 0x03;
    int majorQuality = (opcuaQuality >> majorQualityBitIndex) & majorQualityBitMask;
    switch (majorQuality) {
      case DATA_QUALITY_GOOD:
        return DataQuality.GOOD;
      case DATA_QUALITY_BAD:
        return DataQuality.BAD;
      case DATA_QUALITY_UNCERTAIN:
      default:
        return DataQuality.UNCERTAIN;
    }
  }

  /**
   * Private (internal) constructor for creating an instance of {@link DataQuality} with a selected
   * data quality byte.
   *
   * <p>Note: Data quality bytes shall be unique.
   *
   * @param instanceDataQuality byte to represent data quality
   * @since 1.0.0
   */
  private DataQuality(byte instanceDataQuality) {
    this.instanceDataQuality = instanceDataQuality;
  }

  /**
   * Returns the constant byte representing the data quality.
   *
   * @return byte representing data quality
   * @since 1.0.0
   */
  public byte getRawDataQuality() {
    return instanceDataQuality;
  }

  /**
   * Returns a string representation of the data quality.
   *
   * @return string representation of data quality
   * @since 1.1.0
   */
  public String toString() {
    String qualityString;
    if (this == GOOD) {
      qualityString = DATA_QUALITY_GOOD_STRING;
    } else if (this == BAD) {
      qualityString = DATA_QUALITY_BAD_STRING;
    } else if (this == UNCERTAIN) {
      qualityString = DATA_QUALITY_UNCERTAIN_STRING;
    } else {
      qualityString = DATA_QUALITY_UNKNOWN_STRING;
    }
    return qualityString;
  }
}
