package com.hms_networks.americas.sc.extensions.system.time;

import com.ewon.ewonitf.SysControlBlock;
import com.hms_networks.americas.sc.extensions.datapoint.DataPoint;
import com.hms_networks.americas.sc.extensions.localization.LocalizationManager;
import com.hms_networks.americas.sc.extensions.localization.LocalizationManager.DateTimeKey;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

/**
 * Utility class which provides methods for accessing and changing the configuration of the Flexy's
 * local time.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.0.0
 */
public class SCTimeUtils {

  /** Key for accessing the time zone name item in the Flexy's system control block. */
  private static final String SCB_TIME_ZONE_KEY = "Timezone";

  /** Key for accessing the UTC export item in the Flexy's system control block. */
  private static final String SCB_UTC_EXPORT_KEY = "UTCExport";

  /**
   * String value used to represent a true value for the UTC export item in the Flexy's system
   * control block.
   */
  private static final String SCB_UTC_EXPORT_VALUE_TRUE = "1";

  /** The name of the Java time zone representing GMT/UTC. */
  private static final String GMT_TIME_ZONE_NAME = "GMT";

  /** The designator used for the GMT/UTC time zone, which has no offset. */
  private static final String GMT_TIME_ZONE_DESIGNATOR = "Z";

  /** The simple date format pattern used for representing ISO 8601 date and time strings. */
  private static final String SIMPLE_DATE_FORMAT_PATTERN_ISO_8601 = "yyyy-MM-dd'T'HH:mm:ss.SSS";

  /** The simple date format representing ISO 8601 format with the time zone as UTC. */
  private static SimpleDateFormat iso8601UtcTimeFormat = null;

  /**
   * The simple date format representing ISO 8601 format with the local time zone offset appended
   * (or Z if the time zone is GMT/UTC).
   */
  private static SimpleDateFormat iso8601LocalTimeFormat = null;

  /** The designator for the local time zone. (i.e. -05:00, +02:00, Z, etc.) */
  private static String localTimeZoneDesignator = null;

  /** Boolean value indicating if tag data is exported using UTC time stamps. */
  private static boolean tagDataExportedInUtc = false;

  /**
   * Boolean value indicating of the tag data is exported using UTC time stamps option has been read
   * from configuration.
   */
  private static boolean didReadTagDataExportedInUtc = false;

  /**
   * Gets a boolean value indicating if tag data is exported using UTC time stamps.
   *
   * @return true if tag data is exported using UTC time stamps, false otherwise
   * @throws Exception if unable to get UTC export value
   */
  public static boolean getTagDataExportedInUtc() throws Exception {
    if (!didReadTagDataExportedInUtc) {
      // Create system control block
      SysControlBlock sysControlBlock = new SysControlBlock(SysControlBlock.SYS);

      // Get UTC export value from system control block
      tagDataExportedInUtc =
          sysControlBlock.getItem(SCB_UTC_EXPORT_KEY).trim().equals(SCB_UTC_EXPORT_VALUE_TRUE);
      didReadTagDataExportedInUtc = true;
    }
    return tagDataExportedInUtc;
  }

  /**
   * Gets the name of the Flexy's local time zone (i.e. America/New_York, Europe/London, etc.).
   *
   * @return local time zone name
   * @throws Exception if unable to get local time zone name
   */
  public static String getTimeZoneName() throws Exception {
    // Create system control block
    SysControlBlock sysControlBlock = new SysControlBlock(SysControlBlock.SYS);

    // Get time zone name from system control block
    return sysControlBlock.getItem(SCB_TIME_ZONE_KEY);
  }

  /**
   * Returns the simple date format representing ISO 8601 format with the time zone as UTC.
   *
   * @return simple date format representing ISO 8601 format with the time zone as UTC
   * @throws IllegalStateException if the local time has not yet been injected
   */
  public static SimpleDateFormat getIso8601UtcTimeFormat() throws IllegalStateException {
    if (iso8601UtcTimeFormat == null) {
      throw new IllegalStateException("The local time has not yet been injected.");
    }

    return iso8601UtcTimeFormat;
  }

