package com.hms_networks.americas.sc.extensions.historicaldata;

/**
 * Rapid Catch Up tracking object.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.14.2
 */
public class RapidCatchUpTracker {
  /**
   * Boolean indicating that historical tracking has caught up to a period where data can be
   * expected in responses.
   */
  private boolean isHistoricalTrackingCaughtUp;

  /**
   * Ending timestamp from the last check for historical data. When historical reads are empty, this
   * is the end time. When the response is not empty, this is the timestamp of the first found
   * response. This value is in milliseconds and UTC.
   */
  private long trackingEndTimeMilliseconds;

  /**
   * Constructor for a tracking object.
   *
   * @param isCaughtUp {@code true}/{@code false} the last request indicated that historical
   *     tracking is caught up to a period with data.
   * @param epochMilliSeconds Epoch time in milliseconds UTC.
   */
  RapidCatchUpTracker(boolean isCaughtUp, long epochMilliSeconds) {
    this.isHistoricalTrackingCaughtUp = isCaughtUp;
    this.trackingEndTimeMilliseconds = epochMilliSeconds;
  }

  /**
   * Getter method for trackingEndTimeMilliseconds.
   *
   * @return End time for tracking in milliseconds since epoch UTC
   */
  public long getTrackingEndTimeMilliseconds() {
    return trackingEndTimeMilliseconds;
  }

  /**
   * Getter method for isHistoricalTrackCaughtUp boolean.
   *
   * @return {@code true}/{@code false} historical data tracking has caught up to a period when
   *     results can be expected.
   */
  public boolean isHistoricalTrackingCaughtUp() {
    return isHistoricalTrackingCaughtUp;
  }
}
