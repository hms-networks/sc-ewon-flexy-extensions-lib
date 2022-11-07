package com.hms_networks.americas.sc.extensions.mqtt;

import com.ewon.ewonitf.EWException;
import com.ewon.ewonitf.MqttMessage;
import com.hms_networks.americas.sc.extensions.retry.AutomaticRetryCode;
import com.hms_networks.americas.sc.extensions.retry.AutomaticRetryCodeExponential;
import com.hms_networks.americas.sc.extensions.retry.AutomaticRetryCodeLinear;
import com.hms_networks.americas.sc.extensions.retry.AutomaticRetryState;
import com.hms_networks.americas.sc.extensions.system.application.SCAppManagement;
import com.hms_networks.americas.sc.extensions.system.time.SCTimeUnit;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * A wrapper class for {@link MqttManager} that provides additional functionality such as error
 * handling and retrying.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.12.0
 * @version 1.0.0
 */
public abstract class ConstrainedMqttManager {

  /**
   * The WAN IP timeout value used to indicate that waiting for the WAN IP is disabled.
   *
   * @since 1.0.0
   */
  private static final int WAIT_FOR_WAN_IP_TIMEOUT_DISABLED = -1;

  /**
   * The WAN IP timeout value used to indicate an indefinite wait for the WAN IP.
   *
   * @since 1.0.0
   */
  private static final int WAIT_FOR_WAN_IP_TIMEOUT_INDEFINITE = 0;

  /**
   * The ID of the MQTT client.
   *
   * @since 1.0.0
   */
  private final String mqttId;

  /**
   * The host (URL) of the MQTT client connection.
   *
   * @since 1.0.0
   */
  private final String mqttHost;

  /**
   * The boolean indicating if UTF-8 encoding should be used for MQTT messages.
   *
   * @since 1.0.0
   */
  private final boolean utf8Enable;

  /**
   * The {@link MqttManager} instance used to manage the MQTT connection.
   *
   * @since 1.0.0
   */
  private MqttManager mqttManager;

  /**
   * The {@link ArrayList} of MQTT topics to subscribe to. This list is populated by the {@link
   * #addSubscription(String)}, {@link #removeSubscription(String)}, and {@link
   * #removeAllSubscriptions()} methods.
   *
   * @since 1.0.0
   */
  private final ArrayList mqttSubscriptions;

  /**
   * The port number of the MQTT client connection.
   *
   * @since 1.0.0
   */
  private String mqttPort;

  /**
   * The CA file path of the MQTT client connection.
   *
   * @since 1.0.0
   */
  private String mqttCaFilePath;

  /**
   * The TLS version of the MQTT client connection.
   *
   * @since 1.0.0
   */
  private String mqttTlsVersion;

  /**
   * The authentication username of the MQTT client connection.
   *
   * @since 1.0.0
   */
  private String mqttUsername;

  /**
   * The authentication password of the MQTT client connection.
   *
   * @since 1.0.0
   */
  private String mqttPassword;

  /**
   * The QoS (quality of service) level of the MQTT client connection.
   *
   * @since 1.0.0
   */
  private int mqttQos;

  /**
   * The time interval (in ms) for sleeping between intervals of the MQTT thread.
   *
   * @since 1.0.0
   */
  private long mqttThreadSleepIntervalMs;

  /**
   * The boolean indicating if the MQTT client has subscribed to the topics in the {@link
   * #mqttSubscriptions} list.
   *
   * @since 1.0.0
   */
  private boolean mqttSubscribed;