  /**
   * Returns the simple date format representing ISO 8601 format with the time zone offset appended
   * (or Z if the timezone is GMT/UTC).
   *
   * @return simple date format representing ISO 8601 format with the time zone offset appended
   * @throws IllegalStateException if the local time has not yet been injected
   */
  public static SimpleDateFormat getIso8601LocalTimeFormat() throws IllegalStateException {
    if (iso8601LocalTimeFormat == null) {
      throw new IllegalStateException("The local time has not yet been injected.");
    }

    return iso8601LocalTimeFormat;
  }

  /**
   * Returns the designator for the local time zone. (i.e. -05:00, +02:00, Z, etc.)
   *
   * @return designator for the local time zone
   * @throws IllegalStateException if the local time has not yet been injected
   */
  public static String getLocalTimeZoneDesignator() throws IllegalStateException {
    if (localTimeZoneDesignator == null) {
      throw new IllegalStateException("The local time has not yet been injected.");
    }

    return localTimeZoneDesignator;
  }

  /**
   * Injects the local time into the JVM with the local time offset (from UTC) information from
   * {@link LocalTimeOffsetCalculator}. A modified time zone with the local time offset (from UTC)
   * is configured as the JVM default time zone.
   *
   * @throws Exception if unable to get the local time zone name
   */
  public static void injectJvmLocalTime() throws Exception {
    // Get current time offset
    final int calculatedTimeOffsetMilliseconds =
        (int) LocalTimeOffsetCalculator.calculateLocalTimeOffsetMilliseconds();

    injectCustomJvmLocalTime(calculatedTimeOffsetMilliseconds);
  }

  /**
   * Injects the local time into the JVM with the specified local time offset (from UTC) in
   * milliseconds. A modified time zone with the local time offset (from UTC) is configured as the
   * JVM default time zone.
   *
   * @param timeZoneOffsetMillis local time offset (from UTC) in milliseconds
   * @throws Exception if unable to get the local time zone name
   */
  public static void injectCustomJvmLocalTime(int timeZoneOffsetMillis) throws Exception {
    // Round time in milliseconds to the nearest 15 minutes
    final int fifteenMinutesMilliseconds = (int) SCTimeUnit.MINUTES.toMillis(15);
    int roundedTimeOffsetMillis =
        Math.round((float) timeZoneOffsetMillis / (float) fifteenMinutesMilliseconds)
            * fifteenMinutesMilliseconds;

    // Convert offset from local time -> UTC to offset from UTC -> local time.
    final int rawTimeZoneOffset = (int) roundedTimeOffsetMillis * -1;

    // Create new time zone and set as JVM default
    TimeZone newTimeZone = new SimpleTimeZone(rawTimeZoneOffset, getTimeZoneName());
    TimeZone.setDefault(newTimeZone);

    // Build applicable date formats
    boolean isTimeZoneGmt = newTimeZone.getDisplayName().equals(GMT_TIME_ZONE_NAME);
    localTimeZoneDesignator =
        isTimeZoneGmt
            ? GMT_TIME_ZONE_DESIGNATOR
            : newTimeZone.getDisplayName().substring(GMT_TIME_ZONE_NAME.length());
    final String localTimeFormatString =
        SIMPLE_DATE_FORMAT_PATTERN_ISO_8601 + "'" + localTimeZoneDesignator + "'";
    final String utcTimeFormatString =
        SIMPLE_DATE_FORMAT_PATTERN_ISO_8601 + "'" + GMT_TIME_ZONE_DESIGNATOR + "'";
    iso8601LocalTimeFormat = new SimpleDateFormat(localTimeFormatString, Locale.getDefault());
    iso8601UtcTimeFormat = new SimpleDateFormat(utcTimeFormatString, Locale.getDefault());
    iso8601UtcTimeFormat.setTimeZone(TimeZone.getTimeZone(GMT_TIME_ZONE_NAME));
  }

