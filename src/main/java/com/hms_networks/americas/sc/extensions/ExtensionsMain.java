package com.hms_networks.americas.sc.extensions;

/**
 * Main class for the project. This class does not perform any functions, as it is contained in a
 * library.
 *
 * @since 1.0.0
 * @author HMS Networks, MU Americas Solution Center
 */
public class ExtensionsMain {

  /**
   * Main method for the project main class.
   *
   * @param args program arguments
   */
  public static void main(String[] args) {
    // Get project information
    String projectName = ExtensionsMain.class.getPackage().getImplementationTitle();
    String projectVersion = ExtensionsMain.class.getPackage().getImplementationVersion();

    // Display error message -- not a runnable Jar
    System.err.println(
        "The project "
            + projectName
            + " v"
            + projectVersion
            + " is not a runnable Jar application and should be referenced using the Java"
            + " classpath.");
  }
}