  /**
   * Constructor for a new {@link ConstrainedMqttManager} instance which does not wait for a WAN IP
   * address to be available.
   *
   * @param mqttId the ID of the MQTT client
   * @param mqttHost the host (URL) of the MQTT client connection
   * @param utf8Enable the boolean indicating if UTF-8 encoding should be used for MQTT messages
   * @param mqttPort the port number of the MQTT client connection
   * @param mqttCaFilePath the CA file path of the MQTT client connection
   * @param mqttTlsVersion the TLS version of the MQTT client connection
   * @param mqttUsername the authentication username of the MQTT client connection
   * @param mqttPassword the authentication password of the MQTT client connection
   * @param mqttQos the QoS (quality of service) level of the MQTT client connection
   * @throws IllegalStateException if a WAN IP address is not available.
   * @throws Exception if unable to start the {@link MqttManager} instance using the provided
   *     parameters.
   * @since 1.0.0
   */
  public ConstrainedMqttManager(
      String mqttId,
      String mqttHost,
      boolean utf8Enable,
      String mqttPort,
      String mqttCaFilePath,
      String mqttTlsVersion,
      String mqttUsername,
      String mqttPassword,
      int mqttQos)
      throws Exception {
    this(
        mqttId,
        mqttHost,
        utf8Enable,
        mqttPort,
        mqttCaFilePath,
        mqttTlsVersion,
        mqttUsername,
        mqttPassword,
        mqttQos,
        WAIT_FOR_WAN_IP_TIMEOUT_DISABLED,
        null);
  }

  /**
   * Constructor for a new {@link ConstrainedMqttManager} instance which waits for a WAN IP address
   * to be available if the specified {@code waitForWanIp} boolean is {@code true}.
   *
   * @param mqttId the ID of the MQTT client
   * @param mqttHost the host (URL) of the MQTT client connection
   * @param utf8Enable the boolean indicating if UTF-8 encoding should be used for MQTT messages
   * @param mqttPort the port number of the MQTT client connection
   * @param mqttCaFilePath the CA file path of the MQTT client connection
   * @param mqttTlsVersion the TLS version of the MQTT client connection
   * @param mqttUsername the authentication username of the MQTT client connection
   * @param mqttPassword the authentication password of the MQTT client connection
   * @param mqttQos the QoS (quality of service) level of the MQTT client connection
   * @param waitForWanIp the boolean indicating if the {@link MqttManager} instance should wait for
   *     a WAN IP address to be available.
   * @throws IllegalStateException if a WAN IP address is not available.
   * @throws Exception if unable to start the {@link MqttManager} instance using the provided
   *     parameters.
   * @since 1.0.0
   */
  public ConstrainedMqttManager(
      String mqttId,
      String mqttHost,
      boolean utf8Enable,
      String mqttPort,
      String mqttCaFilePath,
      String mqttTlsVersion,
      String mqttUsername,
      String mqttPassword,
      int mqttQos,
      boolean waitForWanIp)
      throws Exception {
    this(
        mqttId,
        mqttHost,
        utf8Enable,
        mqttPort,
        mqttCaFilePath,
        mqttTlsVersion,
        mqttUsername,
        mqttPassword,
        mqttQos,
        waitForWanIp ? WAIT_FOR_WAN_IP_TIMEOUT_INDEFINITE : WAIT_FOR_WAN_IP_TIMEOUT_DISABLED,
        null);
  }

  /**
   * Constructor for a new {@link ConstrainedMqttManager} instance which waits for a WAN IP address
   * to be available until the specified {@code wanIpTimeout} is reached. If the {@code
   * wanIpTimeout} (in the specified {@code wanIpTimeoutUnit}s) is reached before a WAN IP address
   * is available, an {@link IllegalStateException} is thrown.
   *
   * @param mqttId the ID of the MQTT client
   * @param mqttHost the host (URL) of the MQTT client connection
   * @param utf8Enable the boolean indicating if UTF-8 encoding should be used for MQTT messages
   * @param mqttPort the port number of the MQTT client connection
   * @param mqttCaFilePath the CA file path of the MQTT client connection
   * @param mqttTlsVersion the TLS version of the MQTT client connection
   * @param mqttUsername the authentication username of the MQTT client connection
   * @param mqttPassword the authentication password of the MQTT client connection
   * @param mqttQos the QoS (quality of service) level of the MQTT client connection
   * @param wanIpTimeout the timeout (in the specified {@code wanIpTimeoutUnit}s) to wait for a WAN
   *     IP address to be available.
   * @param wanIpTimeoutUnit the {@link SCTimeUnit} of the specified {@code wanIpTimeout}.
   * @throws IllegalStateException if the specified {@code wanIpTimeout} (in the specified {@code
   *     wanIpTimeoutUnit}s) is reached before a WAN IP address is available.
   * @throws Exception if unable to start the {@link MqttManager} instance using the provided
   *     parameters.
   * @since 1.0.0
   */
  public ConstrainedMqttManager(
      String mqttId,
      String mqttHost,
      boolean utf8Enable,
      String mqttPort,
      String mqttCaFilePath,
      String mqttTlsVersion,
      String mqttUsername,
      String mqttPassword,
      int mqttQos,
      int wanIpTimeout,
      SCTimeUnit wanIpTimeoutUnit)
      throws Exception {
    this.mqttId = mqttId;
    this.mqttHost = mqttHost;
    this.utf8Enable = utf8Enable;
    this.mqttPort = mqttPort;
    this.mqttCaFilePath = mqttCaFilePath;
    this.mqttTlsVersion = mqttTlsVersion;
    this.mqttUsername = mqttUsername;
    this.mqttPassword = mqttPassword;
    this.mqttQos = mqttQos;
    this.mqttSubscriptions = new ArrayList();
    this.mqttSubscribed = false;

    // Wait for WAN IP (if enabled)
    if (wanIpTimeout == WAIT_FOR_WAN_IP_TIMEOUT_INDEFINITE) {
      SCAppManagement.waitForWanIp();
    } else if (wanIpTimeout != WAIT_FOR_WAN_IP_TIMEOUT_DISABLED && wanIpTimeoutUnit != null) {
      SCAppManagement.waitForWanIp(wanIpTimeout, wanIpTimeoutUnit);
    }

    // Check for WAN IP
    if (!SCAppManagement.deviceHasWanIP()) {
      throw new IllegalStateException("Device does not have WAN IP");
    }
  }

