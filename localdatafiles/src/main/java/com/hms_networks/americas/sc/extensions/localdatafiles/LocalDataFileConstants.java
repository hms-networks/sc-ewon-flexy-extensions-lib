package com.hms_networks.americas.sc.extensions.localdatafiles;

/**
 * Constants required by the local data file library.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.0.0
 */
public class LocalDataFileConstants {

  /** The number of milliseconds in one second. */
  public static final int NUM_MILLISECONDS_PER_SECOND = 1000;

  /** The number of months in one year */
  public static final int NUM_MONTHS_PER_YEAR = 12;

  /** The prefix appended to the beginning of the name of local data files. */
  public static final String LOCAL_DATA_FILE_NAME_PREFIX = "DataReport_";

  /** The file extension used for local data files. */
  public static final String LOCAL_DATA_FILE_EXTENSION = ".csv";

  /**
   * The number of outdated months to attempt to delete from the filesystem. For example, if files
   * are retained for six months, the application will attempt to delete up to the set number of
   * months of outdated files.
   */
  public static final int LOCAL_DATA_FILE_MAX_MONTHS_DELETE = 32;

  /** The interval at which outdated local data files are deleted from the file system. */
  public static final long LOCAL_DATA_FILE_OUTDATED_FILE_DELETE_INTERVAL_MILLIS = 3600000;

  /** The line separator character(s) for local data files. */
  public static final String LOCAL_DATA_FILE_LINE_SEPARATOR = "\r\n";

  /** The delimiter for local data files. */
  public static final String LOCAL_DATA_FILE_DELIMITER = ",";

  /** The string value used as a filler for non-applicable or empty column values. */
  public static final String LOCAL_DATA_FILE_FILLER_VALUE = "";

  /** The list of column headers added as the first line of local data files. */
  public static final String[] LOCAL_DATA_FILE_COLUMN_HEADERS = {
    "utcTime",
    "localTime",
    "type",
    "tagName",
    "tagType",
    "tagValue",
    "dataQuality",
    "alarmType",
    "alarmStatus"
  };

  /** The value for 'kind' when the row is a data point. */
  public static final String LOCAL_DATA_FILE_ROW_TYPE_DATA = "data";

  /** The value for 'kind' when the row is an alarm. */
  public static final String LOCAL_DATA_FILE_ROW_TYPE_ALARM = "alarm";

  /** The string to be printed in local data files to represent alarm status ACK. */
  public static final String LOCAL_DATA_FILE_ALARM_STATUS_ACK_STRING = "ACK";

  /** The string to be printed in local data files to represent alarm status ALM. */
  public static final String LOCAL_DATA_FILE_ALARM_STATUS_ALM_STRING = "ALM";

  /** The string to be printed in local data files to represent alarm status NONE. */
  public static final String LOCAL_DATA_FILE_ALARM_STATUS_NONE_STRING = "NONE";

  /** The string to be printed in local data files to represent alarm status RTN. */
  public static final String LOCAL_DATA_FILE_ALARM_STATUS_RTN_STRING = "RTN";

  /**
   * The string to be printed in local data files to represent alarm status UNKNOWN. This alarm
   * status does not represent an actual Ewon alarm status integer, and was added to allow for
   * alarms to be output to local data files regardless of an unknown alarm status.
   */
  public static final String LOCAL_DATA_FILE_ALARM_STATUS_UNKNOWN_STRING = "UNKNOWN";

  /** The string to be printed in local data files to represent alarm type NONE. */
  public static final String LOCAL_DATA_FILE_ALARM_TYPE_NONE_STRING = "NONE";

  /** The string to be printed in local data files to represent alarm type LOW LOW. */
  public static final String LOCAL_DATA_FILE_ALARM_TYPE_LOW_LOW_STRING = "LOWLOW";

  /** The string to be printed in local data files to represent alarm type LOW. */
  public static final String LOCAL_DATA_FILE_ALARM_TYPE_LOW_STRING = "LOW";

  /** The string to be printed in local data files to represent alarm type LEVEL. */
  public static final String LOCAL_DATA_FILE_ALARM_TYPE_LEVEL_STRING = "LEVEL";

  /** The string to be printed in local data files to represent alarm type HIGH. */
  public static final String LOCAL_DATA_FILE_ALARM_TYPE_HIGH_STRING = "HIGH";

  /** The string to be printed in local data files to represent alarm type HIGH_HIGH. */
  public static final String LOCAL_DATA_FILE_ALARM_TYPE_HIGH_HIGH_STRING = "HIGHHIGH";

  /**
   * The string to be printed in local data files to represent alarm type UNKNOWN. This alarm type
   * does not represent an actual Ewon alarm type integer, and was added to allow for alarms to be
   * output to local data files regardless of an unknown alarm type.
   */
  public static final String LOCAL_DATA_FILE_ALARM_TYPE_UNKNOWN_STRING = "UNKNOWN";

  /** The string to be printed in local data files to represent data quality BAD. */
  public static final String LOCAL_DATA_FILE_QUALITY_BAD = "BAD";

  /** The string to be printed in local data files to represent data quality GOOD. */
  public static final String LOCAL_DATA_FILE_QUALITY_GOOD = "GOOD";

  /** The string to be printed in local data files to represent data quality UNCERTAIN. */
  public static final String LOCAL_DATA_FILE_QUALITY_UNCERTAIN = "UNCERTAIN";

  /**
   * The string to be printed in local data files to represent data quality UNKNOWN. This data
   * quality does not represent an actual Ewon data quality integer, and was added to allow for data
   * points to be output to local data files regardless of an unknown data quality.
   */
  public static final String LOCAL_DATA_FILE_QUALITY_UNKNOWN = "UNKNOWN";

  /** The string to be printed in local data files to represent the tag/data type BOOLEAN. */
  public static final String LOCAL_DATA_FILE_DATA_TYPE_BOOLEAN = "BOOLEAN";

  /** The string to be printed in local data files to represent the tag/data type DWORD. */
  public static final String LOCAL_DATA_FILE_DATA_TYPE_DWORD = "DWORD";

  /** The string to be printed in local data files to represent the tag/data type FLOAT. */
  public static final String LOCAL_DATA_FILE_DATA_TYPE_FLOAT = "FLOAT";

  /** The string to be printed in local data files to represent the tag/data type INTEGER. */
  public static final String LOCAL_DATA_FILE_DATA_TYPE_INTEGER = "INTEGER";

  /** The string to be printed in local data files to represent the tag/data type STRING. */
  public static final String LOCAL_DATA_FILE_DATA_TYPE_STRING = "STRING";

  /**
   * The string to be printed in local data files to represent the tag/data type
   * INTEGER_MAPPED_STRING.
   */
  public static final String LOCAL_DATA_FILE_DATA_TYPE_INTEGER_MAPPED_STRING =
      "INTEGER_MAPPED_STRING";

  /**
   * The string to be printed in local data files to represent the tag/data type UNKNOWN. This
   * tag/data type does not represent an actual Ewon tag/data type integer, and was added to allow
   * for data points to be output to local data files regardless of an unknown tag/data type.
   */
  public static final String LOCAL_DATA_FILE_DATA_TYPE_UNKNOWN = "UNKNOWN";
}
