package com.hms_networks.americas.sc.extensions.historicaldata;

import com.hms_networks.americas.sc.extensions.datapoint.DataPoint;
import com.hms_networks.americas.sc.extensions.fileutils.FileAccessManager;
import com.hms_networks.americas.sc.extensions.json.JSONException;
import com.hms_networks.americas.sc.extensions.system.time.SCTimeSpan;
import com.hms_networks.americas.sc.extensions.system.time.SCTimeUnit;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Class to manage queueing historical tag data and retrieving it in chunks based on a configurable
 * time span of data.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.0.0
 */
public class HistoricalDataQueueManager {

  /**
   * Value for the maximum amount of time (in minutes) Historical FIFO can get behind which
   * indicates that there is no maximum (disable check).
   */
  public static final long DISABLED_MAX_HIST_FIFO_GET_BEHIND_MINS = -1;

  /**
   * Value for the maximum amount of time (in milliseconds) Historical FIFO can get behind which
   * indicates that there is no maximum (disable check).
   */
  private static final long DISABLED_MAX_HIST_FIFO_GET_BEHIND_MS =
      SCTimeUnit.MINUTES.toMillis(DISABLED_MAX_HIST_FIFO_GET_BEHIND_MINS);

  /** Time span for fetching FIFO queue data. Default is 1 minute. */
  private static long queueFifoTimeSpanMins = 1;

  /** Boolean flag indicating if string history data should be included in queue data. */
  private static boolean stringHistoryEnabled = false;

  /**
   * Boolean flag indicating if reads are from QUEUE_TIME_FILE_1_NAME or QUEUE_TIME_FILE_2_NAME. If
   * set to false, reads are from QUEUE_TIME_FILE_2_NAME.
   */
  private static boolean isFile1CurrTimeTrackerFile = true;

  /** Boolean flag indicating if time has been initialized. */
  private static boolean hasInitTime = false;

  /**
   * Boolean flag indicating whether the historical data queue diagnostic tags should be enabled.
   */
  private static boolean enableDiagnosticTags = false;

  /**
   * The threshold (in seconds) for the running behind time tag value to be displayed. If the
   * running behind time is less than this value, the diagnostic tag value will be 0.
   *
   * <p>The running behind time display threshold helps to prevent users from being alarmed by a
   * running behind time which is within acceptable limits.
   */
  private static long queueRunningBehindTimeTagDisplayThresholdSeconds =
      HistoricalDataConstants.DEFAULT_QUEUE_DIAGNOSTIC_TAG_RUNNING_BEHIND_SECONDS_DISPLAY_THRESHOLD;

  /** The count of poll requests that have been made to the historical data queue. */
  private static long queuePollCount = 0;

  /** File path for time marker file 1. */
  private static final String timeMarkerFile1Name =
      HistoricalDataConstants.QUEUE_FILE_FOLDER
          + "/"
          + HistoricalDataConstants.QUEUE_TIME_FILE_1_NAME
          + HistoricalDataConstants.QUEUE_FILE_EXTENSION;

  /** File path for time marker file 2. */
  private static final String timeMarkerFile2Name =
      HistoricalDataConstants.QUEUE_FILE_FOLDER
          + "/"
          + HistoricalDataConstants.QUEUE_TIME_FILE_2_NAME
          + HistoricalDataConstants.QUEUE_FILE_EXTENSION;

  /** Maximum amount of time that the historical Fifo can get behind in milliseconds. */
  private static long maxQueueGetsBehindMs = DISABLED_MAX_HIST_FIFO_GET_BEHIND_MS;

  /**
   * {@code true}/{@code false} whether the number of dataPoints from the most recent historical
   * read was zero
   */
  private static boolean lastReadDataPointsEmpty = false;

  /**
   * Get the current configured FIFO queue time span in milliseconds.
   *
   * @return FIFO queue time span in ms
   */
  private static synchronized long getQueueFifoTimeSpanMillis() {
    return SCTimeUnit.MINUTES.toMillis(queueFifoTimeSpanMins);
  }

  /**
   * Get the current configured FIFO queue time span in minutes.
   *
   * @return FIFO queue time span in mins
   */
  public static synchronized long getQueueFifoTimeSpanMins() {
    return queueFifoTimeSpanMins;
  }

