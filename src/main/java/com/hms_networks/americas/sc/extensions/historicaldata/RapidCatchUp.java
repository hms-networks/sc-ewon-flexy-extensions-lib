package com.hms_networks.americas.sc.extensions.historicaldata;

import com.ewon.ewonitf.Exporter;
import com.hms_networks.americas.sc.extensions.datapoint.DataPoint;
import com.hms_networks.americas.sc.extensions.json.JSONException;
import com.hms_networks.americas.sc.extensions.logging.Logger;
import com.hms_networks.americas.sc.extensions.string.StringUtils;
import com.hms_networks.americas.sc.extensions.system.time.SCTimeUnit;
import com.hms_networks.americas.sc.extensions.system.time.SCTimeUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * This class addresses the need for historical data tracking to "catch up" quickly when there are
 * periods where the tracker is not running. For example, the Flexy is powered off for several
 * weeks. For correct behavior, it is required that injectJvmLocalTime from {@link SCTimeUtils} is
 * called at startup.
 *
 * @author HMS Networks, MU Americas Solution Center
 */
public class RapidCatchUp {

  /**
   * Threshold for enabling rapid catch up mode in milliseconds. If current time - start time is
   * less than this amount, catchup mode will not be enabled.
   */
  public static final long RAPID_CATCH_UP_TIME_DELTA_MILLISECONDS = SCTimeUnit.HOURS.toMillis(1);

  /**
   * Starting point for request period, which changes based on previous result.
   *
   * <p>10 minutes in milliseconds
   */
  public static final long RAPID_CATCH_UP_TIME_BASE_MILLISECONDS = SCTimeUnit.MINUTES.toMillis(10);

  /**
   * Maximum amount of time for the request period. 48 hours was used for testing. This was with a
   * Flexy that had 100 tags at 10 seconds intervals.
   *
   * <p>24 hours is below the testing 48, but sill will provide aggressive catch up.
   */
  public static final long RAPID_CATCH_UP_TIME_DURATION_MAX_MILLISECONDS =
      SCTimeUnit.HOURS.toMillis(1);

  /** Dynamic duration for requesting historical data during catch up period */
  public static long catchUpRequestDurationMilliseconds = RAPID_CATCH_UP_TIME_BASE_MILLISECONDS;

  /** Stream max read tries */
  public static final int MAX_STREAM_READS = 3;

  /** Stream buffer size */
  public static final int STREAM_BUFFER_SIZE_BYTES = 256;

  /**
   * {@code true}/{@code false} enter rapid catch up mode. The decision is based on if the last
   * historical data read was empty and how far behind is the historical tracking time from current
   * system time.
   *
   * @param lastReadEmpty the last read of {@link DataPoint} returned 0
   * @param startTimeMilliseconds milliseconds since epoch UTC
   * @return {@code true}/{@code false} should enter rapid catch up mode
   */
  public static boolean shouldEnterRapidCatchUpMode(
      boolean lastReadEmpty, long startTimeMilliseconds) {

    /* If last read to historical logs was empty and the current tracking time is older than RAPID_CATCH_UP_TIME_DELTA_MILLISECONDS, enable RapidCatchUp */
    return lastReadEmpty
        && System.currentTimeMillis() - startTimeMilliseconds
            > RAPID_CATCH_UP_TIME_DELTA_MILLISECONDS;
  }

  /**
   * Dynamically adjust the request period by doubling it, but do not exceed {@link
   * RAPID_CATCH_UP_TIME_DURATION_MAX_MILLISECONDS}.
   */
  public static void adjustRapidCatchUpPeriod() {
    // Double duration
    final int adjustmentFactor = 2;
    catchUpRequestDurationMilliseconds =
        Math.min(
            adjustmentFactor * catchUpRequestDurationMilliseconds,
            RAPID_CATCH_UP_TIME_DURATION_MAX_MILLISECONDS);
  }

  /** Reset the request period to {@link RAPID_CATCH_UP_TIME_BASE_MILLISECONDS }. */
  public static void resetRapidCatchUpPeriod() {
    catchUpRequestDurationMilliseconds = RAPID_CATCH_UP_TIME_BASE_MILLISECONDS;
  }

  /**
   * Special purpose stream reader to enable rapid catch up. Performs a limited number of reads and
   * returns up to {@link MAX_STREAM_READS} from {@link InputStream} as a {@link String} in the
   * specified encoding.
   *
   * @param inputStream input stream to read
   * @param encoding encoding to use
   * @return string contents of input stream (not complete)
   * @throws IOException if an error occurs reading, closing the streams
   * @throws NullPointerException should the input stream be a null
   * @throws UnsupportedEncodingException when encoding is not supported
   */
  private static String getStringFromInputStreamCheckAvailable(
      InputStream inputStream, String encoding)
      throws IOException, NullPointerException, UnsupportedEncodingException {
    // Create output stream for result
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    // Create buffer for reading input stream
    byte[] buffer = new byte[STREAM_BUFFER_SIZE_BYTES];
    int readCount = 0;

    // Read from input stream and write to output stream
    final int endOfStreamValue = -1;
    final int offset = 0;
    int length = inputStream.read(buffer);

    while (length != endOfStreamValue && readCount < MAX_STREAM_READS) {
      outputStream.write(buffer, offset, length);
      readCount++;
      length = inputStream.read(buffer);
    }

    // Get result from output stream and cleanup streams
    String result = outputStream.toString(encoding);
    outputStream.close();
    inputStream.close();

    return result;
  }

