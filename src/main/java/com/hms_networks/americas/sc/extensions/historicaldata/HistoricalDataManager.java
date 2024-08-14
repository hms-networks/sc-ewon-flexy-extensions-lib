package com.hms_networks.americas.sc.extensions.historicaldata;

import com.ewon.ewonitf.Exporter;
import com.hms_networks.americas.sc.extensions.datapoint.DataPoint;
import com.hms_networks.americas.sc.extensions.datapoint.DataPointBoolean;
import com.hms_networks.americas.sc.extensions.datapoint.DataPointDword;
import com.hms_networks.americas.sc.extensions.datapoint.DataPointFloat;
import com.hms_networks.americas.sc.extensions.datapoint.DataPointInteger;
import com.hms_networks.americas.sc.extensions.datapoint.DataPointIntegerMappedString;
import com.hms_networks.americas.sc.extensions.datapoint.DataPointString;
import com.hms_networks.americas.sc.extensions.datapoint.DataQuality;
import com.hms_networks.americas.sc.extensions.json.JSONException;
import com.hms_networks.americas.sc.extensions.string.QuoteSafeStringTokenizer;
import com.hms_networks.americas.sc.extensions.string.StringUtils;
import com.hms_networks.americas.sc.extensions.system.time.SCTimeSpan;
import com.hms_networks.americas.sc.extensions.taginfo.TagInfo;
import com.hms_networks.americas.sc.extensions.taginfo.TagInfoEnumeratedIntToString;
import com.hms_networks.americas.sc.extensions.taginfo.TagInfoManager;
import com.hms_networks.americas.sc.extensions.taginfo.TagType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to manage retrieving tag information and historical logs using export block descriptors.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.0.0
 * @version 3.1.0
 */
public class HistoricalDataManager {

  /**
   * Default value for the export data in UTC flag. This is separate from the Ewon's export data in
   * UTC setting, and is used when the value is not specified.
   */
  private static final boolean DEFAULT_EXPORT_DATA_IN_UTC = true;

  /**
   * Reads historical log between <code>startTime</code> and <code>endTime</code>. Returns a list of
   * data points.
   *
   * <p>In version 3.0.0 and later, this method returns data points with an ISO 8601 formatted
   * timestamp. Previous versions used a timestamp integer with the number of seconds since epoch,
   * thus callers of this method must adapt as necessary.
   *
   * @param startTime start time of export
   * @param endTime end time of export
   * @param includeTagGroupA include tag group A
   * @param includeTagGroupB include tag group B
   * @param includeTagGroupC include tag group C
   * @param includeTagGroupD include tag group D
   * @param stringHistorical export string historical logs if true
   * @return data points from response
   * @throws IOException if export block descriptor fails
   * @throws JSONException if unable to parse int to string enumeration file
   * @throws EbdTimeoutException for EBD timeout
   * @since 1.0.0
   * @deprecated Use {@link #readHistoricalFifo(String requestEbd)} instead.
   */
  public static ArrayList readHistoricalFifo(
      String startTime,
      String endTime,
      boolean includeTagGroupA,
      boolean includeTagGroupB,
      boolean includeTagGroupC,
      boolean includeTagGroupD,
      boolean stringHistorical)
      throws IOException, JSONException, EbdTimeoutException {
    return readHistoricalFifo(
        startTime,
        endTime,
        includeTagGroupA,
        includeTagGroupB,
        includeTagGroupC,
        includeTagGroupD,
        stringHistorical,
        DEFAULT_EXPORT_DATA_IN_UTC);
  }

