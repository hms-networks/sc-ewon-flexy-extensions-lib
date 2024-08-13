package com.hms_networks.americas.sc.extensions.historicaldata;

import com.hms_networks.americas.sc.extensions.system.time.SCTimeUnit;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility methods for building EBD requests for historical data according to Export Block
 * Descriptor REFERENCE GUIDE RG-0009-00-EN, Publication date 30/08/2023
 */
public class HistoricalDataEBDRequest {
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
   * @param exportDataInUtc export data with time string as ISO 8601 UTC format if {@code true}
   *     (instead of local)
   * @return formatted time string for EBD calls
   */
  public static String getEBDTimestampType(boolean exportDataInUtc) {
    // Get EBD timestamp type
    String ebdTimestampType;
    if (exportDataInUtc) {
      ebdTimestampType = "U";
    } else {
      ebdTimestampType = "L";
    }
    return EBD_FIELD_TIMESTAMP + ebdTimestampType;
  }

  /**
   * Get an EBD request sub string for historical data.
   *
   * @param includeTagGroupA include tag group A
   * @param includeTagGroupB include tag group B
   * @param includeTagGroupC include tag group C
   * @param includeTagGroupD include tag group D
   * @throws IllegalArgumentException if no tag groups are included
   * @return EBD request string for tag group filter, can be empty if no tag groups are included
   */
  public static String getEBDStringGroupFilter(
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
   * Get EBD sub string for reading historical tags that are strings. String tags are read from a
   * different source than non-string tags.
   *
   * @return EBD sub string
   */
  public static String getEBDStringHistoricalString() {
    return EBD_FIELD_DATA_TYPE + EBD_FIELD_DATA_TYPE_HISTORICAL_STRING;
  }

  /**
   * Get EBD sub string for reading historical log data type - this does not include string tags.
   *
   * @return EBD sub string
   */
  public static String getEBDStringHistoricalLog() {
    return EBD_FIELD_DATA_TYPE + EBD_FIELD_DATA_TYPE_HISTORICAL_LOG;
  }

  /**
   * Convert a <code>long</code> time value to format required for absolute start time and end time
   * EDB calls. Note that
   *
   * @param time <code>long</code> time value to format
   * @return formatted time string for absolute EBD calls
   */
  private static String convertToEBDAbsoluteTimeFormat(long time) {
    return new SimpleDateFormat(HistoricalDataConstants.EBD_TIME_FORMAT).format(new Date(time));
  }

  /**
   * Convert a <code>long</code> time value to format required for relative EBD calls.
   *
   * @param epochTimeMs <code>long</code> time value - should be epoch time in milliseconds
   * @return formatted time string for relative EBD calls
   */
  public static String convertToEBDRelativeTimeFormat(long epochTimeMs) {
    // the difference between the current time and the epoch time passed as a parameter both rounded
    // to the nearest second
    long diffSeconds =
        SCTimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())
            - SCTimeUnit.MILLISECONDS.toSeconds(epochTimeMs);
    return "s" + Long.toString(diffSeconds);
  }

  public static String prepareHistoricalFifoReadEBDString(
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
    // add historical log or historical string data type
    if (stringHistorical) {
      dataType = getEBDStringHistoricalString();
    } else {
      dataType = getEBDStringHistoricalLog();
    }

    // add format as text
    String formatType = (EBD_FIELD_FORMAT_AS_TEXT);

    // add timestamp type
    String timestampType = getEBDTimestampType(exportDataInUtc);

    // add start time
    String startTimeStr;
    if (useLastPoint) {
      startTimeStr = EBD_FIELD_START_TIME + "L";
    } else {
      if (timeRelative) {
        startTimeStr = EBD_FIELD_START_TIME + "_" + convertToEBDRelativeTimeFormat(startTime);
      } else {
        startTimeStr = EBD_FIELD_START_TIME + convertToEBDAbsoluteTimeFormat(startTime);
      }
    }

    // add end time
    String endTimeStr;
    if (timeRelative) {
      endTimeStr = EBD_FIELD_END_TIME + "_" + convertToEBDRelativeTimeFormat(endTime);
    } else {
      endTimeStr = EBD_FIELD_END_TIME + convertToEBDAbsoluteTimeFormat(endTime);
    }

    // add group filter
    String groupFilter =
        getEBDStringGroupFilter(
            includeTagGroupA, includeTagGroupB, includeTagGroupC, includeTagGroupD);

    // add update time
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
