package com.hms_networks.americas.sc.extensions.datapoint;

/**
 * Float data point class
 *
 * <p>Class object for a DataPoint with a float value
 *
 * @since 1.0.0
 * @author HMS Networks, MU Americas Solution Center
 */
public class DataPointFloat extends DataPoint {

  /** Data point value */
  private final float value;

  /**
   * Constructor for a <code>double</code> data point with specified quality.
   *
   * @param tagName data point tag name
   * @param tagId data point tag ID
   * @param tagUnit data point tag unit
   * @param value data point value
   * @param time data point timestamp
   * @param quality data point quality
   */
  public DataPointFloat(
      String tagName, int tagId, String tagUnit, float value, String time, DataQuality quality) {
    this.tagName = tagName;
    this.tagId = tagId;
    this.tagUnit = tagUnit;
    this.value = value;
    this.timestamp = time;
    this.quality = quality;
  }

  /**
   * Constructor for a <code>double</code> data point without specified quality.
   *
   * @param tagName data point tag name
   * @param tagId data point tag ID
   * @param tagUnit data point tag unit
   * @param value data point value
   * @param time data point timestamp
   */
  public DataPointFloat(String tagName, int tagId, String tagUnit, float value, String time) {
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
  public double getValue() {
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
    if (p instanceof DataPointFloat) {
      returnVal =
          p.getTimeStamp().equals(timestamp)
              && ((DataPointFloat) p).getValue() == value
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
    if (p instanceof DataPointFloat) {
      returnVal = ((DataPointFloat) p).getValue() == value;
    }
    return returnVal;
  }

  /**
   * Get the data point type.
   *
   * @return data point type
   */
  public DataType getType() {
    return DataType.FLOAT;
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
    return new Double(value);
  }

  /**
   * Clones the data point.
   *
   * @param tagName The tag name to use for the cloned data point
   * @return cloned data point with the specified tag name
   * @throws CloneNotSupportedException if the data point cannot be cloned
   */
  public DataPoint clone(String tagName) throws CloneNotSupportedException {
    return new DataPointFloat(tagName, tagId, tagUnit, value, timestamp, quality);
  }
}
