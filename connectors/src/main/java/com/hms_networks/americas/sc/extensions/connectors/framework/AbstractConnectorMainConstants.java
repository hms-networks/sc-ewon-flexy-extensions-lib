package com.hms_networks.americas.sc.extensions.connectors.framework;

import com.ewon.ewonitf.SysControlBlock;
import com.hms_networks.americas.sc.extensions.historicaldata.HistoricalDataQueueManager;
import com.hms_networks.americas.sc.extensions.logging.Logger;
import java.io.File;

/**
 * Constants for the {@link AbstractConnectorMain}, {@link AbstractConnectorMainUtils}, and {@link
 * AbstractConnectorConfig} classes.
 *
 * @since 1.15.2
 * @see AbstractConnectorMain
 * @see AbstractConnectorMainUtils
 * @see AbstractConnectorConfig
 * @author HMS Networks, MU Americas Solution Center
 */
public class AbstractConnectorMainConstants {

  // region: General Constants

  /**
   * The HTTP connection timeout time in seconds. This value affects the Ewon's global HTTP
   * timeouts.
   */
  public static final String HTTP_TIMEOUT_SECONDS_STRING = "10";

  /** Application watchdog timeout */
  public static final int APP_WATCHDOG_TIMEOUT_MIN = 5;

  /** The Ewon SCB (System Control Block) key for accessing the serial number of the Ewon. */
  public static final String SCB_ITEM_KEY_SERIAL_NUMBER = "SERNUM";

  /** The Ewon SCB (System Control Block) type for accessing the serial number of the Ewon. */
  public static final int SCB_ITEM_TYPE_SERIAL_NUMBER = SysControlBlock.INF;

  /** The directory path for storing certificates used by HTTP. */
  public static final String HTTP_CERTIFICATE_DIRECTORY_PATH = "/usr/connectorCertificates/";

  // endregion

  // region: Data Polling Constants

  /** The minimum memory (in bytes) required to perform a poll of the data queue. */
  public static final int QUEUE_DATA_POLL_MIN_MEMORY_BYTES = 5000000;

  /** The time (in milliseconds) that the data queue must be behind by before warning the user. */
  public static final long QUEUE_DATA_POLL_BEHIND_MILLIS_WARN = 300000;

  /** The maximum number of historical data queue poll failures before a reset is triggered. */
  public static final int QUEUE_DATA_POLL_FAILURE_RESET_THRESHOLD = 5;

  /** The data aggregation period value which indicates that data aggregation is disabled. */
  public static final long QUEUE_DATA_AGGREGATION_PERIOD_SECS_DISABLED = -1;

  // endregion

  // region: Control Tag Constants

  /** The name of the tag that is used to halt the connector execution. */
  public static final String CONNECTOR_HALT_TAG_NAME = "_ConnectorHalt";

  /** The name of the IO server for the tag that is used to halt the connector execution. */
  public static final String CONNECTOR_HALT_TAG_IO_SERVER_NAME = "MEM";

  /** The type of the tag (boolean) that is used to halt the connector execution. */
  public static final int CONNECTOR_HALT_TAG_TYPE = 0;

  /** The description of the tag that is used to halt the connector execution. */
  public static final String CONNECTOR_HALT_TAG_DESCRIPTION =
      "Tag which is used to halt the execution of the connector application.";

  /** The value of the connector halt tag that permits the application to execute. */
  public static final int CONNECTOR_CONTROL_TAG_RUN_VALUE = 0;

  /** The name of the tag that is used to enable/disable connector data polling. */
  public static final String CONNECTOR_DATA_POLLING_DISABLE_TAG_NAME =
      "ConnectorDataPollingDisable";

  /** The disable value for the tag that is used to enable/disable connector data polling. */
  public static final int CONNECTOR_DATA_POLLING_DISABLE_TAG_DISABLE_VALUE = 1;

  /** The type of the tag that is used to enable/disable connector data polling. */
  public static final int CONNECTOR_DATA_POLLING_DISABLE_TAG_TYPE = 0;

  /** The description of the tag that is used to enable/disable connector data polling. */
  public static final String CONNECTOR_DATA_POLLING_DISABLE_TAG_DESCRIPTION =
      "Tag which is used to enable/disable data polling in the connector application.";

  // endregion

  // region: Configuration File Constants

  /** Indent factor for the configuration file JSON contents. */
  public static final int CONFIG_FILE_JSON_INDENT_FACTOR = 3;

  /** Key for accessing the 'General' object in the configuration file. */
  public static final String CONFIG_FILE_GENERAL_KEY = "General";

  /** Key for accessing the 'LogLevel' object in the configuration file. */
  public static final String CONFIG_FILE_LOG_LEVEL_KEY = "LogLevel";

  /** Key for accessing the 'UTF8StringSupport' object in the configuration file. */
  public static final String CONFIG_FILE_UTF8_STRING_SUPPORT_KEY = "UTF8StringSupport";

  /** Key for accessing the 'QueueEnableStringHistory' object in the configuration file. */
  public static final String CONFIG_FILE_QUEUE_STRING_HISTORY_KEY = "QueueEnableStringHistory";

  /** Key for accessing the 'QueueDataPollSizeMins' object in the configuration file. */
  public static final String CONFIG_FILE_QUEUE_DATA_POLL_SIZE_MINS_KEY = "QueueDataPollSizeMins";

  /** Key for accessing the 'QueueDataPollWarnBehindTimeMins' object in the configuration file. */
  public static final String CONFIG_FILE_QUEUE_DATA_POLL_WARN_BEHIND_TIME_MINS_KEY =
      "QueueDataPollWarnBehindTimeMins";

