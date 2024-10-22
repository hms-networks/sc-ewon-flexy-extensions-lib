package com.hms_networks.americas.sc.extensions.connectors.framework;

import com.ewon.ewonitf.EventHandlerThread;
import com.ewon.ewonitf.RuntimeControl;
import com.ewon.ewonitf.SysControlBlock;
import com.ewon.ewonitf.TagControl;
import com.hms_networks.americas.sc.extensions.historicaldata.HistoricalDataQueueManager;
import com.hms_networks.americas.sc.extensions.logging.Logger;
import com.hms_networks.americas.sc.extensions.system.application.SCAppArgsParser;
import com.hms_networks.americas.sc.extensions.system.application.SCAppManagement;
import com.hms_networks.americas.sc.extensions.system.http.SCHttpUtility;
import com.hms_networks.americas.sc.extensions.system.info.SCSystemInfo;
import com.hms_networks.americas.sc.extensions.system.time.SCTimeSpan;
import com.hms_networks.americas.sc.extensions.system.time.SCTimeUnit;
import com.hms_networks.americas.sc.extensions.system.time.SCTimeUtils;
import com.hms_networks.americas.sc.extensions.system.time.TimeZoneManager;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Abstract class for the main class of a connector application. This class contains a significant
 * amount of common functionality for connector applications, including:
 *
 * <ul>
 *   <li>Historical data queue configuration
 *   <li>Connector application control tags
 *   <li>Common configuration file interface
 *   <li>Restart connector and/or Ewon device
 *   <li>Automatic restart functionality and watchdog
 * </ul>
 *
 * <p>Connector applications should extend this class and implement each of the lifecycles, as well
 * as configuration file loading, which requires extending the {@link AbstractConnectorConfig} class
 * as well.
 *
 * <p>A common connector main method is provided by the {@link #connectorMain(String[])} method. To
 * use this functionality, the implementing class should simply instantiate the connector main class
 * and call the {@link #connectorMain(String[])} method. For example:
 *
 * <pre>
 *   public static void main(String[] args) {
 *     // Create instance of connector main class
 *     instance = new MyConnectorMain();
 *
 *     // Run connector main method
 *     instance.connectorMain(args);
 *   }
 * </pre>
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.15.2
 */
public abstract class AbstractConnectorMain {

  /**
   * The friendly name of the connector application. This value is stored during connector
   * instantiation and may be used for various purposes, including to identify the connector in
   * logging messages.
   *
   * @since 1.15.2
   */
  private final String connectorFriendlyName;

  /**
   * The cycle time of the connector application. This value is stored during connector
   * instantiation and is used primarily to determine the time between each iteration of the main
   * loop.
   *
   * @since 1.15.2
   */
  private SCTimeSpan connectorCycleTime;

  /**
   * The serial number of the host Ewon device. This value is loaded during connector initialization
   * and may be used for various purposes, including as a unique identifier for the specific
   * connector instance/installation.
   *
   * @since 1.15.2
   */
  private String ewonSerialNumber;

  /**
   * The device name of the host Ewon device. This value may be used for various purposes, including
   * as an identifier for the specific connector instance(s)/installation(s).
   *
   * @since 1.15.2
   */
  private String ewonDeviceName;

  /**
   * The boolean flag indicating whether the connector is running. This flag is used to exit the
   * main loop of the connector application.
   *
   * @since 1.15.2
   */
  private boolean isRunning = true;

  /**
   * The boolean flag indicating whether the connector data polling is disabled. This flag is used
   * to determine whether the connector should poll for data during each iteration of the main loop.
   *
   * @since 1.15.2
   */
  private boolean isDataPollingDisabled = false;

  /**
   * The boolean flag indicating whether the connector should data polling is blocked. This flag is
   * used to determine whether the connector should block polling for data during each iteration of
   * the main loop.
   *
   * <p>Unlike {@link #isDataPollingDisabled}, which indicates whether data polling has been
   * disabled, this flag indicates whether data polling has been blocked due to a temporary
   * condition or error.
   *
   * @since 1.15.12
   */
  private boolean isDataPollingBlocked = false;

  /**
   * The boolean flag indicating whether the connector automatic restart is enabled. This flag is
   * used to determine whether the connector automatic restart functionality was enabled during
   * initialization/start up.
   *
   * @since 1.15.2
   */
  private boolean isAppAutoRestartEnabled = false;

  /**
   * The boolean flag indicating whether the connector has been requested to restart when it shuts
   * down. This flag is used to determine whether the connector should disable automatic restart
   * functionality upon shutdown, or allow it to restart the connector.
   *
   * @since 1.15.2
   */
  private boolean restartAppAfterShutdown = false;

  /**
   * The boolean flag indicating whether the connector has been requested to restart the Ewon device
   * when it shuts down. This flag is used to determine whether the connector should perform a
   * {@link RuntimeControl#reboot()} call upon shutdown.
   *
   * @since 1.15.2
   */
  private boolean restartDeviceAfterShutdown = false;

  /**
   * The tag control object used to access the connector control tag. This tag is used to halt the
   * connector application when requested by the user.
   *
   * @since 1.15.2
   */
  private TagControl connectorControlTag;

  /**
   * The tag control object used to access the connector data polling disable tag. This tag is used
   * to enable or disable the connector data polling functionality.
   *
   * @since 1.15.2
   */
  private TagControl connectorDataPollingDisableTag;

  /**
   * The connector configuration object used to access the connector configuration file. This object
   * is loaded during connector initialization and may be used for various purposes, including to
   * configure the connector application.
   *
   * @since 1.15.2
   */
  private AbstractConnectorConfig abstractConnectorConfig;

  /**
   * Integer counter variable for tracking the number of consecutive failures of polling the
   * historical data queue.
   *
   * @since 1.15.2
   */
  private static int queuePollFailCount = 0;

  /**
   * Boolean flag indicating if the application is running out of memory.
   *
   * @since 1.15.2
   */
  private static boolean isMemoryCurrentlyLow;

  /**
   * Long value used to track the last time the application checked for historical data update.
   *
   * @since 1.15.2
   */
  private static long lastUpdateTimestampMillis = 0;

  /**
   * Constructor for the abstract connector main class. This constructor is used to set the
   * connector-friendly name and cycle time.
   *
   * @param connectorFriendlyName the friendly name of the connector
   * @param connectorCycleTime the cycle time of the connector
   * @since 1.15.2
   */
  public AbstractConnectorMain(String connectorFriendlyName, SCTimeSpan connectorCycleTime) {
    this.connectorFriendlyName = connectorFriendlyName;
    this.connectorCycleTime = connectorCycleTime;
  }

  /**
   * Gets the serial number of the host Ewon device. This value may be used for various purposes,
   * including as a unique identifier for the specific connector instance/installation.
   *
   * @return the serial number of the host Ewon device
   * @since 1.15.2
   */
  public String getEwonSerialNumber() {
    return ewonSerialNumber;
  }

  /**
   * Gets the device name of the host Ewon device. This value may be used for various purposes,
   * including as an identifier for the specific connector instance(s)/installation(s).
   *
   * @return the device name of the host Ewon device
   * @since 1.15.2
   */
  public String getEwonDeviceName() {
    return ewonDeviceName;
  }

  /**
   * Gets the friendly name of the connector application. This value may be used for various
   * purposes, including to identify the connector in logging messages.
   *
   * @return the friendly name of the connector application
   * @since 1.15.2
   */
  public String getConnectorFriendlyName() {
    return connectorFriendlyName;
  }

  /**
   * Gets the cycle time of the connector application. This value is used primarily to determine the
   * time between each iteration of the main loop.
   *
   * @return the cycle time of the connector application
   * @since 1.15.2
   */
  public SCTimeSpan getConnectorCycleTime() {
    return connectorCycleTime;
  }

  /**
   * Sets the cycle time of the connector application. This value is used primarily to determine the
   * time between each iteration of the main loop.
   *
   * @param connectorCycleTime the cycle time of the connector application
   * @since 1.15.2
   */
  public void setConnectorCycleTime(SCTimeSpan connectorCycleTime) {
    this.connectorCycleTime = connectorCycleTime;
  }

  /**
   * Sets the boolean flag indicating whether the connector should block data polling during each
   * iteration of the main loop.
   *
   * <p>This flag is different from {@link #isDataPollingDisabled}, which indicates whether data
   * polling has been disabled. This flag indicates whether data polling has been blocked due to a
   * temporary condition or error.
   *
   * @param isDataPollingBlocked the boolean flag indicating whether the connector should block data
   *     polling during each iteration of the main loop
   * @since 1.15.12
   */
  public void setDataPollingBlocked(boolean isDataPollingBlocked) {
    this.isDataPollingBlocked = isDataPollingBlocked;
  }

  /**
   * Gets the tag control object used to access the connector control tag. This tag is used to halt
   * the connector application when requested by the user.
   *
   * @return the tag control object used to access the connector control tag
   * @since 1.15.2
   */
  public TagControl getConnectorControlTag() {
    return connectorControlTag;
  }

  /**
   * Gets the tag control object used to access the connector data polling disable tag. This tag is
   * used to enable or disable the connector data polling functionality.
   *
   * @return the tag control object used to access the connector data polling disable tag
   * @since 1.15.2
   */
  public TagControl getConnectorDataPollingDisableTag() {
    return connectorDataPollingDisableTag;
  }

  /**
   * Gets the boolean flag indicating whether the connector is running. This flag is used to exit
   * the main loop of the connector application.
   *
   * @return the boolean flag indicating whether the connector is running
   * @since 1.15.2
   */
  public boolean isRunning() {
    return isRunning;
  }

  /**
   * Gets the boolean flag indicating whether the connector data polling is disabled. This flag is
   * used to determine whether the connector should poll for data during each iteration of the main
   * loop.
   *
   * @return the boolean flag indicating whether the connector data polling is disabled
   * @since 1.15.2
   */
  public boolean isDataPollingDisabled() {
    return isDataPollingDisabled;
  }

  /**
   * Gets the boolean flag indicating whether the connector automatic restart is enabled. This flag
   * is used to determine whether the connector automatic restart functionality was enabled during
   * initialization/start up.
   *
   * @return the boolean flag indicating whether the connector automatic restart is enabled
   * @since 1.15.2
   */
  public boolean isAppAutoRestartEnabled() {
    return isAppAutoRestartEnabled;
  }

  /**
   * Gets the boolean flag indicating whether the connector has been requested to restart when it
   * shuts down. This flag is used to determine whether the connector should disable automatic
   * restart functionality upon shutdown, or allow it to restart the connector.
   *
   * @return the boolean flag indicating whether the connector has been requested to restart when it
   *     shuts down
   * @since 1.15.2
   */
  public boolean getRestartAppAfterShutdown() {
    return restartAppAfterShutdown;
  }

  /**
   * Gets the boolean flag indicating whether the connector has been requested to restart the Ewon
   * device when it shuts down. This flag is used to determine whether the connector should perform
   * a {@link RuntimeControl#reboot()} call upon shutdown.
   *
   * @return the boolean flag indicating whether the connector has been requested to restart the
   *     Ewon device when it shuts down
   * @since 1.15.2
   */
  public boolean getRestartDeviceAfterShutdown() {
    return restartDeviceAfterShutdown;
  }

  /**
   * Requests the connector application to shut down then restart. This method does nothing if the
   * connector automatic restart functionality is disabled, because the connector will be unable to
   * restart. If a previous restart request has been made, this method will cancel the previous
   * restart request.
   *
   * @return the current restart connector application request status
   * @since 1.15.2
   */
  public boolean requestRestartConnector() {
    // Request only valid if automatic restart is enabled
    if (isAppAutoRestartEnabled) {
      // Cancel restart request(s)
      restartDeviceAfterShutdown = false;

      // Set restart connector application flag to true and running flag to false
      restartAppAfterShutdown = true;
      isRunning = false;
    }

    // Return restart connector application flag
    return restartAppAfterShutdown;
  }

  /**
   * Requests the connector application to shut down and restart the Ewon device. If a previous
   * restart request has been made, this method will cancel the previous restart request.
   *
   * @return the current restart device request status
   * @since 1.15.2
   */
  public boolean requestRestartDevice() {
    // Cancel restart request(s)
    restartAppAfterShutdown = false;

    // Set restart device flag to true and running flag to false
    restartDeviceAfterShutdown = true;
    isRunning = false;

    // Return restart device flag
    return restartDeviceAfterShutdown;
  }

  /**
   * Requests the connector application to shut down. If a previous restart request has been made,
   * this method will cancel the previous restart request.
   *
   * @since 1.15.2
   */
  public void requestShutdownConnector() {
    // Cancel restart request(s)
    restartAppAfterShutdown = false;
    restartDeviceAfterShutdown = false;

    // Set running flag to false
    isRunning = false;
  }

  /**
   * Method for performing connector application initialization steps.
   *
   * @param args program arguments
   * @return true if successful, false otherwise
   * @since 1.15.2
   */
  private boolean initialize(String[] args) {
    Logger.LOG_CRITICAL("Initializing " + connectorFriendlyName + "...");
    boolean initializeSuccess = true;

    // Parse arguments
    SCAppArgsParser argsParser = new SCAppArgsParser(args);

    // Load any HTTP certificates if they are present
    try {
      File certificateFolder =
          new File(AbstractConnectorMainConstants.HTTP_CERTIFICATE_DIRECTORY_PATH);
      if (!certificateFolder.exists()) {
        certificateFolder.mkdir();
      }
      SCHttpUtility.setHttpCertificatePath(
          AbstractConnectorMainConstants.HTTP_CERTIFICATE_DIRECTORY_PATH);
    } catch (Exception e) {
      Logger.LOG_CRITICAL("Failed to set Ewon HTTP certificate directory!");
      Logger.LOG_EXCEPTION(e);
      initializeSuccess = false;
    }

    // Load Ewon serial number
    try {
      ewonSerialNumber =
          new SysControlBlock(AbstractConnectorMainConstants.SCB_ITEM_TYPE_SERIAL_NUMBER)
              .getItem(AbstractConnectorMainConstants.SCB_ITEM_KEY_SERIAL_NUMBER);
    } catch (Exception e) {
      Logger.LOG_CRITICAL("Failed to load Ewon serial number!");
      Logger.LOG_EXCEPTION(e);
      initializeSuccess = false;
    }

    // Load Ewon device name
    try {
      ewonDeviceName = SCSystemInfo.getEwonName();
    } catch (Exception e) {
      Logger.LOG_CRITICAL("Failed to load Ewon device name!");
      Logger.LOG_EXCEPTION(e);
      initializeSuccess = false;
    }

    // Start thread for default event manager if not started by multi-loader
    if (!argsParser.getStartedByMultiLoader()) {
      try {
        boolean autorun = false;
        EventHandlerThread eventHandler = new EventHandlerThread(autorun);
        eventHandler.runEventManagerInThread();
      } catch (Exception e) {
        Logger.LOG_CRITICAL("Failed to start default event manager thread!");
        Logger.LOG_EXCEPTION(e);
        initializeSuccess = false;
      }
    } else {
      Logger.LOG_DEBUG(
          connectorFriendlyName
              + " was started by the multi-loader application or in a multi-execution context. "
              + "Skipping default event handler thread initialization.");
    }

    // Calculate local time offset and configure queue
    try {
      SCTimeUtils.injectJvmLocalTime();

      final Date currentTime = new Date();
      final String currentLocalTime = SCTimeUtils.getIso8601LocalTimeFormat().format(currentTime);
      final String currentUtcTime = SCTimeUtils.getIso8601UtcTimeFormat().format(currentTime);
      Logger.LOG_DEBUG(
          "The local time zone is "
              + SCTimeUtils.getTimeZoneName()
              + " with an identifier of "
              + SCTimeUtils.getLocalTimeZoneDesignator()
              + ". The current local time is "
              + currentLocalTime
              + ", and the current UTC time is "
              + currentUtcTime
              + ".");
    } catch (Exception e) {
      Logger.LOG_CRITICAL("Failed to inject local time into the JVM!");
      Logger.LOG_EXCEPTION(e);
      initializeSuccess = false;
    }

    // Configure Ewon's HTTP timeouts
    try {
      SCHttpUtility.setHttpTimeouts(AbstractConnectorMainConstants.HTTP_TIMEOUT_SECONDS_STRING);
    } catch (Exception e) {
      Logger.LOG_CRITICAL("Failed to set the Ewon system's HTTP/HTTPS timeout value!");
      Logger.LOG_EXCEPTION(e);
      initializeSuccess = false;
    }

    // Configure auto-restart if not started by multi-loader
    if (!argsParser.getStartedByMultiLoader()) {
      SCAppManagement.enableAppAutoRestart();
    } else {
      Logger.LOG_DEBUG(
          connectorFriendlyName
              + " was started by the multi-loader application or in a multi-execution context. "
              + "Skipping auto-restart configuration.");
    }

    // Load configuration file
    try {
      abstractConnectorConfig = connectorConfigLoad();
    } catch (Exception e) {
      Logger.LOG_CRITICAL("Failed to load the connector configuration file!");
      Logger.LOG_EXCEPTION(e);
      initializeSuccess = false;
      abstractConnectorConfig = null;
    }

    // Verify configuration file loaded successfully
    if (abstractConnectorConfig == null && initializeSuccess) {
      Logger.LOG_CRITICAL("Failed to load the connector configuration file!");
      initializeSuccess = false;
    } else if (abstractConnectorConfig != null
        && !abstractConnectorConfig.checkRequiredConfigLoaded()) {
      Logger.LOG_CRITICAL(
          "The connector configuration file is missing required or critical values!");
      initializeSuccess = false;
    }

    // Proceed with initialization if configuration file loaded successfully
    if (initializeSuccess) {
      // Load connector log level (default of TRACE, but should never encounter this)
      int connectorLogLevel = Logger.LOG_LEVEL_TRACE;
      try {
        connectorLogLevel = abstractConnectorConfig.getConnectorLogLevel();
      } catch (Exception e) {
        Logger.LOG_CRITICAL("Unable to load the connector configuration file!");
        Logger.LOG_EXCEPTION(e);
        initializeSuccess = false;
      }
      boolean logLevelSetSuccess = Logger.SET_LOG_LEVEL(connectorLogLevel);
      if (!logLevelSetSuccess) {
        Logger.LOG_CRITICAL(
            "The log level specified in the connector configuration file is invalid! Please "
                + "refer to the documentation for details on available log levels!");
        initializeSuccess = false;
      }

      // Configure queue string history data option
      try {
        HistoricalDataQueueManager.setStringHistoryEnabled(
            abstractConnectorConfig.getQueueDataStringEnabled());
      } catch (Exception e) {
        Logger.LOG_CRITICAL(
            "Failed to configure the queue option for enabling/disabling string history data!");
        Logger.LOG_EXCEPTION(e);
        initializeSuccess = false;
      }

      // Configure queue data poll size
      try {
        HistoricalDataQueueManager.setQueueFifoTimeSpanMins(
            abstractConnectorConfig.getQueueDataPollSizeMinutes());
      } catch (Exception e) {
        Logger.LOG_CRITICAL(
            "Failed to configure the queue data poll size interval (minutes) option!");
        Logger.LOG_EXCEPTION(e);
        initializeSuccess = false;
      }

      // Configure queue max fall behind time option
      try {
        long queueDataPollMaxBehindTimeMinutes =
            abstractConnectorConfig.getQueueDataPollMaxBehindTimeMinutes();
        if (queueDataPollMaxBehindTimeMinutes
            == HistoricalDataQueueManager.DISABLED_MAX_HIST_FIFO_GET_BEHIND_MINS) {
          Logger.LOG_WARN("Queue maximum fall behind time (minutes) option is not enabled!");
        } else {
          Logger.LOG_DEBUG(
              "Setting the queue maximum fall behind time (minutes) option to "
                  + queueDataPollMaxBehindTimeMinutes
                  + ".");
        }
        HistoricalDataQueueManager.setQueueMaxBehindMins(queueDataPollMaxBehindTimeMinutes);
      } catch (Exception e) {
        Logger.LOG_CRITICAL(
            "Failed to configure the queue data poll maximum fall behind time (minutes) option!");
        Logger.LOG_EXCEPTION(e);
        initializeSuccess = false;
      }

      // Configure queue diagnostic tags option
      try {
        Logger.LOG_CRITICAL(
            "Setting the queue diagnostic tags option to "
                + abstractConnectorConfig.getQueueDiagnosticTagsEnabled()
                + ".");
        HistoricalDataQueueManager.setEnableDiagnosticTags(
            abstractConnectorConfig.getQueueDiagnosticTagsEnabled(),
            SCTimeUnit.MINUTES.toSeconds(
                abstractConnectorConfig.getQueueDataPollWarnBehindTimeMinutes()));
      } catch (Exception e) {
        Logger.LOG_CRITICAL("Failed to configure the queue diagnostic tags enabled option!");
        Logger.LOG_EXCEPTION(e);
        initializeSuccess = false;
      }
    }

    // Invoke connector initialize hook
    initializeSuccess &= connectorInitialize();

    if (initializeSuccess) {
      Logger.LOG_CRITICAL("Finished initializing " + connectorFriendlyName + ".");
    } else {
      Logger.LOG_CRITICAL("Failed to initialize " + connectorFriendlyName + "!");
    }
    return initializeSuccess;
  }

  /**
   * Method for performing connector application start up steps.
   *
   * @return true if successful, false otherwise
   * @since 1.15.2
   */
  private boolean startUp() {
    Logger.LOG_CRITICAL("Starting " + connectorFriendlyName + "...");
    boolean startUpSuccess = true;

    // Configure connector control tags
    connectorControlTag = AbstractConnectorMainUtils.setUpConnectorControlTag();
    connectorDataPollingDisableTag =
        AbstractConnectorMainUtils.setupConnectorDataPollingDisableControlTag();
    if (connectorControlTag == null || connectorDataPollingDisableTag == null) {
      startUpSuccess = false;
    }

    // Invoke connector start up hook
    startUpSuccess &= connectorStartUp();

    // Configure the application watchdog (if shutdown/restart not already requested)
    if (isRunning) {
      RuntimeControl.configureAppWatchdog(AbstractConnectorMainConstants.APP_WATCHDOG_TIMEOUT_MIN);
    }

    if (startUpSuccess) {
      Logger.LOG_CRITICAL("Finished starting " + connectorFriendlyName + ".");
    } else {
      Logger.LOG_CRITICAL("Failed to start " + connectorFriendlyName + "!");
    }
    return startUpSuccess;
  }

  /**
   * Method for performing connector application shut down steps.
   *
   * @since 1.15.2
   */
  private void shutDown() {
    Logger.LOG_CRITICAL("Shutting down " + connectorFriendlyName + "...");
    boolean shutDownClean = true;

    // Invoke connector shutdown hook
    shutDownClean &= connectorShutDown();

    // Log shutdown status
    if (shutDownClean) {
      Logger.LOG_CRITICAL("Finished shutting down " + connectorFriendlyName + ".");
    } else {
      Logger.LOG_CRITICAL("Failed to shut down " + connectorFriendlyName + " properly!");
    }
  }

  /**
   * Method for performing connector application clean up steps.
   *
   * @since 1.15.2
   */
  private void cleanUp() {
    Logger.LOG_CRITICAL("Cleaning up " + connectorFriendlyName + "...");
    boolean cleanUpSuccess = true;

    // Disable app watchdog
    try {
      final int watchDogTimeoutDisabled = 0;
      RuntimeControl.configureAppWatchdog(watchDogTimeoutDisabled);
    } catch (Exception e) {
      Logger.LOG_CRITICAL("Failed to disable the application watchdog!");
      Logger.LOG_EXCEPTION(e);
      cleanUpSuccess = false;
    }

    // Invoke connector clean up hook
    try {
      cleanUpSuccess &= connectorCleanUp();
    } catch (Exception e) {
      Logger.LOG_CRITICAL("Failed to clean up due to an uncaught exception!");
      Logger.LOG_EXCEPTION(e);
      cleanUpSuccess = false;
    }

    // Clear config variable
    abstractConnectorConfig = null;

    // Log clean up status
    if (cleanUpSuccess) {
      Logger.LOG_CRITICAL("Finished cleaning up " + connectorFriendlyName + ".");
    } else {
      Logger.LOG_CRITICAL("Failed to clean up " + connectorFriendlyName + " properly!");
    }
  }

  /**
   * Method for performing connector application post clean up steps. This method is called after
   * the shutdown and clean up lifecycles to facilitate a connector or device restart, if requested.
   * There is no connector-specific implementation for this method.
   *
   * @since 1.15.2
   */
  private void postCleanUp() {
    // Trigger a restart of the connector (if flag is set)
    if (restartAppAfterShutdown) {
      // Exit with non-zero exit code otherwise app auto restart doesn't work
      final int nonNormalExitCode = -1;
      Logger.LOG_CRITICAL(connectorFriendlyName + " is restarting...");
      System.exit(nonNormalExitCode);
    }
    // Trigger a restart of the device (if flag is set)
    else if (restartDeviceAfterShutdown) {
      Logger.LOG_CRITICAL(connectorFriendlyName + " is restarting the device...");
      RuntimeControl.reboot();
    }
    // Otherwise, disable app auto restart
    else {
      Logger.LOG_CRITICAL(connectorFriendlyName + " has finished running.");
      SCAppManagement.disableAppAutoRestart();
    }
  }

  /**
   * Method for performing connector application data polling steps. This method is called during
   * iterations of the main loop, and is used to poll the historical data queue for new data points.
   * There is no connector-specific implementation for this method. Resulting data points are passed
   * to the {@link #connectorProcessDataPoints(List)} or {@link
   * #connectorProcessAggregatedDataPoints(Map)} method based on the configuration of data
   * aggregation.
   *
   * @since 1.15.2
   */
  private void pollData() {
    // Get queue data poll interval (millis) from config
    long queueDataPollIntervalMillis = abstractConnectorConfig.getQueueDataPollIntervalMillis();

    // Store current timestamp and available memory
    long currentReadTimestampMillis = System.currentTimeMillis();
    long availableMemoryBytes = Runtime.getRuntime().freeMemory();
    if ((currentReadTimestampMillis - lastUpdateTimestampMillis) >= queueDataPollIntervalMillis) {

      // Check if memory is within permissible range to poll data queue
      if (availableMemoryBytes < AbstractConnectorMainConstants.QUEUE_DATA_POLL_MIN_MEMORY_BYTES) {
        // Show low memory warning
        Logger.LOG_WARN("Low memory on device, " + (availableMemoryBytes / 1000) + " MB left!");

        // If low memory flag not set, set it and request garbage collection
        if (!isMemoryCurrentlyLow) {
          // Set low memory flag
          isMemoryCurrentlyLow = true;

          // Tell the JVM that it should garbage collect soon
          System.gc();
        }
      } else {
        // There is enough memory to run, reset memory state variable.
        if (isMemoryCurrentlyLow) {
          isMemoryCurrentlyLow = false;
        }

        // Get aggregation configuration
        long queueDataAggregationPeriodSecs =
            abstractConnectorConfig.getQueueDataAggregationPeriodSecs();

        // Retrieve data from queue (if required)
        try {
          // Check if a new time tracker should be started
          final boolean startNewTimeTracker;
          if (HistoricalDataQueueManager.doesTimeTrackerExist()
              && queuePollFailCount
                  < AbstractConnectorMainConstants.QUEUE_DATA_POLL_FAILURE_RESET_THRESHOLD) {
            startNewTimeTracker = false;
          } else {
            if (queuePollFailCount
                >= AbstractConnectorMainConstants.QUEUE_DATA_POLL_FAILURE_RESET_THRESHOLD) {
              Logger.LOG_WARN(
                  "The maximum number of failures to read the historical "
                      + "data queue has been reached ("
                      + AbstractConnectorMainConstants.QUEUE_DATA_POLL_FAILURE_RESET_THRESHOLD
                      + "). Forcing a new queue time tracker!");
            }
            startNewTimeTracker = true;
          }

          // Read data points from queue
          int numDatapointsReadFromQueue;
          ArrayList datapointsReadFromQueueList = null;
          Map datapointsReadFromQueueMap = null;
          if (queueDataAggregationPeriodSecs
              == AbstractConnectorMainConstants.QUEUE_DATA_AGGREGATION_PERIOD_SECS_DISABLED) {
            datapointsReadFromQueueList =
                HistoricalDataQueueManager.getFifoNextSpanDataAllGroups(startNewTimeTracker);
            numDatapointsReadFromQueue = datapointsReadFromQueueList.size();
          } else {
            SCTimeSpan dataAggregationTimeSpan =
                new SCTimeSpan(queueDataAggregationPeriodSecs, SCTimeUnit.SECONDS);
            datapointsReadFromQueueMap =
                HistoricalDataQueueManager.getFifoNextSpanDataAllGroups(
                    startNewTimeTracker, dataAggregationTimeSpan);
            numDatapointsReadFromQueue = datapointsReadFromQueueMap.size();
          }

          Logger.LOG_DEBUG(
              "Read " + numDatapointsReadFromQueue + " data points from the historical log.");

          // Reset failure counter
          queuePollFailCount = 0;

          // Check if queue is behind
          try {
            long queueBehindMillis = HistoricalDataQueueManager.getQueueTimeBehindMillis();
            if (queueBehindMillis
                >= AbstractConnectorMainConstants.QUEUE_DATA_POLL_BEHIND_MILLIS_WARN) {
              Logger.LOG_WARN(
                  "The historical data queue is running behind by "
                      + SCTimeUtils.getDayHourMinSecsForMillis((int) queueBehindMillis));
            }

          } catch (IOException e) {
            Logger.LOG_SERIOUS("Unable to detect if historical data queue is running behind.");
            Logger.LOG_EXCEPTION(e);
          }

          // Process data points
          boolean processDataPointsSuccess = false;
          if (datapointsReadFromQueueList != null) {
            processDataPointsSuccess = connectorProcessDataPoints(datapointsReadFromQueueList);
          } else if (datapointsReadFromQueueMap != null) {
            processDataPointsSuccess =
                connectorProcessAggregatedDataPoints(datapointsReadFromQueueMap);
          }

          // Update last update timestamp if data points were processed successfully
          if (processDataPointsSuccess) {
            lastUpdateTimestampMillis = currentReadTimestampMillis;
          }
        } catch (Exception e) {
          Logger.LOG_CRITICAL(
              "An error occurred while reading "
                  + "data from the historical log. (#"
                  + ++queuePollFailCount
                  + ")");
          Logger.LOG_EXCEPTION(e);
        }
      }
    }
  }

  /**
   * Main method for Ewon Flexy Cumulocity Connector.
   *
   * @param args project arguments
   * @since 1.15.2
   */
  public void connectorMain(String[] args) {
    // Initialize connector
    boolean initialized = initialize(args);

    // Start connector if initialization was successful
    boolean startedUp = false;
    if (initialized) {
      startedUp = startUp();
    }

    // Run connector main loop if initialization and startup were successful
    if (initialized && startedUp) {
      // Cyclically run main loop and sleep while connector is running
      while (isRunning) {
        // Service the watchdog
        RuntimeControl.refreshWatchdog();

        // Check for connector control tag value change
        if (connectorControlTag != null && isRunning) {
          isRunning =
              (connectorControlTag.getTagValueAsInt()
                  == AbstractConnectorMainConstants.CONNECTOR_CONTROL_TAG_RUN_VALUE);
        }

        // Check for connector data polling disable tag value change
        if (connectorDataPollingDisableTag != null) {
          isDataPollingDisabled =
              (connectorDataPollingDisableTag.getTagValueAsInt()
                  == AbstractConnectorMainConstants
                      .CONNECTOR_DATA_POLLING_DISABLE_TAG_DISABLE_VALUE);
        }

        // Invoke connector data polling (if not disabled)
        if (isDataPollingDisabled) {
          Logger.LOG_DEBUG("Data polling is disabled and has been skipped.");
        } else if (isDataPollingBlocked) {
          Logger.LOG_DEBUG("Data polling is blocked and has been skipped.");
        } else {
          pollData();
        }

        // Invoke connector main loop
        connectorLoopRun();

        try {
          TimeZoneManager.checkUpdateTimeZone();
        } catch (Exception e) {
          Logger.LOG_SERIOUS(
              "An error occurred while attempting to check and update the local timezone offset.");
          Logger.LOG_EXCEPTION(e);
        }

        // Sleep for specified cycle time
        try {
          Thread.sleep(connectorCycleTime.getTimeSpanMillis());
        } catch (InterruptedException e) {
          Logger.LOG_WARN("Connector main thread interrupted while sleeping.");
          Logger.LOG_EXCEPTION(e);
        }
      }
    }

    // Shutdown and cleanup connector
    shutDown();
    cleanUp();
    postCleanUp();
  }

  /**
   * Performs connector initialization steps. This method is invoked once at the beginning of the
   * connector application lifecycle. It is intended to be used for performing any necessary
   * initialization steps, such as reading configuration files, initializing data structures, etc.
   * This method should return true if initialization was successful, or false otherwise.
   *
   * @return {@code true} if initialization was successful, or {@code false} otherwise
   * @since 1.15.2
   */
  public abstract boolean connectorInitialize();

  /**
   * Performs connector startup steps. This method is invoked once at the beginning of the connector
   * application lifecycle, after {@link #connectorInitialize()} has been invoked. It is intended to
   * be used for performing any necessary startup steps, such as connecting to external devices,
   * starting threads, etc. This method should return true if startup was successful, or false
   * otherwise.
   *
   * @return {@code true} if startup was successful, or {@code false} otherwise
   * @since 1.15.2
   */
  public abstract boolean connectorStartUp();

  /**
   * Performs processing of data points received from the historical data queue during polling. This
   * method is invoked after data polling has been performed, and data points were successfully
   * retrieved from the historical data queue. It is intended to be used for performing any
   * necessary processing steps, such as preparing data for transmission, etc. This method should
   * return true if data processing was successful, or false otherwise.
   *
   * @param dataPoints the list of data points to process
   * @return {@code true} if data processing was successful, or {@code false} otherwise
   * @throws Exception if an exception occurs while processing the data points
   * @since 1.15.2
   */
  public abstract boolean connectorProcessDataPoints(List dataPoints) throws Exception;

  /**
   * Performs processing of aggregated data points received from the historical data queue during
   * polling. This method is invoked after data polling has been performed, and data points were
   * successfully retrieved from the historical data queue. It is intended to be used for performing
   * any necessary processing steps, such as preparing data for transmission, etc. This method
   * should return true if data processing was successful, or false otherwise.
   *
   * @param dataPoints the map of aggregated data points to process
   * @return {@code true} if data processing was successful, or {@code false} otherwise
   * @throws Exception if an exception occurs while processing the data points
   * @since 1.15.2
   */
  public abstract boolean connectorProcessAggregatedDataPoints(Map dataPoints) throws Exception;

  /**
   * Performs connector main loop steps. This method is invoked cyclically by the connector main
   * loop. It is intended to be used for performing any necessary main loop steps that are not
   * related to data polling, such as checking for remote management commands, etc.
   *
   * <p>Note: This method is always invoked, regardless of whether data polling is enabled or not.
   *
   * @since 1.15.2
   */
  public abstract void connectorLoopRun();

  /**
   * Performs connector shutdown steps. This method is invoked once at the end of the connector
   * application lifecycle, before {@link #connectorCleanUp()} has been invoked. It is intended to
   * be used for performing any necessary shutdown steps, such as stopping threads, disconnecting
   * from external devices, etc. This method should return true if shutdown was successful, or false
   * otherwise.
   *
   * @return {@code true} if shutdown was successful, or {@code false} otherwise
   * @since 1.15.2
   */
  public abstract boolean connectorShutDown();

  /**
   * Performs connector cleanup steps. This method is invoked once at the end of the connector
   * application lifecycle, after {@link #connectorShutDown()} has been invoked. It is intended to
   * be used for performing any necessary cleanup steps, such as clearing caches, freeing memory,
   * etc. This method should return true if cleanup was successful, or false otherwise.
   *
   * @return {@code true} if cleanup was successful, or {@code false} otherwise
   * @since 1.15.2
   */
  public abstract boolean connectorCleanUp();

  /**
   * Performs connector configuration load steps. This method is invoked once at the beginning of
   * the connector application lifecycle, during {@link #connectorInitialize()}. It is intended to
   * be used for loading the connector configuration from the configuration file, and returning the
   * resulting {@link AbstractConnectorConfig} object.
   *
   * <p>The {@link AbstractConnectorConfig} object returned by this method is used to configure
   * various aspects of the connector, such as the historical queue settings, log level, etc.
   *
   * <p>The returned {@link AbstractConnectorConfig} object will be verified to ensure that all
   * required/critical values are loaded using the {@link
   * AbstractConnectorConfig#checkRequiredConfigLoaded()} method. The connector may optionally call
   * the {@link AbstractConnectorConfig#checkRequiredConfigLoaded()} method directly, and as
   * required, could set default values, or wait for external configuration, such as through HTTP
   * APIs.
   *
   * @throws Exception if the configuration file could not be loaded
   * @return the {@link AbstractConnectorConfig} object containing the connector configuration. If
   *     the configuration file could not be loaded, this method should return null or throw an
   *     exception.
   * @since 1.15.2
   */
  public abstract AbstractConnectorConfig connectorConfigLoad() throws Exception;
}
