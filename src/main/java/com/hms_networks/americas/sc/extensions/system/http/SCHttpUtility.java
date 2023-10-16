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

  /**
   * Ewon RequestHTTPX string to indicate a PUT request.
   *
   * @since 1.15.2
   */
  public static final String HTTPX_PUT_STRING = "PUT";

  /**
   * Ewon RequestHTTPX string to indicate a PATCH request.
   *
   * @since 1.15.2
   */
  public static final String HTTPX_PATCH_STRING = "PATCH";

  /**
   * Ewon RequestHTTPX string to indicate a DELETE request.
   *
   * @since 1.15.2
   */
  public static final String HTTPX_DELETE_STRING = "DELETE";

  /**
   * Ewon RequestHTTPX string to indicate an OPTIONS request.
   *
   * @since 1.15.2
   */
  public static final String HTTPX_OPTIONS_STRING = "OPTIONS";

  /**
   * Ewon RequestHTTPX string to indicate a HEAD request.
   *
   * @since 1.15.2
   */
  public static final String HTTPX_HEAD_STRING = "HEAD";

  /**
   * Ewon RequestHTTPX string to indicate a PURGE request.
   *
   * @since 1.15.2
   */
  public static final String HTTPX_PURGE_STRING = "PURGE";

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

  /** Key for accessing the HTTP certificate path. */
  private static final String SCB_CERT_PATH_KEY = "HttpCertDir";

  /** Counter used to uniquely identify temporary files used to store HTTP responses. */
  private static int tempResponseFileNameCounter = 0;

  /**
   * Sets the http timeouts. Note: This changes the Ewon's global HTTP timeouts and stores these
   * values in NV memory.
   *
   * @param seconds the timeout in seconds
   * @throws Exception if unable to set timeouts
   * @since 1.3.0
   */
  public static void setHttpTimeouts(String seconds) throws Exception {
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
   * body. If the specified {@code outputFile} is null, a temporary file will be used to store the
   * response. If the specified {@code outputFile} is not null, and the file exists, it will be
   * overwritten.
   *
   * @param url URL to make request
   * @param header request header
   * @param body request body
   * @param method HTTP method to use (POST or GET)
   * @param outputFile file to store response in (if null, a temporary file will be used)
   * @throws EWException if unable to make request
   * @throws IOException if unable to read response file
   * @throws SCHttpEwonException if an Ewon error occurs during the request
   * @throws SCHttpAuthException if an authentication error occurs during the request
   * @throws SCHttpConnectionException if a connection error occurs during the request
   * @throws SCHttpUnknownException if an unknown error occurs during the request
   * @return http response string
   * @since 1.3.0
   */
  private static String httpRequest(
      String url, String header, String body, String method, String outputFile)
      throws EWException,
          IOException,
          SCHttpEwonException,
          SCHttpAuthException,
          SCHttpConnectionException,
          SCHttpUnknownException {
    // Reset counter if it reaches max
    if (tempResponseFileNameCounter >= Integer.MAX_VALUE) {
      tempResponseFileNameCounter = 0;
    }

    // Create file for storing response. File is deleted if it already exists.
    final File responseFile;
    if (outputFile != null) {
      responseFile = new File(outputFile);
    } else {
      responseFile =
          new File(
              TEMP_RESPONSE_FILE_FOLDER
                  + TEMP_RESPONSE_FILE_NAME_PREFIX
                  + tempResponseFileNameCounter++
                  + "."
                  + method);
    }
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
      throw new SCHttpEwonException(
          "An Ewon error was encountered while performing an HTTP "
              + method
              + " request to "
              + url
              + "! Data loss may result."
              + (outputFile == null ? "(Temporary File Cleaned: " + responseFile.delete() : ""));
    } else if (httpStatus == HTTPX_CODE_AUTH_ERROR) {
      throw new SCHttpAuthException(
          "An authentication error was encountered while performing an HTTP "
              + method
              + " request to "
              + url
              + "! Data loss may result."
              + (outputFile == null ? "(Temporary File Cleaned: " + responseFile.delete() : ""));
    } else if (httpStatus == HTTPX_CODE_CONNECTION_ERROR) {
      throw new SCHttpConnectionException(
          "A connection error was encountered while performing an HTTP "
              + method
              + " request to "
              + url
              + "! Data loss may result."
              + (outputFile == null ? "(Temporary File Cleaned: " + responseFile.delete() : ""));
    } else {
      throw new SCHttpUnknownException(
          "An unknown error ("
              + httpStatus
              + ") was encountered while performing an HTTP "
              + method
              + " request to "
              + url
              + "! Data loss may result."
              + (outputFile == null ? "(Temporary File Cleaned: " + responseFile.delete() : ""));
    }

    // Delete response file (if temporary)
    if (outputFile == null) {
      responseFile.delete();
    }
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
   * @return response string
   * @since 1.3.0
   */
  public static String httpPost(String url, String header, String body)
      throws EWException,
          IOException,
          SCHttpEwonException,
          SCHttpAuthException,
          SCHttpConnectionException,
          SCHttpUnknownException {
    return httpRequest(url, header, body, HTTPX_POST_STRING, null);
  }

  /**
   * Performs an HTTP POST request to the specified URL using the specified request header and body,
   * then outputs the response to the specified file. If the specified file already exists, it will
   * be overwritten.
   *
   * @param url URL to make request
   * @param header request header
   * @param body request body
   * @param outputFile file to output response to
   * @throws EWException if unable to make request
   * @throws IOException if unable to read response file
   * @throws SCHttpEwonException if an Ewon error occurs during the request
   * @throws SCHttpAuthException if an authentication error occurs during the request
   * @throws SCHttpConnectionException if a connection error occurs during the request
   * @throws SCHttpUnknownException if an unknown error occurs during the request
   * @return response string
   * @since 1.12.0
   */
  public static String httpPost(String url, String header, String body, String outputFile)
      throws EWException,
          IOException,
          SCHttpEwonException,
          SCHttpAuthException,
          SCHttpConnectionException,
          SCHttpUnknownException {
    return httpRequest(url, header, body, HTTPX_POST_STRING, outputFile);
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
   * @return http response string
   * @since 1.3.0
   */
  public static String httpGet(String url, String header, String body)
      throws EWException,
          IOException,
          SCHttpEwonException,
          SCHttpAuthException,
          SCHttpConnectionException,
          SCHttpUnknownException {
    return httpRequest(url, header, body, HTTPX_GET_STRING, null);
  }

  /**
   * Performs an HTTP GET request to the specified URL using the specified request header and body,
   * then outputs the response to the specified file. If the specified file already exists, it will
   * be overwritten.
   *
   * @param url URL to make request
   * @param header request header
   * @param body request body
   * @param outputFile file to output response to
   * @throws EWException if unable to make request
   * @throws IOException if unable to read response file
   * @throws SCHttpEwonException if an Ewon error occurs during the request
   * @throws SCHttpAuthException if an authentication error occurs during the request
   * @throws SCHttpConnectionException if a connection error occurs during the request
   * @throws SCHttpUnknownException if an unknown error occurs during the request
   * @return http response string
   * @since 1.12.0
   */
  public static String httpGet(String url, String header, String body, String outputFile)
      throws EWException,
          IOException,
          SCHttpEwonException,
          SCHttpAuthException,
          SCHttpConnectionException,
          SCHttpUnknownException {
    return httpRequest(url, header, body, HTTPX_GET_STRING, outputFile);
  }

  /**
   * Performs an HTTP PUT request to the specified URL using the specified request header and body.
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
   * @return http response string
   * @since 1.15.2
   */
  public static String httpPut(String url, String header, String body)
      throws EWException,
          IOException,
          SCHttpEwonException,
          SCHttpAuthException,
          SCHttpConnectionException,
          SCHttpUnknownException {
    return httpRequest(url, header, body, HTTPX_PUT_STRING, null);
  }

  /**
   * Performs an HTTP PUT request to the specified URL using the specified request header and body,
   * then outputs the response to the specified file. If the specified file already exists, it will
   * be overwritten.
   *
   * @param url URL to make request
   * @param header request header
   * @param body request body
   * @param outputFile file to output response to
   * @throws EWException if unable to make request
   * @throws IOException if unable to read response file
   * @throws SCHttpEwonException if an Ewon error occurs during the request
   * @throws SCHttpAuthException if an authentication error occurs during the request
   * @throws SCHttpConnectionException if a connection error occurs during the request
   * @throws SCHttpUnknownException if an unknown error occurs during the request
   * @return http response string
   * @since 1.15.2
   */
  public static String httpPut(String url, String header, String body, String outputFile)
      throws EWException,
          IOException,
          SCHttpEwonException,
          SCHttpAuthException,
          SCHttpConnectionException,
          SCHttpUnknownException {
    return httpRequest(url, header, body, HTTPX_PUT_STRING, outputFile);
  }

  /**
   * Performs an HTTP PATCH request to the specified URL using the specified request header and
   * body.
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
   * @return http response string
   * @since 1.15.2
   */
  public static String httpPatch(String url, String header, String body)
      throws EWException,
          IOException,
          SCHttpEwonException,
          SCHttpAuthException,
          SCHttpConnectionException,
          SCHttpUnknownException {
    return httpRequest(url, header, body, HTTPX_PATCH_STRING, null);
  }

  /**
   * Performs an HTTP PATCH request to the specified URL using the specified request header and
   * body, then outputs the response to the specified file. If the specified file already exists, it
   * will be overwritten.
   *
   * @param url URL to make request
   * @param header request header
   * @param body request body
   * @param outputFile file to output response to
   * @throws EWException if unable to make request
   * @throws IOException if unable to read response file
   * @throws SCHttpEwonException if an Ewon error occurs during the request
   * @throws SCHttpAuthException if an authentication error occurs during the request
   * @throws SCHttpConnectionException if a connection error occurs during the request
   * @throws SCHttpUnknownException if an unknown error occurs during the request
   * @return http response string
   * @since 1.15.2
   */
  public static String httpPatch(String url, String header, String body, String outputFile)
      throws EWException,
          IOException,
          SCHttpEwonException,
          SCHttpAuthException,
          SCHttpConnectionException,
          SCHttpUnknownException {
    return httpRequest(url, header, body, HTTPX_PATCH_STRING, outputFile);
  }

  /**
   * Performs an HTTP DELETE request to the specified URL using the specified request header and
   * body.
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
   * @return http response string
   * @since 1.15.2
   */
  public static String httpDelete(String url, String header, String body)
      throws EWException,
          IOException,
          SCHttpEwonException,
          SCHttpAuthException,
          SCHttpConnectionException,
          SCHttpUnknownException {
    return httpRequest(url, header, body, HTTPX_DELETE_STRING, null);
  }

  /**
   * Performs an HTTP DELETE request to the specified URL using the specified request header and
   * body, then outputs the response to the specified file. If the specified file already exists, it
   * will be overwritten.
   *
   * @param url URL to make request
   * @param header request header
   * @param body request body
   * @param outputFile file to output response to
   * @throws EWException if unable to make request
   * @throws IOException if unable to read response file
   * @throws SCHttpEwonException if an Ewon error occurs during the request
   * @throws SCHttpAuthException if an authentication error occurs during the request
   * @throws SCHttpConnectionException if a connection error occurs during the request
   * @throws SCHttpUnknownException if an unknown error occurs during the request
   * @return http response string
   * @since 1.15.2
   */
  public static String httpDelete(String url, String header, String body, String outputFile)
      throws EWException,
          IOException,
          SCHttpEwonException,
          SCHttpAuthException,
          SCHttpConnectionException,
          SCHttpUnknownException {
    return httpRequest(url, header, body, HTTPX_DELETE_STRING, outputFile);
  }

  /**
   * Performs an HTTP OPTIONS request to the specified URL using the specified request header and
   * body.
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
   * @return http response string
   * @since 1.15.2
   */
  public static String httpOptions(String url, String header, String body)
      throws EWException,
          IOException,
          SCHttpEwonException,
          SCHttpAuthException,
          SCHttpConnectionException,
          SCHttpUnknownException {
    return httpRequest(url, header, body, HTTPX_OPTIONS_STRING, null);
  }

  /**
   * Performs an HTTP OPTIONS request to the specified URL using the specified request header and
   * body, then outputs the response to the specified file. If the specified file already exists, it
   * will be overwritten.
   *
   * @param url URL to make request
   * @param header request header
   * @param body request body
   * @param outputFile file to output response to
   * @throws EWException if unable to make request
   * @throws IOException if unable to read response file
   * @throws SCHttpEwonException if an Ewon error occurs during the request
   * @throws SCHttpAuthException if an authentication error occurs during the request
   * @throws SCHttpConnectionException if a connection error occurs during the request
   * @throws SCHttpUnknownException if an unknown error occurs during the request
   * @return http response string
   * @since 1.15.2
   */
  public static String httpOptions(String url, String header, String body, String outputFile)
      throws EWException,
          IOException,
          SCHttpEwonException,
          SCHttpAuthException,
          SCHttpConnectionException,
          SCHttpUnknownException {
    return httpRequest(url, header, body, HTTPX_OPTIONS_STRING, outputFile);
  }

  /**
   * Performs an HTTP HEAD request to the specified URL using the specified request header and body.
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
   * @return http response string
   * @since 1.15.2
   */
  public static String httpHead(String url, String header, String body)
      throws EWException,
          IOException,
          SCHttpEwonException,
          SCHttpAuthException,
          SCHttpConnectionException,
          SCHttpUnknownException {
    return httpRequest(url, header, body, HTTPX_HEAD_STRING, null);
  }

  /**
   * Performs an HTTP HEAD request to the specified URL using the specified request header and body,
   * then outputs the response to the specified file. If the specified file already exists, it will
   * be overwritten.
   *
   * @param url URL to make request
   * @param header request header
   * @param body request body
   * @param outputFile file to output response to
   * @throws EWException if unable to make request
   * @throws IOException if unable to read response file
   * @throws SCHttpEwonException if an Ewon error occurs during the request
   * @throws SCHttpAuthException if an authentication error occurs during the request
   * @throws SCHttpConnectionException if a connection error occurs during the request
   * @throws SCHttpUnknownException if an unknown error occurs during the request
   * @return http response string
   * @since 1.15.2
   */
  public static String httpHead(String url, String header, String body, String outputFile)
      throws EWException,
          IOException,
          SCHttpEwonException,
          SCHttpAuthException,
          SCHttpConnectionException,
          SCHttpUnknownException {
    return httpRequest(url, header, body, HTTPX_HEAD_STRING, outputFile);
  }

  /**
   * Performs an HTTP PURGE request to the specified URL using the specified request header and
   * body.
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
   * @return http response string
   * @since 1.15.2
   */
  public static String httpPurge(String url, String header, String body)
      throws EWException,
          IOException,
          SCHttpEwonException,
          SCHttpAuthException,
          SCHttpConnectionException,
          SCHttpUnknownException {
    return httpRequest(url, header, body, HTTPX_PURGE_STRING, null);
  }

  /**
   * Performs an HTTP PURGE request to the specified URL using the specified request header and
   * body, then outputs the response to the specified file. If the specified file already exists, it
   * will be overwritten.
   *
   * @param url URL to make request
   * @param header request header
   * @param body request body
   * @param outputFile file to output response to
   * @throws EWException if unable to make request
   * @throws IOException if unable to read response file
   * @throws SCHttpEwonException if an Ewon error occurs during the request
   * @throws SCHttpAuthException if an authentication error occurs during the request
   * @throws SCHttpConnectionException if a connection error occurs during the request
   * @throws SCHttpUnknownException if an unknown error occurs during the request
   * @return http response string
   * @since 1.15.2
   */
  public static String httpPurge(String url, String header, String body, String outputFile)
      throws EWException,
          IOException,
          SCHttpEwonException,
          SCHttpAuthException,
          SCHttpConnectionException,
          SCHttpUnknownException {
    return httpRequest(url, header, body, HTTPX_PURGE_STRING, outputFile);
  }

  /**
   * Gets the directory that the Ewon uses to check for SSL Certificates
   *
   * @return path configured to use for HTTP certificates
   * @throws EWException if unable to get the certificate path
   * @since 1.14.3
   */
  public static String getHttpCertificatePath() throws EWException {
    SysControlBlock SCB = new SysControlBlock(SysControlBlock.SYS);
    return SCB.getItem(SCB_CERT_PATH_KEY);
  }

  /**
   * Sets the directory that the Ewon uses to check for SSL Certificates
   *
   * @param httpCertPath path to use for HTTP certificates
   * @throws EWException if unable to set the certificate path
   * @since 1.14.3
   */
  public static void setHttpCertificatePath(String httpCertPath) throws EWException {
    SysControlBlock SCB = new SysControlBlock(SysControlBlock.SYS);

    if (!SCB.getItem(SCB_CERT_PATH_KEY).equals(httpCertPath)) {
      SCB.setItem(SCB_CERT_PATH_KEY, httpCertPath);
      SCB.saveBlock(true);
    }
  }
}
