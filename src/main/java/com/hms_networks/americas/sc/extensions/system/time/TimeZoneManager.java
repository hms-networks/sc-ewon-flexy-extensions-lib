package com.hms_networks.americas.sc.extensions.system.time;

import java.util.Date;

/**
 * Class which provides management of the JVM time zone. The Flexy JVM provides only a single
 * TimeZone object: GMT. This class will check when to measure the local time zone offset and set
 * the default time zone for the JVM. Having the proper time zone offset allows for proper creation
 * of Date objects and other time-related operations. The update in necessary to handle daylight
 * savings time.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.16.0
 */
public class TimeZoneManager {

  /** Tracking time zone update minutes. Initialize to -1 to indicate has not been set. */
  private static int tzTrackMinutes = -1;

  /** Tracking time zone update hours. Initialized to -1 to indicate has not been set. */
  private static int tzTrackHour = -1;

  /**
   * Default interval for time zone update check. This value will provide checks on the 0, 15, 30
   * and 45 minute of every hour. These offsets are when daylight savings time changes occur.
   */
  private static final int DEFAULT_UPDATE_MINS = 15;

  /** Check for timezone updates on this interval. */
  private static int timeZoneCheckIntervalMins = DEFAULT_UPDATE_MINS;

  /**
   * Set the time zone check interval in minutes. The interval is used to determine when to check if
   * the time zone offset should be updated. The default interval is 15 minutes.
   *
   * @param intervalMins the interval in minutes
   */
  public static void setTimeZoneCheckIntervalMins(int intervalMins) {
    timeZoneCheckIntervalMins = intervalMins;
  }

  /**
   * This method will round down the minute value to a whole interval. For example, if the interval
   * is 15 minutes, and the minute is 17, the rounded down value will be 15. Rounding down is
   * necessary for the timing of the time zone update.
   *
   * @param minute the minute value to round
   * @return the rounded down minute value
   */
  private static int roundDownMinuteValue(int minute) {
    return (minute / timeZoneCheckIntervalMins) * timeZoneCheckIntervalMins;
  }

  /**
   * Check the if current time should trigger a potential local time zone offset (offset from GMT)
   * change. The check is based on the current hour and the rounded down minute; when one or both of
   * these values have changed, an update will be triggered. The update action is to call the
   * injectJvmLocalTime method. This may change the JVM's default time zone.
   *
   * @throws Exception if an error occurs while checking the time zone offset, or injecting the
   *     local time offset.
   */
  public static void checkUpdateTimeZone() throws Exception {
    Date current = new Date();
    int hour = current.getHours();
    int minute = roundDownMinuteValue(current.getMinutes());
    if (hour == tzTrackHour && minute == tzTrackMinutes) {
      return;
    }
    // Update the tracking values
    tzTrackHour = hour;
    tzTrackMinutes = minute;
    // Inject the local time zone offset
    SCTimeUtils.injectJvmLocalTime();
  }
}
