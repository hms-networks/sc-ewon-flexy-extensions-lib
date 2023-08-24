package com.hms_networks.americas.sc.extensions.mqtt;

/**
 * Class containing constants for the the {@link com.hms_networks.americas.sc.extensions.mqtt}
 * library.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.0.0
 */
public class MqttConstants {

  /** Key for MQTT port option */
  public static String MQTT_PORT_OPTION_KEY = "PORT";

  /** Default value for MQTT port when using non-secure MQTT connections */
  public static String MQTT_PORT_OPTION_DEFAULT_INSECURE = "1883";

  /** Default value for MQTT port when using secure MQTT connections */
  public static String MQTT_PORT_OPTION_DEFAULT_SECURE = "8883";

  /** Key for MQTT log level option */
  public static String MQTT_LOG_LEVEL_OPTION_KEY = "LOG";

  /** Default value for MQTT log level option */
  public static String MQTT_LOG_LEVEL_OPTION_DEFAULT = "1";

  /** Key for MQTT keep alive (secs) option */
  public static String MQTT_KEEP_ALIVE_OPTION_KEY = "KEEPALIVE";

  /** Default value for MQTT keep alive (secs) option */
  public static String MQTT_KEEP_ALIVE_OPTION_DEFAULT = "10";

  /** Key for MQTT protocol version option */
  public static String MQTT_PROTOCOL_VERSION_OPTION_KEY = "PROTOCOLVERSION";

  /** Default value for MQTT protocol version option */
  public static String MQTT_PROTOCOL_VERSION_OPTION_DEFAULT = "3.1.1";

  /** Key for MQTT TLS version option */
  public static String MQTT_TLS_VERSION_OPTION_KEY = "TLSVERSION";

  /** Key for MQTT CA file path option */
  public static String MQTT_CA_FILE_OPTION_KEY = "CAFILE";

  /** Key for MQTT device certificate file path option */
  public static String MQTT_CERT_FILE_OPTION_KEY = "CERTFILE";

  /** Key for MQTT device certificate key file path option */
  public static String MQTT_KEY_FILE_OPTION_KEY = "KEYFILE";

  /** Key for MQTT username option */
  public static String MQTT_USERNAME_OPTION_KEY = "USERNAME";

  /** Key for MQTT password option */
  public static String MQTT_PASSWORD_OPTION_KEY = "PASSWORD";

  /** Key for MQTT clean session option */
  public static String MQTT_CLEAN_SESSION_OPTION_KEY = "CLEANSESSION";

  /** Key for MQTT max in flight option */
  public static String MQTT_MAX_IN_FLIGHT_OPTION_KEY = "MAXINFLIGHT";

  /** Key for MQTT last will payload option */
  public static String MQTT_WILL_PAYLOAD_OPTION_KEY = "WILLPAYLOAD";

  /** Key for MQTT last will topic option */
  public static String MQTT_WILL_TOPIC_OPTION_KEY = "WILLTOPIC";

  /** Key for MQTT last will QoS option */
  public static String MQTT_WILL_QOS_OPTION_KEY = "WILLQOS";

  /** Key for MQTT last will retain option */
  public static String MQTT_WILL_RETAIN_OPTION_KEY = "WILLRETAIN";
}
