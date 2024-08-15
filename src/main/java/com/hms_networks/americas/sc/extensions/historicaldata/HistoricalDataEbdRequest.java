package com.hms_networks.americas.sc.extensions.historicaldata;

import com.hms_networks.americas.sc.extensions.system.time.SCTimeUnit;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility methods for building EBD requests for historical data according to Export Block
 * Descriptor REFERENCE GUIDE RG-0009-00-EN, Publication date 30/08/2023
 *
 * @author HMS Americas FAE team
 * @version 1.0
 * @since 1.16.0
 */
public class HistoricalDataEbdRequest {
  /** EBD syntax for group filter */
  private static final String EBD_FIELD_GROUP_FILTER = "$fl";

  /** EBD syntax for data type */
  private static final String EBD_FIELD_DATA_TYPE = "$dt";

  /** EBD syntax for data type historical log */
  private static final String EBD_FIELD_DATA_TYPE_HISTORICAL_LOG = "HL";

  /** EBD syntax for data type historical string */
  private static final String EBD_FIELD_DATA_TYPE_HISTORICAL_STRING = "HS";

  /** EBD syntax for start time */
  private static final String EBD_FIELD_START_TIME = "$st";

  /** EBD syntax for start time */
  private static final String EBD_FIELD_END_TIME = "$et";

  /** EBD syntax for respond format text */
  private static final String EBD_FIELD_FORMAT_AS_TEXT = "$ftT";

  /** EBD syntax for timestamp type */
  private static final String EBD_FIELD_TIMESTAMP = "$ts";

  /** EBD syntax for Flexy should keep last point in memory for subsequent historical data reads */
  private static final String EBD_FIELD_UPDATE_TIME = "$ut";

  /**
   * Get EBD request string for timestamp type.
   *
   * @param exportDataInUtc export data with time string as ISO 8601 UTC format if {@code true}
   *     (instead of local)
   * @return formatted time string for EBD calls
   */
  private static String getEbdTimestampType(boolean exportDataInUtc) {
    // For details, see RG-0009-00-EN, Publication date 30/08/2023
    final String EBD_TIME_FORMAT_LOCAL = "L";
    final String EBD_TIME_FORMAT_UTC = "U";
    // Get EBD timestamp type
    String ebdTimestampType;
    if (exportDataInUtc) {
      // Results in UTC time format: YYYY-MM-DDTHH:MM:SSZ -Example: 2018-09-19T10:48:12Z
      ebdTimestampType = EBD_TIME_FORMAT_UTC;
    } else {
      // Results in Local time format: YYYY-MM-DDTHH:MM:SS±000 -Example: 2018-09-19T12:48:12+0200
      ebdTimestampType = EBD_TIME_FORMAT_LOCAL;
    }
    return EBD_FIELD_TIMESTAMP + ebdTimestampType;
  }

  /**
   * Get EBD request string for group filter.
   *
   * @param includeTagGroupA include tag group A
   * @param includeTagGroupB include tag group B
   * @param includeTagGroupC include tag group C
   * @param includeTagGroupD include tag group D
   * @throws IllegalArgumentException if no tag groups are included
   * @return EBD request string for tag group filter, can be empty if no tag groups are included
   */
  private static String getEbdStringGroupFilter(
      boolean includeTagGroupA,
      boolean includeTagGroupB,
      boolean includeTagGroupC,
      boolean includeTagGroupD)
      throws IllegalArgumentException {
    // Check for valid group selection
    if (!includeTagGroupA && !includeTagGroupB && !includeTagGroupC && !includeTagGroupD) {
      throw new IllegalArgumentException(
          "Cannot generate historical logs with no tag groups selected.");
    }

    // Build string of tag groups for filter type
    String tagGroupFilterStr = "";
    if (includeTagGroupA) {
      tagGroupFilterStr += "A";
    }
    if (includeTagGroupB) {
      tagGroupFilterStr += "B";
    }
    if (includeTagGroupC) {
      tagGroupFilterStr += "C";
    }
    if (includeTagGroupD) {
      tagGroupFilterStr += "D";
    }

    return EBD_FIELD_GROUP_FILTER + tagGroupFilterStr;
  }

  /**
   * Get EBD string for reading historical tags that are strings. String tags are read from a
   * different source than non-string tags.
   *
   * @return EBD sub string
   */
  public static String getEbdStringHistoricalString() {
    return EBD_FIELD_DATA_TYPE + EBD_FIELD_DATA_TYPE_HISTORICAL_STRING;
  }

