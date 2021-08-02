package com.hms_networks.americas.sc.extensions.retry;

/**
 * Abstract class which allows for an implemented method of code to be retried up to the maximum
 * number of retries specified by {@link #getMaxRetries()} using a linear backoff algorithm to
 * determine the delay between each retry.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.0.0
 */
public abstract class AutomaticRetryCodeLinear extends AutomaticRetryCode {

  /**
   * Method to return the length of delay (milliseconds) before the specified retry number.
   *
   * @param retry retry number
   * @return number of milliseconds delay before specified retry number
   */
  protected long getDelayMillisBeforeRetry(int retry) {

    long delayMillisBeforeRetry = 0;

    // Check that delayMillisBeforeRetry will not overflow long
    if ((Long.MAX_VALUE / getLinearSlopeMillis()) > retry) {
      delayMillisBeforeRetry = retry * getLinearSlopeMillis();

      // Check if above maximum delay, set to maximum if larger
      if (delayMillisBeforeRetry > getMaxDelayMillisBeforeRetry()) {
        delayMillisBeforeRetry = getMaxDelayMillisBeforeRetry();
      }
    } else {
      // If retry number is large enough to cause long overflow, set to max long.
      delayMillisBeforeRetry = Long.MAX_VALUE;
    }

    return delayMillisBeforeRetry;
  }

  /**
   * Abstract method which must be implemented to return the slope of the linear backoff algorithm
   * (milliseconds). The linear slope is the number of milliseconds increase in delay between each
   * retry.
   *
   * @return maximum number of retries
   */
  protected abstract long getLinearSlopeMillis();

  /**
   * Abstract method which must be implemented to return the maximum delay (milliseconds) before
   * retrying.
   *
   * @return maximum number of milliseconds delay before retry
   */
  protected abstract long getMaxDelayMillisBeforeRetry();
}
