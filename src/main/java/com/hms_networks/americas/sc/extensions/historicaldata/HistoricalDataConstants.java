package com.hms_networks.americas.sc.extensions.historicaldata;

import com.hms_networks.americas.sc.extensions.system.time.SCTimeUnit;

/**
 * Class for storing constants used in the queueing package.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.0.0
 */
class HistoricalDataConstants {

  /** Number of seconds in one minute */
  static final int TIME_SECS_PER_MIN = 60;

  /** Number of milliseconds in one second */
  static final int TIME_MS_PER_SEC = 1000;

  /** Delimiter used in EBD lines */
  static final String EBD_LINE_DELIMITER = ";";

  /** Index of tag ID in EBD lines */
  static final int EBD_LINE_TAG_ID_INDEX = 0;

  /** Index of time int in EBD lines */
  static final int EBD_LINE_TAG_TIMEINT_INDEX = 1;

  /** Index of tag value in EBD lines */
  static final int EBD_LINE_TAG_VALUE_INDEX = 4;

  /** Length of EBD lines */
  static final int EBD_LINE_LENGTH = 6;

  /** Time format used for EBD files */
  static final String EBD_TIME_FORMAT = "ddMMyyyy_HHmmss";

  /** Folder to store queue files */
  static final String QUEUE_FILE_FOLDER = "/usr/hist-data-queue";

  /**
   * Extension to use for queue files. Note, storing with a non-standard extension reduces the risk
   * of manual file tampering.
   */
  static final String QUEUE_FILE_EXTENSION = ".q";

  /** Name of EBD export call file */
  static final String QUEUE_EBD_FILE_NAME = "histDataEBD";

  /** Name of EBD string export call file */
  static final String QUEUE_EBD_STRING_FILE_NAME = "histStringDataEBD";

  /** Name of historical data queue time tracker file 1. */
  static final String QUEUE_TIME_FILE_1_NAME = "histDataTime1";

  /** Name of historical data queue time tracker file 2. */
  static final String QUEUE_TIME_FILE_2_NAME = "histDataTime2";

  /** Minimum amount of Historical FIFO queue that can be set by library users. */
  static final long MIN_QUEUE_SPAN_MINS = 1;

  /** Default maximum amount of time Historical FIFO can get behind. */
  static final long MAX_HIST_FIFO_GET_BEHIND_MS = SCTimeUnit.HOURS.toMillis(12);

  /** Default thread sleep time waiting for EBD in ms. */
  static final int DEFAULT_EBD_THREAD_SLEEP_MS = 5;

  /** Maximum amount of time that EBD call should wait in milliseconds before timing out. */
  static final long MAX_EBD_WAIT_MS = 15000;
}
