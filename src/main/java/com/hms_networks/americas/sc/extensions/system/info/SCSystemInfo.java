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

  /** Key for accessing the ewon serial number in a system control block. */
  private static final String SCB_EWON_SERIAL_NUM_KEY = "SERNUM";

  /** Default major firmware version requirement */
  private static final int DEFAULT_MIN_MAJOR_FW_VER = 14;

  /** Default minor firmware version requirement */
  private static final int DEFAULT_MIN_MINOR_FW_VER = 6;

  /** Constant for non set firmware version identifier */
  private static final int FW_VER_NON_INIT = -1;

  /** Value for current Ewon firmware major version */
  private static int currFwVerMajor = FW_VER_NON_INIT;

  /** Value for current Ewon firmware minor version */
  private static int currFwVerMinor = FW_VER_NON_INIT;

  /** Value for current required Ewon firmware major version */
  private static int currReqFwVerMajor = DEFAULT_MIN_MAJOR_FW_VER;

  /** Value for current required Ewon firmware major version */
  private static int currReqFwVerMinor = DEFAULT_MIN_MINOR_FW_VER;

  /**
   * Gets the minor firmware version as an int.
   *
   * @return minor firmware version
   * @throws EWException for Flexy specific Exception
   */
  public static int getFirmwareVerMinor() throws EWException {

    if (currFwVerMinor == FW_VER_NON_INIT) {
      SysControlBlock SCB = new SysControlBlock(SysControlBlock.INF);
      currFwVerMinor = Integer.parseInt(SCB.getItem(SCB_FW_VER_LO_KEY));
    }

    return currFwVerMinor;
  }

  /**
   * Gets the major firmware version as an int.
   *
   * @return major firmware version
   * @throws EWException for Flexy specific Exception
   */
  public static int getFirmwareVerMajor() throws EWException {

    if (currFwVerMajor == FW_VER_NON_INIT) {
      SysControlBlock SCB = new SysControlBlock(SysControlBlock.INF);
      currFwVerMajor = Integer.parseInt(SCB.getItem(SCB_FW_VER_HI_KEY));
    }

    return currFwVerMajor;
  }

  /**
   * Checks if current firmware version is equal or greater than the default required version.
   *
   * @return is the firmware version equal or greater than the minium version
   * @throws EWException for Flexy specific Exception
   */
  public static boolean checkMinFirmwareVersion() throws EWException {
    boolean isFwVerValid = false;
    int fwVerMajor = getFirmwareVerMajor();
    int fwVerMinor = getFirmwareVerMinor();

    if (fwVerMajor > currReqFwVerMajor) {
      isFwVerValid = true;
    } else if ((fwVerMajor == currReqFwVerMajor) && (fwVerMinor >= currReqFwVerMinor)) {
      isFwVerValid = true;
    }

    return isFwVerValid;
  }

  /**
   * Sets the required minimum firmware version
   *
   * @param requiredFwrVerMajor Major firmware version requirement
   * @param requiredFwrVerMinor Minor firmware version requirement
   */
  public static void setMinFirmwareVersion(int requiredFwrVerMajor, int requiredFwrVerMinor) {
    currReqFwVerMajor = requiredFwrVerMajor;
    currReqFwVerMinor = requiredFwrVerMinor;
  }

  /**
   * Gets the configured name for the Ewon.
   *
   * @return The Ewon's name
   * @throws EWException for Flexy specific Exception
   */
  public static String getEwonName() throws EWException {
    SysControlBlock SCB = new SysControlBlock(SysControlBlock.SYS);
    return SCB.getItem(SCB_EWON_NAME_KEY);
  }

  /**
   * Gets the serial number of the Ewon.
   *
   * @return The Ewon's serial number
   * @throws EWException for Flexy specific Exception
   */
  public static String getEwonSerialNum() throws EWException {
    SysControlBlock SCB = new SysControlBlock(SysControlBlock.INF);
    return SCB.getItem(SCB_EWON_SERIAL_NUM_KEY);
  }

  /**
   * Gets the firmware version string in "MAJOR.MINOR" format.
   *
   * @return Firmware version in "MAJOR.MINOR" format. (Ex: "14.4")
   * @throws EWException for Flexy specific Exception
   */
  public static String getFirmwareString() throws EWException {
    return getFirmwareVerMajor() + "." + getFirmwareVerMinor();
  }

  /**
   * Gets the required firmware version string in "MAJOR.MINOR" format.
   *
   * @return Required firmware version in "MAJOR.MINOR" format. (Ex: "14.4")
   */
  public static String getRequiredFirmwareString() {
    return currReqFwVerMajor + "." + currReqFwVerMinor;
  }

  /**
   * Gets the version of Java running on the Ewon.
   *
   * @return Java version (Ex: "1.4.4")
   * @throws EWException for Flexy specific Exception
   */
  public static String getJavaVersionString() throws EWException {
    SysControlBlock SCB = new SysControlBlock(SysControlBlock.INF);
    return SCB.getItem(SCB_JAVA_VER_KEY);
  }
}
