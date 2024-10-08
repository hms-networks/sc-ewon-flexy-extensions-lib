package com.hms_networks.americas.sc.extensions.system.application;

import com.hms_networks.americas.sc.extensions.config.ConfigFileAccessManager;
import com.hms_networks.americas.sc.extensions.json.JSONException;
import com.hms_networks.americas.sc.extensions.json.JSONObject;
import com.hms_networks.americas.sc.extensions.system.time.SCTimeUnit;
import java.io.File;
import java.io.IOException;

/**
 * Utility class for handling cyclic reboots of a Java application running on the Ewon Flexy system.
 * At startup the reboot watchdog will count the reboot, once the application is stable and has been
 * running for a sufficient amount of time the application should service the watchdog. Servicing
 * the watchdog clears the reboot count. If the watchdog is not serviced before another reboot the
 * reboot count is again incremented. If the reboot count gets too high the jvmrun file is disabled.
 * This functionality helps prevent cyclic reboots in faulty applications. Devices in these reboot
 * loops can be difficult to access via http, ftp, or vpn.
 *
 * @since 1.14.0
 * @author HMS Networks, MU Americas Solution Center
 */
public class SCAppRebootWatchdog {

  /** Path for jvmrun filename when disabled by the watchdog */
  private static final String DISABLED_JVM_RUN_PATH =
      SCAppConstants.JVM_RUN_DIRECTORY
          + File.separator
          + "disabled_"
          + SCAppConstants.JVM_RUN_FILENAME;

  /** Path for reboot watchdog data file */
  private static final String REBOOT_WATCHDOG_FILE_PATH =
      File.separator + "usr" + File.separator + "rebootWatchdog.json";

  /** Key in reboot watchdog data file for reboot count */
  private static final String REBOOT_COUNT_KEY = "RebootCount";

  /** Indent factor for reboot watchdog data file */
  private static final int REBOOT_WATCHDOG_FILE_INDENT_FACTOR = 4;

  /** Max number of reboots before watchdog disables the jvmrun file */
  private static final int MAX_REBOOT_COUNT = 10;

  /** Default time the watchdog has to run before it can be serviced */
  private static final int DEFAULT_WATCHDOG_MIN_SERVICE_TIME_SEC = 0;

  /** Contents of reboot watchdog data file */
  private static JSONObject rebootWatchdogObject;

  /** Status indicator if reboot watchdog is initialized */
  private static boolean isRebootWatchdogInit = false;

  /** Status indicator if reboot watchdog is serviced */
  private static boolean isRebootWatchdogServiced = false;

  /** The time the watchdog will accept servicing */
  private static long watchdogReadyTimeMillis;

  /**
   * Enables the reboot watchdog.
   *
   * <p>The watchdog will be enabled with a default service time of 0 seconds. This means that when
   * using the default service time, the watchdog can be serviced immediately after initialization.
   * This ensures compatibility with existing applications that have implemented the reboot
   * watchdog, but have not incorporated a sizeable delay between watchdog initialization and
   * servicing.
   *
   * <p>For new applications, this method should not be used. Instead, the {@link
   * #enableRebootWatchdog(int)} method should be used.
   *
   * @throws IOException If watchdog file cannot be read from filesystem.
   * @throws JSONException If watchdog file is not valid JSON or doesn't have the correct structure.
   */
  public static void enableRebootWatchdog() throws IOException, JSONException {
    enableRebootWatchdog(DEFAULT_WATCHDOG_MIN_SERVICE_TIME_SEC);
  }

  /**
   * Enables the reboot watchdog.
   *
   * @param serviceTimeSec The time the watchdog has to run before it can be serviced
   * @throws IOException If watchdog file cannot be read from filesystem.
   * @throws JSONException If watchdog file is not valid JSON or doesn't have the correct structure.
   */
  public static void enableRebootWatchdog(int serviceTimeSec) throws IOException, JSONException {
    setWatchdogReadyTime(SCTimeUnit.SECONDS.toMillis(serviceTimeSec));
    initRebootWatchdogFile();
    isRebootWatchdogInit = true;
  }

  /**
   * Services the reboot watchdog. This should be called when the application is in a stable state
   * and successful application and Ewon initialization has been detected by the application.
   *
   * @throws IOException If watchdog file cannot be read from filesystem.
   * @throws JSONException If watchdog file is not valid JSON or doesn't have the correct structure.
   * @return If the watchdog was successfully serviced.
   */
  public static boolean serviceRebootWatchdog() throws IOException, JSONException {
    // Only service the watchdog if its been initialized and not previously serviced
    if (!isRebootWatchdogServiced && isRebootWatchdogInit && checkWatchdogWaitTimer()) {
      resetRebootWatchdogFile();
      isRebootWatchdogServiced = true;
    }

    return isRebootWatchdogServiced;
  }

  /**
   * Forces the service of the reboot watchdog. This should be called if the application needs to
   * stop, potentially before the timer expires, and the reboot counter should not be incremented.
   *
   * @throws IOException If watchdog file cannot be read from filesystem.
   * @throws JSONException If watchdog file is not valid JSON or doesn't have the correct structure.
   */
  public static void forceServiceRebootWatchdog() throws IOException, JSONException {
    resetRebootWatchdogFile();
    isRebootWatchdogServiced = true;
  }

