package com.hms_networks.americas.sc.extensions.system.time;

/**
 * Class for identifying time units (seconds, minutes, hours, etc) and allowing for easy conversion
 * between units. All time units are stored as a converted milliseconds value. This class is
 * intended to mimic the behavior of the Java {@code TimeUnit} class, which was introduced in Java
 * 1.5.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.0.0
 * @version 1.0.1
 */
public class SCTimeUnit {

  /**
   * The number of milliseconds in one millisecond.
   *
   * @since 1.0.0
   */
  private static final long MILLISECONDS_PER_MILLISECOND = 1L;

  /**
   * The number of milliseconds in one second.
   *
   * @since 1.0.0
   */
  private static final long MILLISECONDS_PER_SECOND = 1000L;

  /**
   * The number of milliseconds in one minute.
   *
   * @since 1.0.0
   */
  private static final long MILLISECONDS_PER_MINUTE = 60000L;

  /**
   * The number of milliseconds in one hour.
   *
   * @since 1.0.0
   */
  private static final long MILLISECONDS_PER_HOUR = 3600000L;

  /**
   * The number of milliseconds in one day.
   *
   * @since 1.0.0
   */
  private static final long MILLISECONDS_PER_DAY = 86400000L;

  /**
   * The number of milliseconds in one week.
   *
   * @since 1.0.0
   */
  private static final long MILLISECONDS_PER_WEEK = 604800000L;

  /**
   * The number of milliseconds in one month.
   *
   * @since 1.0.0
   */
  private static final long MILLISECONDS_PER_MONTH = 2629800000L;

  /**
   * The number of milliseconds in one year.
   *
   * @since 1.0.0
   */
  private static final long MILLISECONDS_PER_YEAR = 31557600000L;

  /**
   * The number of milliseconds which correspond to the time unit.
   *
   * @since 1.0.0
   */
  private final long millisecondsInUnit;

  /**
   * Time unit object representing one millisecond.
   *
   * @since 1.0.0
   */
  public static final SCTimeUnit MILLISECONDS = new SCTimeUnit(MILLISECONDS_PER_MILLISECOND);

  /**
   * Time unit object alias representing one millisecond.
   *
   * @since 1.0.1
   */
  public static final SCTimeUnit MILLISECOND = MILLISECONDS;

  /**
   * Time unit object representing one second.
   *
   * @since 1.0.0
   */
  public static final SCTimeUnit SECONDS = new SCTimeUnit(MILLISECONDS_PER_SECOND);

  /**
   * Time unit object alias representing one second.
   *
   * @since 1.0.1
   */
  public static final SCTimeUnit SECOND = SECONDS;

  /**
   * Time unit object representing one minute.
   *
   * @since 1.0.0
   */
  public static final SCTimeUnit MINUTES = new SCTimeUnit(MILLISECONDS_PER_MINUTE);

  /**
   * Time unit object alias representing one minute.
   *
   * @since 1.0.1
   */
  public static final SCTimeUnit MINUTE = MINUTES;

  /**
   * Time unit object representing one hour.
   *
   * @since 1.0.0
   */
  public static final SCTimeUnit HOURS = new SCTimeUnit(MILLISECONDS_PER_HOUR);

  /**
   * Time unit object alias representing one hour.
   *
   * @since 1.0.1
   */
  public static final SCTimeUnit HOUR = HOURS;

  /**
   * Time unit object representing one day.
   *
   * @since 1.0.0
   */
  public static final SCTimeUnit DAYS = new SCTimeUnit(MILLISECONDS_PER_DAY);

  /**
   * Time unit object alias representing one day.
   *
   * @since 1.0.1
   */
  public static final SCTimeUnit DAY = DAYS;

  /**
   * Time unit object representing one week.
   *
   * @since 1.0.0
   */
  public static final SCTimeUnit WEEKS = new SCTimeUnit(MILLISECONDS_PER_WEEK);

  /**
   * Time unit object alias representing one week.
   *
   * @since 1.0.1
   */
  public static final SCTimeUnit WEEK = WEEKS;

  /**
   * Time unit object representing one month.
   *
   * @since 1.0.0
   */
  public static final SCTimeUnit MONTHS = new SCTimeUnit(MILLISECONDS_PER_MONTH);

  /**
   * Time unit object alias representing one month.
   *
   * @since 1.0.1
   */
  public static final SCTimeUnit MONTH = MONTHS;

  /**
   * Time unit object representing one year.
   *
   * @since 1.0.0
   */
  public static final SCTimeUnit YEARS = new SCTimeUnit(MILLISECONDS_PER_YEAR);

  /**
   * Time unit object alias representing one year.
   *
   * @since 1.0.1
   */
  public static final SCTimeUnit YEAR = YEARS;

  /**
   * Constructor (private) for creating a time unit with the specified number of milliseconds.
   *
   * @param millisecondsInUnit number of milliseconds in time unit.
   * @since 1.0.0
   */
  private SCTimeUnit(long millisecondsInUnit) {
    this.millisecondsInUnit = millisecondsInUnit;
  }

  /**
   * Gets the number of milliseconds which correspond to the time unit.
   *
   * @return number of milliseconds which correspond to the time unit
   * @since 1.0.0
   * @deprecated Use {@link #toMillis()} instead. This method will be removed in a future release.
   */
  public long getMilliseconds() {
    return millisecondsInUnit;
  }

  /**
   * Gets the number of seconds which correspond to the time unit.
   *
   * @return number of seconds which correspond to the time unit
   * @since 1.0.0
   * @deprecated Use {@link #toSeconds()} instead. This method will be removed in a future release.
   */
  public long getSeconds() {
    return millisecondsInUnit / MILLISECONDS_PER_SECOND;
  }

