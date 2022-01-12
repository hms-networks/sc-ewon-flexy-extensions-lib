package com.hms_networks.americas.sc.extensions.system.time;

import com.ewon.ewonitf.SysControlBlock;

/**
 * Utility class which provides methods for accessing and changing the configuration of the Flexy's
 * local time.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.0.0
 */
public class SCTimeUtils {

  /** Key for accessing the time zone name item in the Flexy's system control block. */
  private static final String SCB_TIME_ZONE_KEY = "Timezone";

  /**
   * Gets the name of the Flexy's local time zone.
   *
   * @return local time zone name
   * @throws Exception if unable to get local time zone name
   */
  public static String getTimeZoneName() throws Exception {
    // Create system control block
    SysControlBlock sysControlBlock = new SysControlBlock(SysControlBlock.SYS);

    // Get time zone name from system control block
    return sysControlBlock.getItem(SCB_TIME_ZONE_KEY);
  }
}