  /**
   * Gets the current number of unsuccessful reboots.
   *
   * @return The current number of unsuccessful reboots. -1 if data file has not been read.
   * @throws JSONException If the reboot count could not be read.
   */
  public static int getCurrentRebootCount() throws JSONException {
    if (rebootWatchdogObject != null) {
      return rebootWatchdogObject.getInt(REBOOT_COUNT_KEY);
    } else {
      final int notInitializedValue = -1;
      return notInitializedValue;
    }
  }

  /**
   * Returns if the reboot watchdog has been initialized.
   *
   * @return Initialization status
   */
  public static boolean isInitialized() {
    return isRebootWatchdogInit;
  }

  /**
   * Returns if the reboot watchdog has been serviced.
   *
   * @return Serviced status
   */
  public static boolean isServiced() {
    return isRebootWatchdogServiced;
  }

  /**
   * Returns the time the watchdog will accept servicing
   *
   * @return the time the watchdog will accept servicing
   */
  public static long getWatchdogReadyTimeMillis() {
    return watchdogReadyTimeMillis;
  }

  /**
   * Initializes the reboot watchdog config file. If the file does not exist it is created and the
   * reboot count is set to zero. If it exists the reboot count is read and incremented by one.
   *
   * @throws IOException If watchdog file cannot be read from filesystem.
   * @throws JSONException If watchdog file is not valid JSON or doesn't have the correct structure.
   */
  private static void initRebootWatchdogFile() throws IOException, JSONException {
    int rebootCount;
    File rebootWatchdogFile = new File(REBOOT_WATCHDOG_FILE_PATH);

    // Check if reboot watchdog file exists, if not create it
    if (!rebootWatchdogFile.isFile()) {
      rebootWatchdogObject = getDefaultRebootWatchdogObject();
      rebootCount = rebootWatchdogObject.getInt(REBOOT_COUNT_KEY);
    }
    // Watchdog file exists, increment the reboot count
    else {
      rebootWatchdogObject =
          ConfigFileAccessManager.getJsonObjectFromFile(REBOOT_WATCHDOG_FILE_PATH);
      rebootCount = rebootWatchdogObject.getInt(REBOOT_COUNT_KEY);
      rebootCount += 1;

      // Device reboot loop has been detected
      if (rebootCount > MAX_REBOOT_COUNT) {
        disableJvmRunFile();
        rebootCount = 0;
      }

      rebootWatchdogObject.put(REBOOT_COUNT_KEY, rebootCount);
    }

    ConfigFileAccessManager.writeJsonObjectToFile(
        REBOOT_WATCHDOG_FILE_PATH, REBOOT_WATCHDOG_FILE_INDENT_FACTOR, rebootWatchdogObject);
  }

  /**
   * Disables the jvmrun file. This is done by modifying the filename as to preserve the contents of
   * the file once the system anomaly causing reboots has been corrected.
   *
   * @throws IOException If watchdog file cannot be read from filesystem.
   * @throws JSONException If watchdog file is not valid JSON or doesn't have the correct structure.
   * @return If the file was successfully disabled (renamed).
   */
  private static boolean disableJvmRunFile() throws IOException, JSONException {
    File jvmRunFile = new File(SCAppConstants.JVM_RUN_PATH);
    File disabledJvmRunFile = new File(DISABLED_JVM_RUN_PATH);

    boolean isRenamed = jvmRunFile.renameTo(disabledJvmRunFile);

    resetRebootWatchdogFile();

    return isRenamed;
  }

  /**
   * Resets the reboot count in the watchdog file.
   *
   * @throws IOException If watchdog file cannot be read from filesystem.
   * @throws JSONException If watchdog file is not valid JSON or doesn't have the correct structure.
   */
  private static void resetRebootWatchdogFile() throws IOException, JSONException {
    if (isRebootWatchdogInit && rebootWatchdogObject != null) {
      final int resetRebootCountValue = 0;
      rebootWatchdogObject.put(REBOOT_COUNT_KEY, resetRebootCountValue);
      ConfigFileAccessManager.writeJsonObjectToFile(
          REBOOT_WATCHDOG_FILE_PATH, REBOOT_WATCHDOG_FILE_INDENT_FACTOR, rebootWatchdogObject);
    }
  }

  /**
   * Generates a default watchdog file contents.
   *
   * @return A default watchdog file as a JSONObject
   * @throws JSONException If the watchdog file object could not be created.
   */
  private static JSONObject getDefaultRebootWatchdogObject() throws JSONException {
    final int startingRebootCount = 0;
    JSONObject defaultRebootWatchdogObject = new JSONObject();

    defaultRebootWatchdogObject.put(REBOOT_COUNT_KEY, startingRebootCount);

    return defaultRebootWatchdogObject;
  }

  /**
   * Sets the time the watchdog will accept servicing
   *
   * @param watchdogWaitTimeMillis the amount of time the watchdog should wait before accepting
   *     servicing
   */
  private static void setWatchdogReadyTime(long watchdogWaitTimeMillis) {
    watchdogReadyTimeMillis = System.currentTimeMillis() + watchdogWaitTimeMillis;
  }

  /**
   * Checks if the watchdog has been running for a sufficient amount of time
   *
   * @return A boolean indicating if the watchdog has been running for a sufficient amount of time
   */
  private static boolean checkWatchdogWaitTimer() {
    return System.currentTimeMillis() >= watchdogReadyTimeMillis;
  }
}
