package com.hms_networks.americas.sc.extensions.datapoint;

/**
 * Class to represent tag data quality in a similar fashion to enums in Java 1.5+
 *
 * @since 1.0.0
 * @author HMS Networks, MU Americas Solution Center
 */
public class DataQuality {

  /** Byte assigned to represent bad data quality. */
  private static final byte DATA_QUALITY_BAD = 0;

  /** Byte assigned to represent unknown data quality. */
  private static final byte DATA_QUALITY_UNCERTAIN = 1;

  /** Byte assigned to represent good data quality. */
  private static final byte DATA_QUALITY_GOOD = 3;

  /** Public instance of {@link DataQuality} representing bad data quality */
  public static final DataQuality BAD = new DataQuality(DATA_QUALITY_BAD);

  /** Public instance of {@link DataQuality} representing uncertain data quality. */
  public static final DataQuality UNCERTAIN = new DataQuality(DATA_QUALITY_UNCERTAIN);

  /** Public instance of {@link DataQuality} representing good data quality. */
  public static final DataQuality GOOD = new DataQuality(DATA_QUALITY_GOOD);

  /** Instance data quality. */
  private final byte instanceDataQuality;

  /**
   * Gets the {@link DataQuality} object corresponding to the specified raw data quality, or {@link
   * DataQuality#UNCERTAIN} if the raw data quality is unknown.
   *
   * @param rawDataQuality data quality integer
   * @return corresponding {@link DataQuality} object
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
   * Private (internal) constructor for creating an instance of {@link DataQuality} with a selected
   * data quality byte.
   *
   * <p>Note: Data quality bytes shall be unique.
   *
   * @param instanceDataQuality byte to represent data quality
   */
  private DataQuality(byte instanceDataQuality) {
    this.instanceDataQuality = instanceDataQuality;
  }

  /**
   * Returns the constant byte representing the data quality.
   *
   * @return byte representing data quality
   */
  public byte getRawDataQuality() {
    return instanceDataQuality;
  }

  public String toString() {
    String qualityString;
    if (this == GOOD) {
      qualityString = "good";
    } else if (this == BAD) {
      qualityString = "bad";
    } else if (this == UNCERTAIN) {
      qualityString = "uncertain";
    } else {
      qualityString = "unknown";
    }
    return qualityString;
  }
}
