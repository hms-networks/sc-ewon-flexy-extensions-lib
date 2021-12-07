package com.hms_networks.americas.sc.extensions.system.http;

import com.ewon.ewonitf.EWException;
import com.ewon.ewonitf.ScheduledActionManager;
import com.ewon.ewonitf.SysControlBlock;
import com.hms_networks.americas.sc.extensions.fileutils.FileAccessManager;
import java.io.File;
import java.io.IOException;

/**
 * Utility class for performing and managing HTTP connections on the Ewon Flexy system.
 *
 * @since 1.3.0
 * @author HMS Networks, MU Americas Solution Center
 */
public class SCHttpUtility {

  /** Ewon RequestHTTPX string to indicate a POST request. */
  public static final String HTTPX_POST_STRING = "POST";

  /** Ewon RequestHTTPX string to indicate a GET request. */
  public static final String HTTPX_GET_STRING = "GET";

  /** Ewon RequestHTTPX response code for no error. */
  public static final int HTTPX_CODE_NO_ERROR = 0;

  /** Ewon RequestHTTPX response code for an Ewon error. */
  public static final int HTTPX_CODE_EWON_ERROR = 1;

  /** Ewon RequestHTTPX response code for an authentication error. */
  public static final int HTTPX_CODE_AUTH_ERROR = 2;

  /** Ewon RequestHTTPX response code for a connection error. */
  public static final int HTTPX_CODE_CONNECTION_ERROR = 32601;

  /** Folder used to temporarily store HTTP responses as a file. */
  private static final String TEMP_RESPONSE_FILE_FOLDER = "/usr/http/";

  /** Prefix appended to temporary files used to store HTTP responses. */
  private static final String TEMP_RESPONSE_FILE_NAME_PREFIX = "sc_http_response_";

  /** Counter used to uniquely identify temporary files used to store HTTP responses. */
  private static int tempResponseFileNameCounter = 0;

  /**
   * Sets the http timeouts. Note: This changes the Ewon's global HTTP timeouts and stores these
   * values in NV memory.
   *
   * @param seconds the timeout in seconds
   * @throws Exception if unable to set timeouts
   */
  private static void setHttpTimeouts(String seconds) throws Exception {
    final String httpSendTimeoutName = "HTTPC_SDTO";
    final String httpReadTimeoutName = "HTTPC_RDTO";

    boolean needsSave = false;

    SysControlBlock SCB = new SysControlBlock(SysControlBlock.SYS);

    if (!SCB.getItem(httpSendTimeoutName).equals(seconds)) {
      SCB.setItem(httpSendTimeoutName, seconds);
      needsSave = true;
    }

    if (!SCB.getItem(httpReadTimeoutName).equals(seconds)) {
      SCB.setItem(httpReadTimeoutName, seconds);
      needsSave = true;
    }

    // Only save the block if the value has changed, this reduces wear on the flash memory.
    if (needsSave) {
      SCB.saveBlock(true);
    }
  }

