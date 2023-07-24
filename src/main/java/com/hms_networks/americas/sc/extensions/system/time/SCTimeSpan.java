package com.hms_networks.americas.sc.extensions.system.time;

/**
 * Class for identifying time spans as a specified unit (seconds, minutes, hours, etc). This class
 * also contains helper methods which allow for easy conversion of the time span, regardless of the
 * specified unit.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.14.2
 * @version 1.0.0
 */
public class SCTimeSpan {

  /**
   * The time span value in the specified time unit.
   *
   * @since 1.0.0
   */
  private final long timeSpan;

  /**
   * The time unit of the time span value.
   *
   * @since 1.0.0
   */
  private final SCTimeUnit timeUnit;

  /**
   * Constructs a time span object with the specified time span value and time unit. This method is
   * equivalent to calling the constructor {@link #SCTimeSpan(long, SCTimeUnit)}.
   *
   * @param timeSpan time span value
   * @param timeUnit time unit of time span value
   * @return time span object
   * @since 1.0.0
   */
  public static SCTimeSpan of(long timeSpan, SCTimeUnit timeUnit) {
    return new SCTimeSpan(timeSpan, timeUnit);
  }

  /**
   * Constructs a time span object with the specified milliseconds time span value.
   *
   * @param timeSpanMillis time span value in milliseconds
   * @return time span object
   * @since 1.0.0
   */
  public static SCTimeSpan ofMillis(long timeSpanMillis) {
    return new SCTimeSpan(timeSpanMillis, SCTimeUnit.MILLISECONDS);
  }

  /**
   * Constructs a time span object with the specified seconds time span value.
   *
   * @param timeSpanSeconds time span value in seconds
   * @return time span object
   * @since 1.0.0
   */
  public static SCTimeSpan ofSeconds(long timeSpanSeconds) {
    return new SCTimeSpan(timeSpanSeconds, SCTimeUnit.SECONDS);
  }

  /**
   * Constructs a time span object with the specified minutes time span value.
   *
   * @param timeSpanMinutes time span value in minutes
   * @return time span object
   * @since 1.0.0
   */
  public static SCTimeSpan ofMinutes(long timeSpanMinutes) {
    return new SCTimeSpan(timeSpanMinutes, SCTimeUnit.MINUTES);
  }

  /**
   * Constructs a time span object with the specified hours time span value.
   *
   * @param timeSpanHours time span value in hours
   * @return time span object
   * @since 1.0.0
   */
  public static SCTimeSpan ofHours(long timeSpanHours) {
    return new SCTimeSpan(timeSpanHours, SCTimeUnit.HOURS);
  }

  /**
   * Constructs a time span object with the specified days time span value.
   *
   * @param timeSpanDays time span value in days
   * @return time span object
   * @since 1.0.0
   */
  public static SCTimeSpan ofDays(long timeSpanDays) {
    return new SCTimeSpan(timeSpanDays, SCTimeUnit.DAYS);
  }

  /**
   * Constructs a time span object with the specified weeks time span value.
   *
   * @param timeSpanWeeks time span value in weeks
   * @return time span object
   * @since 1.0.0
   */
  public static SCTimeSpan ofWeeks(long timeSpanWeeks) {
    return new SCTimeSpan(timeSpanWeeks, SCTimeUnit.WEEKS);
  }

  /**
   * Constructs a time span object with the specified months time span value.
   *
   * @param timeSpanMonths time span value in months
   * @return time span object
   * @since 1.0.0
   */
  public static SCTimeSpan ofMonths(long timeSpanMonths) {
    return new SCTimeSpan(timeSpanMonths, SCTimeUnit.MONTHS);
  }

  /**
   * Constructs a time span object with the specified years time span value.
   *
   * @param timeSpanYears time span value in years
   * @return time span object
   * @since 1.0.0
   */
  public static SCTimeSpan ofYears(long timeSpanYears) {
    return new SCTimeSpan(timeSpanYears, SCTimeUnit.YEARS);
  }