  /**
   * Returns an ISO 8601 formatted string representing the timestamp of the specified data point in
   * the applicable time zone (UTC or local, depending on the value returned by {@link
   * #getTagDataExportedInUtc()}).
   *
   * @param dataPoint data point to get timestamp for
   * @return ISO 8601 formatted string representing the timestamp of the specified data point in the
   *     applicable time zone
   * @throws Exception if unable to get UTC export value
   */
  public static String getIso8601FormattedTimestampForDataPoint(DataPoint dataPoint)
      throws Exception {
    String formattedTimestamp;
    if (getTagDataExportedInUtc()) {
      formattedTimestamp = getIso8601UtcTimeFormat().format(dataPoint.getTimeStampAsDate());
    } else {
      formattedTimestamp = getIso8601LocalTimeFormat().format(dataPoint.getTimeStampAsDate());
    }
    return formattedTimestamp;
  }

  /**
   * Returns a string representing the specified time span (in milliseconds) in the format of
   * '[days] [days text], [hours] [hours text], [minutes] [minutes text], [seconds] [seconds text]'.
   * The text for each time span is determined by the current locale.
   *
   * @param milliseconds time span (in milliseconds)
   * @return string representing the specified time span (in milliseconds) in the format of '[days]
   *     [days text], [hours] [hours text], [minutes] [minutes text], [seconds] [seconds text]'
   */
  public static String getDayHourMinSecsForMillis(long milliseconds) {
    final String daysText = LocalizationManager.getDateTimeString(DateTimeKey.DAYS);
    final String hoursText = LocalizationManager.getDateTimeString(DateTimeKey.HOURS);
    final String minutesText = LocalizationManager.getDateTimeString(DateTimeKey.MINUTES);
    final String secondsText = LocalizationManager.getDateTimeString(DateTimeKey.SECONDS);
    return getDayHourMinSecsForMillis(milliseconds, daysText, hoursText, minutesText, secondsText);
  }

  /**
   * Returns a string representing the specified time span (in milliseconds) in the format of
   * '[days] [days text], [hours] [hours text], [minutes] [minutes text], [seconds] [seconds text]'.
   *
   * @param milliseconds time span (in milliseconds)
   * @param daysText text used to represent days
   * @param hoursText text used to represent hours
   * @param minutesText text used to represent minutes
   * @param secondsText text used to represent seconds
   * @return string representing the specified time span (in milliseconds) in the format of '[days]
   *     [days text], [hours] [hours text], [minutes] [minutes text], [seconds] [seconds text]'
   */
  public static String getDayHourMinSecsForMillis(
      long milliseconds, String daysText, String hoursText, String minutesText, String secondsText) {
    final long days = SCTimeUnit.MILLISECONDS.toDays(milliseconds);
    final long hours =
        SCTimeUnit.MILLISECONDS.toHours(milliseconds)
            - SCTimeUnit.DAYS.toHours(SCTimeUnit.MILLISECONDS.toDays(milliseconds));
    final long minutes =
        SCTimeUnit.MILLISECONDS.toMinutes(milliseconds)
            - SCTimeUnit.HOURS.toMinutes(SCTimeUnit.MILLISECONDS.toHours(milliseconds));
    final long seconds =
        SCTimeUnit.MILLISECONDS.toSeconds(milliseconds)
            - SCTimeUnit.MINUTES.toSeconds(SCTimeUnit.MILLISECONDS.toMinutes(milliseconds));
    return days
        + " "
        + daysText
        + ", "
        + hours
        + " "
        + hoursText
        + ", "
        + minutes
        + " "
        + minutesText
        + ", "
        + seconds
        + " "
        + secondsText;
  }
}
