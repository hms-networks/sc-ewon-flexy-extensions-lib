package com.hms_networks.americas.sc.extensions.config;

import jregex.*;

/**
 * Class for representing a version of a configuration file.
 *
 * @since 1.15.4
 * @author HMS Networks, MU Americas Solution Center
 */
public class ConfigFileVersion implements Comparable {

  /**
   * Version string
   *
   * @since 1.15.4
   */
  private String versionString;

  /**
   * Major version
   *
   * @since 1.15.4
   */
  private int majorVersion;

  /**
   * Minor version
   *
   * @since 1.15.4
   */
  private int minorVersion;

  /**
   * Patch version
   *
   * @since 1.15.4
   */
  private int patchVersion;

  /**
   * Regex for parsing version string
   *
   * @since 1.15.4
   */
  private final String VERSION_REGEX =
      "^(0|[1-9]\\d*)\\.(0|[1-9]\\d*)\\.(0|[1-9]\\d*)(?:-((?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*)(?:\\.(?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*))*))?(?:\\+([0-9a-zA-Z-]+(?:\\.[0-9a-zA-Z-]+)*))?$";

  /**
   * Group number for Major Version in {@link VERSION_REGEX}
   *
   * @since 1.15.4
   */
  private final int MAJOR_VERSION_GROUP = 1;

  /**
   * Group number for Minor Version in {@link VERSION_REGEX}
   *
   * @since 1.15.4
   */
  private final int MINOR_VERSION_GROUP = 2;

  /**
   * Group number for Patch Version in {@link VERSION_REGEX}
   *
   * @since 1.15.4
   */
  private final int PATCH_VERSION_GROUP = 3;

  /**
   * Minimum expected number of groups to find in version string
   *
   * @since 1.15.4
   */
  private final int MIN_EXPECTED_GROUP_COUNT = 3;

  /**
   * Constructor for {@link ConfigFileVersion}
   *
   * @param versionString the version string
   * @throws IllegalArgumentException if the version string is invalid
   * @since 1.15.4
   */
  public ConfigFileVersion(String versionString) throws IllegalArgumentException {
    this.versionString = versionString;

    Pattern pattern = new Pattern(VERSION_REGEX);

    Matcher matcher = pattern.matcher(this.versionString);

    if (!matcher.matches() | matcher.groupCount() < MIN_EXPECTED_GROUP_COUNT) {
      throw new IllegalArgumentException("Invalid version string: \"" + this.versionString + "\"");
    }

    try {
      majorVersion = Integer.parseInt(matcher.group(MAJOR_VERSION_GROUP));
      minorVersion = Integer.parseInt(matcher.group(MINOR_VERSION_GROUP));
      patchVersion = Integer.parseInt(matcher.group(PATCH_VERSION_GROUP));
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Invalid version string: \"" + this.versionString + "\"");
    }
  }

  /**
   * Get the version string
   *
   * @return the version string
   * @since 1.15.4
   */
  public String getVersionString() {
    return versionString;
  }

  /**
   * Get the major version
   *
   * @return the major version
   * @since 1.15.4
   */
  public int getMajorVersion() {
    return majorVersion;
  }

  /**
   * Get the minor version
   *
   * @return the minor version
   * @since 1.15.4
   */
  public int getMinorVersion() {
    return minorVersion;
  }

  /**
   * Get the patch version
   *
   * @return the patch version
   * @since 1.15.4
   */
  public int getPatchVersion() {
    return patchVersion;
  }

  /**
   * Compare this {@link ConfigFileVersion} to another Object
   *
   * @param o the Object to compare to
   * @return a negative integer, zero, or a positive integer as the specified version is greater
   *     than, equal to, or less than this version.
   * @since 1.15.4
   */
  public int compareTo(Object o) {
    final int lessThanResult = -1;

    if (o instanceof ConfigFileVersion) {
      return compareTo((ConfigFileVersion) o);
    } else {
      return lessThanResult;
    }
  }

  /**
   * Compare this {@link ConfigFileVersion} to another {@link ConfigFileVersion}
   *
   * @param compConfigFileVersion the {@link ConfigFileVersion} to compare to
   * @return a negative integer, zero, or a positive integer as the specified version is greater
   *     than, equal to, or less than this version.
   * @since 1.15.4
   */
  public int compareTo(ConfigFileVersion compConfigFileVersion) {
    final int lessThanResult = -1;
    final int greaterThanResult = 1;
    final int equalResult = 0;

    if (majorVersion > compConfigFileVersion.getMajorVersion()) {
      return greaterThanResult;
    } else if (majorVersion < compConfigFileVersion.getMajorVersion()) {
      return lessThanResult;
    } else {
      if (minorVersion > compConfigFileVersion.getMinorVersion()) {
        return greaterThanResult;
      } else if (minorVersion < compConfigFileVersion.getMinorVersion()) {
        return lessThanResult;
      } else {
        if (patchVersion > compConfigFileVersion.getPatchVersion()) {
          return greaterThanResult;
        } else if (patchVersion < compConfigFileVersion.getPatchVersion()) {
          return lessThanResult;
        } else {
          return equalResult;
        }
      }
    }
  }

  /**
   * Get the string representation of this {@link ConfigFileVersion}.
   *
   * @return the string representation of this {@link ConfigFileVersion}
   * @since 1.15.4
   */
  public String toString() {
    return versionString;
  }
}
