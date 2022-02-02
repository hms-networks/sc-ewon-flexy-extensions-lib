package com.hms_networks.americas.sc.extensions.mqtt;

import com.ewon.ewonitf.EWException;
import com.ewon.ewonitf.MqttClient;
import com.ewon.ewonitf.MqttMessage;
import java.io.UnsupportedEncodingException;

/**
 * A helper class for managing MQTT connections and their properties/configuration.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.0.0
 */
public abstract class MqttManager extends MqttClient {

  /**
   * Thread used for running MQTT tasks on loop with a configured delay between executions.
   *
   * @see #mqttThreadSleepIntervalMs
   */
  private Thread mqttThread;

  /** Delay between executions of the MQTT thread. */
  private long mqttThreadSleepIntervalMs = 1000;

  /** Boolean flag to control the MQTT thread loop. */
  private boolean mqttThreadRun = true;

  /**
   * Boolean flag to enable UTF8 string conversion. Will enable non-ASCII characters, but decreases
   * performance by a significant amount. Defaults to false, and can be set by constructor.
   */
  private boolean mqttUtf8Convert = false;

  /** UTF-8 charset name */
  private static final String UTF_8 = "UTF-8";

  /**
   * Main constructor for MQTT manager class. Required specified MQTT Client ID and MQTT Broker URL.
   *
   * @param mqttId MQTT client ID
   * @param mqttHost MQTT broker URL
   * @throws Exception if unable to create MQTT client
   */
  public MqttManager(String mqttId, String mqttHost) throws Exception {
    super(mqttId, mqttHost);

    // Set necessary default options
    setPortNonSecureDefault();
    setLogLevel(MqttConstants.MQTT_LOG_LEVEL_OPTION_DEFAULT);
    setKeepAliveSecs(MqttConstants.MQTT_KEEP_ALIVE_OPTION_DEFAULT);
    setProtocolVersion(MqttConstants.MQTT_PROTOCOL_VERSION_OPTION_DEFAULT);
  }

  /**
   * Additional constructor for MqttManager class. Required parameters MQTT Client ID, MQTT Broker
   * URL and boolean to enable UTF-8 conversion of payload. UTF-8 is recommended when tag names or
   * tag values may have non-ASCII values.
   *
   * @param mqttId MQTT client ID
   * @param mqttHost MQTT broker URL
   * @param enableUtf8 enable support for UTF-8 strings, necessary for non-ASCII characters
   * @throws Exception if unable to create MQTT client
   */
  public MqttManager(String mqttId, String mqttHost, boolean enableUtf8) throws Exception {
    this(mqttId, mqttHost);
    this.mqttUtf8Convert = enableUtf8;
  }

  /**
   * Publish an MQTT message. This method will first create the MqttMessage and then publish it.
   *
   * @param topic MQTT topic
   * @param payload MQTT payload
   * @param qos MQTT quality of service
   * @param retain if the message should be retained by MQTT broker for future clients
   * @throws EWException Ewon exception - check Ewon events file for details
   * @throws UnsupportedEncodingException character encoding is not supported
   */
  public void mqttPublish(String topic, String payload, int qos, boolean retain)
      throws EWException, UnsupportedEncodingException {
    MqttMessage mqttMessage;
    if (mqttUtf8Convert) {
      byte[] payloadUtf8Bytes = payload.getBytes(UTF_8);
      mqttMessage = new MqttMessage(topic, payloadUtf8Bytes);
    } else {
      mqttMessage = new MqttMessage(topic, payload);
    }
    publish(mqttMessage, qos, retain);
  }

  /**
   * Starts the MQTT thread. This method creates a new MQTT thread, and will overwrite existing
   * ones.
   */
  public void startMqttThread() {
    // Stop MQTT thread if running
    stopMqttThread();

    // Set up MQTT thread to call internal MQTT thread method and start
    mqttThread =
        new Thread(
            new Runnable() {
              public void run() {
                internalMqttThreadMethod();
              }
            });

    mqttThread.start();
  }

  /** Stops the MQTT thread (if it is running) */
  public void stopMqttThread() {
    mqttThreadRun = false;
  }

  /**
   * Set the sleep interval (in ms) for sleeping between intervals of the MQTT thread.
   *
   * @param mqttThreadSleepIntervalMs sleep interval (in ms)
   */
  public void setMqttThreadSleepIntervalMs(long mqttThreadSleepIntervalMs) {
    this.mqttThreadSleepIntervalMs = mqttThreadSleepIntervalMs;
  }

