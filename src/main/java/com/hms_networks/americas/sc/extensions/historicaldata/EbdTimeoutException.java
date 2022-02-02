package com.hms_networks.americas.sc.extensions.historicaldata;

/**
 * Class to throw custom exception in the case of Export Block Descriptor calls timing out.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.6.0
 */
public class EbdTimeoutException extends Exception {

  /**
   * Constructor for the EbdTimeoutException.
   *
   * @param errorMessage the message to associate with this exception
   */
  public EbdTimeoutException(String errorMessage) {
    super(errorMessage);
  }
}
