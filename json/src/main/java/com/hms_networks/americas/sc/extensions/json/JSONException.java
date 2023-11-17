package com.hms_networks.americas.sc.extensions.json;

/**
 * The JSONException is thrown by the JSON.org classes then things are amiss.
 *
 * @author JSON.org
 * @version 2
 * @since 1.0.0
 */
public class JSONException extends Exception {

  private static final long serialVersionUID = -7375858803424286230L;
  private Throwable cause;

  /**
   * Constructs a JSONException with an explanatory message.
   *
   * @param message Detail about the reason for the exception.
   */
  public JSONException(String message) {
    super(message);
  }

  public JSONException(Throwable t) {
    super(t.getMessage());
    this.cause = t;
  }

  public Throwable getCause() {
    return this.cause;
  }
}
