package com.hms_networks.americas.sc.extensions.localdatafiles;

import com.hms_networks.americas.sc.extensions.datapoint.DataPoint;
import com.hms_networks.americas.sc.extensions.datapoint.DataQuality;
import com.hms_networks.americas.sc.extensions.datapoint.DataType;
import com.hms_networks.americas.sc.extensions.fileutils.FileAccessManager;
import com.hms_networks.americas.sc.extensions.fileutils.FileManager;
import com.hms_networks.americas.sc.extensions.logging.Logger;
import com.hms_networks.americas.sc.extensions.system.time.SCTimeFormat;
import com.hms_networks.americas.sc.extensions.taginfo.TagInfoManager;
import com.hms_networks.americas.sc.extensions.taginfo.TagType;
import com.hms_networks.americas.sc.extensions.time.LocalTimeOffsetCalculator;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Class that manages the naming and storage of local data files containing reports of processed
 * data points and alarms.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.0.0
 */
public class LocalDataFileManager extends Thread {

  /**
   * Root folder where the local data file management folder structure and files are created and
   * stored.
   */
  private final String localDataFileRootFolder;

  /** The number of months to retain local data files before deletion. */
  private final int retainNumberMonths;

  /**
   * The interval at which the local data file buffer is written to the appropriate file on disk by
   * the local data file functionality.
   */
  private final long fileWriteIntervalMillis;

  /** Boolean flag that controls the loop in the local data file manager thread. */
  private boolean doProcessing = true;

  /**
   * String buffer which is used for output that is written to local data files during loops in
   * {@link #run()}.
   */
  private StringBuffer localDataFileWriteBuffer = new StringBuffer();

  /**
   * Timestamp for the last time a call to {@link #deleteOutdatedFiles()} was made. The initial
   * value is 0 to force a call to {@link #deleteOutdatedFiles()} at each connector startup.
   */
  private long lastDeleteOutdatedFilesTimeMillis = 0;

  /**
   * Instantiates a local data file management class with the specified local data file root folder
   * and number of months to retain files. NOTE: This class requires the local time offset provided
   * by LocalTimeOffsetCalculator to provide accurate timestamps. You must call
   * LocalTimeOffsetCalculator#calculateLocalTimeOffsetMilliseconds() prior to instantiating this
   * class.
   *
   * @param localDataFileRootFolder folder where local data file structure will be created/stored
   * @param retainNumberMonths number of months of local data files to retain prior to deletion
   * @param fileWriteIntervalMillis interval which the local data file buffer is written to the
   *     filesystem
   */
  public LocalDataFileManager(
      String localDataFileRootFolder, int retainNumberMonths, long fileWriteIntervalMillis) {
    this.localDataFileRootFolder = localDataFileRootFolder;
    this.retainNumberMonths = retainNumberMonths;
    this.fileWriteIntervalMillis = fileWriteIntervalMillis;
    start();
  }