  /**
   * Builds a new, wrapped {@link MqttManager} instance using the current MQTT parameters.
   *
   * @return the new, wrapped {@link MqttManager} instance
   * @throws Exception if unable to create a new, wrapped {@link MqttManager} instance
   * @since 1.0.0
   */
  private MqttManager buildMqttManager() throws Exception {
    // Return new MQTT manager instance which uses the internal methods
    return new MqttManager(mqttId, mqttHost, utf8Enable) {
      /**
       * Manages errors (exceptions) that occur in the MQTT manager.
       *
       * @param throwable exception to be handled
       * @since 1.0.0
       */
      public void onError(Throwable throwable) {
        internalError(throwable);
      }

      /**
       * Manages received MQTT messages.
       *
       * @param mqttMessage received MQTT message
       * @since 1.0.0
       */
      public void onMessage(MqttMessage mqttMessage) {
        internalMessage(mqttMessage);
      }

      /**
       * Method for processing or performing tasks on the looping MQTT thread.
       *
       * @param currentMqttStatus the current MQTT status integer
       * @since 1.0.0
       */
      public void runOnMqttLoop(int currentMqttStatus) {
        internalRunOnMqttLoop(currentMqttStatus);
      }

      /**
       * Manages MQTT status changes.
       *
       * @param status MQTT status code
       * @since 1.0.0
       */
      public void onStatus(int status) {
        internalOnStatus(status);
      }

      /**
       * Processes a successful MQTT connection.
       *
       * @since 1.0.0
       */
      public void onConnect() {
        internalOnConnect();
      }
    };
  }

  /**
   * Starts the MQTT manager using the configured settings. If the startup fails, it will be retried
   * after the delay specified by {@code delayMillisBeforeRetry}, up to the maximum number of
   * retries specified by {@code maxRetries}.
   *
   * @param maxRetries the maximum number of retries to attempt before giving up and throwing an
   *     exception.
   * @param delayMillisBeforeRetry the delay (milliseconds) before the specified retry number.
   * @throws Exception if unable to configure or start the MQTT manager
   * @since 1.0.0
   */
  public void startWithRetry(final int maxRetries, final long delayMillisBeforeRetry)
      throws Exception {
    // Create MQTT start retry-able task
    AutomaticRetryCode mqttStartRetryTask =
        new AutomaticRetryCode() {

          /**
           * Returns the maximum number of retries before critically failing.
           *
           * @return maximum number of retries
           * @since 1.0.0
           */
          protected int getMaxRetries() {
            return maxRetries;
          }

          /**
           * Returns the delay (milliseconds) before the specified retry number.
           *
           * @param retry retry number
           * @return number of milliseconds delay before specified retry number
           * @since 1.0.0
           */
          protected long getDelayMillisBeforeRetry(int retry) {
            return delayMillisBeforeRetry;
          }

          /**
           * The MQTT start code which will automatically be retried based on the state, which must
           * be set using {@link #setState(AutomaticRetryState)}.
           *
           * @since 1.0.0
           */
          protected void codeToRetry() {
            try {
              start();
              setState(AutomaticRetryState.FINISHED);
            } catch (Exception e) {
              stop();
              setState(AutomaticRetryState.ERROR_RETRY);
            }
          }
        };

    // Run MQTT start retry-able task
    mqttStartRetryTask.run();
  }