  /**
   * Constructor for a time span object with the specified time span value and time unit.
   *
   * @param timeSpan time span value
   * @param timeUnit time unit of time span value
   * @since 1.0.0
   */
  public SCTimeSpan(long timeSpan, SCTimeUnit timeUnit) {
    this.timeSpan = timeSpan;
    this.timeUnit = timeUnit;
  }

  /**
   * Gets the time span value in the specified time unit.
   *
   * @return time span value in the specified time unit
   * @since 1.0.0
   */
  public long getTimeSpan() {
    return timeSpan;
  }

  /**
   * Gets the time unit of the time span value.
   *
   * @return time unit of time span value
   * @since 1.0.0
   */
  public SCTimeUnit getTimeUnit() {
    return timeUnit;
  }

  /**
   * Gets the time span value as milliseconds.
   *
   * <p>This method is equivalent to calling {@link #getTimeUnit()}, and then using {@link
   * SCTimeUnit#toMillis(long)}} with the time span value from {@link #getTimeSpan()}.
   *
   * @return time span value as milliseconds
   * @since 1.0.0
   */
  public long getTimeSpanMillis() {
    return timeUnit.toMillis(timeSpan);
  }

  /**
   * Gets the time span value as seconds.
   *
   * <p>This method is equivalent to calling {@link #getTimeUnit()}, and then using {@link
   * SCTimeUnit#toSeconds(long)}} with the time span value from {@link #getTimeSpan()}.
   *
   * @return time span value as seconds
   * @since 1.0.0
   */
  public long getTimeSpanSeconds() {
    return timeUnit.toSeconds(timeSpan);
  }

  /**
   * Gets the time span value as minutes.
   *
   * <p>This method is equivalent to calling {@link #getTimeUnit()}, and then using {@link
   * SCTimeUnit#toMinutes(long)}} with the time span value from {@link #getTimeSpan()}.
   *
   * @return time span value as minutes
   * @since 1.0.0
   */
  public long getTimeSpanMinutes() {
    return timeUnit.toMinutes(timeSpan);
  }

  /**
   * Gets the time span value as hours.
   *
   * <p>This method is equivalent to calling {@link #getTimeUnit()}, and then using {@link
   * SCTimeUnit#toHours(long)}} with the time span value from {@link #getTimeSpan()}.
   *
   * @return time span value as hours
   * @since 1.0.0
   */
  public long getTimeSpanHours() {
    return timeUnit.toHours(timeSpan);
  }

  /**
   * Gets the time span value as days.
   *
   * <p>This method is equivalent to calling {@link #getTimeUnit()}, and then using {@link
   * SCTimeUnit#toDays(long)}} with the time span value from {@link #getTimeSpan()}.
   *
   * @return time span value as days
   * @since 1.0.0
   */
  public long getTimeSpanDays() {
    return timeUnit.toDays(timeSpan);
  }

  /**
   * Gets the time span value as weeks.
   *
   * <p>This method is equivalent to calling {@link #getTimeUnit()}, and then using {@link
   * SCTimeUnit#toWeeks(long)}} with the time span value from {@link #getTimeSpan()}.
   *
   * @return time span value as weeks
   * @since 1.0.0
   */
  public long getTimeSpanWeeks() {
    return timeUnit.toWeeks(timeSpan);
  }

  /**
   * Gets the time span value as months.
   *
   * <p>This method is equivalent to calling {@link #getTimeUnit()}, and then using {@link
   * SCTimeUnit#toMonths(long)}} with the time span value from {@link #getTimeSpan()}.
   *
   * @return time span value as months
   * @since 1.0.0
   */
  public long getTimeSpanMonths() {
    return timeUnit.toMonths(timeSpan);
  }

  /**
   * Gets the time span value as years.
   *
   * <p>This method is equivalent to calling {@link #getTimeUnit()}, and then using {@link
   * SCTimeUnit#toYears(long)}} with the time span value from {@link #getTimeSpan()}.
   *
   * @return time span value as years
   * @since 1.0.0
   */
  public long getTimeSpanYears() {
    return timeUnit.toYears(timeSpan);
  }
}
