package com.hms_networks.americas.sc.extensions.retry;

/**
 * Abstract class which allows for an implemented method of code to be retried up to the maximum
 * number of retries specified by {@link #getMaxRetries()} with a delay between each retry
 * calculated by {@link #getDelayMillisBeforeRetry(int)}.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.0.0
 */
public abstract class AutomaticRetryCode {

  /** The value for maximum retries used to indicate an unlimited number. */
  public static final int MAX_RETRIES_UNLIMITED_VALUE = -1;

  /** The default value of the {@link #state} variable. */
  private static final AutomaticRetryState STATE_DEFAULT = AutomaticRetryState.PENDING;

  /** The default value of the {@link #retryNumber} variable. */
  private static final int RETRY_NUMBER_DEFAULT = 0;

  /** Variable to track the state of the running code which determines the retry behavior. */
  private AutomaticRetryState state = STATE_DEFAULT;

  /** Variable to track the number of retries */
  private int retryNumber = RETRY_NUMBER_DEFAULT;

  /**
   * Run method for automatic retry code, which attempts to run the implemented {@link
   * #codeToRetry()} method and retry or fail based on the resulting {@link AutomaticRetryState}.
   *
   * @return automatic retry code result
   * @throws InterruptedException should retry be interrupted
   */
  public AutomaticRetryResult run() throws InterruptedException {
    // Loop until finished or critical error
    boolean shouldRun = true;
    while (shouldRun) {
      // Run code
      codeToRetry();

      // Check if should continue running/retrying
      if (state == AutomaticRetryState.FINISHED || state == AutomaticRetryState.ERROR_STOP) {
        shouldRun = false;
      } else if (state == AutomaticRetryState.ERROR_RETRY) {
        // Check if above retry limit, if not or unlimited retries enabled, increment retry counter
        final int maxRetries = getMaxRetries();
        if (maxRetries != MAX_RETRIES_UNLIMITED_VALUE && retryNumber >= maxRetries) {
          // If above retry limit, stop attempting
          shouldRun = false;
        } else {
          retryNumber++;
        }
      }

      // Add delay if loop continuing
      if (shouldRun) {
        Thread.sleep(getDelayMillisBeforeRetry(retryNumber));
      }
    }

    // Return correct result based on state
    AutomaticRetryResult result;
    if (state == AutomaticRetryState.FINISHED) {
      result = AutomaticRetryResult.SUCCESS;
    } else if (state == AutomaticRetryState.ERROR_RETRY) {
      result = AutomaticRetryResult.FAIL_RETRY_LIMIT;
    } else {
      result = AutomaticRetryResult.FAIL_CRITICAL_STOP;
    }
    return result;
  }

  /** Resets the automatic retry code state and retry counter to their initial, default values. */
  public void reset() {
    state = STATE_DEFAULT;
    retryNumber = RETRY_NUMBER_DEFAULT;
  }

  /**
   * Sets the automatic retry code state to the specified value.
   *
   * @param state new automatic retry code state
   */
  protected void setState(AutomaticRetryState state) {
    this.state = state;
  }

  /**
   * Gets the number of the current try of {@link #codeToRetry()}. This is computed by adding 1 to
   * the retry number.
   *
   * @return current try number
   * @since 1.2
   */
  protected int getCurrentTryNumber() {
    return retryNumber;
  }

  /**
   * Abstract method which must be implemented to return the maximum number of retries before
   * critically failing.
   *
   * @return maximum number of retries
   */
  protected abstract int getMaxRetries();

  /**
   * Abstract method which must be implemented to return the length of delay (milliseconds) before
   * the specified retry number.
   *
   * @param retry retry number
   * @return number of milliseconds delay before specified retry number
   */
  protected abstract long getDelayMillisBeforeRetry(int retry);

  /**
   * Abstract method which must be implemented to contain the code which will automatically be
   * retried based on the state, which must be set using {@link #setState(AutomaticRetryState)}.
   */
  protected abstract void codeToRetry();
}