  /** Internal (private) method that runs on {@link #mqttThread}. */
  private void internalMqttThreadMethod() {
    // Set MQTT thread run flag to true
    mqttThreadRun = true;

    // Loop while MQTT thread run flag is true
    while (mqttThreadRun) {
      // Run abstract MQTT loop function
      runOnMqttLoop();

      // Delay for configured seconds before running again
      try {
        Thread.sleep(mqttThreadSleepIntervalMs);
      } catch (InterruptedException e) {
        // Create human-readable exception explanation and call onError().
        String exceptionMsg = "Unable to delay between MQTT loop executions!";
        MqttException mqttException = new MqttException(exceptionMsg, e);
        onError(mqttException);
      }
    }
  }

  /** Disconnects and ends the MQTT connection */
  public void disconnect() {
    close();
  }

  /**
   * Configures the MQTT client to use the default port for non-secure MQTT connections.
   *
   * @see MqttConstants#MQTT_PORT_OPTION_DEFAULT_INSECURE
   * @throws EWException if unable to apply port
   */
  public void setPortNonSecureDefault() throws EWException {
    setPort(MqttConstants.MQTT_PORT_OPTION_DEFAULT_INSECURE);
  }

  /**
   * Configures the MQTT client to use the default port for secure MQTT connections.
   *
   * @see MqttConstants#MQTT_PORT_OPTION_DEFAULT_SECURE
   * @throws EWException if unable to apply port
   */
  public void setPortSecureDefault() throws EWException {
    setPort(MqttConstants.MQTT_PORT_OPTION_DEFAULT_SECURE);
  }

  /**
   * Configures the MQTT client to use the specified port.
   *
   * @param port MQTT client port
   * @throws EWException if unable to apply port
   */
  public void setPort(String port) throws EWException {
    setOption(MqttConstants.MQTT_PORT_OPTION_KEY, port);
  }

  /**
   * Configures the log level of the MQTT client to the specified level.
   *
   * @param logLevel MQTT client log level
   * @throws EWException if unable to apply log level
   */
  public void setLogLevel(String logLevel) throws EWException {
    setOption(MqttConstants.MQTT_LOG_LEVEL_OPTION_KEY, logLevel);
  }

  /**
   * Configures the keep alive setting of the MQTT client to the specified number of seconds.
   *
   * @param keepAliveSecs keep alive setting (secs)
   * @throws EWException if unable to apply keep alive
   */
  public void setKeepAliveSecs(String keepAliveSecs) throws EWException {
    setOption(MqttConstants.MQTT_KEEP_ALIVE_OPTION_KEY, keepAliveSecs);
  }

  /**
   * Configures the MQTT client to use the specified MQTT protocol version.
   *
   * @param protocolVersion MQTT protocol version
   * @throws EWException if unable to apply protocol version
   */
  public void setProtocolVersion(String protocolVersion) throws EWException {
    setOption(MqttConstants.MQTT_PROTOCOL_VERSION_OPTION_KEY, protocolVersion);
  }

  /**
   * Configures the TLS version of the MQTT client to the specified version.
   *
   * @param tlsVersion TLS version
   * @throws EWException if unable to apply TLS version
   */
  public void setTLSVersion(String tlsVersion) throws EWException {
    setOption(MqttConstants.MQTT_TLS_VERSION_OPTION_KEY, tlsVersion);
  }

  /**
   * Configures the MQTT client to use the CA certificate located at the specified file path.
   *
   * @param caFilePath CA file path
   * @throws EWException if unable to apply CA file path
   */
  public void setCAFilePath(String caFilePath) throws EWException {
    setOption(MqttConstants.MQTT_CA_FILE_OPTION_KEY, caFilePath);
  }

  /**
   * Configures the MQTT client to use the device certificate located at the specified file path.
   *
   * @param certFilePath device certificate file path
   * @throws EWException if unable to apply device certificate file path
   */
  public void setCertFilePath(String certFilePath) throws EWException {
    setOption(MqttConstants.MQTT_CERT_FILE_OPTION_KEY, certFilePath);
  }

  /**
   * Configures the MQTT client to use the device key located at the specified file path.
   *
   * @param keyFilePath device certificate key file path
   * @throws EWException if unable to apply device certificate key file path
   */
  public void setKeyFilePath(String keyFilePath) throws EWException {
    setOption(MqttConstants.MQTT_KEY_FILE_OPTION_KEY, keyFilePath);
  }

