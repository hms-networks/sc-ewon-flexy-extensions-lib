package com.hms_networks.americas.sc.extensions.config;

import com.hms_networks.americas.sc.extensions.json.JSONObject;

/**
 * Abstract class for representing a step in the process of updating a configuration file.
 *
 * @since 1.15.4
 * @author HMS Networks, MU Americas Solution Center
 */
public abstract class ConfigFileUpdateStep implements Comparable {

  /**
   * Version of the configuration file that this step will update the configuration file to.
   *
   * @since 1.15.4
   */
  private ConfigFileVersion stepVersion;

  /**
   * Minimum version of the configuration file that this step is compatible with.
   *
   * @since 1.15.4
   */
  private ConfigFileVersion minCompatibleVersion;

  /**
   * Constructor for {@link ConfigFileUpdateStep}.
   *
   * @param stepVersion version of the configuration file that this step will update the
   *     configuration file to
   * @param minCompatibleVersion minimum version of the configuration file that this step is
   *     compatible with
   * @since 1.15.4
   */
  public ConfigFileUpdateStep(
      ConfigFileVersion stepVersion, ConfigFileVersion minCompatibleVersion) {
    this.stepVersion = stepVersion;
    this.minCompatibleVersion = minCompatibleVersion;
  }

  /**
   * Updates the configuration file to the version specified by this step.
   *
   * @param config configuration file
   * @return true if successful, false otherwise
   * @since 1.15.4
   */
  public abstract boolean updateConfig(JSONObject config);

  /**
   * Checks if the specified {@link ConfigFileVersion} is compatible with this step.
   *
   * @param version version to check
   * @return true if compatible, false otherwise
   * @since 1.15.4
   */
  public boolean isCompatibleWithVersion(ConfigFileVersion version) {
    return version.compareTo(minCompatibleVersion) >= 0 && version.compareTo(stepVersion) < 0;
  }

  /**
   * Gets the {@link ConfigFileVersion} this step will update the configuration to.
   *
   * @return {@link ConfigFileVersion} this step will update the configuration to
   * @since 1.15.4
   */
  public ConfigFileVersion getStepVersion() {
    return stepVersion;
  }

  /**
   * Compare this {@link ConfigFileUpdateStep} version to another {@link ConfigFileUpdateStep}
   * version.
   *
   * @param compConfigFileVersion the {@link ConfigFileUpdateStep} to compare to
   * @return a negative integer, zero, or a positive integer as the specified version is greater
   *     than, equal to, or less than this version.
   * @since 1.15.4
   */
  public int compareTo(Object o) throws IllegalArgumentException {
    if (o instanceof ConfigFileUpdateStep) {
      ConfigFileUpdateStep other = (ConfigFileUpdateStep) o;
      return stepVersion.compareTo(other.stepVersion);
    } else {
      throw new IllegalArgumentException(
          "Cannot compare ConfigFileUpdateStep to " + o.getClass().getName() + ".");
    }
  }
}