  /**
   * Set the FIFO queue time span in minutes.
   *
   * @param timeSpanMins new FIFO queue time span in minutes
   * @throws IllegalArgumentException if unable to read a file
   */
  public static synchronized void setQueueFifoTimeSpanMins(long timeSpanMins) {
    if (timeSpanMins < HistoricalDataConstants.MIN_QUEUE_SPAN_MINS) {
      throw new IllegalArgumentException(
          "The FifoTimeSpan must not be less than "
              + HistoricalDataConstants.MIN_QUEUE_SPAN_MINS
              + ".");
    }
    queueFifoTimeSpanMins = timeSpanMins;
  }

  /**
   * Set the maximum amount the Historical FIFO queue can get behind in minutes. This check can be
   * disabled by setting the value to {@link #DISABLED_MAX_HIST_FIFO_GET_BEHIND_MINS}.
   *
   * @param timeMins new maximum amount the Historical FIFO queue can get behind in minutes
   * @throws IllegalArgumentException if parameter is not greater than 0
   */
  public static synchronized void setQueueMaxBehindMins(long timeMins) {
    if (timeMins <= 0 && timeMins != DISABLED_MAX_HIST_FIFO_GET_BEHIND_MINS) {
      throw new IllegalArgumentException(
          "The parameter passed must be greater than 0 or be "
              + DISABLED_MAX_HIST_FIFO_GET_BEHIND_MINS
              + ".");
    }
    maxQueueGetsBehindMs = SCTimeUnit.MINUTES.toMillis(timeMins);
  }

  /**
   * Get the maximum amount the Historical FIFO queue can get behind in minutes.
   *
   * @return maximum amount the Historical FIFO queue can get behind in minutes
   */
  public static synchronized long getQueueMaxBehindMins() {
    return SCTimeUnit.MILLISECONDS.toMinutes(maxQueueGetsBehindMs);
  }

  /**
   * Convert a <code>long</code> time value to format required for EDB calls.
   *
   * @param time <code>long</code> time value to format
   * @return formatted time string for EBD calls
   */
  static String convertToEBDTimeFormat(long time) {
    return new SimpleDateFormat(HistoricalDataConstants.EBD_TIME_FORMAT).format(new Date(time));
  }

  /**
   * Sets the flag indicating if string history data should be included in queue data.
   *
   * @param stringHistoryEnabled true if string history should be included, false if not
   */
  public static void setStringHistoryEnabled(boolean stringHistoryEnabled) {
    HistoricalDataQueueManager.stringHistoryEnabled = stringHistoryEnabled;
  }

  /**
   * Sets the flag indicating if the historical data queue diagnostic tags should be enabled.
   *
   * <p>This method does not set the threshold for the running behind time tag value to be
   * displayed. To set the threshold, use {@link #setEnableDiagnosticTags(boolean, long)}, which
   * sets the threshold in addition to the desired historical data queue diagnostic tags enable
   * value.
   *
   * <p>If a threshold is not set, the default threshold will be used, which is defined in {@link
   * HistoricalDataConstants#DEFAULT_QUEUE_DIAGNOSTIC_TAG_RUNNING_BEHIND_SECONDS_DISPLAY_THRESHOLD}.
   *
   * @param enableDiagnosticTags true if historical data queue diagnostic tags should be enabled,
   *     false if not
   */
  public static void setEnableDiagnosticTags(boolean enableDiagnosticTags) {
    HistoricalDataQueueManager.enableDiagnosticTags = enableDiagnosticTags;
  }

  /**
   * Sets the flag indicating if the historical data queue diagnostic tags should be enabled, and
   * the threshold (in seconds) for the running behind time tag value to be displayed. If the
   * running behind time is less than this value, the diagnostic tag value will be 0.
   *
   * <p>The running behind time display threshold helps to prevent users from being alarmed by a
   * running behind time which is within acceptable limits.
   *
   * @param enableDiagnosticTags true if historical data queue diagnostic tags should be enabled,
   *     false if not
   * @param queueRunningBehindTimeTagDisplayThresholdSeconds the threshold (in seconds) for the
   *     running behind time tag value to be displayed. If the running behind time is less than this
   *     value, the diagnostic tag value will be 0. This helps to prevent users from being alarmed
   *     by a running behind time which is within acceptable limits.
   */
  public static void setEnableDiagnosticTags(
      boolean enableDiagnosticTags, long queueRunningBehindTimeTagDisplayThresholdSeconds) {
    HistoricalDataQueueManager.enableDiagnosticTags = enableDiagnosticTags;
    HistoricalDataQueueManager.queueRunningBehindTimeTagDisplayThresholdSeconds =
        queueRunningBehindTimeTagDisplayThresholdSeconds;
  }

