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

  /**
   * The API endpoint/operation name used for the shutdown operation in the HTTP application control
   * API.
   */
  private static final String SHUTDOWN_API_NAME = "shutdown";

  /**
   * The API endpoint/operation name used for the restart operation in the HTTP application control
   * API.
   */
  private static final String RESTART_API_NAME = "restart";

  /**
   * The API endpoint/operation name used for the get version information operation in the HTTP
   * application status API.
   */
  private static final String GET_VERSION_API_NAME = "getVersion";

  /**
   * The API endpoint/operation name used for the get config information operation in the HTTP
   * application status API.
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
  protected static final String RESPONSE_CONTENT_TYPE = "application/json";

  /** The filler value used to indicate the error message in the bad request response. */
  protected static final String RESPONSE_BAD_ERROR_FILLER = "%ERROR%";

  /** The response to a bad request. */
  protected static final String RESPONSE_BAD =
      "{\"status\":\"error\",\"error\":\"" + RESPONSE_BAD_ERROR_FILLER + "\"}";

  /** The response to a request for an unknown or non-supported form. */
  protected static final String RESPONSE_UNKNOWN_FORM =
      "{\"status\":\"error\",\"error\":\"unknown form\"}";

  /** The response to a request for an unknown or non-supported parameter. */
  protected static final String RESPONSE_UNKNOWN_PARAM =
      "{\"status\":\"error\",\"error\":\"unknown parameter\"}";

  /** The array of registered custom forms which are handled by this listener. */
  private final String[] registeredCustomForms;

  /**
   * Constructor for a new {@link ApplicationControlApiListener} object with no custom forms
   * registered. This constructor must be called by the implementing application.
   */
  public ApplicationControlApiListener() {
    // Call super constructor
    super();

    // Initialize empty registered custom forms array
    registeredCustomForms = new String[] {};
  }

  /**
   * Constructor for a new {@link ApplicationControlApiListener} object with the specified custom
   * form registered. This constructor must be called by the implementing application.
   *
   * @param customSupportedForm the custom form to register with this listener
   */
  public ApplicationControlApiListener(String customSupportedForm) {
    // Call super constructor
    super();

    // Initialize registered custom forms array
    registeredCustomForms = new String[] {customSupportedForm};
  }

  /**
   * Constructor for a new {@link ApplicationControlApiListener} object with the specified array of
   * custom forms registered. This constructor must be called by the implementing application.
   *
   * @param registeredCustomForms the array of custom forms to register with this listener
   */
  public ApplicationControlApiListener(String[] registeredCustomForms) {
    // Call super constructor
    super();

    // Initialize registered custom forms array
    this.registeredCustomForms = registeredCustomForms;
  }

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

    // Create variable to track if a supported form and parameter was matched
    boolean matchedForm = false;
    boolean matchedParam = false;

    // Check if form is a supported custom form
    if (registeredCustomForms.length > 0) {
      // Loop through supported custom forms and check if the form name matches
      for (int customSupportedFormIndex = 0;
          customSupportedFormIndex < registeredCustomForms.length;
          customSupportedFormIndex++) {
        // Check if form name matches/is supported
        String customSupportedFormName = registeredCustomForms[customSupportedFormIndex];
        if (customSupportedFormName.equals(formName)) {
          matchedForm = true;

          // Pass request to custom form handler
          String customFormResponse = onCustomForm(formName);
          if (customFormResponse != null) {
            matchedParam = true;
            printStream.println(customFormResponse);
          }
        }
      }
    }

    // If form not already matched, check if form is a supported standard form
    if (!matchedForm) {
      // Loop through supported forms and check if the form name matches
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
   * @param value the value passed in the request
   * @return the response to the request
   */
  public abstract String onRestart(String value);

  /**
   * The abstract method which is called when the listener receives a request for the application to
   * shutdown.
   *
   * @param value the value passed in the request
   * @return the response to the request
   */
  public abstract String onShutdown(String value);

  /**
   * The abstract method which is called when the listener receives a request for the application to
   * return version information.
   *
   * @param value the value passed in the request
   * @return the response to the request
   */
  public abstract String onGetVersion(String value);

  /**
   * The abstract method which is called when the listener receives a request for the application to
   * return configuration information.
   *
   * @param value the value passed in the request
   * @return the response to the request
   */
  public abstract String onGetConfig(String value);

  /**
   * The abstract method which is called when the listener receives a request on a registered custom
   * form.
   *
   * <p>Please note that custom form parameter(s) are not inspected, retrieved, or supplied by the
   * listener. It is the responsibility of the implementation to inspect for and retrieve any
   * desired parameter(s).
   *
   * <p>Custom form parameter(s) may be retrieved using the {@link #getWebVar(String, String)}
   * method, where the first parameter is the name of the parameter and the second parameter is the
   * default value to return if the parameter is not found (non-null).
   *
   * <p>If the implementation determines that the given parameter(s) are invalid or not supported, a
   * null value may be returned, which will trigger a {@link #RESPONSE_UNKNOWN_PARAM} response.
   *
   * @return the response to the request, or null to trigger a {@link #RESPONSE_UNKNOWN_PARAM}
   *     response. Responses must be in the form of a JSON string.
   */
  public abstract String onCustomForm(String form);
}
