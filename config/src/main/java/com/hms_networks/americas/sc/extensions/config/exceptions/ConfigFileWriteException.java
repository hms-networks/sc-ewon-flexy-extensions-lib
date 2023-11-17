package com.hms_networks.americas.sc.extensions.config.exceptions;

/**
 * A custom exception for providing a human-readable explanation for exceptions thrown while writing
 * a configuration file to disk in the {@link com.hms_networks.americas.sc.extensions.config}
 * package.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.0.0
 */
public class ConfigFileWriteException extends ConfigFileException {
  /**
   * Constructor providing for a human-readable explanation for a config file write exception, and a
   * throwable for the backtrace.
   *
   * @param s human-readable explanation
   * @param t throwable for backtrace
   */
  public ConfigFileWriteException(String s, Throwable t) {
    super(s, t);
  }
}