  /**
   * Gets a boolean representing if both of the time tracker file exists.
   *
   * @return true if both time tracker file exists
   */
  public static boolean doesTimeTrackerExist() {
    return (new File(timeMarkerFile1Name).isFile() && new File(timeMarkerFile2Name).isFile());
  }

  /**
   * Gets the time (in milliseconds) that the queue is running behind by.
   *
   * @return time (in milliseconds) that the queue is running behind by
   * @throws IOException if unable to read a file
   * @throws TimeTrackerUnrecoverableException if unable to read both time tracker files
   */
  public static long getQueueTimeBehindMillis()
      throws IOException, TimeTrackerUnrecoverableException {
    return System.currentTimeMillis() - getCurrentTimeTrackerValue();
  }

  /**
   * Gets the current value in the time tracker file.
   *
   * @throws IOException if unable to read file
   * @throws CorruptedTimeTrackerException if one of the tracking files is corrupted
   * @throws TimeTrackerUnrecoverableException if both time tracking files are corrupted
   * @return time tracker file value
   */
  public static long getCurrentTimeTrackerValue()
      throws IOException, CorruptedTimeTrackerException, TimeTrackerUnrecoverableException {
    String readFile;
    long startTimeTrackerMsLong = 0;
    if (isFile1CurrTimeTrackerFile) {
      readFile = timeMarkerFile1Name;
    } else {
      readFile = timeMarkerFile2Name;
    }

    try {
      String startTimeTrackerMs = FileAccessManager.readFileToString(readFile);
      startTimeTrackerMsLong = Long.parseLong(startTimeTrackerMs);
    } catch (Exception e) {
      initTimeTrackerFiles();
    }
    return startTimeTrackerMsLong;
  }

  /**
   * Get the historical log data for all tag groups, as a list of data points, within the next FIFO
   * queue time span. The operations performed in this method consume a significant amount of memory
   * and it is recommended that the Ewon Flexy Java heap size be increased to 25M (25 MB) or
   * greater. Failure to do so may result in slow performance or unexpected behavior.<br>
   * (Parameterized list type: List&lt;DataPoint&gt;)
   *
   * @param startNewTimeTracker if new time tracker should be generated, not read from storage
   * @return historical log data
   * @throws IOException if unable to read or write files
   * @throws TimeTrackerUnrecoverableException if both time tracking files are corrupted
   * @throws CorruptedTimeTrackerException if the current time tracking file is corrupted
   * @throws JSONException if unable to parse int to string enumeration file
   * @throws CircularizedFileException if circularized file exception was found
   * @throws EbdTimeoutException for EBD timeouts
   * @throws DiagnosticTagConfigurationException if unable to configure diagnostic tags
   * @throws DiagnosticTagUpdateException if unable to update diagnostic tags
   * @throws Exception for errors related to getting fields of {@link DataPoint} objects
   */
  public static synchronized ArrayList getFifoNextSpanDataAllGroups(boolean startNewTimeTracker)
      throws IOException,
          TimeTrackerUnrecoverableException,
          CorruptedTimeTrackerException,
          JSONException,
          CircularizedFileException,
          EbdTimeoutException,
          DiagnosticTagConfigurationException,
          DiagnosticTagUpdateException,
          Exception {
    final boolean includeTagGroupA = true;
    final boolean includeTagGroupB = true;
    final boolean includeTagGroupC = true;
    final boolean includeTagGroupD = true;
    return getFifoNextSpanData(
        startNewTimeTracker,
        includeTagGroupA,
        includeTagGroupB,
        includeTagGroupC,
        includeTagGroupD);
  }