  /**
   * Gets the number of minutes which correspond to the time unit.
   *
   * @return number of minutes which correspond to the time unit
   * @since 1.0.0
   * @deprecated Use {@link #toMinutes()} instead. This method will be removed in a future release.
   */
  public long getMinutes() {
    return millisecondsInUnit / MILLISECONDS_PER_MINUTE;
  }

  /**
   * Gets the number of hours which correspond to the time unit.
   *
   * @return number of hours which correspond to the time unit
   * @since 1.0.0
   * @deprecated Use {@link #toHours()} instead. This method will be removed in a future release.
   */
  public long getHours() {
    return millisecondsInUnit / MILLISECONDS_PER_HOUR;
  }

  /**
   * Gets the number of days which correspond to the time unit.
   *
   * @return number of days which correspond to the time unit
   * @since 1.0.0
   * @deprecated Use {@link #toDays()} instead. This method will be removed in a future release.
   */
  public long getDays() {
    return millisecondsInUnit / MILLISECONDS_PER_DAY;
  }

  /**
   * Converts the specified time to milliseconds.
   *
   * @param time in units specified by millisecondsInUnit
   * @return specified time to milliseconds
   * @since 1.0.0
   */
  public long toMillis(long time) {
    long timeInMillis = time * millisecondsInUnit;
    return timeInMillis / MILLISECONDS_PER_MILLISECOND;
  }

  /**
   * Converts the one (1) of the time unit to milliseconds.
   *
   * @return one (1) of the time unit converted to milliseconds
   * @since 1.0.1
   */
  public long toMillis() {
    return millisecondsInUnit;
  }

  /**
   * Converts the specified time to seconds.
   *
   * @param time time in units of millisecondsInUnit
   * @return specified time to seconds
   * @since 1.0.0
   */
  public long toSeconds(long time) {
    long timeInMillis = time * millisecondsInUnit;
    return timeInMillis / MILLISECONDS_PER_SECOND;
  }

  /**
   * Converts the one (1) of the time unit to seconds.
   *
   * @return one (1) of the time unit converted to seconds
   * @since 1.0.1
   */
  public long toSeconds() {
    return millisecondsInUnit / MILLISECONDS_PER_SECOND;
  }

  /**
   * Converts the specified time to minutes.
   *
   * @param time specified time in milliseconds
   * @return specified time to minutes
   * @since 1.0.0
   */
  public long toMinutes(long time) {
    long timeInMillis = time * millisecondsInUnit;
    return timeInMillis / MILLISECONDS_PER_MINUTE;
  }

  /**
   * Converts the one (1) of the time unit to minutes.
   *
   * @return one (1) of the time unit converted to minutes
   * @since 1.0.1
   */
  public long toMinutes() {
    return millisecondsInUnit / MILLISECONDS_PER_MINUTE;
  }

  /**
   * Converts the specified time to hours.
   *
   * @param time specified time in milliseconds
   * @return specified time to hours
   * @since 1.0.0
   */
  public long toHours(long time) {
    long timeInMillis = time * millisecondsInUnit;
    return timeInMillis / MILLISECONDS_PER_HOUR;
  }

  /**
   * Converts the one (1) of the time unit to hours.
   *
   * @return one (1) of the time unit converted to hours
   * @since 1.0.1
   */
  public long toHours() {
    return millisecondsInUnit / MILLISECONDS_PER_HOUR;
  }

  /**
   * Converts the specified time to days.
   *
   * @param time specified time in milliseconds
   * @return specified time to days
   * @since 1.0.0
   */
  public long toDays(long time) {
    long timeInMillis = time * millisecondsInUnit;
    return timeInMillis / MILLISECONDS_PER_DAY;
  }

  /**
   * Converts the one (1) of the time unit to days.
   *
   * @return one (1) of the time unit converted to days
   * @since 1.0.1
   */
  public long toDays() {
    return millisecondsInUnit / MILLISECONDS_PER_DAY;
  }

  /**
   * Converts the specified time to weeks.
   *
   * @param time specified time in milliseconds
   * @return specified time to weeks
   * @since 1.0.0
   */
  public long toWeeks(long time) {
    long timeInMillis = time * millisecondsInUnit;
    return timeInMillis / MILLISECONDS_PER_WEEK;
  }

  /**
   * Converts the one (1) of the time unit to weeks.
   *
   * @return one (1) of the time unit converted to weeks
   * @since 1.0.1
   */
  public long toWeeks() {
    return millisecondsInUnit / MILLISECONDS_PER_WEEK;
  }

  /**
   * Converts the specified time to months.
   *
   * @param time specified time in milliseconds
   * @return specified time to months
   * @since 1.0.0
   */
  public long toMonths(long time) {
    long timeInMillis = time * millisecondsInUnit;
    return timeInMillis / MILLISECONDS_PER_MONTH;
  }

  /**
   * Converts the one (1) of the time unit to months.
   *
   * @return one (1) of the time unit converted to months
   * @since 1.0.1
   */
  public long toMonths() {
    return millisecondsInUnit / MILLISECONDS_PER_MONTH;
  }

  /**
   * Converts the specified time to years.
   *
   * @param time specified time in milliseconds
   * @return specified time to years
   * @since 1.0.0
   */
  public long toYears(long time) {
    long timeInMillis = time * millisecondsInUnit;
    return timeInMillis / MILLISECONDS_PER_YEAR;
  }

  /**
   * Converts the one (1) of the time unit to years.
   *
   * @return one (1) of the time unit converted to years
   * @since 1.0.1
   */
  public long toYears() {
    return millisecondsInUnit / MILLISECONDS_PER_YEAR;
  }
}
