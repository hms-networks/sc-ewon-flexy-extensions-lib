package com.hms_networks.americas.sc.extensions.api;

import com.ewon.ewonitf.EvtWebFormListener;
import com.hms_networks.americas.sc.extensions.logging.Logger;
import com.hms_networks.americas.sc.extensions.string.StringUtils;
import java.io.PrintStream;

/**
 * The listener class for receiving HTTP application control API requests. The implementing
 * application must register this listener with the {@link com.ewon.ewonitf.DefaultEventHandler}
 * class, for example <code>
 * DefaultEventHandler.setDefaultWebFormtListener(new ApplicationControlApiListener());
 * </code>
 *
 * @since 1.9.0
 * @author HMS Networks, MU Americas Solution Center
 */
public abstract class ApplicationControlApiListener extends EvtWebFormListener {

  /** The API name used for the shutdown operation in the HTTP application control API. */
  private static final String SHUTDOWN_API_NAME = "shutdown";

  /** The API name used for the restart operation in the HTTP application control API. */
  private static final String RESTART_API_NAME = "restart";

  /**
   * The API name used for the get version information operation in the HTTP application control
   * API.
   */
  private static final String GET_VERSION_API_NAME = "getVersion";

  /**
   * The API name used for the get config information operation in the HTTP application control API.
   */
  private static final String GET_CONFIG_API_NAME = "getConfig";

  /** The list of supported forms which are handled by this listener. */
  private static final String[] SUPPORTED_FORMS = {"controlApi", "statusApi"};

  /**
   * The list of supported parameters for each of the supported forms defined in {@link
   * #SUPPORTED_FORMS}. The index of the array corresponds to the index of the form in {@link
   * #SUPPORTED_FORMS}.
   */
  private static final String[][] SUPPORTED_PARAMS = {
    {SHUTDOWN_API_NAME, RESTART_API_NAME}, {GET_VERSION_API_NAME, GET_CONFIG_API_NAME}
  };

  /** The content type for the response to the request. */
  private static final String RESPONSE_CONTENT_TYPE = "application/json";

  /** The filler value used to indicate the error message in the bad request response. */
  private static final String RESPONSE_BAD_ERROR_FILLER = "%ERROR%";

  /** The response to a bad request. */
  private static final String RESPONSE_BAD =
      "{\"status\":\"error\",\"error\":\"" + RESPONSE_BAD_ERROR_FILLER + "\"}";

  /** The response to a request for an unknown or non-supported form. */
  private static final String RESPONSE_UNKNOWN_FORM =
      "{\"status\":\"error\",\"error\":\"unknown form\"}";

  /** The response to a request for an unknown or non-supported parameter. */
  private static final String RESPONSE_UNKNOWN_PARAM =
      "{\"status\":\"error\",\"error\":\"unknown parameter\"}";

  /**
   * The listener interface for receiving HTTP application control API requests to the JVM form
   * specified in the <code>formName</code> parameter.
   *
   * @param formName the name of the form where the HTTP application control API requests was sent
   */
  public void callForm(String formName) {
    // Create print stream for writing the response
    PrintStream printStream = new PrintStream(this);

    // Set response web header
    setWebHeader(RESPONSE_CONTENT_TYPE);

    // Loop through supported forms and check if the form name matches
    boolean matchedForm = false;
    boolean matchedParam = false;
    for (int formIndex = 0; formIndex < SUPPORTED_FORMS.length; formIndex++) {
      // Check if form name matches/is supported
      if (SUPPORTED_FORMS[formIndex].equals(formName)) {
        matchedForm = true;
        // Check if the request contains a supported parameter
        for (int paramIndex = 0; paramIndex < SUPPORTED_PARAMS[formIndex].length; paramIndex++) {
          String supportedParameterName = SUPPORTED_PARAMS[formIndex][paramIndex];
          try {
            String supportedParameterValue = getWebVar(supportedParameterName, "");
            if (supportedParameterValue.length() > 0) {
              matchedParam = true;
              Logger.LOG_SERIOUS(
                  "Processing request for form ["
                      + formName
                      + "], parameter ["
                      + supportedParameterName
                      + "] with value ["
                      + supportedParameterValue
                      + "]");

              // Pass request value to proper handler
              if (supportedParameterName.equals(SHUTDOWN_API_NAME)) {
                printStream.println(onShutdown(supportedParameterValue));
              } else if (supportedParameterName.equals(RESTART_API_NAME)) {
                printStream.println(onRestart(supportedParameterValue));
              } else if (supportedParameterName.equals(GET_VERSION_API_NAME)) {
                printStream.println(onGetVersion(supportedParameterValue));
              } else if (supportedParameterName.equals(GET_CONFIG_API_NAME)) {
                printStream.println(onGetConfig(supportedParameterValue));
              }
            }
          } catch (Exception e) {
            Logger.LOG_SERIOUS(
                "Unable to process web API request for form ["
                    + formName
                    + "], parameter ["
                    + supportedParameterName
                    + "].");
            Logger.LOG_EXCEPTION(e);
            String errorMessage =
                "Unable to process web API request for form ["
                    + formName
                    + "], parameter ["
                    + supportedParameterName
                    + "]: "
                    + e.getMessage();
            printStream.println(
                StringUtils.replace(RESPONSE_BAD, RESPONSE_BAD_ERROR_FILLER, errorMessage));
          }
        }
      }
    }

    // Check if the request was for an unknown or non-supported form
    if (!matchedForm) {
      printStream.println(RESPONSE_UNKNOWN_FORM);
    }

    // Check if the request was for an unknown or non-supported parameter
    if (!matchedParam) {
      printStream.println(RESPONSE_UNKNOWN_PARAM);
    }

    // Close print stream
    printStream.close();
  }

  /**
   * The abstract method which is called when the listener receives a request for the application to
   * restart.
   *
   * @return the response to the request
   */
  public abstract String onRestart(String value);

  /**
   * The abstract method which is called when the listener receives a request for the application to
   * shutdown.
   *
   * @return the response to the request
   */
  public abstract String onShutdown(String value);

  /**
   * The abstract method which is called when the listener receives a request for the application to
   * return version information.
   *
   * @return the response to the request
   */
  public abstract String onGetVersion(String value);

  /**
   * The abstract method which is called when the listener receives a request for the application to
   * return configuration information.
   *
   * @return the response to the request
   */
  public abstract String onGetConfig(String value);
}