  /**
   * Reads historical log between <code>startTime</code> and <code>endTime</code>. Returns a list of
   * data points.
   *
   * <p>In version 3.0.0 and later, this method returns data points with an ISO 8601 formatted
   * timestamp. Previous versions used a timestamp integer with the number of seconds since epoch,
   * thus callers of this method must adapt as necessary.
   *
   * @param startTime start time of export
   * @param endTime end time of export
   * @param includeTagGroupA include tag group A
   * @param includeTagGroupB include tag group B
   * @param includeTagGroupC include tag group C
   * @param includeTagGroupD include tag group D
   * @param stringHistorical export string historical logs if true
   * @param exportDataInUtc export data in ISO 8601 UTC format if {@code true} (instead of local)
   * @return data points from response
   * @throws IOException if export block descriptor fails
   * @throws JSONException if unable to parse int to string enumeration file
   * @throws EbdTimeoutException for EBD timeout
   * @since 3.0.0
   * @deprecated Use {@link #readHistoricalFifo(String requestEbd)} instead.
   */
  public static ArrayList readHistoricalFifo(
      String startTime,
      String endTime,
      boolean includeTagGroupA,
      boolean includeTagGroupB,
      boolean includeTagGroupC,
      boolean includeTagGroupD,
      boolean stringHistorical,
      boolean exportDataInUtc)
      throws IOException, JSONException, EbdTimeoutException {

    // create EBD string
    final String ebdStr =
        prepareHistoricalFifoReadEBDString(
            startTime,
            endTime,
            includeTagGroupA,
            includeTagGroupB,
            includeTagGroupC,
            includeTagGroupD,
            stringHistorical,
            exportDataInUtc);

    // Execute EBD call and parse results
    final Exporter exporter = executeEbdCall(ebdStr);
    return parseEBDHistoricalLogExportResponse(exporter);
  }

  /**
   * Reads historical log between <code>startTime</code> and <code>endTime</code>. Returns a list of
   * data points.
   *
   * <p>In version 3.0.0 and later, this method returns data points with an ISO 8601 formatted
   * timestamp. Previous versions used a timestamp integer with the number of seconds since epoch,
   * thus callers of this method must adapt as necessary.
   *
   * @param requestEbd historical data request EBD string
   * @return data points from response
   * @throws IOException if export block descriptor fails
   * @throws JSONException if unable to parse int to string enumeration file
   * @throws EbdTimeoutException for EBD timeout
   * @since 3.0.0
   */
  public static ArrayList readHistoricalFifo(String requestEbd)
      throws IOException, JSONException, EbdTimeoutException {

    // Execute EBD call and parse results
    final Exporter exporter = executeEbdCall(requestEbd);
    return parseEBDHistoricalLogExportResponse(exporter);
  }

  /**
   * Reads historical log between <code>startTime</code> and <code>endTime</code>. Returns a map of
   * rounded timestamps to lists of data points. <br>
   * (Parameterized map type: Map&lt;Date, List&lt;DataPoint&gt;&gt;)
   *
   * <p>In version 3.0.0 and later, this method returns data points with an ISO 8601 formatted
   * timestamp. Previous versions used a timestamp integer with the number of seconds since epoch,
   * thus callers of this method must adapt as necessary.
   *
   * @param startTime start time of export
   * @param endTime end time of export
   * @param includeTagGroupA include tag group A
   * @param includeTagGroupB include tag group B
   * @param includeTagGroupC include tag group C
   * @param includeTagGroupD include tag group D
   * @param stringHistorical export string historical logs if true
   * @param timeSpan time span to round data point time stamps to
   * @return data points from response
   * @throws IOException if export block descriptor fails
   * @throws JSONException if unable to parse int to string enumeration file
   * @throws EbdTimeoutException for EBD timeout
   * @throws IllegalArgumentException if time unit is null, unknown, or not supported
   * @throws Exception if unable to parse data point timestamp to date
   * @return map object of data points
   * @since 1.0.0
   * @deprecated use {@link #readHistoricalFifo(String requestEbd, SCTimeSpan timeSpan) } instead.
   */
  public static Map readHistoricalFifo(
      String startTime,
      String endTime,
      boolean includeTagGroupA,
      boolean includeTagGroupB,
      boolean includeTagGroupC,
      boolean includeTagGroupD,
      boolean stringHistorical,
      SCTimeSpan timeSpan)
      throws Exception {
    return readHistoricalFifo(
        startTime,
        endTime,
        includeTagGroupA,
        includeTagGroupB,
        includeTagGroupC,
        includeTagGroupD,
        stringHistorical,
        DEFAULT_EXPORT_DATA_IN_UTC,
        timeSpan);
  }

