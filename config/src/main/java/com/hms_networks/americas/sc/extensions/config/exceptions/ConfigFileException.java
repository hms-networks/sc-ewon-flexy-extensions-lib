package com.hms_networks.americas.sc.extensions.config.exceptions;

/**
 * A custom exception for providing a super class to catch all custom exceptions thrown in the
 * {@link com.hms_networks.americas.sc.extensions.config} package.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.0.0
 */
public class ConfigFileException extends Exception {
  /**
   * Constructor providing for a human-readable explanation for the exception, and a throwable for
   * the backtrace.
   *
   * @param s human-readable explanation
   * @param t throwable for backtrace
   */
  public ConfigFileException(String s, Throwable t) {
    super(s, t);
  }
}
