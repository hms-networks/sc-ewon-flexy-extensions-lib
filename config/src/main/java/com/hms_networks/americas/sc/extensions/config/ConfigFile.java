package com.hms_networks.americas.sc.extensions.config;

import com.hms_networks.americas.sc.extensions.config.exceptions.ConfigFileParseException;
import com.hms_networks.americas.sc.extensions.config.exceptions.ConfigFileReadException;
import com.hms_networks.americas.sc.extensions.config.exceptions.ConfigFileWriteException;
import com.hms_networks.americas.sc.extensions.json.JSONException;
import com.hms_networks.americas.sc.extensions.json.JSONObject;
import java.io.File;
import java.io.IOException;

/**
 * Abstract class for simplifying and managing the creation and storage of configuration files in
 * various applications.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.0.0
 */
public abstract class ConfigFile {
  /** Configuration object */
  public JSONObject configurationObject;

  /**
   * Gets the file path for reading and saving the configuration to disk.
   *
   * @return configuration file path
   */
  public abstract String getConfigFilePath();

  /**
   * Gets the indent factor used when saving the configuration to file.
   *
   * @return JSON file indent factor
   */
  public abstract int getJSONIndentFactor();

  /**
   * Creates a configuration JSON object containing fields and their default values.
   *
   * @return configuration object with defaults
   * @throws JSONException for errors parsing JSON
   */
  public abstract JSONObject getDefaultConfigurationObject() throws JSONException;

  /**
   * Reads the configuration from the file at the file path specified in {@link
   * #getConfigFilePath()}.
   *
   * @throws ConfigFileReadException if unable to read file
   * @throws ConfigFileParseException if unable to parse file contents
   */
  public void read() throws ConfigFileReadException, ConfigFileParseException {
    // Attempt to read configuration file from disk
    try {
      configurationObject = ConfigFileAccessManager.getJsonObjectFromFile(getConfigFilePath());
    } catch (IOException e) {
      /*
       * If unable to read from file, use default config and rethrow exception as a more
       * human-readable exception with an explanation.
       */
      try {
        configurationObject = getDefaultConfigurationObject();
      } catch (JSONException e2) {
        throw new ConfigFileReadException(
            "Failed to create a default configuration while handling an error with reading "
                + "the configuration from file. Check the specified file path.",
            e2);
      }
      throw new ConfigFileReadException(
          "Unable to read configuration from disk. Check the specified file path.", e);
    } catch (JSONException e) {
      /*
       * If unable to parse file, use default config and rethrow exception as a more
       * human-readable exception with an explanation.
       */
      try {
        configurationObject = getDefaultConfigurationObject();
      } catch (JSONException e2) {
        throw new ConfigFileParseException(
            "Failed to create a default configuration while handling an error with parsing "
                + "the configuration file. Check the file format and contents.",
            e2);
      }
      throw new ConfigFileParseException(
          "Unable to parse configuration from disk. Check the file format and contents.", e);
    }
  }

  /**
   * Writes the configuration to file using the file path specified in {@link #getConfigFilePath()}.
   *
   * @throws ConfigFileWriteException if unable to write to file
   */
  public void save() throws ConfigFileWriteException {
    // Attempt to write configuration file to disk
    try {
      ConfigFileAccessManager.writeJsonObjectToFile(
          getConfigFilePath(), getJSONIndentFactor(), configurationObject);
    } catch (Exception e) {
      // If an exception occurs, rethrow it as a more human-readable exception with an explanation.
      throw new ConfigFileWriteException(
          "Unable to write configuration to disk. Invalid file path or configuration object"
              + " specified.",
          e);
    }
  }

  /**
   * Gets a boolean representing the presence of the configuration file in the file system.
   *
   * @return true if configuration file exists on in the file system
   */
  public boolean fileExists() {
    File configFile = new File(getConfigFilePath());
    return configFile.isFile();
  }

  /**
   * Loads the default configuration contents into memory and writes to the file system.
   *
   * @throws ConfigFileWriteException if unable to write to file
   */
  public void loadAndSaveDefaultConfiguration() throws ConfigFileWriteException {
    try {
      configurationObject = getDefaultConfigurationObject();
    } catch (JSONException e) {
      throw new ConfigFileWriteException(
          "Unable to create default configuration for write to file.", e);
    }
    save();
  }
}
