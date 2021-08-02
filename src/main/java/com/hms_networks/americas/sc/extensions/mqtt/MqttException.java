package com.hms_networks.americas.sc.extensions.mqtt;

/**
 * A custom exception for providing a human-readable explanation for exceptions thrown while using
 * {@link MqttManager}.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.0.0
 */
public class MqttException extends Exception {

  /**
   * Constructor providing for a human-readable explanation for an exception thrown while using
   * {@link MqttManager} and the associated backtrace.
   *
   * @param s human-readable explanation
   * @param t throwable for backtrace
   */
  public MqttException(String s, Throwable t) {
    super(s, t);
  }

  /**
   * Constructor providing for a human-readable explanation for an error.
   *
   * @param s human-readable explanation
   */
  public MqttException(String s) {
    super(s);
  }
}
