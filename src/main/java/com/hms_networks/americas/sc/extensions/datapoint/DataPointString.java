package com.hms_networks.americas.sc.extensions.datapoint;

/**
 * String data point class
 *
 * <p>Class object for a DataPoint with a string value
 *
 * @since 1.0.0
 * @author HMS Networks, MU Americas Solution Center
 */
public class DataPointString extends DataPoint {

  /** Data point value */
  private final String value;

  /**
   * Constructor for a <code>String</code> data point with specified quality.
   *
   * @param tagName data point tag name
   * @param tagId data point tag ID
   * @param tagUnit data point tag Unit
   * @param value data point value
   * @param timestampInt data point timestamp as an {@code int}, representing seconds since the
   *     epoch
   * @param timestampString data point timestamp as a {@link String}, in ISO-8601 format
   * @param quality data point quality
   */
  public DataPointString(
      String tagName,
      int tagId,
      String tagUnit,
      String value,
      int timestampInt,
      String timestampString,
      DataQuality quality) {
    this.tagName = tagName;
    this.tagId = tagId;
    this.tagUnit = tagUnit;
    this.value = value;
    this.timestampInt = timestampInt;
    this.timestampString = timestampString;
    this.quality = quality;
  }

  /**
   * Constructor for a <code>String</code> data point without specified quality.
   *
   * @param tagName data point tag name
   * @param tagId data point tag ID
   * @param tagUnit data point tag Unit
   * @param value data point value
   * @param timestampInt data point timestamp as an {@code int}, representing seconds since the
   *     epoch
   * @param timestampString data point timestamp as a {@link String}, in ISO-8601 format
   */
  public DataPointString(
      String tagName,
      int tagId,
      String tagUnit,
      String value,
      int timestampInt,
      String timestampString) {
    this.tagName = tagName;
    this.tagId = tagId;
    this.tagUnit = tagUnit;
    this.value = value;
    this.timestampInt = timestampInt;
    this.timestampString = timestampString;
    this.quality = DataQuality.GOOD;
  }

  /**
   * Get and return the data point value.
   *
   * @return data point value
   */
  public String getValue() {
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
    if (p instanceof DataPointString) {
      returnVal =
          p.getTimeStampInt() == timestampInt
              && ((DataPointString) p).getValue().equals(value)
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
    if (p instanceof DataPointString) {
      returnVal = ((DataPointString) p).getValueString().equals(value);
    }
    return returnVal;
  }

  /**
   * Get the data point type.
   *
   * @return data point type
   */
  public DataType getType() {
    return DataType.STRING;
  }

  /**
   * Get the {@link String} representation of the data point value.
   *
   * @return data point value as a {@link String}
   */
  public String getValueString() {
    return value;
  }

  /**
   * Get the {@link Object} representation of the data point value.
   *
   * @return data point value as an {@link Object}
   */
  public Object getValueObject() {
    return getValue();
  }

  /**
   * Clones the data point.
   *
   * @param tagName The tag name to use for the cloned data point
   * @return cloned data point with the specified tag name
   * @throws CloneNotSupportedException if the data point cannot be cloned
   */
  public DataPoint clone(String tagName) throws CloneNotSupportedException {
    return new DataPointString(
        tagName, tagId, tagUnit, value, timestampInt, timestampString, quality);
  }
}