  /**
   * Performs an HTTP request to the specified URL using the specified method, request header and
   * body.
   *
   * @param url URL to make request
   * @param header request header
   * @param body request body
   * @param method HTTP method to use (POST or GET)
   * @throws EWException if unable to make request
   * @throws IOException if unable to read response file
   * @throws SCHttpEwonException if an Ewon error occurs during the request
   * @throws SCHttpAuthException if an authentication error occurs during the request
   * @throws SCHttpConnectionException if a connection error occurs during the request
   * @throws SCHttpUnknownException if an unknown error occurs during the request
   */
  private static String httpRequest(String url, String header, String body, String method)
      throws EWException, IOException, SCHttpEwonException, SCHttpAuthException,
          SCHttpConnectionException, SCHttpUnknownException {
    // Reset counter if it reaches max
    if (tempResponseFileNameCounter >= Integer.MAX_VALUE) {
      tempResponseFileNameCounter = 0;
    }

    // Create file for storing response
    final File responseFile =
        new File(
            TEMP_RESPONSE_FILE_FOLDER
                + TEMP_RESPONSE_FILE_NAME_PREFIX
                + tempResponseFileNameCounter++
                + "."
                + method);
    responseFile.getParentFile().mkdirs();
    responseFile.delete();

    // Perform POST request to specified URL
    int httpStatus =
        ScheduledActionManager.RequestHttpX(
            url, method, header, body, "", responseFile.getAbsolutePath());

    // Read response contents and return
    String responseFileString = "";
    if (httpStatus == HTTPX_CODE_NO_ERROR) {
      responseFileString = FileAccessManager.readFileToString(responseFile.getAbsolutePath());
    } else if (httpStatus == HTTPX_CODE_EWON_ERROR) {
      boolean responseFileCleaned = responseFile.delete();
      throw new SCHttpEwonException(
          "An Ewon error was encountered while performing an HTTP "
              + method
              + " request to "
              + url
              + "! Data loss may result. (Temporary Response File Cleaned: "
              + responseFileCleaned
              + ")");
    } else if (httpStatus == HTTPX_CODE_AUTH_ERROR) {
      boolean responseFileCleaned = responseFile.delete();
      throw new SCHttpAuthException(
          "An authentication error was encountered while performing an HTTP "
              + method
              + " request to "
              + url
              + "! Data loss may result. (Temporary Response File Cleaned: "
              + responseFileCleaned
              + ")");
    } else if (httpStatus == HTTPX_CODE_CONNECTION_ERROR) {
      boolean responseFileCleaned = responseFile.delete();
      throw new SCHttpConnectionException(
          "A connection error was encountered while performing an HTTP "
              + method
              + " request to "
              + url
              + "! Data loss may result. (Temporary Response File Cleaned: "
              + responseFileCleaned
              + ")");
    } else {
      boolean responseFileCleaned = responseFile.delete();
      throw new SCHttpUnknownException(
          "An unknown error ("
              + httpStatus
              + ") was encountered while performing an HTTP "
              + method
              + " request to "
              + url
              + "! Data loss may result. (Temporary Response File Cleaned: "
              + responseFileCleaned
              + ")");
    }

    responseFile.delete();
    return responseFileString;
  }

  /**
   * Performs an HTTP POST request to the specified URL using the specified request header and body.
   *
   * @param url URL to make request
   * @param header request header
   * @param body request body
   * @throws EWException if unable to make request
   * @throws IOException if unable to read response file
   * @throws SCHttpEwonException if an Ewon error occurs during the request
   * @throws SCHttpAuthException if an authentication error occurs during the request
   * @throws SCHttpConnectionException if a connection error occurs during the request
   * @throws SCHttpUnknownException if an unknown error occurs during the request
   */
  public static String httpPost(String url, String header, String body)
      throws EWException, IOException, SCHttpEwonException, SCHttpAuthException,
          SCHttpConnectionException, SCHttpUnknownException {
    return httpRequest(url, header, body, HTTPX_POST_STRING);
  }

  /**
   * Performs an HTTP GET request to the specified URL using the specified request header and body.
   *
   * @param url URL to make request
   * @param header request header
   * @param body request body
   * @throws EWException if unable to make request
   * @throws IOException if unable to read response file
   * @throws SCHttpEwonException if an Ewon error occurs during the request
   * @throws SCHttpAuthException if an authentication error occurs during the request
   * @throws SCHttpConnectionException if a connection error occurs during the request
   * @throws SCHttpUnknownException if an unknown error occurs during the request
   */
  public static String httpGet(String url, String header, String body)
      throws EWException, IOException, SCHttpEwonException, SCHttpAuthException,
          SCHttpConnectionException, SCHttpUnknownException {
    return httpRequest(url, header, body, HTTPX_GET_STRING);
  }
}
