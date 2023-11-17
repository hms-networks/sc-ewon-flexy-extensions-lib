package com.hms_networks.americas.sc.extensions.taginfo;

import java.io.IOException;

/**
 * A custom exception class for errors related to buffering of tag information. Applications that
 * wish to differentiate this exception from IOException, should catch TagInfoBufferException before
 * catching IOException.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.0.0
 */
public class TagInfoBufferException extends IOException {

  /**
   * Constructor providing for a human-readable explanation as parameter
   *
   * @param explanation human-readable explanation
   */
  public TagInfoBufferException(String explanation) {
    super(explanation);
  }
}