  /**
   * Reads historical log between <code>startTime</code> and <code>endTime</code>. Returns a map of
   * rounded timestamps to lists of data points. <br>
   * (Parameterized map type: Map&lt;Date, List&lt;DataPoint&gt;&gt;)
   *
   * <p>In version 3.0.0 and later, this method returns data points with an ISO 8601 formatted
   * timestamp. Previous versions used a timestamp integer with the number of seconds since epoch,
   * thus callers of this method must adapt as necessary.
   *
   * @param startTime start time of export
   * @param endTime end time of export
   * @param includeTagGroupA include tag group A
   * @param includeTagGroupB include tag group B
   * @param includeTagGroupC include tag group C
   * @param includeTagGroupD include tag group D
   * @param stringHistorical export string historical logs if true
   * @param exportDataInUtc export data in ISO 8601 UTC format if {@code true} (instead of local)
   * @param timeSpan time span to round data point time stamps to
   * @return data points from response
   * @throws IOException if export block descriptor fails
   * @throws JSONException if unable to parse int to string enumeration file
   * @throws EbdTimeoutException for EBD timeout
   * @throws IllegalArgumentException if time unit is null, unknown, or not supported
   * @throws Exception if unable to parse data point timestamp to date
   * @since 3.0.0
   * @deprecated use {@link #readHistoricalFifo(String requestEbd, SCTimeSpan timeSpan)} instead.
   */
  public static Map readHistoricalFifo(
      String startTime,
      String endTime,
      boolean includeTagGroupA,
      boolean includeTagGroupB,
      boolean includeTagGroupC,
      boolean includeTagGroupD,
      boolean stringHistorical,
      boolean exportDataInUtc,
      SCTimeSpan timeSpan)
      throws Exception {

    // create EBD string
    final String ebdStr =
        prepareHistoricalFifoReadEBDString(
            startTime,
            endTime,
            includeTagGroupA,
            includeTagGroupB,
            includeTagGroupC,
            includeTagGroupD,
            stringHistorical,
            exportDataInUtc);

    // Execute EBD call and parse results
    final Exporter exporter = executeEbdCall(ebdStr);
    return parseEBDHistoricalLogExportResponse(exporter, timeSpan);
  }

  /**
   * Reads historical log between <code>startTime</code> and <code>endTime</code>. Returns a map of
   * rounded timestamps to lists of data points. <br>
   * (Parameterized map type: Map&lt;Date, List&lt;DataPoint&gt;&gt;)
   *
   * <p>In version 3.0.0 and later, this method returns data points with an ISO 8601 formatted
   * timestamp. Previous versions used a timestamp integer with the number of seconds since epoch,
   * thus callers of this method must adapt as necessary.
   *
   * @param requestEbd historical data request EBD string
   * @param timeSpan time span to round data point time stamps to
   * @return data points from response
   * @throws IOException if export block descriptor fails
   * @throws JSONException if unable to parse int to string enumeration file
   * @throws EbdTimeoutException for EBD timeout
   * @throws IllegalArgumentException if time unit is null, unknown, or not supported
   * @throws Exception if unable to parse data point timestamp to date
   * @since 3.0.0
   */
  public static Map readHistoricalFifo(String requestEbd, SCTimeSpan timeSpan) throws Exception {

    // Execute EBD call and parse results
    final Exporter exporter = executeEbdCall(requestEbd);
    return parseEBDHistoricalLogExportResponse(exporter, timeSpan);
  }

