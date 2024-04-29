package com.hms_networks.americas.sc.extensions.eventfile;

/**
 * Class for storing constants used in the EventFile reading class.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.6.0
 */
public class EventFileConstants {
  /** Default maximum value limit */
  static final int MAX_DURATION_MINS = 90;

  /** Default minimum value limit */
  static final int MIN_DURATION_MINS = 0;

  /** Default duration check in minuets. */
  static final int DEFAULT_DURATION_MINS = 2;

  /** Base string for EBD Event read. */
  static final String EBD_EVENT_BASE = "$dtEV$ftT$st_m";

  /** Event ID for File Circularized Error. */
  static final int FILE_CIRCULARIZED_EVENT_ID = 23607;
}
