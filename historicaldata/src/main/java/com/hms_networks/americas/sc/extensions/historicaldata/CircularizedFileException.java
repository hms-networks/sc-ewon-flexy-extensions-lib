package com.hms_networks.americas.sc.extensions.historicaldata;

/**
 * Class for Circularized File Exception.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.6.0
 */
public class CircularizedFileException extends Exception {

  /**
   * Constructor for a new {@link CircularizedFileException} with specified error message.
   *
   * @param errorMessage error message
   */
  public CircularizedFileException(String errorMessage) {
    super(errorMessage);
  }
}