  /**
   * Starts the MQTT manager using the configured settings. If the startup fails, it will be retried
   * using a linear backoff algorithm with the specified {@code maxRetries}, {@code
   * maxDelayMillisBeforeRetry}, and {@code linearSlopeMillis} parameters.
   *
   * @param maxRetries the maximum number of retries to attempt before giving up and throwing an
   *     exception.
   * @param maxDelayMillisBeforeRetry the maximum delay (in milliseconds) before retrying the
   *     startup.
   * @param linearSlopeMillis the slope of the linear backoff algorithm (milliseconds)
   * @throws Exception if unable to configure or start the MQTT manager
   * @since 1.0.0
   */
  public void startWithLinearRetry(
      final int maxRetries, final long maxDelayMillisBeforeRetry, final long linearSlopeMillis)
      throws Exception {
    // Create MQTT start retry-able task
    AutomaticRetryCodeLinear mqttStartRetryTask =
        new AutomaticRetryCodeLinear() {

          /**
           * Returns the slope of the linear backoff algorithm (milliseconds). The linear slope is
           * the number of milliseconds increase in delay between each retry.
           *
           * @return slope of the linear backoff algorithm (milliseconds)
           * @since 1.0.0
           */
          protected long getLinearSlopeMillis() {
            return linearSlopeMillis;
          }

          /**
           * Returns the maximum delay (milliseconds) before retrying.
           *
           * @return maximum number of milliseconds delay before retry
           * @since 1.0.0
           */
          protected long getMaxDelayMillisBeforeRetry() {
            return maxDelayMillisBeforeRetry;
          }

          /**
           * Returns the maximum number of retries before critically failing.
           *
           * @return maximum number of retries
           * @since 1.0.0
           */
          protected int getMaxRetries() {
            return maxRetries;
          }

          /**
           * The MQTT start code which will automatically be retried based on the state, which must
           * be set using {@link #setState(AutomaticRetryState)}.
           *
           * @since 1.0.0
           */
          protected void codeToRetry() {
            try {
              start();
              setState(AutomaticRetryState.FINISHED);
            } catch (Exception e) {
              stop();
              setState(AutomaticRetryState.ERROR_RETRY);
            }
          }
        };

    // Run MQTT start retry-able task
    mqttStartRetryTask.run();
  }

  /**
   * Starts the MQTT manager using the configured settings. If the startup fails, it will be retried
   * using an exponential backoff algorithm with the specified {@code maxRetries} and {@code
   * maxDelayMillisBeforeRetry} parameters.
   *
   * @param maxRetries the maximum number of retries to attempt before giving up and throwing an
   *     exception.
   * @param maxDelayMillisBeforeRetry the maximum delay (in milliseconds) before retrying the
   *     startup.
   * @throws Exception if unable to configure or start the MQTT manager
   * @since 1.0.0
   */
  public void startWithExponentialRetry(final int maxRetries, final long maxDelayMillisBeforeRetry)
      throws Exception {
    // Create MQTT start retry-able task
    AutomaticRetryCodeExponential mqttStartRetryTask =
        new AutomaticRetryCodeExponential() {

          /**
           * Returns the maximum delay (milliseconds) before retrying.
           *
           * @return maximum number of milliseconds delay before retry
           * @since 1.0.0
           */
          protected long getMaxDelayMillisBeforeRetry() {
            return maxDelayMillisBeforeRetry;
          }

          /**
           * Returns the maximum number of retries before critically failing.
           *
           * @return maximum number of retries
           * @since 1.0.0
           */
          protected int getMaxRetries() {
            return maxRetries;
          }

          /**
           * The MQTT start code which will automatically be retried based on the state, which must
           * be set using {@link #setState(AutomaticRetryState)}.
           *
           * @since 1.0.0
           */
          protected void codeToRetry() {
            try {
              start();
              setState(AutomaticRetryState.FINISHED);
            } catch (Exception e) {
              stop();
              setState(AutomaticRetryState.ERROR_RETRY);
            }
          }
        };

    // Run MQTT start retry-able task
    mqttStartRetryTask.run();
  }