  /**
   * Get the historical log data for all tag groups, as a map of rounded timestamps to lists of data
   * points, within the next FIFO queue time span. The operations performed in this method consume a
   * significant amount of memory and it is recommended that the Ewon Flexy Java heap size be
   * increased to 25M (25 MB) or greater. Failure to do so may result in slow performance or
   * unexpected behavior.<br>
   * (Parameterized map type: Map&lt;Date, List&lt;DataPoint&gt;&gt;)
   *
   * @param startNewTimeTracker if new time tracker should be generated, not read from storage
   * @param timeSpan time span to round data point time stamps to
   * @return historical log data
   * @throws IOException if unable to read or write files
   * @throws TimeTrackerUnrecoverableException if both time tracking files are corrupted
   * @throws CorruptedTimeTrackerException if the current time tracking file is corrupted
   * @throws JSONException if unable to parse int to string enumeration file
   * @throws CircularizedFileException if circularized file exception was found
   * @throws EbdTimeoutException for EBD timeouts
   * @throws DiagnosticTagConfigurationException if unable to configure diagnostic tags
   * @throws DiagnosticTagUpdateException if unable to update diagnostic tags
   * @throws Exception for errors related to getting fields of {@link DataPoint} objects
   */
  public static synchronized Map getFifoNextSpanDataAllGroups(
      boolean startNewTimeTracker, SCTimeSpan timeSpan)
      throws IOException,
          TimeTrackerUnrecoverableException,
          CorruptedTimeTrackerException,
          JSONException,
          CircularizedFileException,
          EbdTimeoutException,
          DiagnosticTagConfigurationException,
          DiagnosticTagUpdateException,
          Exception {
    final boolean includeTagGroupA = true;
    final boolean includeTagGroupB = true;
    final boolean includeTagGroupC = true;
    final boolean includeTagGroupD = true;
    return getFifoNextSpanData(
        startNewTimeTracker,
        includeTagGroupA,
        includeTagGroupB,
        includeTagGroupC,
        includeTagGroupD,
        timeSpan);
  }

  /**
   * Determine which of the time tracking files is the current file. Recover from corrupted state if
   * necessary. This function should be called before the isFile1CurrTimeTrackerFile flag is first
   * used.
   *
   * @throws TimeTrackerUnrecoverableException if both time tracking files are corrupted
   * @throws IOException if unable to read or write files
   * @throws CorruptedTimeTrackerException if the one of the tracking files is corrupted
   */
  private static void initTimeTrackerFiles()
      throws TimeTrackerUnrecoverableException, IOException, CorruptedTimeTrackerException {
    hasInitTime = true;
    String startTimeTrackerMsMain;
    String startTimeTrackerMsFallBack;
    long file1Time = 0;
    long file2Time = 0;
    boolean file1TimeFailed = false;
    boolean file2TimeFailed = false;
    final String errorMessageBoth = "Time tracker is unrecoverable.";
    final String errorMessageSingle = "Current time tracker corrupted, using backup.";

    try {
      startTimeTrackerMsMain = FileAccessManager.readFileToString(timeMarkerFile1Name);
      file1Time = Long.parseLong(startTimeTrackerMsMain);
    } catch (Exception e) {
      file1TimeFailed = true;
    }
    try {
      startTimeTrackerMsFallBack = FileAccessManager.readFileToString(timeMarkerFile2Name);
      file2Time = Long.parseLong(startTimeTrackerMsFallBack);
    } catch (Exception e) {
      file2TimeFailed = true;
    }

    if (file1TimeFailed && file2TimeFailed) {
      writeNewTime(timeMarkerFile1Name);
      writeNewTime(timeMarkerFile2Name);
      throw new TimeTrackerUnrecoverableException(errorMessageBoth);
    } else if (file1TimeFailed) {
      isFile1CurrTimeTrackerFile = false;
      throw new CorruptedTimeTrackerException(errorMessageSingle);
    } else if (file2TimeFailed) {
      isFile1CurrTimeTrackerFile = true;
      throw new CorruptedTimeTrackerException(errorMessageSingle);
    } else {
      // Both files are in a good state, compare for more recent time.
      isFile1CurrTimeTrackerFile = file1Time > file2Time;
    }
  }

  /**
   * Retrieve the start time if it exists, otherwise create a new start time and return the new
   * start time.
   *
   * @param startNewTimeTracker set to true if a new time tracker file is needed.
   * @param readFile the file path of the time tracking file to read from
   * @param writeFile the file path of the time tracking file to write to
   * @return the long representation of the stored time
   * @throws IOException if unable to read or write files
   * @throws CorruptedTimeTrackerException if one of the tracking files is corrupted
   * @throws TimeTrackerUnrecoverableException if both time tracking files are corrupted
   */
  private static long getStartTime(boolean startNewTimeTracker, String readFile, String writeFile)
      throws IOException, CorruptedTimeTrackerException, TimeTrackerUnrecoverableException {
    long startTimeTrackerMsLong = 0;
    long enforceMaxGetsBehindMs = System.currentTimeMillis() - maxQueueGetsBehindMs;
    String startTimeTrackerMs;
    if (startNewTimeTracker) {
      startTimeTrackerMsLong = writeNewTime(writeFile);
    } else {
      try {
        startTimeTrackerMs = FileAccessManager.readFileToString(readFile);
        startTimeTrackerMsLong = Long.parseLong(startTimeTrackerMs);
      } catch (Exception e) {
        initTimeTrackerFiles();
      }
    }
    // Here we enforce the cannot get behind maxQueueGetsBehind value if not disabled
    if (maxQueueGetsBehindMs != DISABLED_MAX_HIST_FIFO_GET_BEHIND_MS
        && enforceMaxGetsBehindMs > startTimeTrackerMsLong) {
      startTimeTrackerMsLong = enforceMaxGetsBehindMs;
    }
    return startTimeTrackerMsLong;
  }

