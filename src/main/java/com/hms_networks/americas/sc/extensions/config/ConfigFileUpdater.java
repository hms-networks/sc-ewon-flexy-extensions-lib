package com.hms_networks.americas.sc.extensions.config;

import com.hms_networks.americas.sc.extensions.json.JSONException;
import com.hms_networks.americas.sc.extensions.json.JSONObject;
import com.hms_networks.americas.sc.extensions.logging.Logger;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Class for updating a configuration file.
 *
 * @since 1.15.4
 * @author HMS Networks, MU Americas Solution Center
 */
public class ConfigFileUpdater {

  /**
   * Configuration file
   *
   * @since 1.15.4
   */
  private JSONObject config;

  /**
   * List of steps to execute when updating the configuration file
   *
   * @since 1.15.4
   */
  private ArrayList steps;

  /**
   * Application JSON key
   *
   * @since 1.15.4
   */
  private String applicationJsonKey;

  /**
   * Configuration version JSON key
   *
   * @since 1.15.4
   */
  private String configVersionJsonKey;

  /**
   * Default version string to use when no version is specified in the configuration file
   *
   * @since 1.15.4
   */
  static final String DEFAULT_VERSION_NOT_SPECIFIED = "0.0.0";

  /**
   * Constructor for {@link ConfigFileUpdater}.
   *
   * @param config configuration file
   * @param applicationJsonKey application JSON key
   * @param configVersionJsonKey configuration version JSON key
   * @since 1.15.4
   */
  public ConfigFileUpdater(
      JSONObject config, String applicationJsonKey, String configVersionJsonKey) {
    this.config = config;
    steps = new ArrayList();
    this.applicationJsonKey = applicationJsonKey;
    this.configVersionJsonKey = configVersionJsonKey;
  }

  /**
   * Checks if the configuration file needs to be updated.
   *
   * @param version version to check against
   * @return true if the configuration file needs to be updated, false otherwise
   * @since 1.15.5
   */
  public boolean needsUpdate(ConfigFileVersion version) {
    final int versionsEqual = 0;

    return version.compareTo(getConfigFileVersion()) > versionsEqual;
  }

  /**
   * Gets the configuration file.
   *
   * @return a {@link JSONObject} containing the configuration file
   * @since 1.15.4
   */
  public JSONObject getConfig() {
    return config;
  }

  /**
   * Gets the {@link ConfigFileVersion} of the configuration file.
   *
   * @return version of configuration file
   * @since 1.15.4
   */
  private ConfigFileVersion getConfigFileVersion() {
    String versionString;
    try {
      versionString = config.getJSONObject(applicationJsonKey).getString(configVersionJsonKey);
    } catch (JSONException e) {
      versionString = DEFAULT_VERSION_NOT_SPECIFIED;
    }
    return new ConfigFileVersion(versionString);
  }

  /**
   * Sets the {@link ConfigFileVersion} of the configuration file.
   *
   * @param version version to set
   * @return true if successful, false otherwise
   * @since 1.15.4
   */
  private boolean setConfigFileVersion(ConfigFileVersion version) {
    boolean success = true;

    try {
      config
          .getJSONObject(applicationJsonKey)
          .put(configVersionJsonKey, version.getVersionString());
    } catch (JSONException e) {
      Logger.LOG_CRITICAL(
          "Unable to set configuration file version to version: " + version.toString() + "!", e);
      success = false;
    }

    return success;
  }

  /**
   * Adds a step to the list of steps to execute when updating the configuration file.
   *
   * @param step {@link ConfigFileUpdateStep} to add
   * @since 1.15.4
   */
  public void addStep(ConfigFileUpdateStep step) {
    steps.add(step);
  }

  /**
   * Updates the configuration file by executing the steps in version order.
   *
   * @return true if all steps were executed successfully, false otherwise
   * @since 1.15.4
   */
  public boolean updateConfig() {
    boolean success = true;

    // Sort the steps by version so that they are applied in order
    try {
      Collections.sort(steps);
    } catch (Exception e) {
      Logger.LOG_CRITICAL(
          "Cannot sort configuration update steps! Aborting config file update.", e);
      success = false;
      return success;
    }

    // Execute each step in order
    for (int i = 0; i < steps.size(); i++) {
      ConfigFileUpdateStep step = (ConfigFileUpdateStep) steps.get(i);

      if (success && step.isCompatibleWithVersion(getConfigFileVersion())) {
        success = success && step.updateConfig(config);
        if (success) {
          success = setConfigFileVersion(step.getStepVersion());
        }
      } else {
        success = false;
        break;
      }
    }

    return success;
  }
}