  /**
   * Starts the MQTT manager using the configured settings.
   *
   * @throws Exception if unable to configure or start the MQTT manager
   * @since 1.0.0
   */
  public void start() throws Exception {
    mqttManager = buildMqttManager();
    mqttManager.setPort(mqttPort);
    mqttManager.setCAFilePath(mqttCaFilePath);
    mqttManager.setTLSVersion(mqttTlsVersion);
    mqttManager.setAuthUsername(mqttUsername);
    mqttManager.setAuthPassword(mqttPassword);
    mqttManager.setMqttThreadSleepIntervalMs(mqttThreadSleepIntervalMs);
    mqttManager.startMqttThread();
    mqttManager.connect();
  }

  /**
   * Restarts the MQTT manager. If the MQTT manager is not currently running, this method will start
   * it.
   *
   * @throws Exception if unable to configure or start the MQTT manager
   * @since 1.0.0
   */
  public void restart() throws Exception {
    stop();
    start();
  }

  /**
   * Stops the MQTT manager if it is currently running.
   *
   * @since 1.0.0
   */
  public void stop() {
    if (mqttManager != null) {
      mqttManager.disconnect();
      mqttManager.stopMqttThread();
    }
    mqttManager = null;
    mqttSubscribed = false;
  }

  /**
   * Sets the MQTT port to be used for the MQTT connection. If the MQTT manager is currently
   * running, the MQTT connection will be restarted with the new port.
   *
   * @param mqttPort MQTT port to be used
   * @throws Exception if unable to configure MQTT port or restart MQTT connection
   * @since 1.0.0
   */
  public void setPort(String mqttPort) throws Exception {
    this.mqttPort = mqttPort;
    if (mqttManager != null) {
      restart();
    }
  }

  /**
   * Sets the MQTT CA file path to be used for the MQTT connection. If the MQTT manager is currently
   * running, the MQTT connection will be restarted with the new CA file path.
   *
   * @param mqttCaFilePath MQTT CA file path to be used
   * @throws Exception if unable to configure MQTT CA file path or restart MQTT connection
   * @since 1.0.0
   */
  public void setCAFilePath(String mqttCaFilePath) throws Exception {
    this.mqttCaFilePath = mqttCaFilePath;
    if (mqttManager != null) {
      restart();
    }
  }

  /**
   * Sets the MQTT TLS version to be used for the MQTT connection. If the MQTT manager is currently
   * running, the MQTT connection will be restarted with the new TLS version.
   *
   * @param mqttTlsVersion MQTT TLS version to be used
   * @throws Exception if unable to configure MQTT TLS version or restart MQTT connection
   * @since 1.0.0
   */
  public void setTLSVersion(String mqttTlsVersion) throws Exception {
    this.mqttTlsVersion = mqttTlsVersion;
    if (mqttManager != null) {
      restart();
    }
  }

  /**
   * Sets the MQTT username to be used for the MQTT connection. If the MQTT manager is currently
   * running, the MQTT connection will be restarted with the new username.
   *
   * @param mqttUsername MQTT username to be used
   * @throws Exception if unable to configure MQTT username or restart MQTT connection
   * @since 1.0.0
   */
  public void setAuthUsername(String mqttUsername) throws Exception {
    this.mqttUsername = mqttUsername;
    if (mqttManager != null) {
      restart();
    }
  }

  /**
   * Sets the MQTT password to be used for the MQTT connection. If the MQTT manager is currently
   * running, the MQTT connection will be restarted with the new password.
   *
   * @param mqttPassword MQTT password to be used
   * @throws Exception if unable to configure MQTT password or restart MQTT connection
   * @since 1.0.0
   */
  public void setAuthPassword(String mqttPassword) throws Exception {
    this.mqttPassword = mqttPassword;
    if (mqttManager != null) {
      restart();
    }
  }

