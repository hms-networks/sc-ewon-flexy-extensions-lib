package com.hms_networks.americas.sc.extensions.historicaldata;

/**
 * Class for Diagnostic Tag Update Exception.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.14.1
 */
public class DiagnosticTagUpdateException extends Exception {

  /**
   * Constructor for a new {@link DiagnosticTagUpdateException} with specified error message.
   *
   * @param errorMessage error message
   * @param cause cause of exception
   */
  public DiagnosticTagUpdateException(String errorMessage, Throwable cause) {
    super(errorMessage, cause);
  }
}
