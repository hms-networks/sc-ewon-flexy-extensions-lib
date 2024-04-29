package com.hms_networks.americas.sc.extensions.localization;

import java.util.ResourceBundle;

/**
 * The {@link LocalizationManager} class manages the localization of strings for the extension
 * library.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.12.0
 * @version 1.0.0
 */
public class LocalizationManager {

  /// region: Date and Time Localization

  /**
   * The path to the localization resource bundle for date/time strings.
   *
   * @since 1.0.0
   */
  private static final String DATE_TIME_RESOURCE_BUNDLE_PATH =
      "com.hms_networks.americas.sc.extensions.localization.DateTimeStrings";

  /**
   * Gets the localized date/time {@link String} for the specified key.
   *
   * @param key the key to get the localized date/time {@link String} for
   * @return the localized date/time {@link String} for the specified key
   * @since 1.0.0
   */
  public static String getDateTimeString(DateTimeKey key) {
    return getResourceBundleString(DATE_TIME_RESOURCE_BUNDLE_PATH, key);
  }

  /**
   * Class for storing easy-to-use date/time string keys in an enum-like manner. This class is
   * intended to be used in conjunction with the {@link #getDateTimeString(DateTimeKey)} method.
   *
   * @author HMS Networks, MU Americas Solution Center
   * @since 1.0.0
   * @version 1.0.0
   */
  public static final class DateTimeKey extends Key {

    /**
     * The {@link DateTimeKey} for 'Years' and corresponding translations.
     *
     * @since 1.0.0
     */
    public static final DateTimeKey YEARS = new DateTimeKey("years");

    /**
     * The {@link DateTimeKey} for 'Months' and corresponding translations.
     *
     * @since 1.0.0
     */
    public static final DateTimeKey MONTHS = new DateTimeKey("months");

    /**
     * The {@link DateTimeKey} for 'Weeks' and corresponding translations.
     *
     * @since 1.0.0
     */
    public static final DateTimeKey WEEKS = new DateTimeKey("weeks");

    /**
     * The {@link DateTimeKey} for 'Days' and corresponding translations.
     *
     * @since 1.0.0
     */
    public static final DateTimeKey DAYS = new DateTimeKey("days");

    /**
     * The {@link DateTimeKey} for 'Hours' and corresponding translations.
     *
     * @since 1.0.0
     */
    public static final DateTimeKey HOURS = new DateTimeKey("hours");

    /**
     * The {@link DateTimeKey} for 'Minutes' and corresponding translations.
     *
     * @since 1.0.0
     */
    public static final DateTimeKey MINUTES = new DateTimeKey("minutes");

    /**
     * The {@link DateTimeKey} for 'Seconds' and corresponding translations.
     *
     * @since 1.0.0
     */
    public static final DateTimeKey SECONDS = new DateTimeKey("seconds");

    /**
     * The {@link DateTimeKey} for 'Milliseconds' and corresponding translations.
     *
     * @since 1.0.0
     */
    public static final DateTimeKey MILLISECONDS = new DateTimeKey("milliseconds");

    /**
     * Creates a new {@link DateTimeKey} instance with the specified key.
     *
     * @param key the key for the resource bundle string.
     * @since 1.0.0
     */
    private DateTimeKey(String key) {
      super(key);
    }
  }

  /// endregion: Date and Time Localization

  /// region: Common and Utility Methods/Classes

  /**
   * Gets the {@link String} for the specified key from the specified resource bundle.
   *
   * @param resourceBundlePath the path to the resource bundle
   * @param key the key to get the {@link String} for
   * @return the {@link String} for the specified key from the specified resource bundle
   * @since 1.0.0
   */
  public static String getResourceBundleString(String resourceBundlePath, Key key) {
    return ResourceBundle.getBundle(resourceBundlePath).getString(key.key);
  }

  /**
   * An internal utility class for simplifying the parameterization of {@link
   * LocalizationManager#getResourceBundleString(String,Key)} and associated methods.
   *
   * @author HMS Networks, MU Americas Solution Center
   * @since 1.0.0
   * @version 1.0.0
   */
  public static class Key {

    /**
     * The key for the resource bundle string.
     *
     * @since 1.0.0
     */
    private final String key;

    /**
     * Creates a new {@link Key} instance with the specified key.
     *
     * @param key the key for the resource bundle string.
     * @since 1.0.0
     */
    private Key(String key) {
      this.key = key;
    }
  }

  /// endregion: Common and Utility Methods
}
