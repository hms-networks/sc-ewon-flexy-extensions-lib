package com.hms_networks.americas.sc.extensions.retry;

/**
 * Class to represent the result of automatic retry code in a similar fashion to enums in Java 1.5+.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.0.0
 */
public class AutomaticRetryResult {

  /** Integer assigned to represent a successful result. */
  private static final int SUCCESS_INT = 0;

  /** Integer assigned to represent a failing result, due to reaching the limit of retries. */
  private static final int FAIL_RETRY_LIMIT_INT = 1;

  /** Integer assigned to represent a failing result, due to encountering a critical error. */
  private static final int FAIL_CRITICAL_STOP_INT = 2;

  /** Public instance of {@link AutomaticRetryResult} representing a successful result. */
  public static final AutomaticRetryResult SUCCESS = new AutomaticRetryResult(SUCCESS_INT);

  /**
   * Public instance of {@link AutomaticRetryResult} representing a failing result, due to reaching
   * the limit of retries.
   */
  public static final AutomaticRetryResult FAIL_RETRY_LIMIT =
      new AutomaticRetryResult(FAIL_RETRY_LIMIT_INT);

  /**
   * Public instance of {@link AutomaticRetryResult} representing a failing result, due to
   * encountering a critical error.
   */
  public static final AutomaticRetryResult FAIL_CRITICAL_STOP =
      new AutomaticRetryResult(FAIL_CRITICAL_STOP_INT);

  /** Instance result integer. */
  private final int resultInt;

  /**
   * Private (internal) constructor for creating an instance of {@link AutomaticRetryResult} with a
   * result integer.
   *
   * <p>Note: Result integer shall be unique.
   *
   * @param resultInt integer to represent results
   */
  private AutomaticRetryResult(int resultInt) {
    this.resultInt = resultInt;
  }
}
