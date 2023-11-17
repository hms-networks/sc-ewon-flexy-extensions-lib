package com.hms_networks.americas.sc.extensions.historicaldata;

import com.hms_networks.americas.sc.extensions.ebd.EbdTimeoutException;
import com.hms_networks.americas.sc.extensions.eventfile.EventFile;
import java.io.IOException;

/**
 * Class to check for Circularized file exceptions
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.13.11
 */
public class CircularizedFileCheck {
  /** Event Id for circularized file error */
  private static final String CIRCULARIZED_FILE_ID_STRING = "23607";

  /**
   * Read EventFile, check for File Circularized event after start timestamp
   *
   * @param absoluteSinceStartTimeMilliseconds - absolute epoch start timestamp for start time of
   *     Event File request
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
