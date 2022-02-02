package com.hms_networks.americas.sc.extensions.eventfile;

import com.ewon.ewonitf.Exporter;
import com.hms_networks.americas.sc.extensions.historicaldata.HistoricalDataManager;
import com.hms_networks.americas.sc.extensions.json.JSONException;
import com.hms_networks.americas.sc.extensions.string.QuoteSafeStringTokenizer;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeoutException;

/**
 * Class to manage retrieving events using export block descriptors.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.6.0
 */
public class EventFile {
  /**
   * Read EventFile, check for File Circularized event
   *
   * @return boolean - did a File Circularized event occur in the recent past
   * @throws TimeoutException for timeout in EBD read
   * @throws IOException for errors in parsing response
   * @throws JSONException for error parsing JSON
   */
  public static boolean DidFileCircularizedEventOccur()
      throws IOException, TimeoutException, JSONException {
    return EventDidOccurInDurationMins(
        EventFileConstants.DEFAULT_DURATION_MINS, EventFileConstants.FILE_CIRCULARIZED_EVENT_ID);
  }

  /**
   * Read EventFile, check for event
   *
   * @param eventId event id to look for
   * @return boolean - did the event occur in the time frame
   * @throws TimeoutException for timeout in EBD read
   * @throws IOException for errors in parsing response
   * @throws JSONException for error parsing JSON
   */
  public static boolean EventDidOccur(int eventId)
      throws IOException, TimeoutException, JSONException {
    return EventDidOccurInDurationMins(EventFileConstants.DEFAULT_DURATION_MINS, eventId);
  }

  /**
   * Read EventFile, check for event
   *
   * @param mins duration in minuets to check for event id
   * @param eventId event id to look for
   * @return boolean - did the event occur in the time frame
   * @throws TimeoutException for timeout in EBD read
   * @throws IOException for errors in parsing response
   * @throws JSONException for error parsing JSON
   */
  public static boolean EventDidOccurInDurationMins(int mins, int eventId)
      throws IOException, TimeoutException, JSONException {

    if (mins < EventFileConstants.MIN_DURATION_MINS
        || mins > EventFileConstants.MAX_DURATION_MINS) {
      throw new IllegalArgumentException(
          "The parameter passed must be greater than 0, but less than 90.");
    }

    String ebdStr = "$dtEV$ftT$st_m" + Integer.toString(mins);
    Exporter ex = HistoricalDataManager.ExecuteEBDCall(ebdStr);
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
    final DataInputStream dataStream = new DataInputStream(exporter);
    final BufferedReader reader = new BufferedReader(new InputStreamReader(dataStream));

    // Create line counter and variable to store current line
    int lineCount = 0;
    String line = reader.readLine();
    // Loop through lines in file until end and store data points
    while (line != null) {

      // Only parse lines 1 and greater, skip header
      if (lineCount > 0) {
        // Parse line
        if (checkEventFileLine(line, eventId) == true) {
          return true;
        }
      }

      // Increment line count
      lineCount++;
      // Read next line before looping again
      line = reader.readLine();
    }
    reader.close();
    exporter.close();
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
    QuoteSafeStringTokenizer tokenizer = new QuoteSafeStringTokenizer(line, ";", returnDelimiters);

    // Loop through each token
    String currentToken = "";
    while (tokenizer.hasMoreElements()) {
      // Get next token
      currentToken = tokenizer.nextToken();
    }
    return currentToken.equals(target);
  }
}