  /**
   * Write out a new start time to the time tracking file.
   *
   * @param writeFile the file path of the time tracking file to write to
   * @return the value written out to the time tracking file.
   * @throws IOException if unable to read or write files
   */
  private static long writeNewTime(String writeFile) throws IOException {
    long startTimeTrackerMsLong = System.currentTimeMillis();
    String startTimeTrackerMs = Long.toString(startTimeTrackerMsLong);
    FileAccessManager.writeStringToFile(writeFile, startTimeTrackerMs);
    return startTimeTrackerMsLong;
  }

  /**
   * Get the historical log data, as a list of data points, for the specified tag groups within the
   * next FIFO queue time span. The operations performed in this method consume a significant amount
   * of memory and it is recommended that the Ewon Flexy Java heap size be increased to 20M or
   * greater. Failure to do so may result in slow performance or unexpected behavior. <br>
   * (Parameterized list type: List&lt;DataPoint&gt;)
   *
   * @param startNewTimeTracker if new time tracker should be generated, not read from storage
   * @param includeTagGroupA if tag group A data should be included
   * @param includeTagGroupB if tag group B data should be included
   * @param includeTagGroupC if tag group C data should be included
   * @param includeTagGroupD if tag group D data should be included
   * @return historical log data
   * @throws IOException if unable to read or write files
   * @throws TimeTrackerUnrecoverableException if both time tracking files are corrupted
   * @throws CorruptedTimeTrackerException one of the tracking files is corrupted
   * @throws JSONException if unable to parse int to string enumeration file
   * @throws EbdTimeoutException when EBD call times out
   * @throws CircularizedFileException if circularized file exception was found
   * @throws DiagnosticTagConfigurationException if unable to configure diagnostic tags
   * @throws DiagnosticTagUpdateException if unable to update diagnostic tags
   * @throws Exception for errors related to getting fields of {@link DataPoint} objects
   */
  public static synchronized ArrayList getFifoNextSpanData(
      boolean startNewTimeTracker,
      boolean includeTagGroupA,
      boolean includeTagGroupB,
      boolean includeTagGroupC,
      boolean includeTagGroupD)
      throws IOException,
          TimeTrackerUnrecoverableException,
          CorruptedTimeTrackerException,
          JSONException,
          EbdTimeoutException,
          CircularizedFileException,
          DiagnosticTagConfigurationException,
          DiagnosticTagUpdateException,
          Exception {
    SCTimeSpan timeSpanNoRoundingEnabled = null;
    return (ArrayList)
        getFifoNextSpanDataRaw(
            startNewTimeTracker,
            includeTagGroupA,
            includeTagGroupB,
            includeTagGroupC,
            includeTagGroupD,
            timeSpanNoRoundingEnabled);
  }

  /**
   * Get the historical log data, as a map of rounded timestamps to lists of data points, for the
   * specified tag groups within the next FIFO queue time span. The operations performed in this
   * method consume a significant amount of memory and it is recommended that the Ewon Flexy Java
   * heap size be increased to 20M or greater. Failure to do so may result in slow performance or
   * unexpected behavior. <br>
   * (Parameterized map type: Map&lt;Date, List&lt;DataPoint&gt;&gt;)
   *
   * @param startNewTimeTracker if new time tracker should be generated, not read from storage
   * @param includeTagGroupA if tag group A data should be included
   * @param includeTagGroupB if tag group B data should be included
   * @param includeTagGroupC if tag group C data should be included
   * @param includeTagGroupD if tag group D data should be included
   * @param timeSpan time span to round data point time stamps to
   * @return historical log data
   * @throws IOException if unable to read or write files
   * @throws TimeTrackerUnrecoverableException if both time tracking files are corrupted
   * @throws CorruptedTimeTrackerException one of the tracking files is corrupted
   * @throws JSONException if unable to parse int to string enumeration file
   * @throws EbdTimeoutException when EBD call times out
   * @throws CircularizedFileException if circularized file exception was found
   * @throws DiagnosticTagConfigurationException if unable to configure diagnostic tags
   * @throws DiagnosticTagUpdateException if unable to update diagnostic tags
   * @throws Exception for errors related to getting fields of {@link DataPoint} objects
   */
  public static synchronized Map getFifoNextSpanData(
      boolean startNewTimeTracker,
      boolean includeTagGroupA,
      boolean includeTagGroupB,
      boolean includeTagGroupC,
      boolean includeTagGroupD,
      SCTimeSpan timeSpan)
      throws IOException,
          TimeTrackerUnrecoverableException,
          CorruptedTimeTrackerException,
          JSONException,
          EbdTimeoutException,
          CircularizedFileException,
          DiagnosticTagConfigurationException,
          DiagnosticTagUpdateException,
          Exception {
    return (Map)
        getFifoNextSpanDataRaw(
            startNewTimeTracker,
            includeTagGroupA,
            includeTagGroupB,
            includeTagGroupC,
            includeTagGroupD,
            timeSpan);
  }