  /**
   * Get EBD string for reading historical log data type - this does not include string tags.
   *
   * @return EBD sub string
   */
  private static String getEbdStringHistoricalLog() {
    return EBD_FIELD_DATA_TYPE + EBD_FIELD_DATA_TYPE_HISTORICAL_LOG;
  }

  /**
   * Convert a <code>long</code> time value to format required for absolute start time and end time
   * EDB calls. Note that this method will format the time in the local time zone which is what EBD
   * absolute time calls expect.
   *
   * @param time <code>long</code> time value to format
   * @return formatted time string for absolute EBD calls
   */
  private static String convertToEbdAbsoluteTimeFormat(long time) {
    return new SimpleDateFormat(HistoricalDataConstants.EBD_TIME_FORMAT).format(new Date(time));
  }

  /**
   * Convert a <code>long</code> epoch millisecond timestamp to format required for relative EBD
   * calls. Will round to the nearest second. Use of update time makes the lack of precision a
   * non-issue.
   *
   * @param epochTimeMs <code>long</code> time value - should be epoch time in milliseconds
   * @return formatted time string for relative EBD calls
   */
  private static String convertToEbdRelativeTimeFormat(long epochTimeMs) {
    // the difference between the current time and the epoch time passed as a parameter both rounded
    // to the nearest second
    long diffSeconds =
        SCTimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())
            - SCTimeUnit.MILLISECONDS.toSeconds(epochTimeMs);
    return "s" + Long.toString(diffSeconds);
  }

  /**
   * Prepare an EBD string for reading historical data from a Flexy. This method will build the EBD
   * request based on the provided parameters.
   *
   * @param startTime millisecond epoch timestamp for the start time, should be relative to UTC
   * @param endTime millisecond epoch timestamp for the end time, should be relative to UTC
   * @param timeRelative if {@code true}, the start and end times are relative to the current time,
   *     otherwise they are absolute
   * @param useLastPoint if {@code true}, the last point in memory will be used as the start time
   * @param updateLastPoint if {@code true}, the last point in memory will be updated to the end
   *     time
   * @param includeTagGroupA if {@code true}, include tag group A in the request
   * @param includeTagGroupB if {@code true}, include tag group B in the request
   * @param includeTagGroupC if {@code true}, include tag group C in the request
   * @param includeTagGroupD if {@code true}, include tag group D in the request
   * @param stringHistorical if {@code true}, the request will be for string historical data,
   *     otherwise it will be for historical log data
   * @param exportDataInUtc if {@code true}, the data will be exported in UTC time, otherwise it
   *     will be exported in local time
   * @return EBD request string for historical data
   */
  public static String prepareHistoricalFifoReadEbdString(
      long startTime,
      long endTime,
      boolean timeRelative,
      boolean useLastPoint,
      boolean updateLastPoint,
      boolean includeTagGroupA,
      boolean includeTagGroupB,
      boolean includeTagGroupC,
      boolean includeTagGroupD,
      boolean stringHistorical,
      boolean exportDataInUtc) {

    String dataType;
    // Add historical log or historical string data type
    if (stringHistorical) {
      dataType = getEbdStringHistoricalString();
    } else {
      dataType = getEbdStringHistoricalLog();
    }

    // Add format as text
    String formatType = (EBD_FIELD_FORMAT_AS_TEXT);

    // Add timestamp type
    String timestampType = getEbdTimestampType(exportDataInUtc);

    // Add start time
    String startTimeStr;
    if (useLastPoint) {
      startTimeStr = EBD_FIELD_START_TIME + "L";
    } else {
      if (timeRelative) {
        startTimeStr = EBD_FIELD_START_TIME + "_" + convertToEbdRelativeTimeFormat(startTime);
      } else {
        startTimeStr = EBD_FIELD_START_TIME + convertToEbdAbsoluteTimeFormat(startTime);
      }
    }

    // Add end time
    String endTimeStr;
    if (timeRelative) {
      endTimeStr = EBD_FIELD_END_TIME + "_" + convertToEbdRelativeTimeFormat(endTime);
    } else {
      endTimeStr = EBD_FIELD_END_TIME + convertToEbdAbsoluteTimeFormat(endTime);
    }

    // Add group filter
    String groupFilter =
        getEbdStringGroupFilter(
            includeTagGroupA, includeTagGroupB, includeTagGroupC, includeTagGroupD);

    // Add update time
    String updateTime;
    if (updateLastPoint) {
      updateTime = EBD_FIELD_UPDATE_TIME;
    } else {
      updateTime = "";
    }

    return dataType
        + formatType
        + timestampType
        + startTimeStr
        + endTimeStr
        + groupFilter
        + updateTime;
  }
}
