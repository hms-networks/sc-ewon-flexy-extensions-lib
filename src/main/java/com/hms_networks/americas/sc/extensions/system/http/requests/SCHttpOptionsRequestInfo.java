package com.hms_networks.americas.sc.extensions.system.http.requests;

import com.hms_networks.americas.sc.extensions.system.http.SCHttpUtility;

/**
 * Class to hold request information when creating HTTP(s) OPTIONS requests, such as the URL,
 * headers, and body.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.15.2
 */
public class SCHttpOptionsRequestInfo extends SCHttpRequestInfo {

  /**
   * Instantiate a {@link SCHttpOptionsRequestInfo} object with all information needed to make an
   * API request.
   *
   * @param url The url to send to
   * @param headers The headers to send with
   * @param body The JSON body to send with
   * @since 1.15.2
   */
  public SCHttpOptionsRequestInfo(String url, String headers, String body) {
    super(url, headers, body);
  }

  /**
   * Get the method (OPTIONS) of the request object.
   *
   * @return the request method (OPTIONS)
   * @since 1.15.2
   */
  public String getMethod() {
    return SCHttpUtility.HTTPX_OPTIONS_STRING;
  }
}
