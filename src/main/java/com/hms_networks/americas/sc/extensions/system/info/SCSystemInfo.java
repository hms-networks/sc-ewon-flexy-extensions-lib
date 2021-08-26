package com.hms_networks.americas.sc.extensions.system.info;

import com.ewon.ewonitf.EWException;
import com.ewon.ewonitf.SysControlBlock;

/**
 * Utility class for getting system information for the Ewon Flexy.
 *
 * @since 1.1.0
 * @author HMS Networks, MU Americas Solution Center
 */
public class SCSystemInfo {

  /** Key for accessing the ewon name item in a system control block. */
  private static final String SCB_EWON_NAME_KEY = "Identification";

  /** Key for accessing the firmware version low item in a system control block. */
  private static final String SCB_FW_VER_LO_KEY = "FwrVerLo";

  /** Key for accessing the firmware version hight item in a system control block. */
  private static final String SCB_FW_VER_HI_KEY = "FwrVerHi";

  /** Key for accessing the java version item in a system control block. */
  private static final String SCB_JAVA_VER_KEY = "JavaVersion";

  /**
   * Gets the configured name for the Ewon.
   *
   * @return The Ewon's name
   * @throws EWException
   */
  public static String getEwonName() throws EWException {
    SysControlBlock SCB = new SysControlBlock(SysControlBlock.SYS);
    return SCB.getItem(SCB_EWON_NAME_KEY);
  }

  /**
   * Gets the firmware version string in "MAJOR.MINOR" format.
   *
   * @return Firmware version in "MAJOR.MINOR" format. (Ex: "14.4")
   * @throws EWException
   */
  public static String getFirmwareString() throws EWException {
    SysControlBlock SCB = new SysControlBlock(SysControlBlock.INF);
    return SCB.getItem(SCB_FW_VER_HI_KEY) + "." + SCB.getItem(SCB_FW_VER_LO_KEY);
  }

  /**
   * Gets the version of Java running on the Ewon.
   *
   * @return Java version (Ex: "1.4.4")
   * @throws EWException
   */
  public static String getJavaVersionString() throws EWException {
    SysControlBlock SCB = new SysControlBlock(SysControlBlock.INF);
    return SCB.getItem(SCB_JAVA_VER_KEY);
  }
}
