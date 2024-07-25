package com.hms_networks.americas.sc.extensions.datapoint;

/**
 * {@link Number} data point class
 *
 * <p>Class object for a DataPoint with a {@link Number} value
 *
 * @since 1.15.14
 * @author HMS Networks; Americas
 */
public class DataPointNumber extends DataPoint {

  /** Data point value */
  protected Number value;

  /**
   * Constructor for a {@link Number} data point with specified quality.
   *
   * @param tagName data point tag name
   * @param tagId data point tag ID
   * @param tagUnit data point tag Unit
   * @param value data point value
   * @param time data point timestamp, UNIX epoch seconds
   * @param timeIso8601 data point timestamp, ISO 8601 format
   * @param quality data point quality
   */
  public DataPointNumber(
      String tagName,
      int tagId,
      String tagUnit,
      Number value,
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
   * Constructor for a {@link Number} data point without specified quality.
   *
   * @param tagName data point tag name
   * @param tagId data point tag ID
   * @param tagUnit data point tag Unit
   * @param value data point value
   * @param time data point timestamp
   * @param timeIso8601 data point timestamp, ISO 8601 format
   */
  public DataPointNumber(
      String tagName, int tagId, String tagUnit, Number value, String time, String timeIso8601) {
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
  public Number getValue() {
    return value;
  }

  /**
   * Set the data point value.
   *
   * <p>This method is unique to the {@link DataPointNumber} class, and is not available in other
   * implementations of {@link DataPoint}.
   *
   * @param value data point value
   */
  public void setValue(Number value) {
    this.value = value;
  }

  /**
   * Compares the data point to another data point.
   *
   * @param p data point to compare
   * @return true if the timestamp, type, and value are the same
   */
  public boolean equals(DataPoint p) {
    boolean returnVal = false;
    if (p instanceof DataPointNumber) {
      returnVal =
          p.getTimeStamp().equals(timestamp)
              && (((DataPointNumber) p).getValue() == null
                  ? value == null
                  : ((DataPointNumber) p).getValue().equals(value))
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
    if (p instanceof DataPointNumber) {
      returnVal =
          ((DataPointNumber) p).getValue() == null
              ? value == null
              : ((DataPointNumber) p).getValue().equals(value);
    }
    return returnVal;
  }

  /**
   * Get the data point type.
   *
   * @return data point type
   */
  public DataType getType() {
    return DataType.NUMBER;
  }

  /**
   * Get the {@link String} representation of the data point value.
   *
   * @return data point value as a {@link String}
   */
  public String getValueString() {
    return value.toString();
  }

  /**
   * Get the {@link Object} representation of the data point value.
   *
   * @return data point value as an {@link Object}
   */
  public Object getValueObject() {
    return value;
  }

  /**
   * Clones the data point.
   *
   * @param tagName The tag name to use for the cloned data point
   * @return cloned data point with the specified tag name
   * @throws CloneNotSupportedException if the data point cannot be cloned
   */
  public DataPoint clone(String tagName) throws CloneNotSupportedException {
    return new DataPointNumber(
        tagName, tagId, tagUnit, value, timestamp, iso8601Timestamp, quality);
  }
}