  /**
   * Make rapid catch up request. This request does not read and return all {@link DataPoint}s. It
   * checks to see if they exist in the historical time period, and if they do, returns a tracker
   * with the very first timestamp. If not, returns a tracker with end time.
   *
   * @param startTimeTrackingMilliseconds - the start time for tracking, epoch milliseconds UTC
   * @param includeTagGroupA include tag group A
   * @param includeTagGroupB include tag group B
   * @param includeTagGroupC include tag group C
   * @param includeTagGroupD include tag group D
   * @param stringHistorical export string historical logs if {@code true}
   * @return {@link RapidCatchUpTracker} object
   * @throws Exception for {@link DataPoint} getTimeStampAsDate() errors
   * @throws IOException for errors closing exporter
   */
  public static RapidCatchUpTracker rapidCatchUpRequest(
      long startTimeTrackingMilliseconds,
      boolean includeTagGroupA,
      boolean includeTagGroupB,
      boolean includeTagGroupC,
      boolean includeTagGroupD,
      boolean stringHistorical)
      throws Exception, IOException {

    // The end time is the start + catch up duration
    long endTimeTrackingMilliseconds =
        catchUpRequestDurationMilliseconds + startTimeTrackingMilliseconds;

    final String ebdStartTime =
        HistoricalDataQueueManager.convertToEBDTimeFormat(startTimeTrackingMilliseconds);
    final String ebdEndTime =
        HistoricalDataQueueManager.convertToEBDTimeFormat(endTimeTrackingMilliseconds);

    final String ebdRequest =
        HistoricalDataManager.prepareHistoricalFifoReadEBDString(
            ebdStartTime,
            ebdEndTime,
            includeTagGroupA,
            includeTagGroupB,
            includeTagGroupC,
            includeTagGroupD,
            stringHistorical);

    RapidCatchUpTracker histTracker;
    try {
      final Exporter exporter = HistoricalDataManager.executeEbdCall(ebdRequest);
      histTracker =
          parseEBDHistoricalLogExportResponseRapidCatchUp(exporter, endTimeTrackingMilliseconds);
      if (histTracker.isHistoricalTrackingCaughtUp()) {
        // if caught up, reset the period
        resetRapidCatchUpPeriod();
      } else {
        // if not, change period
        adjustRapidCatchUpPeriod();
      }
    } catch (EbdTimeoutException e) {
      Logger.LOG_SERIOUS(
          "EBD timeout Exception during rapid catch up, will reset catch up period.");
      // If the base period is causing timeout, drop all the way down to 1 minute
      if (catchUpRequestDurationMilliseconds == RAPID_CATCH_UP_TIME_BASE_MILLISECONDS) {

        catchUpRequestDurationMilliseconds = SCTimeUnit.MINUTES.toMillis(1);
      } else {
        resetRapidCatchUpPeriod();
      }
      histTracker = new RapidCatchUpTracker(false, startTimeTrackingMilliseconds);
    }

    return histTracker;
  }

  /**
   * This function tries to find the time exact time in the export response, where entries start.
   * It's likely that the export response will not contain data because the request was for a time
   * period where the Flexy was powered off. However, should there be data, this indicates the end
   * of a powered off period. The function will parses the Export Block Descriptor Historical Log
   * response into {@link DataPoint}(s) and take the timestamp from the very first DataPoint.
   *
   * <p>Note: this function only handles Historical List responses.
   *
   * @param exporter EBD Exporter
   * @param endTimeMilliseconds end time in milliseconds since epoch UTC
   * @return {@link RapidCatchUpTracker} indicating if data was found and the end time
   * @throws Exception for {@link DataPoint} getTimeStampAsDate() errors
   * @throws IOException for errors closing exporter
   * @throws JSONException for errors related to string enumeration configuration
   */
  private static RapidCatchUpTracker parseEBDHistoricalLogExportResponseRapidCatchUp(
      Exporter exporter, long endTimeMilliseconds) throws Exception, IOException, JSONException {

    final String exporterFile = getStringFromInputStreamCheckAvailable(exporter, "UTF-8");
    final List eventFileLines = StringUtils.split(exporterFile, "\n");
    exporter.close();
    // There is always a header line, even when there are no results, so only read results when
    // eventFileLines' size is greater than 1
    final int headerSizeLines = 1;
    if (eventFileLines.size() > headerSizeLines) {
      final int firstDataPointIdx = 1;
      String line = (String) eventFileLines.get(firstDataPointIdx);
      DataPoint lineDataPoint = HistoricalDataManager.parseHistoricalFileLine(line.trim());
      if (lineDataPoint != null) {
        long firstTimeMilliseconds = lineDataPoint.getTimeStampAsDate().getTime();
        // For EBD calls, the st (start time) is not inclusive, subtract a second to ensure
        // subsequent calls include the first found datapoint
        return new RapidCatchUpTracker(
            true, firstTimeMilliseconds - SCTimeUnit.SECONDS.toMillis(1));
      }
    }
    boolean isCaughtUp = false;
    return new RapidCatchUpTracker(isCaughtUp, endTimeMilliseconds);
  }
}