  /**
   * Get the historical log data for the specified tag groups within the next FIFO queue time span.
   * The operations performed in this method consume a significant amount of memory and it is
   * recommended that the Ewon Flexy Java heap size be increased to 20M or greater. Failure to do so
   * may result in slow performance or unexpected behavior. <br>
   * If rounding is performed (a time unit is specified), the returned data will be rounded to the
   * specified time unit, and returned as a map of rounded timestamps to lists of data points. <br>
   * If rounding is not performed (no time unit specified), the returned data will be returned as a
   * list of data points. <br>
   * (Parameterized list type: List&lt;DataPoint&gt;) (Parameterized map type: Map&lt;Date,
   * List&lt;DataPoint&gt;&gt;)
   *
   * @param startNewTimeTracker if new time tracker should be generated, not read from storage
   * @param includeTagGroupA if tag group A data should be included
   * @param includeTagGroupB if tag group B data should be included
   * @param includeTagGroupC if tag group C data should be included
   * @param includeTagGroupD if tag group D data should be included
   * @param timeSpan time span to round data point time stamps to. If null, no rounding will be
   *     performed.
   * @return historical log data
   * @throws IOException if unable to read or write files
   * @throws TimeTrackerUnrecoverableException if both time tracking files are corrupted
   * @throws CorruptedTimeTrackerException one of the tracking files is corrupted
   * @throws JSONException if unable to parse int to string enumeration file
   * @throws EbdTimeoutException when EBD call times out
   * @throws CircularizedFileException if circularized file exception was found
   * @throws DiagnosticTagConfigurationException if unable to configure diagnostic tags
   * @throws DiagnosticTagUpdateException if unable to update diagnostic tags
   * @throws Exception for errors related to getting fields of {@link DataPoint} objects
   */
  private static synchronized Object getFifoNextSpanDataRaw(
      boolean startNewTimeTracker,
      boolean includeTagGroupA,
      boolean includeTagGroupB,
      boolean includeTagGroupC,
      boolean includeTagGroupD,
      SCTimeSpan timeSpan)
      throws IOException,
          TimeTrackerUnrecoverableException,
          CorruptedTimeTrackerException,
          JSONException,
          EbdTimeoutException,
          CircularizedFileException,
          DiagnosticTagConfigurationException,
          DiagnosticTagUpdateException,
          Exception {

    if (!hasInitTime) {
      initTimeTrackerFiles();
    }

    // Configure diagnostic tag manager if it should be enabled and is not already configured
    if (enableDiagnosticTags && !HistoricalDataQueueDiagnosticTagManager.isConfigured()) {
      try {
        HistoricalDataQueueDiagnosticTagManager.configureQueueDiagnosticTags();
      } catch (Exception e) {
        throw new DiagnosticTagConfigurationException(
            "Unable to configure historical data queue diagnostic tags!", e);
      }
    }

    // If diagnostic tags are enabled, reset if requested (and not already requested)
    boolean startNewTimeTrackerFinal = startNewTimeTracker;
    if (enableDiagnosticTags
        && HistoricalDataQueueDiagnosticTagManager.isQueueDiagnosticTagForceResetRequested()) {
      startNewTimeTrackerFinal = true;
      queuePollCount = 0;
    }

    // Increment poll count
    queuePollCount++;

    // Get start time from file, or start new time tracker if startNewTimeTracker is true.
    long startTimeTrackerMsLong = getTrackingStartTime(startNewTimeTrackerFinal);

    /*
     * Calculate end time from start time + time span. Use current time if calculated
     * end time is in the future.
     */
    long startTimeTrackerMsPlusSpan = startTimeTrackerMsLong + getQueueFifoTimeSpanMillis();
    long endTimeTrackerMsLong = Math.min(startTimeTrackerMsPlusSpan, System.currentTimeMillis());

    ArrayList queueDataList = null;
    Map queueDataMap = null;

    // Check to see if rapid catch up should be enabled
    if (RapidCatchUp.shouldEnterRapidCatchUpMode(lastReadDataPointsEmpty, endTimeTrackerMsLong)) {

      boolean stringHistorical = false;
      RapidCatchUpTracker catchUpResult =
          RapidCatchUp.rapidCatchUpRequest(
              startTimeTrackerMsLong,
              includeTagGroupA,
              includeTagGroupB,
              includeTagGroupC,
              includeTagGroupD,
              stringHistorical);

      if (catchUpResult.isHistoricalTrackingCaughtUp()) {
        // If historical tracking is caught up, set lastReadDataPointsEmpty to false
        lastReadDataPointsEmpty = false;
      }

      // Regardless of caught up status, update end time
      endTimeTrackerMsLong = catchUpResult.getTrackingEndTimeMilliseconds();

      // Initialize empty queueDataList or queueDataMap
      if (timeSpan != null) {
        queueDataMap = new HashMap();
      } else {
        queueDataList = new ArrayList();
      }
    } else {

      // Calculate EBD start and end time
      final String ebdStartTime = convertToEBDTimeFormat(startTimeTrackerMsLong);
      final String ebdEndTime = convertToEBDTimeFormat(endTimeTrackerMsLong);

      // Run standard EBD export call (int, float, ...)
      boolean stringHistorical = false;

      final long startOfEbdHistoricalReadMs = System.currentTimeMillis();

      // Read historical data into queueDataList or queueDataMap
      if (timeSpan != null) {
        queueDataMap =
            HistoricalDataManager.readHistoricalFifo(
                ebdStartTime,
                ebdEndTime,
                includeTagGroupA,
                includeTagGroupB,
                includeTagGroupC,
                includeTagGroupD,
                stringHistorical,
                timeSpan);
      } else {
        queueDataList =
            HistoricalDataManager.readHistoricalFifo(
                ebdStartTime,
                ebdEndTime,
                includeTagGroupA,
                includeTagGroupB,
                includeTagGroupC,
                includeTagGroupD,
                stringHistorical);
      }

      // Run string EBD export call if enabled
      if (stringHistoryEnabled) {
        stringHistorical = true;

        if (queueDataMap != null) {
          Map queueStringDataMap =
              HistoricalDataManager.readHistoricalFifo(
                  ebdStartTime,
                  ebdEndTime,
                  includeTagGroupA,
                  includeTagGroupB,
                  includeTagGroupC,
                  includeTagGroupD,
                  stringHistorical,
                  timeSpan);

          // Combine with standard EBD call results
          Iterator queueStringDataMapIterator = queueStringDataMap.entrySet().iterator();
          while (queueStringDataMapIterator.hasNext()) {
            Map.Entry queueStringDataMapEntry = (Map.Entry) queueStringDataMapIterator.next();
            Date queueStringDataMapKey = (Date) queueStringDataMapEntry.getKey();
            ArrayList queueStringDataMapValue = (ArrayList) queueStringDataMapEntry.getValue();

            // Check if queue data map contains the key
            if (queueDataMap.containsKey(queueStringDataMapKey)) {
              // Get the data points list at the key
              ArrayList dataPointsListAtKey = (ArrayList) queueDataMap.get(queueStringDataMapKey);

              // Add the data points list at the key
              dataPointsListAtKey.addAll(queueStringDataMapValue);
            } else {
              // Add the data points list at the key
              queueDataMap.put(queueStringDataMapKey, queueStringDataMapValue);
            }
          }
        } else {
          ArrayList queueStringDataList =
              HistoricalDataManager.readHistoricalFifo(
                  ebdStartTime,
                  ebdEndTime,
                  includeTagGroupA,
                  includeTagGroupB,
                  includeTagGroupC,
                  includeTagGroupD,
                  stringHistorical);

          // Combine with standard EBD call results
          queueDataList.addAll(queueStringDataList);
        }
      }

      // Check for Circularized Event
      if (CircularizedFileCheck.didFileCircularizedEventOccurSinceAbsolute(
          startOfEbdHistoricalReadMs)) {
        throw new CircularizedFileException("A circularized event was found in the event logs.");
      }

      // Set the lastReadDataPointsEmpty if size is zero or less
      if (queueDataMap != null) {
        lastReadDataPointsEmpty = queueDataMap.isEmpty();
      } else {
        lastReadDataPointsEmpty = queueDataList.isEmpty();
      }
    }

    // Store end time +1 ms (to prevent duplicate data)
    long nextStartTimeTrackerMsLong = endTimeTrackerMsLong + 1;
    updateTrackingStartTime(nextStartTimeTrackerMsLong);

    // Update diagnostic tags (if they are enabled)
    if (enableDiagnosticTags) {
      // Get running behind time (and set to 0 if threshold not met)
      long newRunningBehindTimeMsValue = System.currentTimeMillis() - nextStartTimeTrackerMsLong;
      if (newRunningBehindTimeMsValue
          < SCTimeUnit.SECONDS.toMillis(queueRunningBehindTimeTagDisplayThresholdSeconds)) {
        newRunningBehindTimeMsValue = 0;
      }

      // Always set reset queue diagnostic tag to false, reset is one-time
      boolean newForceResetValue = false;

      // Update diagnostic tags
      try {
        HistoricalDataQueueDiagnosticTagManager.updateDiagnosticTags(
            newRunningBehindTimeMsValue, newForceResetValue, queuePollCount);
      } catch (Exception e) {
        throw new DiagnosticTagUpdateException(
            "Unable to update historical data queue diagnostic tags!", e);
      }
    }

    // Return data as raw object (public method stub will convert to expected type)
    return queueDataMap != null ? (Object) queueDataMap : (Object) queueDataList;
  }

