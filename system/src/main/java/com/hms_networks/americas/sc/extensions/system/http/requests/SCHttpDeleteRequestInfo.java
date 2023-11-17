package com.hms_networks.americas.sc.extensions.system.http.requests;

import com.hms_networks.americas.sc.extensions.system.http.SCHttpUtility;

/**
 * Class to hold request information when creating HTTP(s) DELETE requests, such as the URL,
 * headers, and body.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.15.2
 */
public class SCHttpDeleteRequestInfo extends SCHttpRequestInfo {

  /**
   * Instantiate a {@link SCHttpDeleteRequestInfo} object with all information needed to make an API
   * request.
   *
   * @param url The url to send to
   * @param headers The headers to send with
   * @since 1.15.2
   */
  public SCHttpDeleteRequestInfo(String url, String headers) {
    super(url, headers, "");
  }

  /**
   * Get the method (DELETE) of the request object.
   *
   * @return the request method (DELETE)
   * @since 1.15.2
   */
  public String getMethod() {
    return SCHttpUtility.HTTPX_DELETE_STRING;
  }
}