  /**
   * Sets the MQTT QoS level to be used for the MQTT connection. If the MQTT manager is currently
   * running, the MQTT connection will be restarted with the new QoS level.
   *
   * @param mqttQos MQTT QoS level to be used
   * @throws Exception if unable to configure MQTT QoS level or restart MQTT connection
   * @since 1.0.0
   */
  public void setQos(int mqttQos) throws Exception {
    this.mqttQos = mqttQos;
    if (mqttManager != null) {
      restart();
    }
  }

  /**
   * Gets the MQTT QoS level to be used for the MQTT connection.
   *
   * @return MQTT QoS level to be used
   * @since 1.0.0
   */
  public int getQos() {
    return mqttQos;
  }

  /**
   * Gets the MQTT ID to be used for the MQTT connection.
   *
   * @return MQTT ID to be used
   * @since 1.0.0
   */
  public String getMqttId() {
    return mqttId;
  }

  /**
   * Gets the boolean indicating if the MQTT manager has subscribed to the configured MQTT topics.
   *
   * @return boolean indicating if the MQTT manager has subscribed to the configured MQTT topics
   * @since 1.0.0
   */
  public boolean getMqttSubscribed() {
    return mqttSubscribed;
  }

  /**
   * Sets the time (in ms) for sleeping between intervals of the MQTT thread. If the MQTT manager is
   * currently running, the MQTT connection will be updated with the new sleep interval.
   *
   * @param mqttThreadSleepIntervalMs time (in ms) for sleeping between intervals of the MQTT
   *     thread.
   * @since 1.0.0
   */
  public void setMqttThreadSleepIntervalMs(long mqttThreadSleepIntervalMs) {
    this.mqttThreadSleepIntervalMs = mqttThreadSleepIntervalMs;
    if (mqttManager != null) {
      mqttManager.setMqttThreadSleepIntervalMs(mqttThreadSleepIntervalMs);
    }
  }

  /**
   * Adds the MQTT topic to the list of topics to be subscribed to. If the MQTT manager is currently
   * running, the subscription will be added to the existing MQTT connection.
   *
   * @param topic MQTT topic to be subscribed to
   * @throws Exception if unable to subscribe to MQTT topic
   */
  public void addSubscription(String topic) throws Exception {
    mqttSubscriptions.add(topic);

    // If the MQTT manager is already running, subscribe to the topic.
    if (mqttManager != null && mqttSubscribed) {
      mqttManager.subscribe(topic, mqttQos);
    }
  }

  /**
   * Removes the MQTT topic from the list of topics to be subscribed to. If the MQTT manager is
   * currently running, the MQTT connection will be restarted with the updated list of
   * subscriptions.
   *
   * @param topic MQTT topic to be unsubscribed from
   * @throws Exception if unable to unsubscribe from MQTT topic
   */
  public void removeSubscription(String topic) throws Exception {
    mqttSubscriptions.remove(topic);
    if (mqttManager != null) {
      restart();
    }
  }

  /**
   * Clears the list of MQTT topics to be subscribed to. If the MQTT manager is currently running,
   * the MQTT connection will be restarted with the updated list of subscriptions (none).
   *
   * @throws Exception if unable to unsubscribe from MQTT topics
   */
  public void removeAllSubscriptions() throws Exception {
    mqttSubscriptions.clear();
    if (mqttManager != null) {
      restart();
    }
  }

  /**
   * Publishes the given message to the given topic using the currently configured QoS level. If the
   * MQTT manager is not currently running, an {@link IllegalStateException} will be thrown.
   *
   * @param topic the MQTT topic to publish the message to
   * @param payload the MQTT message payload to publish
   * @param retain the boolean indicating whether the message should be retained by the MQTT broker
   *     for future clients
   * @throws EWException if an Ewon exception occurs. See the Ewon event log for more details.
   * @throws UnsupportedEncodingException if the character encoding is not supported.
   * @throws IllegalStateException if the MQTT manager is not currently running.
   * @see #getQos()
   */
  public void mqttPublish(String topic, String payload, boolean retain)
      throws EWException, UnsupportedEncodingException {
    if (mqttManager == null) {
      throw new IllegalStateException("MQTT Manager is not started!");
    }
    mqttManager.mqttPublish(topic, payload, mqttQos, retain);
  }

