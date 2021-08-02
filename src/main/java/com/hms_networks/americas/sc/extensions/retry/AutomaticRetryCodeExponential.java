package com.hms_networks.americas.sc.extensions.retry;

/**
 * Abstract class which allows for an implemented method of code to be retried up to the maximum
 * number of retries specified by {@link #getMaxRetries()} using an exponential backoff algorithm to
 * determine the delay between each retry.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.0.0
 */
public abstract class AutomaticRetryCodeExponential extends AutomaticRetryCode {

  /** Constant indicating the number of milliseconds in one (1) second. */
  private static final long MILLIS_PER_SECOND = 1000;

  /**
   * Method to return the length of delay (milliseconds) before the specified retry number.
   *
   * @param retry retry number
   * @return number of milliseconds delay before specified retry number
   */
  protected long getDelayMillisBeforeRetry(int retry) {

    /*
     * Check the retry number wont cause long overflow in delayMillisBeforeRetry.
     * The largest retry that will produce a positive delayMillisBeforeRetry is 53.
     * (2^53 - 1) * 1000 = 9.0071993e+18
     */
    final int maxRetryNumber = 53;
    if (retry > maxRetryNumber) {
      retry = maxRetryNumber;
    }

    // Formula: 2ᶜ - 1
    long delaySecondsBeforeRetry = (long) (Math.pow(2, retry) - 1);
    long delayMillisBeforeRetry = delaySecondsBeforeRetry * MILLIS_PER_SECOND;

    // Check if above maximum delay, set to maximum if larger
    if (delayMillisBeforeRetry > getMaxDelayMillisBeforeRetry()) {
      delayMillisBeforeRetry = getMaxDelayMillisBeforeRetry();
    }

    return delayMillisBeforeRetry;
  }

  /**
   * Abstract method which must be implemented to return the maximum delay (milliseconds) before
   * retrying.
   *
   * @return maximum number of milliseconds delay before retry
   */
  protected abstract long getMaxDelayMillisBeforeRetry();
}
