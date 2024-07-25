package com.hms_networks.americas.sc.extensions.historicaldata;

import com.hms_networks.americas.sc.extensions.taginfo.TagType;

/**
 * Class for storing constants used in the queueing package.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.0.0
 */
class HistoricalDataConstants {

  /** Delimiter used in EBD lines */
  public static final String EBD_LINE_DELIMITER = ";";

  /** Index of tag ID in EBD lines */
  public static final int EBD_LINE_TAG_ID_INDEX = 0;

  /** Index of time int in EBD lines */
  public static final int EBD_LINE_TAG_TIMEINT_INDEX = 1;

  /** Index of time string in EBD lines */
  public static final int EBD_LINE_TAG_TIMESTR_INDEX = 2;

  /** Index of tag value in EBD lines */
  public static final int EBD_LINE_TAG_VALUE_INDEX = 4;

  /** Length of EBD lines */
  public static final int EBD_LINE_LENGTH = 6;

  /** Time format used for EBD files */
  public static final String EBD_TIME_FORMAT = "ddMMyyyy_HHmmss";

  /** Folder to store queue files */
  public static final String QUEUE_FILE_FOLDER = "/usr/hist-data-queue";

  /**
   * Extension to use for queue files. Note, storing with a non-standard extension reduces the risk
   * of manual file tampering.
   */
  public static final String QUEUE_FILE_EXTENSION = ".q";

  /** Name of historical data queue time tracker file 1. */
  public static final String QUEUE_TIME_FILE_1_NAME = "histDataTime1";

  /** Name of historical data queue time tracker file 2. */
  public static final String QUEUE_TIME_FILE_2_NAME = "histDataTime2";

  /** Minimum amount of Historical FIFO queue that can be set by library users. */
  public static final long MIN_QUEUE_SPAN_MINS = 1;

  /** Default thread sleep time waiting for EBD in ms. */
  public static final int DEFAULT_EBD_THREAD_SLEEP_MS = 5;

  /** Maximum amount of time that EBD call should wait in milliseconds before timing out. */
  public static final long MAX_EBD_WAIT_MS = 15000;

  /** Tag value representing the negative infinity Float/Double value. */
  public static final String TAG_VALUE_NEGATIVE_INFINITY = "-inf";

  /** Tag value representing the positive infinity Float/Double value. */
  public static final String TAG_VALUE_POSITIVE_INFINITY = "inf";

  /** Tag value representing the NaN Float/Double value. */
  public static final String TAG_VALUE_NAN = "nan";

  /**
   * The name of the diagnostic tag that is populated with the number of seconds that the queue is
   * running behind.
   */
  public static final String QUEUE_DIAGNOSTIC_TAG_RUNNING_BEHIND_SECONDS_NAME =
      "ConnectorQueueBehindSeconds";

  /**
   * The description of the diagnostic tag that is populated with the number of seconds that the
   * queue is running behind.
   */
  public static final String QUEUE_DIAGNOSTIC_TAG_RUNNING_BEHIND_SECONDS_DESC =
      "Diagnostic tag containing the amount of time, in seconds, that the connector data queue is"
          + " running behind.";

  /**
   * The type of the diagnostic tag (DWord) that is populated with the number of seconds that the
   * queue is running behind.
   */
  public static final int QUEUE_DIAGNOSTIC_TAG_RUNNING_BEHIND_SECONDS_TYPE =
      TagType.DWORD.getTypeInt();

  /**
   * The default threshold for the number of seconds that the queue is running behind before the
   * diagnostic tag value is displayed. This value is used when one is not explicitly provided.
   *
   * <p>The running behind time display threshold helps to prevent users from being alarmed by a
   * running behind time which is within acceptable limits.
   */
  public static final int DEFAULT_QUEUE_DIAGNOSTIC_TAG_RUNNING_BEHIND_SECONDS_DISPLAY_THRESHOLD =
      180;

  /**
   * The name of the diagnostic tag that is monitored for a request to forcibly reset the queue time
   * tracker.
   */
  public static final String QUEUE_DIAGNOSTIC_TAG_FORCE_RESET_NAME = "ConnectorQueueForceReset";

  /**
   * The description of the diagnostic tag that is monitored for a request to forcibly reset the
   * queue time tracker.
   */
  public static final String QUEUE_DIAGNOSTIC_TAG_FORCE_RESET_DESC =
      "Diagnostic tag which can be used to request the connector to reset the queue time tracker.";

  /**
   * The type of the diagnostic tag (boolean) that is monitored for a request to forcibly reset the
   * queue time tracker.
   */
  public static final int QUEUE_DIAGNOSTIC_TAG_FORCE_RESET_TYPE = TagType.BOOLEAN.getTypeInt();

  /**
   * The value used to represent true for the diagnostic tag that is monitored for a request to
   * forcibly reset the queue time tracker.
   */
  public static final int QUEUE_DIAGNOSTIC_TAG_FORCE_RESET_TRUE_VALUE = 1;

  /**
   * The value used to represent false for the diagnostic tag that is monitored for a request to
   * forcibly reset the queue time tracker.
   */
  public static final int QUEUE_DIAGNOSTIC_TAG_FORCE_RESET_FALSE_VALUE = 0;

  /**
   * The name of the diagnostic tag that is populated with the number of times that the queue has
   * been polled for data.
   */
  public static final String QUEUE_DIAGNOSTIC_TAG_POLL_COUNT_NAME = "ConnectorQueuePollCount";

  /**
   * The description of the diagnostic tag that is populated with the number of times that the queue
   * has been polled for data.
   */
  public static final String QUEUE_DIAGNOSTIC_TAG_POLL_COUNT_DESC =
      "Diagnostic tag containing the number of times the queue has been polled for data.";

  /**
   * The type of the diagnostic tag (DWord) that is populated with the number of times that the
   * queue has been polled for data.
   */
  public static final int QUEUE_DIAGNOSTIC_TAG_POLL_COUNT_TYPE = TagType.DWORD.getTypeInt();

  /** The IO server used for queue diagnostic tag(s). */
  public static final String QUEUE_DIAGNOSTIC_TAG_IO_SERVER = "MEM";

  /** Start index of the actual ISO 8601 timestamp return by EBD. */
  public static final int EBD_ISO8601_TIMESTAMP_START_INDEX = 1;

  /** String index where milliseconds should be added to a timestamp that lacks milliseconds. */
  public static final int EBD_ISO8601_TIMESTAMP_START_MILLIS = 20;

  /** String to add to a timestamp that lacks milliseconds. */
  public static final String EBD_ISO8601_TIMESTAMP_ADD_MILLIS_STR = ".000";
}
