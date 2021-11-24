package com.hms_networks.americas.sc.extensions.system.application;

import com.ewon.ewonitf.RuntimeControl;
import com.hms_networks.americas.sc.extensions.fileutils.FileAccessManager;
import com.hms_networks.americas.sc.extensions.logging.Logger;
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
}
