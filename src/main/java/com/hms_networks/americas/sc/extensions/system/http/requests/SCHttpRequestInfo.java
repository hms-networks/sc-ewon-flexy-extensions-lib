package com.hms_networks.americas.sc.extensions.system.http.requests;

import com.ewon.ewonitf.EWException;
import com.hms_networks.americas.sc.extensions.system.http.SCHttpAuthException;
import com.hms_networks.americas.sc.extensions.system.http.SCHttpConnectionException;
import com.hms_networks.americas.sc.extensions.system.http.SCHttpEwonException;
import com.hms_networks.americas.sc.extensions.system.http.SCHttpUnknownException;
import com.hms_networks.americas.sc.extensions.system.http.SCHttpUtility;
import java.io.IOException;

/**
 * Abstract class to hold request information when creating HTTP(s) requests, such as the URL,
 * headers, and body. Concrete classes should implement the {@link #getMethod()} method to return
 * the method (GET, POST, PUT, etc.) of the request.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.15.2
 */
public abstract class SCHttpRequestInfo {

  /** API request url */
  private String url;

  /** API request headers */
  private String headers;

  /** API request body */
  private String body;

  /**
   * Integer to track the number of times a request has failed.
   *
   * @since 1.15.2
   */
  private int failRequestCounter = 0;

  /**
   * Boolean to track if a request has been completed successfully.
   *
   * @since 1.15.2
   */
  private boolean isCompleted = false;

  /**
   * Instantiate a {@link SCHttpRequestInfo} object with all information needed to make an API
   * request.
   *
   * @param url The url to send to
   * @param headers The headers to send with
   * @param body The JSON body to send with
   * @since 1.15.2
   */
  public SCHttpRequestInfo(String url, String headers, String body) {
    this.url = url;
    this.headers = headers;
    this.body = body;
  }

  /**
   * Get the method (GET, POST, PUT, etc.) of the request object.
   *
   * @return the request method
   * @since 1.15.2
   */
  public abstract String getMethod();

  /**
   * Get the URL of the request object.
   *
   * @return the request URL
   * @since 1.15.2
   */
  public String getUrl() {
    return url;
  }

  /**
   * Set the URL of the request object.
   *
   * @since 1.15.2
   */
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * Get the headers of the request object.
   *
   * @return the request headers
   * @since 1.15.2
   */
  public String getHeaders() {
    return headers;
  }

  /**
   * Set the headers of the request object.
   *
   * @since 1.15.2
   */
  public void setHeaders(String headers) {
    this.headers = headers;
  }

  /**
   * Get the body of the request object.
   *
   * @return the request body
   * @since 1.15.2
   */
  public String getBody() {
    return body;
  }

  /**
   * Set the body of the request object.
   *
   * @since 1.15.2
   */
  public void setBody(String body) {
    this.body = body;
  }

  /**
   * Gets the number of times a request has failed.
   *
   * @return the number of times a request has failed
   * @since 1.15.2
   */
  public int getFailRequestCounter() {
    return failRequestCounter;
  }

  /**
   * Increment the number of times a request has failed.
   *
   * @since 1.15.2
   */
  public void incrementFailRequestCounter() {
    failRequestCounter++;
  }

  /**
   * Gets the boolean value of whether a request has been completed successfully.
   *
   * @return the boolean value of whether a request has been completed successfully
   * @since 1.15.2
   */
  public boolean getIsCompleted() {
    return isCompleted;
  }

  /**
   * Sets the boolean value of whether a request has been completed successfully.
   *
   * @since 1.15.2
   */
  public void setIsCompleted(boolean isCompleted) {
    this.isCompleted = isCompleted;
  }

  /**
   * Performs an HTTP(s) request (using the configured method) to the set URL using the set request
   * header and body.
   *
   * @throws EWException if unable to make request
   * @throws IOException if unable to read response file
   * @throws SCHttpEwonException if an Ewon error occurs during the request
   * @throws SCHttpAuthException if an authentication error occurs during the request
   * @throws SCHttpConnectionException if a connection error occurs during the request
   * @throws SCHttpUnknownException if an unknown error occurs during the request
   * @see SCHttpUtility#httpRequest(String, String, String, String, String)
   * @return response string
   * @since 1.15.2
   */
  public String doRequest()
      throws EWException,
          IOException,
          SCHttpEwonException,
          SCHttpAuthException,
          SCHttpConnectionException,
          SCHttpUnknownException {
    return SCHttpUtility.httpRequest(getUrl(), getHeaders(), getBody(), getMethod(), null);
  }

  /**
   * Performs an HTTP(s) request (using the configured method) to the set URL using the set request
   * header and body, then outputs the response to the specified file. If the specified file already
   * exists, it will be overwritten.
   *
   * @param outputFile file to output response to
   * @throws EWException if unable to make request
   * @throws IOException if unable to read response file
   * @throws SCHttpEwonException if an Ewon error occurs during the request
   * @throws SCHttpAuthException if an authentication error occurs during the request
   * @throws SCHttpConnectionException if a connection error occurs during the request
   * @throws SCHttpUnknownException if an unknown error occurs during the request
   * @see SCHttpUtility#httpRequest(String, String, String, String, String)
   * @return response string
   * @since 1.12.0
   */
  public String doRequest(String outputFile)
      throws EWException,
          IOException,
          SCHttpEwonException,
          SCHttpAuthException,
          SCHttpConnectionException,
          SCHttpUnknownException {
    return SCHttpUtility.httpRequest(getUrl(), getHeaders(), getBody(), getMethod(), outputFile);
  }
}
