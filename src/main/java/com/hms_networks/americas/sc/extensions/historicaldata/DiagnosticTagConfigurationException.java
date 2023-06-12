package com.hms_networks.americas.sc.extensions.historicaldata;

/**
 * Class for Diagnostic Tag Configuration Exception.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.14.1
 */
public class DiagnosticTagConfigurationException extends Exception {

  /**
   * Constructor for a new {@link DiagnosticTagConfigurationException} with specified error message.
   *
   * @param errorMessage error message
   * @param cause cause of exception
   */
  public DiagnosticTagConfigurationException(String errorMessage, Throwable cause) {
    super(errorMessage, cause);
  }
}
