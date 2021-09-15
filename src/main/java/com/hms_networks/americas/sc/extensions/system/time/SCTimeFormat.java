package com.hms_networks.americas.sc.extensions.system.time;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Class containing date/time formats and helper methods to convert timestamps to a user-friendly
 * date representation.
 *
 * @since 1.1.1
 * @author HMS Networks, MU Americas Solution Center
 */
public class SCTimeFormat {

  /**
   * Constant {@link SimpleDateFormat} representing the ISO 8601 international date/time standard as
   * described at https://en.wikipedia.org/wiki/ISO_8601 with US locale symbols.
   */
  public static final SimpleDateFormat ISO_8601 =
      new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
}
