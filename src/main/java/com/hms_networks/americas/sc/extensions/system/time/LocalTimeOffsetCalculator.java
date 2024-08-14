package com.hms_networks.americas.sc.extensions.system.time;

import com.ewon.ewonitf.Exporter;
import com.hms_networks.americas.sc.extensions.retry.AutomaticRetryCode;
import com.hms_networks.americas.sc.extensions.retry.AutomaticRetryCodeLinear;
import com.hms_networks.americas.sc.extensions.retry.AutomaticRetryState;
import com.hms_networks.americas.sc.extensions.string.StringUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Utility class for calculating the offset between the local time zone and UTC.
 *
 * <p>Versions of this class prior to v1.4.0 used an HTTP request to get the local time, which has
 * been replaced with a script expression export block descriptor call.
 *
 * @since 1.0.0
 * @author HMS Networks, MU Americas Solution Center
 */
public class LocalTimeOffsetCalculator {

  /** Calculated time offset in milliseconds between the local time zone and UTC. */
  private static long timeOffsetMilliseconds = 0;

  /** Exported local time from export block descriptor call. */
  private static String localTime = "";

  /**
   * Gets the currently stored time offset in milliseconds.
   *
   * @return time offset in milliseconds
   */
  public static synchronized long getLocalTimeOffsetMilliseconds() {
    return timeOffsetMilliseconds;
  }

  /**
   * Reads the current device local time and calculates an offset (in milliseconds) between the
   * local time and UTC time using a script expression export block descriptor call. The returned
   * result is also stored and can be later retrieved using the {@link
   * #getLocalTimeOffsetMilliseconds()} method.
   *
   * @return calculated local time offset (in milliseconds)
   * @throws InterruptedException if interrupted while attempting script expression export block
   *     descriptor call
   * @throws ParseException if unable to parse script expression export block descriptor call result
   *     (local time)
   */
  public static synchronized long calculateLocalTimeOffsetMilliseconds()
      throws InterruptedException, ParseException {
    // Export the local time result file
    exportLocalTime();

    // Parse local time for offset and store/return
    timeOffsetMilliseconds = parseLocalTimeResult();
    return timeOffsetMilliseconds;
  }

  /**
   * Gets and returns the local time using the script expression export block descriptor call
   * specified by {@link TimeLibConstants#TIME_OFFSET_LOCAL_TIME_EBD}.
   *
   * @throws InterruptedException if interrupted while attempting script expression export block
   *     descriptor call
   */
  private static synchronized void exportLocalTime() throws InterruptedException {
    // Build full export block descriptor call and file path
    final String exportBlockDescriptorCall = TimeLibConstants.TIME_OFFSET_LOCAL_TIME_EBD;

    // Run export block descriptor call and retry on failure
    final int maxSecondsDelayBeforeRetry = 30;
    final int secondsDelayBeforeRetryLinearSlope = 2;
    final int maxRetriesBeforeFailure = 20;
    AutomaticRetryCode automaticRetryCode =
        new AutomaticRetryCodeLinear() {
          protected long getLinearSlopeMillis() {
            return SCTimeUnit.SECONDS.toMillis(secondsDelayBeforeRetryLinearSlope);
          }

          protected long getMaxDelayMillisBeforeRetry() {
            return SCTimeUnit.SECONDS.toMillis(maxSecondsDelayBeforeRetry);
          }

          protected int getMaxRetries() {
            return maxRetriesBeforeFailure;
          }

          protected void codeToRetry() {
            try {
              Exporter exporter = new Exporter(exportBlockDescriptorCall);
              localTime = StringUtils.getStringFromInputStream(exporter, "UTF-8");
              if (localTime.trim().length() > 0) {
                setState(AutomaticRetryState.FINISHED);
              }
            } catch (Exception e) {
              setState(AutomaticRetryState.ERROR_RETRY);
            }
          }
        };
    automaticRetryCode.run();
  }

  /**
   * Parses the specified local time (generated by {@link #exportLocalTime()}) and returns the
   * offset from local time to UTC time in milliseconds.
   *
   * @return local time offset (in milliseconds)
   * @throws ParseException if unable to parse specified local time
   */
  private static long parseLocalTimeResult() throws ParseException {
    // Remove unnecessary line break, if present
    int endIndex = localTime.indexOf("<BR>");
    if (endIndex > 0) {
      localTime = localTime.substring(0, endIndex);
    }

    // Get local time from contents of local time offset result file
    SimpleDateFormat sdf = new SimpleDateFormat(TimeLibConstants.TIME_OFFSET_DATE_FORMAT);
    sdf.setTimeZone(TimeZone.getTimeZone(TimeLibConstants.GMT_TIME_ZONE_NAME));
    Date localTimeDateObj = sdf.parse(localTime);

    // Calculate difference between UTC time (Ewon system time) and local time (in milliseconds)
    Date systemTimeDateObj = new Date(System.currentTimeMillis());
    long diffInMilliseconds = 0;
    if (localTimeDateObj != null) {
      diffInMilliseconds = systemTimeDateObj.getTime() - localTimeDateObj.getTime();
    }

    return diffInMilliseconds;
  }

  /**
   * Converts the local epoch milliseconds timestamp to UTC epoch milliseconds. Flexy EBD historical
   * data response will return integer timestamps in local time when "Record Data in UTC" is not
   * enabled, so this method can be used to convert the local time to UTC time.
   *
   * @param localEpochMillis the local epoch milliseconds timestamp
   * @return epoch milliseconds timestamp relative to UTC
   * @throws InterruptedException if interrupted while attempting script expression export block
   *     descriptor call
   * @throws ParseException if unable to parse script expression export block descriptor call result
   *     (local time)
   */
  public static long convertLocalEpochMillisToUtc(long localEpochMillis)
      throws InterruptedException, ParseException {
    return localEpochMillis + calculateLocalTimeOffsetMilliseconds();
  }
}
