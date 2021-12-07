package com.hms_networks.americas.sc.extensions.system.http;

/**
 * A custom exception for providing a human-readable explanation for exceptions thrown while
 * encountering a connection error in {@link SCHttpUtility}.
 *
 * @since 1.3.0
 * @author HMS Networks, MU Americas Solution Center
 */
public class SCHttpConnectionException extends Exception {

  /**
   * Constructor providing for a human-readable explanation for an exception thrown while
   * encountering a connection error in {@link SCHttpUtility} and its backtrace.
   *
   * @param s human-readable explanation
   * @param t throwable for backtrace
   */
  public SCHttpConnectionException(String s, Throwable t) {
    super(s, t);
  }

  /**
   * Constructor providing for a human-readable explanation for an exception thrown while
   * encountering a connection error in {@link SCHttpUtility}.
   *
   * @param s human-readable explanation
   */
  public SCHttpConnectionException(String s) {
    super(s);
  }
}