  /**
   * Get the Historical FIFO starting time from the tracking files
   *
   * @param startNewTimeTracker boolean to start a new time tracker
   * @return tracking start time
   * @throws IOException if unable to read or write files
   * @throws TimeTrackerUnrecoverableException if both time tracking files are corrupted
   * @throws CorruptedTimeTrackerException one of the tracking files is corrupted
   */
  private static long getTrackingStartTime(boolean startNewTimeTracker)
      throws CorruptedTimeTrackerException, IOException, TimeTrackerUnrecoverableException {

    /*
     * The two time tracking files will swap on every iteration of grabbing new
     * data points. Set the correct file using the isFile1CurrTimeTrackerFile flag.
     */
    String readFile;
    String writeFile;
    if (isFile1CurrTimeTrackerFile) {
      readFile = timeMarkerFile1Name;
      writeFile = timeMarkerFile2Name;
    } else {
      readFile = timeMarkerFile2Name;
      writeFile = timeMarkerFile1Name;
    }

    // Get start time from file, or start new time tracker if startNewTimeTracker is true.
    return getStartTime(startNewTimeTracker, readFile, writeFile);
  }

  /**
   * Update Historical FIFO tracking time
   *
   * @param updateTime the time to be written
   * @throws IOException if unable to read or write files
   * @throws CorruptedTimeTrackerException one of the tracking files is corrupted
   */
  private static void updateTrackingStartTime(long updateTime)
      throws CorruptedTimeTrackerException, IOException {

    /*
     * The two time tracking files will swap on every iteration of grabbing new
     * data points. Set the correct file using the isFile1CurrTimeTrackerFile flag.
     */
    String writeFile;
    if (isFile1CurrTimeTrackerFile) {
      writeFile = timeMarkerFile2Name;
    } else {
      writeFile = timeMarkerFile1Name;
    }

    final String newTimeTrackerVal = Long.toString(updateTime);
    FileAccessManager.writeStringToFile(writeFile, newTimeTrackerVal);

    isFile1CurrTimeTrackerFile = !isFile1CurrTimeTrackerFile;
  }

  /**
   * Advance Historical FIFO tracking time. The intention is for exception handlers to make the
   * decision to advance time.
   *
   * @throws IOException if unable to read or write files
   * @throws TimeTrackerUnrecoverableException if both time tracking files are corrupted
   * @throws CorruptedTimeTrackerException one of the tracking files is corrupted
   */
  public static void advanceTrackingStartTime()
      throws CorruptedTimeTrackerException, IOException, TimeTrackerUnrecoverableException {

    long startTimeTrackerMsLong = getTrackingStartTime(false);

    /*
     * Calculate end time from start time + time span. Use current time if calculated
     * end time is in the future.
     */
    long startTimeTrackerMsPlusSpan = startTimeTrackerMsLong + getQueueFifoTimeSpanMillis();
    long endTimeTrackerMsLong = Math.min(startTimeTrackerMsPlusSpan, System.currentTimeMillis());

    updateTrackingStartTime(endTimeTrackerMsLong);
  }
}
