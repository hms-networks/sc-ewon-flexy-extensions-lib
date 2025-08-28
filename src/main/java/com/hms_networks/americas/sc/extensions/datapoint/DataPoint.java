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

  /** Default initialization status for tags created with legacy constructors. */
  public static final boolean DEFAULT_TAG_IS_INIT_VALUE = false;

  /** Name of data point tag */
  protected String tagName;

  /** ID of data point tag */
  protected int tagId;

  /** Unit of data point tag. */
  protected String tagUnit;

  /**
   * Timestamp of data point in seconds since UNIX epoch. Note that timestamp could be relative to
   * UTC or local time depending on Record Data in UTC setting. Please see <a
   * href="https://hmsnetworks.blob.core.windows.net/www/docs/librariesprovider10/downloads-monitored/manuals/knowledge-base/kb-0284-01-en-utc-timestamp-logging.pdf">Knowledge
   * base article on Flexy UTC historical logging setting</a> for details on historical data
   * timestamp settings.
   */
  protected String timestamp;

  /**
   * ISO 8601 format timestamp with millisecond resolution. Can be relative to UTC, or local time.
   * Please see {@link
   * com.hms_networks.americas.sc.extensions.historicaldata.HistoricalDataQueueManager} and
   * exportDataInUtc parameter.
   */
  protected String iso8601Timestamp;

  /** Quality of data point value */
  protected DataQuality quality;

  /** Flag indicating if the data point is an initialization value */
  protected boolean isInitValue;

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
   * Get the {@link String} representation of the UNIX epoch seconds timestamp. Because the
   * timestamp could be in local time or UTC time, it is recommended to use {@link
   * #getIso8601Timestamp} or {@link #getTimeStampAsDate} instead.
   *
   * @return the timestamp as a {@link String}.
   * @deprecated - Use {@link #getIso8601Timestamp} or {@link #getTimeStampAsDate} instead.
   */
  public String getTimeStamp() {
    return timestamp;
  }

  /**
   * Get the {@link String} representation of the ISO 8601 timestamp with milliseconds.
   *
   * <p>Example of local time offset: 2024-07-24T15:08:26.000-05:00
   *
   * <p>Example of UTC time: 2024-07-24T15:13:26.000Z
   *
   * @return the ISO 8601 timestamp as a {@link String}.
   * @since 1.1.0
   */
  public String getIso8601Timestamp() {
    return iso8601Timestamp;
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
   * @return true if data points are of the same type and values are equal
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

  /**
   * Clones the data point.
   *
   * @param tagName The tag name to use for the cloned data point
   * @return cloned data point with the specified tag name
   * @throws CloneNotSupportedException if the data point cannot be cloned
   */
  public abstract DataPoint clone(String tagName) throws CloneNotSupportedException;

  /**
   * Get the {@link boolean} representation of whether the data point is an initialization value.
   *
   * @return <code>true</code> if the data point is an initialization value, <code>false</code>
   *     otherwise
   */
  public boolean isInitValue() {
    return isInitValue;
  }
}
