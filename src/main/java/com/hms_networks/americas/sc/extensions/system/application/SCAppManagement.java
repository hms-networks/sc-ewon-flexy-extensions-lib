package com.hms_networks.americas.sc.extensions.system.application;

import com.ewon.ewonitf.EWException;
import com.ewon.ewonitf.NetManager;
import com.ewon.ewonitf.RuntimeControl;
import com.hms_networks.americas.sc.extensions.fileutils.FileAccessManager;
import com.hms_networks.americas.sc.extensions.logging.Logger;
import com.hms_networks.americas.sc.extensions.system.info.SCSystemInfo;
import com.hms_networks.americas.sc.extensions.system.time.SCTimeUnit;
import java.io.IOException;

/**
 * Utility class for managing a Java application running on the Ewon Flexy system.
 *
 * @since 1.2.0
 * @author HMS Networks, MU Americas Solution Center
 */
public class SCAppManagement {

  /** File path for jvmrun file */
  private static final String JVM_RUN_PATH = "/usr/jvmrun";

  /** Seconds to pause thread when waiting for WAN IP */
  private static final int WAN_WAIT_PERIOD_SECONDS = 20;

  /**
   * Helper function to pause application for set period of time while a WAN connection is being
   * established.
   *
   * @param waitSeconds number of seconds to pause the current thread
   */
  private static void waitForWanHelper(int waitSeconds) throws InterruptedException {
    long sleepTimeMillis = SCTimeUnit.SECONDS.toMillis(waitSeconds);
    Thread.sleep(sleepTimeMillis);
  }

  /**
   * Gets the JVM command from the jvmrun file.
   *
   * @return The JVM command
   * @throws IOException if the jvmrun file is not found or is unreadable.
   */
  public static String getJvmCommand() throws IOException {
    String jvmCommand = FileAccessManager.readFileToString(JVM_RUN_PATH);
    Logger.LOG_DEBUG("Read contents of jvmrun file as: " + jvmCommand);
    return jvmCommand;
  }

  /**
   * Configures the Flexy to launch the application again in the event the application crashes or
   * ends.
   */
  public static void enableAppAutoRestart() {
    // Default command in case the jvmrun file is not found or is not readable.
    String jvmCommand = null;

    try {
      jvmCommand = getJvmCommand();
    } catch (IOException e) {
      Logger.LOG_SERIOUS("Failed to read jvmrun file.");
      Logger.LOG_EXCEPTION(e);
    }

    Logger.LOG_DEBUG("Setting next JVM run command to: " + jvmCommand);
    RuntimeControl.configureNextRunCommand(jvmCommand);
  }

  /**
   * Disables the Flexy from launching the application again in the event the application crashes or
   * ends.
   */
  public static void disableAppAutoRestart() {
    String jvmCommand = null;
    Logger.LOG_DEBUG("Disabling next JVM run command.");
    RuntimeControl.configureNextRunCommand(jvmCommand);
  }

  /**
   * Handles checking minimum firmware version. If current firmware version is less than the
   * configured minimum version this function can optionally end the application.
   *
   * @param shouldExit If the application should be terminated duo to too low of a firmware version
   * @param exitMessage String to be printed if the firmware version is too low and the application
   *     has to exit
   * @return If the current firmware version is equal to or greater than the configured minimum
   *     version
   */
  public static boolean handleFirmwareCheck(boolean shouldExit, String exitMessage) {
    boolean isFwVerValid = false;

    try {
      isFwVerValid = SCSystemInfo.checkMinFirmwareVersion();
    } catch (EWException e) {
      Logger.LOG_SERIOUS("Checking firmware version failed.");
      Logger.LOG_EXCEPTION(e);
    }

    if (!isFwVerValid) {
      try {
        String fwVerTooLowMessage =
            "Current firmware version: "
                + SCSystemInfo.getFirmwareString()
                + " is too low. Please update to Ewon Firmware Version: "
                + SCSystemInfo.getRequiredFirmwareString()
                + " or higher!";
        Logger.LOG_CRITICAL(fwVerTooLowMessage);
      } catch (EWException e) {
        Logger.LOG_EXCEPTION(e);
      }

      if (shouldExit) {
        if (exitMessage == null) {
          exitMessage = "Application ending!";
        }

        Logger.LOG_CRITICAL(exitMessage);

        // Close application
        final int exitStatusCode = 0;
        System.exit(exitStatusCode);
      }
    }

    return isFwVerValid;
  }

  /**
   * Checks if a valid WAN IP address has been assigned to the device.
   *
   * @return true if an initialized WAN IP address is found.
   */
  public static boolean deviceHasWanIP() {
    final String uninitializedWanIp = "0.0.0.0";
    return !(NetManager.getWanIp().equalsIgnoreCase(uninitializedWanIp));
  }

  /**
   * Waits for a number of minutes for a WAN IP to be established if one does not already exist.
   * After the given time has passed, returns false indicating no WAN IP is present.
   *
   * @param timeout the timeout value in the specified time unit
   * @param timeUnit the time unit of the timeout value
   * @return true if an initialized WAN IP is detected, false otherwise
   */
  public static boolean waitForWanIp(int timeout, SCTimeUnit timeUnit) throws InterruptedException {
    // Convert timeout to seconds
    long timeoutSeconds = timeUnit.toSeconds(timeout);

    // Get rounded up number of wait periods in timeout (double cast to avoid integer division)
    int maxWaitPeriods =
        (int) Math.ceil((double) timeoutSeconds / (double) WAN_WAIT_PERIOD_SECONDS);

    boolean wanConnectionEstablished = false;
    // Ensure there is a valid WAN IP address
    for (int i = 0; i < maxWaitPeriods; i++) {

      if (!deviceHasWanIP()) {
        // If no connection is established by end of wait time
        if (i == (maxWaitPeriods - 1)) {
          wanConnectionEstablished = false;
          i = maxWaitPeriods;
        } else {
          waitForWanHelper(WAN_WAIT_PERIOD_SECONDS);
        }
      } else {
        wanConnectionEstablished = true;
        i = maxWaitPeriods;
      }
    }

    return wanConnectionEstablished;
  }

  /**
   * Pause current thread indefinitely if an initialized WAN IP has not been established. When a WAN
   * IP is detected, resume application.
   */
  public static void waitForWanIp() throws InterruptedException {
    // Ensure there is a valid WAN IP address
    while (!deviceHasWanIP()) {
      waitForWanHelper(WAN_WAIT_PERIOD_SECONDS);
    }
  }
}