  /**
   * Executes EBD call, waits for data return.
   *
   * @param ebdStr EBD string
   * @return EBD Exporter - caller must close exporter
   * @throws EbdTimeoutException when there is no response before timeout period
   * @throws IOException when there is an Exporter Exception
   * @since 1.0.0
   */
  public static Exporter executeEbdCall(String ebdStr) throws IOException, EbdTimeoutException {
    final long start = System.currentTimeMillis();
    final Exporter exporter = new Exporter(ebdStr);
    int available = exporter.available();
    // check on data availability
    while (available == 0) {
      // sleep
      try {
        Thread.sleep(HistoricalDataConstants.DEFAULT_EBD_THREAD_SLEEP_MS);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      // check for timeout
      if (System.currentTimeMillis() - start > HistoricalDataConstants.MAX_EBD_WAIT_MS) {
        exporter.close();
        throw new EbdTimeoutException("Timeout waiting for Export Block Descriptor.");
      }

      // check for data available
      available = exporter.available();
    }
    return exporter;
  }

  /**
   * Prepare Read Historical Log Export Block Descriptor (EBD) string.
   *
   * @param startTime start time of export
   * @param endTime end time of export
   * @param includeTagGroupA include tag group A
   * @param includeTagGroupB include tag group B
   * @param includeTagGroupC include tag group C
   * @param includeTagGroupD include tag group D
   * @param stringHistorical export string historical logs if true
   * @param exportDataInUtc export data in ISO 8601 UTC format if {@code true} (instead of local)
   * @return EBD string
   * @since 1.0.0
   * @deprecated - Use method from {@link HistoricalDataEbdRequest}
   */
  static String prepareHistoricalFifoReadEBDString(
      String startTime,
      String endTime,
      boolean includeTagGroupA,
      boolean includeTagGroupB,
      boolean includeTagGroupC,
      boolean includeTagGroupD,
      boolean stringHistorical,
      boolean exportDataInUtc) {

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

    // Get EBD data type
    String ebdDataType;
    if (stringHistorical) {
      ebdDataType = "HS";
    } else {
      ebdDataType = "HL";
    }

    // Get EBD timestamp type
    String ebdTimestampType;
    if (exportDataInUtc) {
      ebdTimestampType = "U";
    } else {
      ebdTimestampType = "L";
    }

    /*
     * Build EBD string with parameters
     * dt[ebdDataType]: data type, value specified in string ebdDataType
     * ftT: file type, text
     * startTime: start time for data
     * endTime: end time for data
     * ts: timestamp type, value specified in string ebdTimestampType
     * flABCD: filter type, specified tag groups
     */
    return "$dt"
        + ebdDataType
        + "$ftT$st"
        + startTime
        + "$et"
        + endTime
        + "$ts"
        + ebdTimestampType
        + "$fl"
        + tagGroupFilterStr;
  }

  /**
   * Parses Export Block Descriptor Historical Log response into a list of data points. Note: this
   * function only handles Historical Log responses.
   *
   * <p>In version 3.0.0 and later, this method returns data points with an ISO 8601 formatted
   * timestamp in addition to the timestamp integer with the number of seconds since epoch.
   *
   * @param exporter EBD Exporter
   * @return a list of data points from the response
   * @throws IOException for parsing Exceptions
   * @throws JSONException for JSON parsing Exceptions
   * @since 1.0.0
   */
  private static ArrayList parseEBDHistoricalLogExportResponse(Exporter exporter)
      throws IOException, JSONException {

    final String exporterFile = StringUtils.getStringFromInputStream(exporter, "UTF-8");
    final List eventFileLines = StringUtils.split(exporterFile, "\n");
    ArrayList dataPoints = new ArrayList();
    for (int x = 1; x < eventFileLines.size(); x++) {
      String line = (String) eventFileLines.get(x);
      DataPoint lineDataPoint = parseHistoricalFileLine(line.trim());
      if (lineDataPoint != null) {
        dataPoints.add(lineDataPoint);
      }
    }

    exporter.close();

    return dataPoints;
  }

  /**
   * Parses Export Block Descriptor Historical Log response into a map of rounded timestamps to
   * lists of data points. Note: this function only handles Historical Log responses. <br>
   * (Parameterized map type: Map&lt;Date, List&lt;DataPoint&gt;&gt;)
   *
   * <p>In version 3.0.0 and later, this method returns data points with an ISO 8601 formatted
   * timestamp in addition to the timestamp integer with the number of seconds since epoch.
   *
   * @param exporter EBD Exporter
   * @param timeSpan time span to round data point time stamps to
   * @return a map of data points and time stamps from the response
   * @throws IOException for parsing Exceptions
   * @throws JSONException for JSON parsing Exceptions
   * @throws IllegalArgumentException if time unit is null, unknown, or not supported
   * @throws Exception if unable to parse data point timestamp to date
   * @since 2.0.0
   */
  private static Map parseEBDHistoricalLogExportResponse(Exporter exporter, SCTimeSpan timeSpan)
      throws Exception {

    final String exporterFile = StringUtils.getStringFromInputStream(exporter, "UTF-8");
    final List eventFileLines = StringUtils.split(exporterFile, "\n");
    Map dataPoints = new HashMap();
    for (int x = 1; x < eventFileLines.size(); x++) {
      String line = (String) eventFileLines.get(x);
      DataPoint lineDataPoint = parseHistoricalFileLine(line.trim());

      if (lineDataPoint != null) {
        // Round data point time stamp to nearest time unit
        long dataPointTimeStampMilliseconds = lineDataPoint.getTimeStampAsDate().getTime();
        long roundTimeSpanMilliseconds = timeSpan.getTimeSpanMillis();
        long dataPointTimeStampMillisecondsRounded =
            Math.round((double) dataPointTimeStampMilliseconds / (double) roundTimeSpanMilliseconds)
                * roundTimeSpanMilliseconds;

        // Add data point to map
        Date dataPointTimeStampRounded = new Date(dataPointTimeStampMillisecondsRounded);
        if (dataPoints.containsKey(dataPointTimeStampRounded)) {
          // If data point list already exists, add new data point to list
          List dataPointList = (List) dataPoints.get(dataPointTimeStampRounded);
          dataPointList.add(lineDataPoint);
        } else {
          // If data point list does not exist, create new list and add data point to list
          List dataPointList = new ArrayList();
          dataPointList.add(lineDataPoint);
          dataPoints.put(dataPointTimeStampRounded, dataPointList);
        }
      }
    }

    exporter.close();

    return dataPoints;
  }

  /**
   * Convert the supplied string representation of a boolean to its corresponding boolean value.
   *
   * @param strBool boolean string representation
   * @return converted boolean
   * @since 1.0.0
   */
  private static boolean convertStrToBool(String strBool) {
    return strBool.equals("1");
  }

  /**
   * Corrects the Export Block Descriptor (EBD) ISO 8601 timestamp by removing quotes and adding
   * milliseconds to the timestamp.
   *
   * <p>Example: "2020-02-24T15:20:58Z" to 2020-02-24T15:20:58.000Z
   *
   * @param ebdTs Export ISO 8601 timestamp
   * @return corrected ISO 8601 timestamp
   */
  private static String correctEbdIsoTimestamp(String ebdTs) {
    return ebdTs.substring(
            HistoricalDataConstants.EBD_ISO8601_TIMESTAMP_START_INDEX,
            HistoricalDataConstants.EBD_ISO8601_TIMESTAMP_START_MILLIS)
        + HistoricalDataConstants.EBD_ISO8601_TIMESTAMP_ADD_MILLIS_STR
        + ebdTs.substring(
            HistoricalDataConstants.EBD_ISO8601_TIMESTAMP_START_MILLIS, ebdTs.length() - 1);
  }

  /**
   * Parse the specified historical file line and return its corresponding data point.
   *
   * <p>In version 3.0.0 and later, this method returns a data point with an ISO 8601 formatted
   * timestamp in addition to a timestamp integer with the number of seconds since epoch.
   *
   * @param line line to parse
   * @return data point
   * @throws IOException if unable to access tag information
   * @throws JSONException if unable to parse int to string enumeration file
   * @since 1.0.0
   */
  static DataPoint parseHistoricalFileLine(String line) throws IOException, JSONException {
    /*
     * Example Line:
     * "TagId";"TimeInt";"TimeStr";"IsInitValue";"Value";"IQuality"
     * 3;1721834576;"2024-07-24T15:22:56-05:00";0;43;3
     */

    // Create variables to store line data
    int tagId = -1;
    String tagTimeEpochStr = "";
    String tagTimeIso8601Str = "";
    String tagValue = "";
    int tagQuality = DataQuality.GOOD.getRawDataQuality();

    // Create DataPoint for returning
    DataPoint returnVal = null;

    // Create tokenizer to process line
    final boolean returnDelimiters = false;
    QuoteSafeStringTokenizer tokenizer =
        new QuoteSafeStringTokenizer(
            line, HistoricalDataConstants.EBD_LINE_DELIMITER, returnDelimiters);

    // Loop through each token
    while (tokenizer.hasMoreElements()) {

      // Get next token
      String currentToken = tokenizer.nextToken();

      /*
       * For each token in the correct spot, grab the data.
       * Add a TagInfo object to
       */
      switch (tokenizer.getPrevTokenIndex()) {
        case HistoricalDataConstants.EBD_LINE_TAG_ID_INDEX:
          tagId = Integer.parseInt(currentToken);
          break;
        case HistoricalDataConstants.EBD_LINE_TAG_TIMEINT_INDEX:
          tagTimeEpochStr = currentToken;
          break;
        case HistoricalDataConstants.EBD_LINE_TAG_TIMESTR_INDEX:
          tagTimeIso8601Str = correctEbdIsoTimestamp(currentToken);
          break;
        case HistoricalDataConstants.EBD_LINE_TAG_VALUE_INDEX:
          tagValue = currentToken;
          break;
        case (HistoricalDataConstants.EBD_LINE_LENGTH - 1):
          // Get tag quality
          tagQuality = Integer.parseInt(currentToken);
          DataQuality dataQuality = DataQuality.fromRawDataQuality(tagQuality);

          // Check if tag information list available, populate list if not
          boolean tagInfoListAvailable = TagInfoManager.isTagInfoListPopulated();
          if (!tagInfoListAvailable) {
            TagInfoManager.refreshTagList();
          }

          // Get corresponding tag info object for tag
          TagInfo tagInfo;
          try {
            tagInfo = TagInfoManager.getTagInfoFromTagId(tagId);
          } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            break;
          }

          if (tagInfo != null) {
            // Create data point for tag type
            TagType tagType = tagInfo.getType();
            String tagName = tagInfo.getName();
            String tagUnit = tagInfo.getUnit();
            if (tagType == TagType.BOOLEAN) {
              boolean boolValue = convertStrToBool(tagValue);
              returnVal =
                  new DataPointBoolean(
                      tagName,
                      tagId,
                      tagUnit,
                      boolValue,
                      tagTimeEpochStr,
                      tagTimeIso8601Str,
                      dataQuality);
            } else if (tagType == TagType.FLOAT) {
              float floatValue;
              if (tagValue.equalsIgnoreCase(HistoricalDataConstants.TAG_VALUE_NEGATIVE_INFINITY)) {
                floatValue = Float.NEGATIVE_INFINITY;
              } else if (tagValue.equalsIgnoreCase(
                  HistoricalDataConstants.TAG_VALUE_POSITIVE_INFINITY)) {
                floatValue = Float.POSITIVE_INFINITY;
              } else if (tagValue.equalsIgnoreCase(HistoricalDataConstants.TAG_VALUE_NAN)) {
                floatValue = Float.NaN;
              } else {
                floatValue = Float.valueOf(tagValue).floatValue();
              }
              returnVal =
                  new DataPointFloat(
                      tagName,
                      tagId,
                      tagUnit,
                      floatValue,
                      tagTimeEpochStr,
                      tagTimeIso8601Str,
                      dataQuality);
            } else if (tagType == TagType.INTEGER) {
              int intValue = Integer.valueOf(tagValue).intValue();
              returnVal =
                  new DataPointInteger(
                      tagName,
                      tagId,
                      tagUnit,
                      intValue,
                      tagTimeEpochStr,
                      tagTimeIso8601Str,
                      dataQuality);
            } else if (tagType == TagType.INTEGER_MAPPED_STRING) {
              int intValue = Integer.valueOf(tagValue).intValue();
              TagInfoEnumeratedIntToString tagInfoEnumeratedIntToString =
                  (TagInfoEnumeratedIntToString) tagInfo;
              returnVal =
                  new DataPointIntegerMappedString(
                      tagName,
                      tagId,
                      tagUnit,
                      intValue,
                      tagTimeEpochStr,
                      tagTimeIso8601Str,
                      dataQuality,
                      tagInfoEnumeratedIntToString.getEnumeratedStringValueMapping());
            } else if (tagType == TagType.DWORD) {
              long dwordValue = Long.valueOf(tagValue).longValue();
              returnVal =
                  new DataPointDword(
                      tagName,
                      tagId,
                      tagUnit,
                      dwordValue,
                      tagTimeEpochStr,
                      tagTimeIso8601Str,
                      dataQuality);
            } else if (tagType == TagType.STRING) {
              returnVal =
                  new DataPointString(
                      tagName,
                      tagId,
                      tagUnit,
                      tagValue,
                      tagTimeEpochStr,
                      tagTimeIso8601Str,
                      dataQuality);
            }
          }
      }
    }
    return returnVal;
  }
}
