package com.hms_networks.americas.sc.extensions.eventfile;

import com.ewon.ewonitf.Exporter;
import com.hms_networks.americas.sc.extensions.historicaldata.EbdTimeoutException;
import com.hms_networks.americas.sc.extensions.historicaldata.HistoricalDataManager;
import com.hms_networks.americas.sc.extensions.json.JSONException;
import com.hms_networks.americas.sc.extensions.string.QuoteSafeStringTokenizer;
import com.hms_networks.americas.sc.extensions.string.StringUtils;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Class to manage retrieving events using export block descriptors.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.6.0
 */
public class EventFile {
  /** Event File line delimiter */
  private static final String EVENT_FILE_LINE_DELIMITER = "\n";

  /** Event File line token delimiter */
  private static final String EVENT_FILE_LINE_TOKEN_DELIMITER = ";";

  /** Base EBD request format for Event File (log) with absolute starting time */
  private static final String EBD_EVENT_LOG_ABSOLUTE_START_BASE = "$dtEV$ftT$st";

  /** Simple Date Format string for EBD date-time */
  private static final String FLEXY_EBD_EVENT_FILE_DATE_FORMAT = "ddMMyyyy_HHmmss";

  /** Event File expected number of tokens after splitting by delimiter */
  private static final int EVENT_LOG_LINE_EXPECTED_TOKEN_SIZE = 6;

  /** Event File line event ID offset */
  private static final int EVENT_LOG_EVENT_ID_OFFSET = 5;

  /** String encoding for reading Event File */
  private static final String EVENT_FILE_STRING_ENCODING = "UTF-8";

  /**
   * Read EventFile, check for File Circularized event
   * 
   * @deprecated use {@link CircularizedFileCheck}
   * @return boolean - did a File Circularized event occur in the recent past
   * @throws EbdTimeoutException for timeout in EBD read
   * @throws IOException for errors in parsing response
   * @throws JSONException for error parsing JSON
   */
  public static boolean didFileCircularizedEventOccur()
      throws IOException, JSONException, EbdTimeoutException {
    return eventDidOccurInDurationMins(
        EventFileConstants.DEFAULT_DURATION_MINS, EventFileConstants.FILE_CIRCULARIZED_EVENT_ID);
  }

  /**
   * Read EventFile, check for event
   *
   * @param eventId event id to look for
   * @return boolean - did the event occur in the time frame
   * @throws EbdTimeoutException for timeout in EBD read
   * @throws IOException for errors in parsing response
   * @throws JSONException for error parsing JSON
   */
  public static boolean eventDidOccur(int eventId)
      throws IOException, EbdTimeoutException, JSONException {
    return eventDidOccurInDurationMins(EventFileConstants.DEFAULT_DURATION_MINS, eventId);
  }

  /**
   * Read EventFile, check for event
   *
   * @param mins duration in minuets to check for event id
   * @param eventId event id to look for
   * @return boolean - did the event occur in the time frame
   * @throws IOException for errors in parsing response
   * @throws JSONException for error parsing JSON
   * @throws EbdTimeoutException for Export Block Descriptor timeouts
   */
  public static boolean eventDidOccurInDurationMins(int mins, int eventId)
      throws IOException, JSONException, EbdTimeoutException {

    if (mins < EventFileConstants.MIN_DURATION_MINS
        || mins > EventFileConstants.MAX_DURATION_MINS) {
      throw new IllegalArgumentException(
          "The parameter passed must be greater than "
              + EventFileConstants.MIN_DURATION_MINS
              + ", but less than "
              + EventFileConstants.MAX_DURATION_MINS
              + ".");
    }

    String ebdStr = "$dtEV$ftT$st_m" + Integer.toString(mins);
    Exporter ex = HistoricalDataManager.executeEbdCall(ebdStr);
    return parseEventFileExportResponse(ex, Long.toString(eventId));
  }

  /**
   * Parses Export Block Descriptor Event Log response, checks for event
   *
   * @param exporter EBD Exporter
   * @param eventId ID of the target event as a string
   * @return was event ID found
   * @throws IOException for parsing exceptions
   * @throws JSONException for JSON parsing exceptions
   */
  private static boolean parseEventFileExportResponse(Exporter exporter, String eventId)
      throws IOException, JSONException {
    final String exporterFile = StringUtils.getStringFromInputStream(exporter, "UTF-8");
    final List eventFileLines = StringUtils.split(exporterFile, EVENT_FILE_LINE_DELIMITER);

    exporter.close();
    for (int x = 1; x < eventFileLines.size(); x++) {
      String line = (String) eventFileLines.get(x);
      if (checkEventFileLine(line.trim(), eventId) == true) {
        return true;
      }
    }
    return false;
  }

  /**
   * Parse the specified Event file line, return true if id was found.
   *
   * @param line line to parse
   * @param target to check
   * @return boolean was id found
   */
  private static boolean checkEventFileLine(String line, String target) {
    // Create tokenizer to process line
    final boolean returnDelimiters = false;
    QuoteSafeStringTokenizer tokenizer =
        new QuoteSafeStringTokenizer(line, EVENT_FILE_LINE_TOKEN_DELIMITER, returnDelimiters);

    // Loop through each token
    String currentToken = "";
    while (tokenizer.hasMoreElements()) {
      // Get next token
      currentToken = tokenizer.nextToken();
    }
    return currentToken.equals(target);
  }

  /**
   * Get the Export Block Descriptor request for Event File lines after the timestamp argument.
   *
   * @param timestampMilliseconds - the start time for request
   * @return request string
   */
  private static String getEbdEventFileSinceAbsoluteTimeRequest(long timestampMilliseconds) {
    Date TimeDateObj = new Date(timestampMilliseconds);
    SimpleDateFormat sf = new SimpleDateFormat(FLEXY_EBD_EVENT_FILE_DATE_FORMAT);
    return EBD_EVENT_LOG_ABSOLUTE_START_BASE + sf.format(TimeDateObj);
  }

  /**
   * Read Event File via EBD using absolute start time, check for event Id
   *
   * @param eventId - ID of the target event
   * @param absoluteStartTimeMilliseconds - epoch time in milliseconds as start time
   * @return boolean - true/false found event ID in logs
   * @throws EbdTimeoutException - for timeout of EBD request
   * @throws IOException - for exception with input stream
   */
  public static boolean didEventOccurSinceAbsolute(
      String eventId, long absoluteStartTimeMilliseconds) throws IOException, EbdTimeoutException {
    Exporter ex =
        HistoricalDataManager.executeEbdCall(
            getEbdEventFileSinceAbsoluteTimeRequest(absoluteStartTimeMilliseconds));
    String exporterFile = StringUtils.getStringFromInputStream(ex, EVENT_FILE_STRING_ENCODING);
    ex.close();

    if (exporterFile.length() > 0) {
      final List eventFileLines = StringUtils.split(exporterFile, EVENT_FILE_LINE_DELIMITER);
      for (int x = 1; x < eventFileLines.size(); x++) {
        String line = (String) eventFileLines.get(x);
        final List tokens = StringUtils.split(line, EVENT_FILE_LINE_TOKEN_DELIMITER);
        if (tokens.size() == EVENT_LOG_LINE_EXPECTED_TOKEN_SIZE) {
          String tokenErrorId = (String) tokens.get(EVENT_LOG_EVENT_ID_OFFSET);
          String trimmedTokenErrorId = tokenErrorId.trim();
          if (trimmedTokenErrorId.compareTo(eventId) == 0) {
            return true;
          }
        }
      }
    }
    return false;
  }
}