  /** Key for accessing the 'QueueDataPollMaxBehindTimeMins' object in the configuration file. */
  public static final String CONFIG_FILE_QUEUE_DATA_POLL_MAX_BEHIND_TIME_MINS_KEY =
      "QueueDataPollMaxBehindTimeMins";

  /** Key for accessing the 'QueueDataPollIntervalMillis' object in the configuration file. */
  public static final String CONFIG_FILE_QUEUE_DATA_POLL_INTERVAL_MILLIS_KEY =
      "QueueDataPollIntervalMillis";

  /** Key for accessing the 'QueueDataAggregationPeriodSecs' object in the configuration file. */
  public static final String CONFIG_FILE_QUEUE_DATA_AGGREGATION_PERIOD_SECS_KEY =
      "QueueDataAggregationPeriodSecs";

  /** The configuration file JSON key for the enable queue diagnostic tags setting. */
  public static final String CONFIG_FILE_ENABLE_QUEUE_DIAGNOSTIC_TAGS_KEY =
      "QueueEnableDiagnosticTags";

  /** Default value of the 'LogLevel' object in the configuration file. */
  public static final int CONFIG_FILE_LOG_LEVEL_DEFAULT = Logger.LOG_LEVEL_WARN;

  /** Default value of the 'UTF8StringSupport' object in the configuration file. */
  public static final boolean CONFIG_FILE_UTF8_STRING_SUPPORT_DEFAULT = false;

  /**
   * The default size (in mins) of each data queue poll. Changing this will modify the amount of
   * data checked during each poll interval.
   */
  public static final long CONFIG_FILE_QUEUE_DATA_POLL_SIZE_MINS_DEFAULT = 1;

  /**
   * The default warning time (in mins) which data polling may run behind. Changing this will modify
   * the amount of time which data polling may run behind by before a warning is logged.
   */
  public static final long CONFIG_FILE_QUEUE_DATA_POLL_WARN_BEHIND_TIME_MINS_DEFAULT = 10;

  /**
   * The default maximum time (in mins) which data polling may run behind. Changing this will modify
   * the maximum amount of time which data polling may run behind by. By default, this functionality
   * is disabled. The value {@link
   * HistoricalDataQueueManager#DISABLED_MAX_HIST_FIFO_GET_BEHIND_MINS} indicates that the
   * functionality is disabled.
   */
  public static final long CONFIG_FILE_QUEUE_DATA_POLL_MAX_BEHIND_TIME_MINS_DEFAULT =
      HistoricalDataQueueManager.DISABLED_MAX_HIST_FIFO_GET_BEHIND_MINS;

  /** The default interval (in milliseconds) to poll the historical data queue. */
  public static final long CONFIG_FILE_QUEUE_DATA_POLL_INTERVAL_MILLIS_DEFAULT = 10000;

  /**
   * The default aggregation period (in seconds) for data points processed from the historical data
   * queue. The value of -1 indicates that data aggregation is disabled.
   */
  public static final long CONFIG_FILE_QUEUE_DATA_AGGREGATION_PERIOD_SECS_DEFAULT =
      QUEUE_DATA_AGGREGATION_PERIOD_SECS_DISABLED;

  /**
   * Default value of boolean flag indicating if string history data should be retrieved from the
   * queue. String history requires an additional EBD call in the underlying queue library, and will
   * take extra processing time, especially in installations with large string tag counts.
   */
  public static final boolean CONFIG_FILE_QUEUE_DATA_STRING_HISTORY_ENABLED_DEFAULT = false;

  /** The default value for the queue diagnostic tags enabled setting. */
  public static final boolean CONFIG_FILE_QUEUE_ENABLE_DIAGNOSTIC_TAGS_DEFAULT = false;

  /** Application base directory. */
  public static final String APPL_BASE_DIR = File.separator + "usr";

  /**
   * The folder of the default configuration file path. This value is used to build the default
   * configuration file path in {@link #CONFIG_FILE_PATH_DEFAULT}. It is also used to build custom
   * default configuration file paths based on the key of the connector configuration.
   */
  public static final String CONFIG_FILE_PATH_DEFAULT_FOLDER = APPL_BASE_DIR;

  /**
   * The name of the default configuration file path. This value is used to build the default
   * configuration file path in {@link #CONFIG_FILE_PATH_DEFAULT}. It is also used to build custom
   * default configuration file paths based on the key of the connector configuration.
   */
  public static final String CONFIG_FILE_PATH_DEFAULT_NAME = "ConnectorConfig";

  /**
   * The extension of the default configuration file path. This value is used to build the default
   * configuration file path in {@link #CONFIG_FILE_PATH_DEFAULT}. It is also used to build custom
   * default configuration file paths based on the key of the connector configuration.
   */
  public static final String CONFIG_FILE_PATH_DEFAULT_EXTENSION = ".json";

  /**
   * The default configuration file path. This value is used if no configuration file path is
   * specified in the constructor.
   *
   * <p>Using the default configuration file path allows for one single file to be used for the
   * configuration of any/all projects using this {@link AbstractConnectorConfig} class. This
   * functionality should simply work out of the box, but if a connector does not use a unique key
   * for its specific connector object in the configuration file, issues may arise.
   */
  public static final String CONFIG_FILE_PATH_DEFAULT =
      CONFIG_FILE_PATH_DEFAULT_FOLDER
          + File.separator
          + CONFIG_FILE_PATH_DEFAULT_NAME
          + CONFIG_FILE_PATH_DEFAULT_EXTENSION;

  // endregion
}