  /**
   * Configures the username of the MQTT client to the specified username.
   *
   * @param username username
   * @throws EWException if unable to configure MQTT client username
   */
  public void setAuthUsername(String username) throws EWException {
    setOption(MqttConstants.MQTT_USERNAME_OPTION_KEY, username);
  }

  /**
   * Configures the password of the MQTT client to the specified password.
   *
   * @param password password
   * @throws EWException if unable to configure MQTT client password
   */
  public void setAuthPassword(String password) throws EWException {
    setOption(MqttConstants.MQTT_PASSWORD_OPTION_KEY, password);
  }

  /**
   * Configures the certificate file paths of the MQTT client to the specified CA certificate,
   * device certificate, and device key file paths.
   *
   * @param caFilePath CA certificate file path
   * @param certFilePath device certificate file path
   * @param keyFilePath device key file path
   * @throws EWException if unable to apply file paths
   */
  public void setCertPaths(String caFilePath, String certFilePath, String keyFilePath)
      throws EWException {
    setCAFilePath(caFilePath);
    setCertFilePath(certFilePath);
    setKeyFilePath(keyFilePath);
  }

  /**
   * Configures the username and password of the MQTT client to the specified username and password.
   *
   * @param username username
   * @param password password
   * @throws EWException if unable to apply username and password
   */
  public void setUsernamePassword(String username, String password) throws EWException {
    setAuthUsername(username);
    setAuthPassword(password);
  }

  /**
   * Method to accompany the existing {@link com.ewon.ewonitf.MqttClient#subscribe(String, int)}
   * method to allow for easy subscription to multiple MQTT topics in a list.
   *
   * @param topics MQTT topics to subscribe to
   * @param qosLevel QOS level for subscription
   * @throws EWException for Flexy related exceptions
   */
  public void subscribeMultiple(String[] topics, int qosLevel) throws EWException {
    // Subscribe to each topic in the specified list of topics
    for (int x = 0; x < topics.length; x++) {
      subscribe(topics[x], qosLevel);
    }
  }

  /**
   * Abstract method to be implemented by the inheriting application for managing errors
   * (exceptions) that occur in the MQTT manager.
   *
   * @param throwable exception to be handled
   */
  public abstract void onError(Throwable throwable);

  /**
   * Abstract method to be implemented by the inheriting application for managing received MQTT
   * messages.
   *
   * @param mqttMessage received MQTT message
   */
  public abstract void onMessage(MqttMessage mqttMessage);

  /**
   * Abstract method to be implemented by the inheriting application for processing or performing
   * tasks on the looping MQTT thread.
   *
   * @see #mqttThread
   */
  public abstract void runOnMqttLoop();

  /**
   * Abstract method to be implemented by the inheriting application for managing MQTT status
   * changes.
   *
   * @param status MQTT status code
   */
  public abstract void onStatus(int status);

  /**
   * Abstract method to be implemented by the inheriting application for processing a successful
   * MQTT connection.
   */
  public abstract void onConnect();

  /**
   * Method inherited from super class ({@link com.ewon.ewonitf.MqttClient}) for handling MQTT
   * events. This method detects the event type and calls the appropriate method to handle it.
   *
   * @param event MQTT event code
   */
  public void callMqttEvent(int event) {
    // Handle MQTT status changes
    if (event == MQTT_STATUS_EVENT) {
      try {
        int status = getStatus();
        onStatus(status);
        if (status == MqttStatusCode.CONNECTED) {
          onConnect();
        }
      } catch (EWException e) {
        // Create human-readable exception explanation and call onError().
        String exceptionMsg = "Unable to read the status code from an MQTT status change event!";
        MqttException mqttException = new MqttException(exceptionMsg, e);
        onError(mqttException);
      }
    }
    // Handle MQTT messages
    else if (event == MQTT_MESSAGE_EVENT) {
      try {
        onMessage(readMessage());
      } catch (EWException e) {
        // Create human-readable exception explanation and call onError().
        String exceptionMsg = "Unable to retrieve message from an MQTT message event!";
        MqttException mqttException = new MqttException(exceptionMsg, e);
        onError(mqttException);
      }
    }
    // Handle unknown event codes
    else {
      // Create human-readable exception explanation and call onError().
      String exceptionMsg = "Unable to identify MQTT event type!";
      MqttException mqttException = new MqttException(exceptionMsg);
      onError(mqttException);
    }
  }
}