  /**
   * Gets the file path of the data file for the current time.
   *
   * @return data file path
   */
  private String getCurrentDataFilePath() {
    // Get current date/time
    Calendar calendar = Calendar.getInstance();
    long localTimeMillis =
        System.currentTimeMillis() - LocalTimeOffsetCalculator.getLocalTimeOffsetMilliseconds();
    calendar.setTimeInMillis(localTimeMillis);

    // Get date sections (add 1 to month - based on 0)
    String year = String.valueOf(calendar.get(Calendar.YEAR));
    String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
    String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));

    // Pad month, day and hour with leading zero if only one digit
    month = month.length() == 1 ? "0" + month : month;
    day = day.length() == 1 ? "0" + day : day;
    hour = hour.length() == 1 ? "0" + hour : hour;

    return localDataFileRootFolder
        + "/"
        + year
        + "/"
        + month
        + "/"
        + day
        + "/"
        + LocalDataFileConstants.LOCAL_DATA_FILE_NAME_PREFIX
        + year
        + month
        + day
        + hour
        + LocalDataFileConstants.LOCAL_DATA_FILE_EXTENSION;
  }

  /**
   * Stops local data file management and returns when the local data file management thread has
   * terminated.
   *
   * @throws InterruptedException if unable to wait for local data file management thread to
   *     terminates
   */
  public void stopLocalDataFileManagement() throws InterruptedException {
    doProcessing = false;
    join();
  }

  /**
   * Deletes outdated local data files from the file system. The configured value of {@link
   * #retainNumberMonths} is subtracted from the current month to get the first month which will be
   * deleted.
   */
  private void deleteOutdatedFiles() {
    // Get current date/time
    Calendar calendar = Calendar.getInstance();
    long localTimeMillis =
        System.currentTimeMillis() - LocalTimeOffsetCalculator.getLocalTimeOffsetMilliseconds();
    calendar.setTimeInMillis(localTimeMillis);

    // Get date sections (add 1 to month - based on 0)
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH) + 1;

    /*
     * Subtract AZConnectorConsts.LOCAL_DATA_FILE_DELETE_AFTER_MONTHS from current date to get month to begin deleting.
     */
    for (int x = 0; x <= retainNumberMonths; x++) {
      month--;
      if (month == 0) {
        month = LocalDataFileConstants.NUM_MONTHS_PER_YEAR;
        year--;
      }
    }

    /*
     * Delete up to AZConnectorConsts.LOCAL_DATA_FILE_DELETE_NUM_MONTHS months of outdated data
     */
    List yearFolderPathsToCleanup = new ArrayList();
    for (int x = 0; x <= LocalDataFileConstants.LOCAL_DATA_FILE_MAX_MONTHS_DELETE; x++) {
      // Get month as string and pad with leading zero if only one digit
      String monthString = String.valueOf(month);
      String paddedMonth = monthString.length() == 1 ? "0" + monthString : monthString;

      // Get folder name to delete
      String folderToDeletePath = localDataFileRootFolder + "/" + year + "/" + paddedMonth;

      // Get file object
      File folderToDelete = new File(folderToDeletePath);

      // Delete folder contents and folder
      FileManager.recursivelyDeleteFileFolder(folderToDelete);

      // Subtract month to get next to delete
      month--;
      if (month == 0) {
        month = LocalDataFileConstants.NUM_MONTHS_PER_YEAR;
        year--;
        yearFolderPathsToCleanup.add(localDataFileRootFolder + "/" + year);
      }
    }

    // Delete outdated year folders (if any)
    for (int x = 0; x < yearFolderPathsToCleanup.size(); x++) {
      String yearFolderToDeletePath = (String) yearFolderPathsToCleanup.get(x);
      File yearFolderToDelete = new File(yearFolderToDeletePath);
      yearFolderToDelete.delete();
    }

    // Store last run time
    lastDeleteOutdatedFilesTimeMillis = System.currentTimeMillis();
  }

  /**
   * Gets the file header for local data files.
   *
   * @return local data file header
   */
  private String getDataFileHeader() {
    final int lastDelimiterIndex = LocalDataFileConstants.LOCAL_DATA_FILE_COLUMN_HEADERS.length - 1;

    // Append each column header
    StringBuffer headerStringBuffer = new StringBuffer();
    for (int headerIndex = 0;
        headerIndex < LocalDataFileConstants.LOCAL_DATA_FILE_COLUMN_HEADERS.length;
        headerIndex++) {
      // Write current column header
      headerStringBuffer.append(LocalDataFileConstants.LOCAL_DATA_FILE_COLUMN_HEADERS[headerIndex]);

      // Append delimiter unless last element
      if (headerIndex < lastDelimiterIndex) {
        headerStringBuffer.append(LocalDataFileConstants.LOCAL_DATA_FILE_DELIMITER);
      }
    }

    // Append line separator
    headerStringBuffer.append(LocalDataFileConstants.LOCAL_DATA_FILE_LINE_SEPARATOR);

    return headerStringBuffer.toString();
  }

  /**
   * Appends each data point in the specified list to the buffer which is written to local data file
   * storage every {@link #fileWriteIntervalMillis} milliseconds.
   *
   * @param dataPoints list of data points to add to local data file
   * @param overwrite boolean indicating if an existing data point (oldest) should be overwritten
   *     with each of the specified data points
   */
  public synchronized void addAllDataPoints(List dataPoints, boolean overwrite) {
    Iterator dataPointsIterator = dataPoints.iterator();
    while (dataPointsIterator.hasNext()) {
      DataPoint currentDataPoint = (DataPoint) dataPointsIterator.next();
      addDataPoint(currentDataPoint, overwrite);
    }
  }

  /**
   * Appends the specified data point to the buffer which is written to local data file storage
   * every {@link #fileWriteIntervalMillis} milliseconds.
   *
   * @param dataPoint data point to add to local data file
   * @param overwrite boolean indicating if an existing data point (oldest) should be overwritten
   *     with the specified data point
   */
  public synchronized void addDataPoint(DataPoint dataPoint, boolean overwrite) {
    // Trim outdated data point if required
    if (overwrite) {
      // Delete the first line (most outdated data point)
      final int indexNotFound = -1;
      final int firstCharacterIndex = 0;
      final int endOfFirstLineIndex =
          localDataFileWriteBuffer.indexOf(LocalDataFileConstants.LOCAL_DATA_FILE_LINE_SEPARATOR);
      if (endOfFirstLineIndex != indexNotFound) {
        localDataFileWriteBuffer.delete(firstCharacterIndex, endOfFirstLineIndex);
      }
    }

    // Get datapoint UTC time as long
    long localDataPointTime =
        Long.parseLong(dataPoint.getTimeStamp())
            * LocalDataFileConstants.NUM_MILLISECONDS_PER_SECOND;
    long utcDataPointTime =
        localDataPointTime + LocalTimeOffsetCalculator.getLocalTimeOffsetMilliseconds();

    // Get local time from datapoint UTC time
    String utcTimestamp = SCTimeFormat.ISO_8601.format(new Date(utcDataPointTime));
    String localTimestamp = SCTimeFormat.ISO_8601.format(new Date(localDataPointTime));

    // Append UTC time and delimiter
    localDataFileWriteBuffer.append(utcTimestamp);
    localDataFileWriteBuffer.append(LocalDataFileConstants.LOCAL_DATA_FILE_DELIMITER);

    // Append local time and delimiter
    localDataFileWriteBuffer.append(localTimestamp);
    localDataFileWriteBuffer.append(LocalDataFileConstants.LOCAL_DATA_FILE_DELIMITER);

    // Append row type and delimiter
    localDataFileWriteBuffer.append(LocalDataFileConstants.LOCAL_DATA_FILE_ROW_TYPE_DATA);
    localDataFileWriteBuffer.append(LocalDataFileConstants.LOCAL_DATA_FILE_DELIMITER);

    // Append tag name and delimiter
    localDataFileWriteBuffer.append(dataPoint.getTagName());
    localDataFileWriteBuffer.append(LocalDataFileConstants.LOCAL_DATA_FILE_DELIMITER);

    // Append tag type and delimiter
    localDataFileWriteBuffer.append(getDataTypeAsString(dataPoint.getType()));
    localDataFileWriteBuffer.append(LocalDataFileConstants.LOCAL_DATA_FILE_DELIMITER);

    // Append tag value and delimiter
    if (dataPoint.getType() == DataType.STRING) {
      localDataFileWriteBuffer.append("\"").append(dataPoint.getValueString()).append("\"");
    } else {
      localDataFileWriteBuffer.append(dataPoint.getValueString());
    }
    localDataFileWriteBuffer.append(LocalDataFileConstants.LOCAL_DATA_FILE_DELIMITER);

    // Append data quality and delimiter
    localDataFileWriteBuffer.append(getDataQualityAsString(dataPoint.getQuality()));
    localDataFileWriteBuffer.append(LocalDataFileConstants.LOCAL_DATA_FILE_DELIMITER);

    // Append alarm type and delimiter (not applicable, use filler)
    localDataFileWriteBuffer.append(LocalDataFileConstants.LOCAL_DATA_FILE_FILLER_VALUE);
    localDataFileWriteBuffer.append(LocalDataFileConstants.LOCAL_DATA_FILE_DELIMITER);

    // Append alarm status (not applicable, use filler)
    localDataFileWriteBuffer.append(LocalDataFileConstants.LOCAL_DATA_FILE_FILLER_VALUE);
    localDataFileWriteBuffer.append(LocalDataFileConstants.LOCAL_DATA_FILE_LINE_SEPARATOR);
  }

  /**
   * Appends the specified alarm point to the buffer which is written to local data file storage
   * every {@link #fileWriteIntervalMillis} milliseconds.
   *
   * @throws IllegalStateException when tag list has not been populated with {@link
   *     TagInfoManager#refreshTagList()}
   * @throws IndexOutOfBoundsException when (tagId - {@link TagInfoManager#getLowestTagIdSeen()}) is
   *     not an index within the TagInfo array bounds.
   * @param tagID alarmed tag ID
   * @param tagName alarmed tag name
   * @param tagValue alarmed tag value
   * @param alarmType alarm type
   * @param alarmStatus alarm status
   * @param utcTimestamp alarm timestamp (UTC)
   * @param localTimestamp alarm timestamp (local time)
   */
  public void addAlarm(
      int tagID,
      String tagName,
      String tagValue,
      String alarmType,
      String alarmStatus,
      String utcTimestamp,
      String localTimestamp)
      throws IllegalStateException, IndexOutOfBoundsException {
    // Get tag type
    TagType tagType = TagInfoManager.getTagInfoFromTagId(tagID).getType();

    // Append UTC time and delimiter
    localDataFileWriteBuffer.append(utcTimestamp);
    localDataFileWriteBuffer.append(LocalDataFileConstants.LOCAL_DATA_FILE_DELIMITER);

    // Append local time and delimiter
    localDataFileWriteBuffer.append(localTimestamp);
    localDataFileWriteBuffer.append(LocalDataFileConstants.LOCAL_DATA_FILE_DELIMITER);

    // Append row type and delimiter
    localDataFileWriteBuffer.append(LocalDataFileConstants.LOCAL_DATA_FILE_ROW_TYPE_ALARM);
    localDataFileWriteBuffer.append(LocalDataFileConstants.LOCAL_DATA_FILE_DELIMITER);

    // Append tag name and delimiter
    localDataFileWriteBuffer.append(tagName);
    localDataFileWriteBuffer.append(LocalDataFileConstants.LOCAL_DATA_FILE_DELIMITER);

    // Append tag type and delimiter
    localDataFileWriteBuffer.append(getTagTypeAsString(tagType));
    localDataFileWriteBuffer.append(LocalDataFileConstants.LOCAL_DATA_FILE_DELIMITER);

    // Append tag value and delimiter
    if (tagType == TagType.STRING) {
      localDataFileWriteBuffer.append("\"").append(tagValue).append("\"");
    } else {
      localDataFileWriteBuffer.append(tagValue);
    }
    localDataFileWriteBuffer.append(LocalDataFileConstants.LOCAL_DATA_FILE_DELIMITER);

    // Append data quality and delimiter (not applicable, use filler)
    localDataFileWriteBuffer.append(LocalDataFileConstants.LOCAL_DATA_FILE_FILLER_VALUE);
    localDataFileWriteBuffer.append(LocalDataFileConstants.LOCAL_DATA_FILE_DELIMITER);

    // Append alarm type and delimiter
    localDataFileWriteBuffer.append(alarmType);
    localDataFileWriteBuffer.append(LocalDataFileConstants.LOCAL_DATA_FILE_DELIMITER);

    // Append alarm status
    localDataFileWriteBuffer.append(alarmStatus);
    localDataFileWriteBuffer.append(LocalDataFileConstants.LOCAL_DATA_FILE_LINE_SEPARATOR);
  }

  /**
   * Gets a string representation of the specified tag type.
   *
   * @param tagType tag type object
   * @return tag type string
   */
  public static String getTagTypeAsString(TagType tagType) {
    String tagTypeString = LocalDataFileConstants.LOCAL_DATA_FILE_DATA_TYPE_UNKNOWN;
    if (tagType == TagType.BOOLEAN) {
      tagTypeString = LocalDataFileConstants.LOCAL_DATA_FILE_DATA_TYPE_BOOLEAN;
    } else if (tagType == TagType.DWORD) {
      tagTypeString = LocalDataFileConstants.LOCAL_DATA_FILE_DATA_TYPE_DWORD;
    } else if (tagType == TagType.FLOAT) {
      tagTypeString = LocalDataFileConstants.LOCAL_DATA_FILE_DATA_TYPE_FLOAT;
    } else if (tagType == TagType.INTEGER) {
      tagTypeString = LocalDataFileConstants.LOCAL_DATA_FILE_DATA_TYPE_INTEGER;
    } else if (tagType == TagType.STRING) {
      tagTypeString = LocalDataFileConstants.LOCAL_DATA_FILE_DATA_TYPE_STRING;
    }
    return tagTypeString;
  }

  /**
   * Gets a string representation of the specified data type.
   *
   * @param dataType data type object
   * @return data type string
   */
  public static String getDataTypeAsString(DataType dataType) {
    String dataTypeString = LocalDataFileConstants.LOCAL_DATA_FILE_DATA_TYPE_UNKNOWN;
    if (dataType == DataType.BOOLEAN) {
      dataTypeString = LocalDataFileConstants.LOCAL_DATA_FILE_DATA_TYPE_BOOLEAN;
    } else if (dataType == DataType.DWORD) {
      dataTypeString = LocalDataFileConstants.LOCAL_DATA_FILE_DATA_TYPE_DWORD;
    } else if (dataType == DataType.FLOAT) {
      dataTypeString = LocalDataFileConstants.LOCAL_DATA_FILE_DATA_TYPE_FLOAT;
    } else if (dataType == DataType.INTEGER) {
      dataTypeString = LocalDataFileConstants.LOCAL_DATA_FILE_DATA_TYPE_INTEGER;
    } else if (dataType == DataType.STRING) {
      dataTypeString = LocalDataFileConstants.LOCAL_DATA_FILE_DATA_TYPE_STRING;
    } else if (dataType == DataType.INTEGER_MAPPED_STRING) {
      dataTypeString = LocalDataFileConstants.LOCAL_DATA_FILE_DATA_TYPE_INTEGER_MAPPED_STRING;
    }
    return dataTypeString;
  }

  /**
   * Gets a string representation of the specified data quality.
   *
   * @param dataQuality data quality object
   * @return data quality string
   */
  public static String getDataQualityAsString(DataQuality dataQuality) {
    String dataQualityString = LocalDataFileConstants.LOCAL_DATA_FILE_QUALITY_UNKNOWN;
    if (dataQuality == DataQuality.BAD) {
      dataQualityString = LocalDataFileConstants.LOCAL_DATA_FILE_QUALITY_BAD;
    } else if (dataQuality == DataQuality.GOOD) {
      dataQualityString = LocalDataFileConstants.LOCAL_DATA_FILE_QUALITY_GOOD;
    } else if (dataQuality == DataQuality.UNCERTAIN) {
      dataQualityString = LocalDataFileConstants.LOCAL_DATA_FILE_QUALITY_UNCERTAIN;
    }
    return dataQualityString;
  }

  /** Local data file manager thread run method. */
  public void run() {
    // Loop until the doProcessing flag is changed to false
    while (doProcessing) {
      synchronized (this) {
        // Write pending data in buffer if present
        if (localDataFileWriteBuffer.length() > 0) {
          try {
            // Get current data file path and write file header if new file
            String currentDataFilePath = getCurrentDataFilePath();
            File currentDataFile = new File(currentDataFilePath);
            if (!currentDataFile.exists()) {
              FileAccessManager.appendStringToFile(currentDataFilePath, getDataFileHeader());
            }

            // Write pending data in buffer to file
            FileAccessManager.appendStringToFile(
                currentDataFilePath, localDataFileWriteBuffer.toString());

            // Clear buffer of processed data
            localDataFileWriteBuffer = new StringBuffer();

            // Request garbage collection for previous buffer
            System.gc();
          } catch (IOException e) {
            Logger.LOG_SERIOUS(
                "An error occurred while writing the data buffer to the corresponding local data"
                    + " file.");
            Logger.LOG_EXCEPTION(e);
          }
        }
      }

      // Check if need to delete outdated local data files
      final long timeSinceLastDeleteOutdatedLocalFilesMillis =
          System.currentTimeMillis() - lastDeleteOutdatedFilesTimeMillis;
      if (timeSinceLastDeleteOutdatedLocalFilesMillis
          >= LocalDataFileConstants.LOCAL_DATA_FILE_OUTDATED_FILE_DELETE_INTERVAL_MILLIS) {
        Logger.LOG_INFO("Deleting outdated data files from the local file system.");
        deleteOutdatedFiles();
      }

      // Sleep for configured local date file write interval
      if (doProcessing) {
        try {
          Thread.yield();
          Thread.sleep(fileWriteIntervalMillis);
        } catch (InterruptedException e) {
          Logger.LOG_WARN(
              "Unable to sleep the local data file write thread for the configured interval of "
                  + fileWriteIntervalMillis
                  + " milliseconds.");
          Logger.LOG_EXCEPTION(e);
        }
      }
    }
  }
}
