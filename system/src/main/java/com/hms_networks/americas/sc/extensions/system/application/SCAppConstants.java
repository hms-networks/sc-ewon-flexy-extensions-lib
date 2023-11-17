package com.hms_networks.americas.sc.extensions.system.application;

import java.io.File;

/**
 * Class for storing constants used in the {@link
 * com.hms_networks.americas.sc.extensions.system.application} package.
 *
 * @since 1.14.0
 * @author HMS Networks, MU Americas Solution Center
 */
public class SCAppConstants {

  /** Filename of jvmrun file */
  public static final String JVM_RUN_FILENAME = "jvmrun";

  /** Directory jvmrun file is located in */
  public static final String JVM_RUN_DIRECTORY = File.separator + "usr";

  /** File path for jvmrun file */
  public static final String JVM_RUN_PATH = JVM_RUN_DIRECTORY + File.separator + JVM_RUN_FILENAME;
}
