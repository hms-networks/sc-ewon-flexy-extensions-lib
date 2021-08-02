package com.hms_networks.americas.sc.extensions.historicaldata;

/**
 * Class to throw custom exception in the case a single time tracking file is corrupted while the
 * backup is still usable.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.0.0
 */
public class CorruptedTimeTrackerException extends IllegalArgumentException {

  /**
   * Constructor for the CorruptedTimeTrackerException.
   *
   * @param errorMessage the message to associate with this exception
   */
  public CorruptedTimeTrackerException(String errorMessage) {
    super(errorMessage);
  }
}
