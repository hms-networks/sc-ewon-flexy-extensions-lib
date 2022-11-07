package com.hms_networks.americas.sc.extensions.mqtt;

/**
 * Class containing constants with a friendly name for MQTT status codes.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.0.0
 * @version 1.1.0
 */
public class MqttStatusCode {

  /**
   * MQTT status code indicating that the status is unknown. This may be used to indicate that the
   * status code is not yet known or that the status code is not available.
   *
   * @since 1.1.0
   */
  public static final int UNKNOWN = -1;

  /**
   * Internal Ewon MQTT status code for 'MQTT process start'.
   *
   * @since 1.1.0
   */
  public static final int INTERNAL_EWON_MQTT_PROCESS_START = 2;

  /**
   * MQTT status code for 'trying to connect'.
   *
   * @since 1.1.0
   */
  public static final int TRYING_TO_CONNECT = 3;

  /**
   * MQTT status code for 'disconnected'.
   *
   * @since 1.1.0
   */
  public static final int DISCONNECTED = 4;

  /**
   * MQTT status code for 'connected'.
   *
   * @since 1.0.0
   */
  public static final int CONNECTED = 5;
}
