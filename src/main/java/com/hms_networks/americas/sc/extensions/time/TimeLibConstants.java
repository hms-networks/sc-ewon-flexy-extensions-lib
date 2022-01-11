package com.hms_networks.americas.sc.extensions.time;

/**
 * Class of constants for the HMS MU Americas Solution Center time library for Ewon Flexy devices.
 *
 * @since 1.0.0
 * @author HMS Networks, MU Americas Solution Center
 */
public class TimeLibConstants {

  /** Date format generated by the local time offset HTML file in result files. */
  public static final String TIME_OFFSET_DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";

  /**
   * The export block descriptor value used to get the local time by retrieving the 'Time$' variable
   * using a script expression. Detailed information about export block descriptors can be found at
   * https://developer.ewon.biz/content/export-block-descriptor.
   */
  public static final String TIME_OFFSET_LOCAL_TIME_EBD = "$dtSE$se\"Time$\"$ftT";
}
