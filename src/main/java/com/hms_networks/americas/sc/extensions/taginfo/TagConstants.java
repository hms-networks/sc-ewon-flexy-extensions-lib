package com.hms_networks.americas.sc.extensions.taginfo;

/**
 * Class for storing constants used in the taginfo package.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.0.0
 */
public class TagConstants {

  /** Integer value used as a placeholder for uninitialized values. */
  public static final int UNINIT_INT_VAL = -1;

  /** New line character used in EBD files/calls. */
  public static final int TAG_EBD_NEW_LINE = '\n';

  /** Carriage return character used in EBD files/calls. */
  public static final int TAG_EBD_CARRIAGE_RETURN = '\r';

  /** End of stream marker used in EBD files/calls. */
  public static final int TAG_EBD_END_OF_STREAM = -1;

  /** The threshold for the number of tag ID gaps before showing a warning message. */
  public static final int TAG_ID_GAPS_WARNING_THRESHOLD = 500;
}
