package com.hms_networks.americas.sc.extensions.alarms;

/**
 * Constants for the alarm monitoring and management library for the Ewon Flexy.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.0.0
 */
public class AlarmConstants {

  /** The string to represent alarm status ACK. */
  public static final String ALARM_STATUS_ACK_STRING = "ACK";

  /** The string to represent alarm status ALM. */
  public static final String ALARM_STATUS_ALM_STRING = "ALM";

  /** The string to represent alarm status NONE. */
  public static final String ALARM_STATUS_NONE_STRING = "NONE";

  /** The string to represent alarm status RTN. */
  public static final String ALARM_STATUS_RTN_STRING = "RTN";

  /**
   * The string to represent alarm status UNKNOWN. This alarm status does not represent an actual
   * Ewon alarm status integer, and was added to allow for alarms to be output regardless of an
   * unknown alarm status.
   */
  public static final String ALARM_STATUS_UNKNOWN_STRING = "UNKNOWN";

  /** The string to represent alarm type NONE. */
  public static final String ALARM_TYPE_NONE_STRING = "NONE";

  /** The string to represent alarm type LOW LOW. */
  public static final String ALARM_TYPE_LOW_LOW_STRING = "LOWLOW";

  /** The string to represent alarm type LOW. */
  public static final String ALARM_TYPE_LOW_STRING = "LOW";

  /** The string to represent alarm type LEVEL. */
  public static final String ALARM_TYPE_LEVEL_STRING = "LEVEL";

  /** The string to represent alarm type HIGH. */
  public static final String ALARM_TYPE_HIGH_STRING = "HIGH";

  /** The string to represent alarm type HIGH_HIGH. */
  public static final String ALARM_TYPE_HIGH_HIGH_STRING = "HIGHHIGH";

  /**
   * The string to represent alarm type UNKNOWN. This alarm type does not represent an actual Ewon
   * alarm type integer, and was added to allow for alarms to be output regardless of an unknown
   * alarm type.
   */
  public static final String ALARM_TYPE_UNKNOWN_STRING = "UNKNOWN";
}
