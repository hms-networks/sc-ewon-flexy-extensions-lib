package com.hms_networks.americas.sc.extensions.system.time;

/**
 * Class for identifying time units (seconds, minutes, hours, etc) and allowing for easy conversion
 * between units. All time units are stored as a converted milliseconds value. This class is
 * intended to mimic the behavior of the Java {@code TimeUnit} class, which was introduced in Java
 * 1.5.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.0.0
 */
public class SCTimeUnit {
  /** The number of milliseconds in one millisecond. */
  private static final long MILLISECONDS_PER_MILLISECOND = 1L;

  /** The number of milliseconds in one second. */
  private static final long MILLISECONDS_PER_SECOND = 1000L;

  /** The number of milliseconds in one minute. */
  private static final long MILLISECONDS_PER_MINUTE = 60000L;

  /** The number of milliseconds in one hour. */
  private static final long MILLISECONDS_PER_HOUR = 3600000L;

  /** The number of milliseconds in one day. */
  private static final long MILLISECONDS_PER_DAY = 86400000L;

  /** The number of milliseconds which correspond to the time unit. */
  private final long millisecondsInUnit;

  /** Time unit object representing one millisecond. */
  public static final SCTimeUnit MILLISECONDS = new SCTimeUnit(MILLISECONDS_PER_MILLISECOND);

  /** Time unit object representing one second. */
  public static final SCTimeUnit SECONDS = new SCTimeUnit(MILLISECONDS_PER_SECOND);

  /** Time unit object representing one minute. */
  public static final SCTimeUnit MINUTES = new SCTimeUnit(MILLISECONDS_PER_MINUTE);

  /** Time unit object representing one hour. */
  public static final SCTimeUnit HOURS = new SCTimeUnit(MILLISECONDS_PER_HOUR);

  /** Time unit object representing one day. */
  public static final SCTimeUnit DAYS = new SCTimeUnit(MILLISECONDS_PER_DAY);

  /**
   * Constructor (private) for creating a time unit with the specified number of milliseconds.
   *
   * @param millisecondsInUnit number of milliseconds in time unit.
   */
  private SCTimeUnit(long millisecondsInUnit) {
    this.millisecondsInUnit = millisecondsInUnit;
  }

  /**
   * Gets the number of milliseconds which correspond to the time unit.
   *
   * @return number of milliseconds which correspond to the time unit
   */
  public long getMilliseconds() {
    return millisecondsInUnit;
  }

  /**
   * Gets the number of seconds which correspond to the time unit.
   *
   * @return number of seconds which correspond to the time unit
   */
  public long getSeconds() {
    return millisecondsInUnit / MILLISECONDS_PER_SECOND;
  }

  /**
   * Gets the number of minutes which correspond to the time unit.
   *
   * @return number of minutes which correspond to the time unit
   */
  public long getMinutes() {
    return millisecondsInUnit / MILLISECONDS_PER_MINUTE;
  }

  /**
   * Gets the number of hours which correspond to the time unit.
   *
   * @return number of hours which correspond to the time unit
   */
  public long getHours() {
    return millisecondsInUnit / MILLISECONDS_PER_HOUR;
  }

  /**
   * Gets the number of days which correspond to the time unit.
   *
   * @return number of days which correspond to the time unit
   */
  public long getDays() {
    return millisecondsInUnit / MILLISECONDS_PER_DAY;
  }

  /**
   * Converts the specified time to milliseconds.
   *
   * @param time in units specified by millisecondsInUnit
   * @return specified time to milliseconds
   */
  public long toMillis(long time) {
    long timeInMillis = time * millisecondsInUnit;
    return timeInMillis / MILLISECONDS_PER_MILLISECOND;
  }

  /**
   * Converts the specified time to seconds.
   *
   * @param time time in units of millisecondsInUnit
   * @return specified time to seconds
   */
  public long toSeconds(long time) {
    long timeInMillis = time * millisecondsInUnit;
    return timeInMillis / MILLISECONDS_PER_SECOND;
  }

  /**
   * Converts the specified time to minutes.
   *
   * @param time specified time in milliseconds
   * @return specified time to minutes
   */
  public long toMinutes(long time) {
    long timeInMillis = time * millisecondsInUnit;
    return timeInMillis / MILLISECONDS_PER_MINUTE;
  }

  /**
   * Converts the specified time to hours.
   *
   * @param time specified time in milliseconds
   * @return specified time to hours
   */
  public long toHours(long time) {
    long timeInMillis = time * millisecondsInUnit;
    return timeInMillis / MILLISECONDS_PER_HOUR;
  }

  /**
   * Converts the specified time to days.
   *
   * @param time specified time in milliseconds
   * @return specified time to days
   */
  public long toDays(long time) {
    long timeInMillis = time * millisecondsInUnit;
    return timeInMillis / MILLISECONDS_PER_DAY;
  }
}
