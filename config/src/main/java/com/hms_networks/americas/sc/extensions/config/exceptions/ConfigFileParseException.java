package com.hms_networks.americas.sc.extensions.config.exceptions;

/**
 * A custom exception for providing a human-readable explanation for exceptions thrown while parsing
 * a configuration file in the {@link com.hms_networks.americas.sc.extensions.config} package.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.0.0
 */
public class ConfigFileParseException extends ConfigFileException {
  /**
   * Constructor providing for a human-readable explanation for a config file parsing exception, and
   * a throwable for the backtrace.
   *
   * @param s human-readable explanation
   * @param t throwable for backtrace
   */
  public ConfigFileParseException(String s, Throwable t) {
    super(s, t);
  }
}
