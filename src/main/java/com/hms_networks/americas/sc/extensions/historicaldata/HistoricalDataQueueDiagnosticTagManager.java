package com.hms_networks.americas.sc.extensions.historicaldata;

import com.ewon.ewonitf.EWException;
import com.ewon.ewonitf.TagControl;
import com.hms_networks.americas.sc.extensions.system.tags.SCTagUtils;
import com.hms_networks.americas.sc.extensions.system.time.SCTimeUnit;

/**
 * Class to manage diagnostic tags for the historical data queue in {@link
 * HistoricalDataQueueManager}.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.14.1-pre1
 */
public class HistoricalDataQueueDiagnosticTagManager {

  /**
   * Tag control object used for updating the value of the queue diagnostic tag for the amount of
   * time running behind in seconds.
   */
  private static TagControl queueDiagnosticRunningBehindSecondsTag = null;

  /**
   * Tag control object used for getting the value of the queue diagnostic tag for forcing a reset
   * of the time tracker.
   */
  private static TagControl queueDiagnosticForceResetTag = null;

  /**
   * Tag control object used for updating the value of the queue diagnostic tag for the queue poll
   * count.
   */
  private static TagControl queueDiagnosticPollCountTag = null;

  /** Boolean flag indicating whether the queue diagnostic tags have been configured. */
  private static boolean isConfigured = false;

  /**
   * Returns a boolean flag indicating whether the queue diagnostic tags have been configured.
   *
   * @return true if the queue diagnostic tags have been configured, false otherwise
   */
  public static synchronized boolean isConfigured() {
    return isConfigured;
  }

  /**
   * Configures the queue diagnostic tags if the specified <code>queueDiagnosticTagsEnabled</code>
   * boolean is true.
   *
   * @throws Exception if a diagnostic tag is missing and cannot be created
   */
  public static synchronized void configureQueueDiagnosticTags() throws Exception {
    // Configure queue running behind diagnostic tag
    queueDiagnosticRunningBehindSecondsTag =
        tryCreateDiagnosticTag(
            HistoricalDataConstants.QUEUE_DIAGNOSTIC_TAG_RUNNING_BEHIND_SECONDS_NAME,
            HistoricalDataConstants.QUEUE_DIAGNOSTIC_TAG_RUNNING_BEHIND_SECONDS_DESC,
            HistoricalDataConstants.QUEUE_DIAGNOSTIC_TAG_IO_SERVER,
            HistoricalDataConstants.QUEUE_DIAGNOSTIC_TAG_RUNNING_BEHIND_SECONDS_TYPE);

    // Configure queue reset tag
    queueDiagnosticForceResetTag =
        tryCreateDiagnosticTag(
            HistoricalDataConstants.QUEUE_DIAGNOSTIC_TAG_FORCE_RESET_NAME,
            HistoricalDataConstants.QUEUE_DIAGNOSTIC_TAG_FORCE_RESET_DESC,
            HistoricalDataConstants.QUEUE_DIAGNOSTIC_TAG_IO_SERVER,
            HistoricalDataConstants.QUEUE_DIAGNOSTIC_TAG_FORCE_RESET_TYPE);

    // Configure queue poll count tag
    queueDiagnosticPollCountTag =
        tryCreateDiagnosticTag(
            HistoricalDataConstants.QUEUE_DIAGNOSTIC_TAG_POLL_COUNT_NAME,
            HistoricalDataConstants.QUEUE_DIAGNOSTIC_TAG_POLL_COUNT_DESC,
            HistoricalDataConstants.QUEUE_DIAGNOSTIC_TAG_IO_SERVER,
            HistoricalDataConstants.QUEUE_DIAGNOSTIC_TAG_POLL_COUNT_TYPE);

    // Set configured flag
    isConfigured = true;
  }

  /**
   * Attempts to create a diagnostic tag with the specified tag name, description, IO server, and
   * type.
   *
   * @param tagName diagnostic tag name
   * @param tagDesc diagnostic tag description
   * @param tagIoServer diagnostic tag IO server
   * @param tagType diagnostic tag type
   * @return corresponding {@link TagControl} object for the diagnostic tag
   * @throws Exception if the diagnostic tag is missing and cannot be created
   */
  public static TagControl tryCreateDiagnosticTag(
      String tagName, String tagDesc, String tagIoServer, int tagType) throws Exception {
    // Attempt to create tag control object (throws exception if tag missing)
    TagControl tagControl;
    try {
      tagControl = new TagControl(tagName);
    } catch (Exception ignored) {
      SCTagUtils.createTag(tagName, tagDesc, tagIoServer, tagType);
      tagControl = new TagControl(tagName);
    }
    return tagControl;
  }

  /**
   * Returns a boolean flag indicating whether the queue diagnostic tag for force reset is set to
   * true.
   *
   * @return true if the queue diagnostic tag for force reset is set to true, false otherwise
   */
  public static synchronized boolean isQueueDiagnosticTagForceResetRequested() {
    return queueDiagnosticForceResetTag != null
        && (queueDiagnosticForceResetTag.getTagValueAsInt()
            == HistoricalDataConstants.QUEUE_DIAGNOSTIC_TAG_FORCE_RESET_TRUE_VALUE);
  }

  /**
   * Updates the queue diagnostic tags with the specified queue time running behind (in
   * milliseconds), force reset value, and queue poll count.
   *
   * @param newQueueRunningBehindTimeMsValue new value for the queue time running behind time (in
   *     milliseconds) diagnostic tag
   * @param newQueueForceResetValue new value for the force reset diagnostic tag
   * @param newQueuePollCountValue new value for the queue poll count diagnostic tag
   * @throws EWException if the queue diagnostic tags are configured but cannot be updated
   */
  public static synchronized void updateDiagnosticTags(
      long newQueueRunningBehindTimeMsValue,
      boolean newQueueForceResetValue,
      long newQueuePollCountValue)
      throws EWException {
    // Update queue running behind time (in milliseconds) diagnostic tag
    if (queueDiagnosticRunningBehindSecondsTag != null) {
      queueDiagnosticRunningBehindSecondsTag.setTagValueAsLong(
          SCTimeUnit.MILLISECONDS.toSeconds(newQueueRunningBehindTimeMsValue));
    }

    // Update force reset diagnostic tag
    if (queueDiagnosticForceResetTag != null) {
      queueDiagnosticForceResetTag.setTagValueAsInt(
          newQueueForceResetValue
              ? HistoricalDataConstants.QUEUE_DIAGNOSTIC_TAG_FORCE_RESET_TRUE_VALUE
              : HistoricalDataConstants.QUEUE_DIAGNOSTIC_TAG_FORCE_RESET_FALSE_VALUE);
    }

    // Update queue poll count diagnostic tag
    if (queueDiagnosticPollCountTag != null) {
      queueDiagnosticPollCountTag.setTagValueAsLong(newQueuePollCountValue);
    }
  }
}
