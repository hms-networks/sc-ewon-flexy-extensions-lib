package com.hms_networks.americas.sc.extensions.datapoint;

/**
 * Integer data point class
 *
 * <p>Class object for a DataPoint with a integer value
 *
 * @since 1.0.0
 * @author HMS Networks, MU Americas Solution Center
 */
public class DataPointInteger extends DataPoint {

  /** Data point value */
  private final int value;

  /**
   * Constructor for a <code>integer</code> data point with specified quality.
   *
   * @param tagName data point tag name
   * @param tagId data point tag ID
   * @param tagUnit data point tag Unit
   * @param value data point value
   * @param time data point timestamp, UNIX epoch seconds
   * @param timeIso8601 data point timestamp, ISO 8601 format
   * @param quality data point quality
   */
  public DataPointInteger(
      String tagName,
      int tagId,
      String tagUnit,
      int value,
      String time,
      String timeIso8601,
      DataQuality quality) {
    this.tagName = tagName;
    this.tagId = tagId;
    this.tagUnit = tagUnit;
    this.value = value;
    this.timestamp = time;
    this.iso8601Timestamp = timeIso8601;
    this.quality = quality;
  }

  /**
   * Constructor for a <code>integer</code> data point without specified quality.
   *
   * @param tagName data point tag name
   * @param tagId data point tag ID
   * @param tagUnit data point tag Unit
   * @param value data point value
   * @param time data point timestamp, UNIX epoch seconds
   * @param timeIso8601 data point timestamp, ISO 8601 format
   */
  public DataPointInteger(
      String tagName, int tagId, String tagUnit, int value, String time, String timeIso8601) {
    this.tagName = tagName;
    this.tagId = tagId;
    this.tagUnit = tagUnit;
    this.value = value;
    this.timestamp = time;
    this.iso8601Timestamp = timeIso8601;
    this.quality = DataQuality.GOOD;
  }

  /**
   * Get and return the data point value.
   *
   * @return data point value
   */
  public int getValue() {
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
    if (p instanceof DataPointInteger) {
      returnVal =
          p.getTimeStamp().equals(timestamp)
              && ((DataPointInteger) p).getValue() == value
              && p.getTagName().equals(tagName);
    }
    return returnVal;
  }

  /**
   * Compares the data point's value to another data point's value.
   *
   * @param p data point to compare
   * @return true if data points are of the same type and values are equal
   */
  public boolean valueEquals(DataPoint p) {
    boolean returnVal = false;
    if (p instanceof DataPointInteger) {
      returnVal = ((DataPointInteger) p).getValue() == value;
    }
    return returnVal;
  }

  /**
   * Get the data point type.
   *
   * @return data point type
   */
  public DataType getType() {
    return DataType.INTEGER;
  }

  /**
   * Get the {@link String} representation of the data point value.
   *
   * @return data point value as a {@link String}
   */
  public String getValueString() {
    return ("" + value);
  }

  /**
   * Get the {@link Object} representation of the data point value.
   *
   * @return data point value as an {@link Object}
   */
  public Object getValueObject() {
    return new Integer(value);
  }

  /**
   * Clones the data point.
   *
   * @param tagName The tag name to use for the cloned data point
   * @return cloned data point with the specified tag name
   * @throws CloneNotSupportedException if the data point cannot be cloned
   */
  public DataPoint clone(String tagName) throws CloneNotSupportedException {
    return new DataPointInteger(
        tagName, tagId, tagUnit, value, timestamp, iso8601Timestamp, quality);
  }
}
