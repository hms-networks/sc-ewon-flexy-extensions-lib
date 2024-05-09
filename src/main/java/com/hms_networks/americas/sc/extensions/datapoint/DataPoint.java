package com.hms_networks.americas.sc.extensions.datapoint;

import com.hms_networks.americas.sc.extensions.system.time.LocalTimeOffsetCalculator;
import com.hms_networks.americas.sc.extensions.system.time.SCTimeUnit;
import com.hms_networks.americas.sc.extensions.system.time.SCTimeUtils;
import java.util.Date;

/**
 * Abstract data point class. Stores tag value and data timestamp from historical logs.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.0.0
 */
public abstract class DataPoint {

  /** Name of data point tag */
  protected String tagName;

  /** ID of data point tag */
  protected int tagId;

  /** Unit of data point tag. */
  protected String tagUnit;

  /** Timestamp of data point */
  protected String timestamp;

  /** Quality of data point value */
  protected DataQuality quality;

  /**
   * Get the name of the data point tag
   *
   * @return name of data point tag
   */
  public String getTagName() {
    return tagName;
  }

  /**
   * Get the ID of the data point tag
   *
   * @return ID of data point tag
   */
  public int getTagId() {
    return tagId;
  }

  /**
   * Get the unit of the data point tag
   *
   * @return unit of data point tag
   */
  public String getTagUnit() {
    return tagUnit;
  }

  /**
   * Sets (overrides) the unit of the data point tag.
   *
   * <p>Unless required, it is recommended to retain the existing tag unit value, which corresponds
   * with the unit configured on the Ewon tag.
   *
   * <p>This method should only be used when the unit of the data point tag is known to be different
   * from the unit configured on the Ewon tag, such as when a value transformation is applied.
   *
   * @param tagUnit the new unit of the data point tag
   */
  public void overrideTagUnit(String tagUnit) {
    this.tagUnit = tagUnit;
  }

  /**
   * Get the {@link String} representation of the time stamp.
   *
   * @return the timestamp as a {@link String}.
   */
  public String getTimeStamp() {
    return timestamp;
  }

  /**
   * Gets the {@link Date} representation of the time stamp.
   *
   * @return the timestamp as a {@link Date}
   * @throws Exception if unable to detect if time stamp is in UTC or local time
   */
  public Date getTimeStampAsDate() throws Exception {
    // Get timestamp in milliseconds
    long timestampMillisecondsTime = SCTimeUnit.SECONDS.toMillis(Long.parseLong(getTimeStamp()));

    // Handle tag timestamp being in local time (default)
    Date timestampDate;
    if (!SCTimeUtils.getTagDataExportedInUtc()) {
      // Add offset to get UTC time (new Date() uses time since epoch in UTC)
      long timestampMillisecondsUtcTime =
          timestampMillisecondsTime + LocalTimeOffsetCalculator.getLocalTimeOffsetMilliseconds();

      timestampDate = new Date(timestampMillisecondsUtcTime);
    }
    // Handle tag timestamp being in UTC time
    else {
      timestampDate = new Date(timestampMillisecondsTime);
    }

    return timestampDate;
  }

  /**
   * Get the quality of the data point value
   *
   * @return quality of data point value
   */
  public DataQuality getQuality() {
    return quality;
  }

  /**
   * Get the tag name, timestamp, value, and the unit returned as a string.
   *
   * @return tag name, timestamp, value, and unit in a string with spaces in between
   */
  public String toString() {
    return tagName + " " + timestamp + " " + getValueString() + " " + tagUnit;
  }

  /**
   * Compares the data point to another data point.
   *
   * @param p data point to compare
   * @return true if the timestamp, type, and value are the same
   */
  public abstract boolean equals(DataPoint p);

  /**
   * Compares the data point's value to another data point's value.
   *
   * @param p data point to compare
   * @return true if values are equal
   */
  public abstract boolean valueEquals(DataPoint p);

  /**
   * Get the data point type.
   *
   * @return data point type
   */
  public abstract DataType getType();

  /**
   * Get the {@link String} representation of the data point value.
   *
   * @return data point value as a {@link String}
   */
  public abstract String getValueString();

  /**
   * Get the {@link Object} representation of the data point value.
   *
   * @return data point value as an {@link Object}
   */
  public abstract Object getValueObject();
}
