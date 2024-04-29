package com.hms_networks.americas.sc.extensions.datapoint;

/**
 * Boolean data point class
 *
 * <p>Class object for a DataPoint with a boolean value
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.0.0
 */
public class DataPointBoolean extends DataPoint {

  /** Data point value */
  private final boolean value;

  /**
   * Constructor for a <code>boolean</code> data point with specified quality.
   *
   * @param tagName data point tag name
   * @param tagId data point tag ID
   * @param tagUnit data point tag unit
   * @param value data point value
   * @param time data point timestamp
   * @param quality data point quality
   */
  public DataPointBoolean(
      String tagName, int tagId, String tagUnit, boolean value, String time, DataQuality quality) {
    this.tagName = tagName;
    this.tagId = tagId;
    this.tagUnit = tagUnit;
    this.value = value;
    this.timestamp = time;
    this.quality = quality;
  }

  /**
   * Constructor for a <code>boolean</code> data point without specified quality.
   *
   * @param tagName data point tag name
   * @param tagId data point tag ID
   * @param tagUnit data point unit
   * @param value data point value
   * @param time data point timestamp
   */
  public DataPointBoolean(String tagName, int tagId, String tagUnit, boolean value, String time) {
    this.tagName = tagName;
    this.tagId = tagId;
    this.tagUnit = tagUnit;
    this.value = value;
    this.timestamp = time;
    this.quality = DataQuality.GOOD;
  }

  /**
   * Get and return the data point value.
   *
   * @return data point value
   */
  public boolean getValue() {
    return value;
  }

  /**
   * Compares the data point to another data point.
   *
   * @param p data point to compare
   * @return true if the timestamp, type, and value are the same
   */
  public boolean equals(DataPoint p) {
    boolean returnVal = false;
    if (p instanceof DataPointBoolean) {
      returnVal =
          p.getTimeStamp().equals(timestamp)
              && ((DataPointBoolean) p).getValue() == value
              && p.getTagName().equals(tagName);
    }
    return returnVal;
  }

  /**
   * Compares the data point's value to another data point's value.
   *
   * @param p data point to compare
   * @return true if values are equal
   */
  public boolean valueEquals(DataPoint p) {
    boolean returnVal = false;
    if (p instanceof DataPointBoolean) {
      returnVal = ((DataPointBoolean) p).getValue() == value;
    }
    return returnVal;
  }

  /**
   * Get the data point type.
   *
   * @return data point type
   */
  public DataType getType() {
    return DataType.BOOLEAN;
  }

  /**
   * Get the {@link String} representation of the data point value.
   *
   * @return data point value as a {@link String}
   */
  public String getValueString() {
    return value ? "1" : "0";
  }

  /**
   * Get the {@link Object} representation of the data point value.
   *
   * @return data point value as an {@link Object}
   */
  public Object getValueObject() {
    return Boolean.valueOf(value);
  }

  public DataPoint clone(String tagName) throws CloneNotSupportedException {
    return new DataPointBoolean(tagName, tagId, tagUnit, value, timestamp, quality);
  }
}
