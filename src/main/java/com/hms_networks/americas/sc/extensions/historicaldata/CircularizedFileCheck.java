package com.hms_networks.americas.sc.extensions.historicaldata;

import com.hms_networks.americas.sc.extensions.eventfile.EventFile;
import java.io.IOException;

public class CircularizedFileCheck {
  /** Event Id for circularized file error */
  private static final String CIRCULARIZED_FILE_ID_STRING = "23607";

  /**
   * Read EventFile, check for File Circularized event after start timestamp
   *
   * @return boolean - did a File Circularized event occur in the recent past
   * @throws EbdTimeoutException - for EBD timeout
   * @throws IOException - for Exception reading input stream
   */
  public static boolean didFileCircularizedEventOccurSinceAbsolute(
      long absoluteSinceStartTimeMilliseconds) throws IOException, EbdTimeoutException {
    return EventFile.didEventOccurSinceAbsolute(
        CIRCULARIZED_FILE_ID_STRING, absoluteSinceStartTimeMilliseconds);
  }
}
