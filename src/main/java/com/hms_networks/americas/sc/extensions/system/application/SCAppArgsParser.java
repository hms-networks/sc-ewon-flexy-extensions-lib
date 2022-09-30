package com.hms_networks.americas.sc.extensions.system.application;

/**
 * A utility class for parsing the arguments passed to the main method of a Solution Center Java
 * application, and providing methods for checking the values of common/possible arguments (or the
 * default value, if applicable).
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.10.0
 * @version 1.0.0
 */
public class SCAppArgsParser {

  /**
   * The argument specified to a Solution Center Java application to indicate that it was started by
   * the Solution Center Ewon Java Multi-Loader application.
   *
   * @since 1.0.0
   */
  public static final String SC_ARG_STARTED_BY_MULTI_LOADER = "-StartedByMultiLoader";

  /**
   * The boolean value used to indicate if the application was started by the Solution Center Ewon
   * Java Multi-Loader application. This value is set to true if the {@link
   * #SC_ARG_STARTED_BY_MULTI_LOADER} argument is included in the application arguments.
   *
   * @since 1.0.0
   */
  private boolean startedByMultiLoader = false;

  /**
   * Parses the specified arguments (passed to the main method of a Solution Center Java
   * application) and sets the values of the appropriate field.
   *
   * @param args the arguments passed to the main method of a Solution Center Java application
   * @since 1.0.0
   */
  public SCAppArgsParser(String[] args) {
    // Loop through to check for known arguments and store their values
    for (int i = 0; i < args.length; i++) {

      // Set the startedByMultiLoader value if the argument is found
      if (args[i].equals(SC_ARG_STARTED_BY_MULTI_LOADER)) {
        startedByMultiLoader = true;
      }
    }
  }

  /**
   * Gets a boolean value indicating if the application was started by the Solution Center Ewon Java
   * Multi-Loader application.
   *
   * @return the boolean value indicating if the application was started by the Solution Center Ewon
   *     Java Multi-Loader application. This value is set to true if the {@link
   *     #SC_ARG_STARTED_BY_MULTI_LOADER} argument is included in the application arguments. If the
   *     argument is not included, this value is set to false.
   * @since 1.0.0
   */
  public boolean getStartedByMultiLoader() {
    return startedByMultiLoader;
  }
}