  /**
   * Publishes the given message to the given topic. If the MQTT manager is not currently running,
   * an {@link IllegalStateException} will be thrown.
   *
   * @param topic the MQTT topic to publish the message to
   * @param payload the MQTT message payload to publish
   * @param qos the MQTT QoS level to publish the message at
   * @param retain the boolean indicating whether the message should be retained by the MQTT broker
   *     for future clients
   * @throws EWException if an Ewon exception occurs. See the Ewon event log for more details.
   * @throws UnsupportedEncodingException if the character encoding is not supported.
   * @throws IllegalStateException if the MQTT manager is not currently running.
   */
  public void mqttPublish(String topic, String payload, int qos, boolean retain)
      throws EWException, UnsupportedEncodingException {
    if (mqttManager == null) {
      throw new IllegalStateException("MQTT Manager is not started!");
    }
    mqttManager.mqttPublish(topic, payload, qos, retain);
  }

  /**
   * Internal method which is called when an error is encountered by the wrapped MQTT manager. This
   * method will attempt to handle the error, and if it does not need to, or is unable to do so,
   * will pass the error to the {@link #onError(Throwable)} implementation method.
   *
   * @param t the error encountered
   */
  private void internalError(Throwable t) {
    // Call implementation onError method
    onError(t);
  }

  /**
   * Internal method which is called when a message is received by the wrapped MQTT manager. This
   * method will attempt to handle the message, and if it does not need to, or is unable to do so,
   * will pass the message to the {@link #onMessage(MqttMessage)} implementation method.
   *
   * @param mqttMessage received MQTT message
   */
  private void internalMessage(MqttMessage mqttMessage) {
    // Call implementation onMessage method
    onMessage(mqttMessage);
  }

  /**
   * Internal method which is called during each iteration of the MQTT thread. This method will
   * perform any tasks that need to be done on the MQTT thread, and then the {@link
   * #runOnMqttLoop(int)} implementation method will be called.
   *
   * @param currentMqttStatus the current MQTT status integer
   * @since 1.0.0
   */
  private void internalRunOnMqttLoop(int currentMqttStatus) {
    // Call implementation runOnMqttLoop method
    runOnMqttLoop(currentMqttStatus);
  }

  /**
   * Internal method which is called when the MQTT connection status changes. This method will
   * attempt to handle the status change, and if it does not need to, or is unable to do so, will
   * pass the status change to the {@link #onStatus(int)} implementation method.
   *
   * @param status MQTT status code
   * @since 1.0.0
   */
  private void internalOnStatus(int status) {
    // Call implementation onStatus method
    onStatus(status);
  }

  /**
   * Internal method which is called when the MQTT connection is successfully established. This
   * method will attempt to subscribe to the configured MQTT topics, if not already subscribed, and
   * if it is unable to do so, will pass the error to the {@link #onError(Throwable)} implementation
   * method. If the MQTT connection is successfully established and topics successfully subscribed
   * to, the {@link #onConnect()} implementation method will be called.
   *
   * @since 1.0.0
   */
  private void internalOnConnect() {
    // Subscribe to topics if not subscribed
    if (!mqttSubscribed) {
      for (int i = 0; i < mqttSubscriptions.size(); i++) {
        try {
          mqttManager.subscribe(mqttSubscriptions.get(i).toString(), mqttQos);
        } catch (EWException e) {
          onError(
              new RuntimeException("Unable to subscribe to topic: " + mqttSubscriptions.get(i)));
        }
      }
      mqttSubscribed = true;
    }

    // Call implementation onConnect method
    onConnect();
  }

  /**
   * Gets the status of the MQTT connection. If the MQTT manager is not currently running, the
   * status will be {@link MqttStatusCode#UNKNOWN}.
   *
   * @return MQTT status code
   * @throws EWException if an Ewon exception occurs. See the Ewon event log for more details.
   * @since 1.0.0
   */
  public int getStatus() throws EWException {
    int status = MqttStatusCode.UNKNOWN;
    if (mqttManager != null) {
      status = mqttManager.getStatus();
    }
    return status;
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
   * @param currentMqttStatus the current MQTT status integer
   */
  public abstract void runOnMqttLoop(int currentMqttStatus);

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
}
