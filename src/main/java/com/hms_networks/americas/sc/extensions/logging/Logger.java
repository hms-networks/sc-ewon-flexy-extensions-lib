package com.hms_networks.americas.sc.extensions.logging;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Set;

/**
 * Logger.java
 *
 * <p>Provides an interface for managing log output using log levels.
 *
 * @since 1.0.0
 * @author HMS Networks, MU Americas Solution Center
 */
public class Logger {

  /** Constant for trace log level. */
  public static final int LOG_LEVEL_TRACE = 6;

  /** Constant for debug log level. */
  public static final int LOG_LEVEL_DEBUG = 5;

  /** Constant for info log level. */
  public static final int LOG_LEVEL_INFO = 4;

  /** Constant for warning log level. */
  public static final int LOG_LEVEL_WARN = 3;

  /** Constant for serious log level. */
  public static final int LOG_LEVEL_SERIOUS = 2;

  /** Constant for critical log level. */
  public static final int LOG_LEVEL_CRITICAL = 1;

  /** Constant for no log level. */
  public static final int LOG_LEVEL_NONE = 0;

  /** Constant for the max size (in chars) of log file. */
  private static final int MAX_CHARS_PER_FILE = 25000;

  /** Constant for the max number of log files. */
  private static final int MAX_NUM_LOG_FILES = 10;

  /** Constant for the max number of queued logs. */
  private static final int MAX_NUM_QUEUED_LOGS = 25;

  /** Constant for the log file path. */
  private static final String LOG_FILE_PATH = "/usr/";

  /** Constant for the log file name. */
  private static final String LOG_FILE_NAME = "log";

  /** Constant for the log file extension. */
  private static final String LOG_FILE_EXTENSION = ".txt";

  /** Stored logging level. Default is DEBUG. */
  private static int loggingLevel = LOG_LEVEL_DEBUG;

  /** Current log file index. */
  private static int currFileNumber = 1;

  /** Number of chars written to current log file. */
  private static long numCharsWrittenToFile = 0;

  /** Indicator if logging to realtime logs. */
  private static boolean isLoggingToRealtime = true;

  /** Indicator if logging to realtime logs. */
  private static boolean isLoggingToSocket = false;

  /** Indicator if logging to file. */
  private static boolean isLoggingToFile = false;

  /** Indicator if queuing logs. */
  private static boolean isQueueing = false;

  /** Queue of unprinted logs. */
  private static LogQueue logQueue;

  /**
   * Set for tracking whether a specific log has been outputted. This is used to prevent spamming
   * the log with the same message.
   *
   * <p>If the log has already been outputted, its key will be in the set, otherwise it will not be
   * present.
   *
   * @since 1.15.9
   */
  private static final Set logOnceList = new HashSet(); // Set<String>

  /**
   * Enable logging to Flexy's realtime logs.
   *
   * @since 1.1
   */
  public static void ENABLE_REALTIME_LOG() {
    isLoggingToRealtime = true;
  }

  /**
   * Disable logging to Flexy's realtime logs.
   *
   * @since 1.1
   */
  public static void DISABLE_REALTIME_LOG() {
    isLoggingToRealtime = false;
  }

  /**
   * Enable logging to socket connection.
   *
   * @since 1.1
   */
  public static void ENABLE_SOCKET_LOG() {
    isLoggingToSocket = true;
  }

  /**
   * Disable logging to socket connection.
   *
   * @since 1.1
   */
  public static void DISABLE_SOCKET_LOG() {
    isLoggingToSocket = false;
  }

  /**
   * Enable queuing unprinted log messages.
   *
   * @since 1.2
   */
  public static void ENABLE_LOG_QUEUE() {
    isQueueing = true;
    logQueue = new LogQueue(MAX_NUM_QUEUED_LOGS);
  }

  /**
   * Set the log level for this class.
   *
   * @param level desired log level
   * @return true if successful
   */
  public static boolean SET_LOG_LEVEL(int level) {
    boolean retval = false;

    if (level < 0) {
      isLoggingToFile = true;
      level = Math.abs(level);
    }

    if (level >= LOG_LEVEL_NONE && level <= LOG_LEVEL_TRACE) {
      loggingLevel = level;
      retval = true;

      if (isLoggingToFile) {
        /* Look for an unused log file index and set the currFileNumber to that index. */
        for (int i = 1; i <= MAX_NUM_LOG_FILES; i++) {
          try {
            File file = new File(LOG_FILE_PATH + LOG_FILE_NAME + i + LOG_FILE_EXTENSION);
            if (!file.exists()) {
              currFileNumber = i;
              break;
            }
          } catch (Exception e) {
            /*
             * Issue with access to log file so use realtime log to print error rather than
             * use Logger functionality.
             */
            System.out.println("Error: Could not access stored log files!");
            e.printStackTrace();
          }
        }

        /* Delete the next file in the log entries. This marks where the application left off. */
        int nextFileNumber = currFileNumber + 1;

        if (nextFileNumber > MAX_NUM_LOG_FILES) {
          nextFileNumber = 1;
        }

        try {
          File nextLogFile =
              new File(LOG_FILE_PATH + LOG_FILE_NAME + nextFileNumber + LOG_FILE_EXTENSION);
          nextLogFile.delete();
        } catch (Exception e) {
          Logger.LOG_SERIOUS("Could not delete next log file in sequence!");
          Logger.LOG_EXCEPTION(e);
        }
      }

      Logger.LOG_INFO("Set logging level to " + level);
    }
    return retval;
  }

  /**
   * Log an exception (uses specified log level).
   *
   * @param level the log level to use
   * @param e exception to log
   * @since 1.15.0
   */
  private static void LOG_EXCEPTION(int level, Exception e) {
    StringWriter exceptionStringWriter = new StringWriter();
    e.printStackTrace(new PrintWriter(exceptionStringWriter));
    String exceptionAsString = exceptionStringWriter.toString();
    LOG(level, exceptionAsString);
  }

  /**
   * Log an exception (uses TRACE log level).
   *
   * @param e exception to log
   */
  public static void LOG_EXCEPTION(Exception e) {
    if (loggingLevel == LOG_LEVEL_TRACE) {
      LOG_EXCEPTION(loggingLevel, e);
    }
  }

  /**
   * Log a string using the specified log level.
   *
   * @param level desired log level
   * @param logString string to log
   */
  public static void LOG(int level, String logString) {
    if (level <= loggingLevel) {
      if (isLoggingToRealtime) {
        System.out.println(logString);
      }
      if (isLoggingToFile) {
        LOG_TO_FILE(logString);
      }
      if (isLoggingToSocket) {
        SocketLogger.LOG(logString);
      }
    } else if (isQueueing) {
      logQueue.addLogEntry(logString);
    }
  }

  public static void LOG_TO_FILE(String logString) {

    boolean isAppendEnabled = true;

    /*
     * Check if the next file should be opened.
     * If so delete the next file in the sequence holding the most out of
     * date log entries. This missing file will be used on startup to be
     * the log file written first. This maintains a linear log history of
     * the most current events.
     */
    if ((numCharsWrittenToFile + logString.length()) > MAX_CHARS_PER_FILE) {
      int nextFileNumber = currFileNumber + 2;
      currFileNumber++;

      if (nextFileNumber > MAX_NUM_LOG_FILES) {
        nextFileNumber = 1;
      }

      /* Delete the next file in the log entries. This marks where the application left off. */
      try {
        File nextLogFile =
            new File(LOG_FILE_PATH + LOG_FILE_NAME + nextFileNumber + LOG_FILE_EXTENSION);
        nextLogFile.delete();
      } catch (Exception e) {
        Logger.LOG_SERIOUS("Could not delete next log file in sequence!");
        Logger.LOG_EXCEPTION(e);
      }

      if (currFileNumber > MAX_NUM_LOG_FILES) {
        currFileNumber = 1;
      }

      /* Reset num char written counter for new file */
      numCharsWrittenToFile = 0;

      /* This will be the first line in the file so there is no need to append */
      isAppendEnabled = false;
    }

    FileWriter logFileWriter = null;
    BufferedWriter logBufferedWriter = null;

    /* Build a log entry string (timestamp + logString) */
    String formattedLogString = ("[" + System.currentTimeMillis() + "] " + logString + "\n");

    /* Write the log entry to the file */
    try {

      /* Open the log file */
      File logFile = new File(LOG_FILE_PATH + LOG_FILE_NAME + currFileNumber + LOG_FILE_EXTENSION);

      logFileWriter = new FileWriter(logFile, isAppendEnabled);
      logBufferedWriter = new BufferedWriter(logFileWriter);

      logBufferedWriter.write(formattedLogString);

      logBufferedWriter.close();
      logFileWriter.close();

      numCharsWrittenToFile += formattedLogString.length();
    } catch (IOException e) {
      /*
       * Do not attempt to call LOG_EXCEPTION() here as it could cause an infinite loop
       * due to recursion. Write stacktrace to realtime log.
       */
      e.printStackTrace();
    }
  }

  /**
   * Log a string with the debug log level.
   *
   * @param logString string to log
   */
  public static void LOG_DEBUG(String logString) {
    LOG(LOG_LEVEL_DEBUG, logString);
  }

  /**
   * Log a string with the debug log level at most once (tracked by specified log key).
   *
   * <p>The specified log key must be unique for each log event that should only be logged once. If
   * the same log key is used for multiple log events, only the first log event will be logged,
   * regardless of the message.
   *
   * @param logKey key for the log event
   * @param logString string to log
   * @since 1.15.9
   */
  public static void LOG_DEBUG_ONCE(String logKey, String logString) {
    if (!getLoggedOnce(logKey)) {
      setLoggedOnce(logKey, true);
      LOG(LOG_LEVEL_DEBUG, logString);
    }
  }

  /**
   * Log a string and exception with the debug log level.
   *
   * @param logString string to log
   * @param logException exception to log
   * @since 1.15.0
   */
  public static void LOG_DEBUG(String logString, Exception logException) {
    LOG(LOG_LEVEL_DEBUG, logString);
    LOG_EXCEPTION(LOG_LEVEL_DEBUG, logException);
  }

  /**
   * Log a string and exception with the debug log level if {@code elevateExceptionLogLevel} is
   * {@code true}, otherwise log the exception with the trace log level.
   *
   * @param logString string to log
   * @param logException exception to log
   * @param elevateExceptionLogLevel elevates the log level used for the specified exception to
   *     debug
   * @since 1.15.0
   */
  public static void LOG_DEBUG(
      String logString, Exception logException, boolean elevateExceptionLogLevel) {
    LOG(LOG_LEVEL_DEBUG, logString);
    LOG_EXCEPTION(elevateExceptionLogLevel ? LOG_LEVEL_DEBUG : LOG_LEVEL_TRACE, logException);
  }

  /**
   * Log a string with the info log level.
   *
   * @param logString string to log
   */
  public static void LOG_INFO(String logString) {
    LOG(LOG_LEVEL_INFO, logString);
  }

  /**
   * Log a string with the info log level at most once (tracked by specified log key).
   *
   * <p>The specified log key must be unique for each log event that should only be logged once. If
   * the same log key is used for multiple log events, only the first log event will be logged,
   * regardless of the message.
   *
   * @param logKey key for the log event
   * @param logString string to log
   * @since 1.15.9
   */
  public static void LOG_INFO_ONCE(String logKey, String logString) {
    if (!getLoggedOnce(logKey)) {
      setLoggedOnce(logKey, true);
      LOG(LOG_LEVEL_INFO, logString);
    }
  }

  /**
   * Log a string and exception with the info log level.
   *
   * @param logString string to log
   * @param logException exception to log
   * @since 1.15.0
   */
  public static void LOG_INFO(String logString, Exception logException) {
    LOG(LOG_LEVEL_INFO, logString);
    LOG_EXCEPTION(LOG_LEVEL_INFO, logException);
  }

  /**
   * Log a string and exception with the info log level if {@code elevateExceptionLogLevel} is
   * {@code true}, otherwise log the exception with the trace log level.
   *
   * @param logString string to log
   * @param logException exception to log
   * @param elevateExceptionLogLevel elevates the log level used for the specified exception to info
   * @since 1.15.0
   */
  public static void LOG_INFO(
      String logString, Exception logException, boolean elevateExceptionLogLevel) {
    LOG(LOG_LEVEL_INFO, logString);
    LOG_EXCEPTION(elevateExceptionLogLevel ? LOG_LEVEL_INFO : LOG_LEVEL_TRACE, logException);
  }

  /**
   * Log a string with the warning log level.
   *
   * @param logString string to log
   */
  public static void LOG_WARN(String logString) {
    LOG(LOG_LEVEL_WARN, logString);
  }

  /**
   * Log a string with the warning log level at most once (tracked by specified log key).
   *
   * <p>The specified log key must be unique for each log event that should only be logged once. If
   * the same log key is used for multiple log events, only the first log event will be logged,
   * regardless of the message.
   *
   * @param logKey key for the log event
   * @param logString string to log
   * @since 1.15.9
   */
  public static void LOG_WARN_ONCE(String logKey, String logString) {
    if (!getLoggedOnce(logKey)) {
      setLoggedOnce(logKey, true);
      LOG(LOG_LEVEL_WARN, logString);
    }
  }

  /**
   * Log a string and exception with the warning log level.
   *
   * @param logString string to log
   * @param logException exception to log
   * @since 1.15.0
   */
  public static void LOG_WARN(String logString, Exception logException) {
    LOG(LOG_LEVEL_WARN, logString);
    LOG_EXCEPTION(LOG_LEVEL_WARN, logException);
  }

  /**
   * Log a string and exception with the warning log level if {@code elevateExceptionLogLevel} is
   * {@code true}, otherwise log the exception with the trace log level.
   *
   * @param logString string to log
   * @param logException exception to log
   * @param elevateExceptionLogLevel elevates the log level used for the specified exception to warn
   * @since 1.15.0
   */
  public static void LOG_WARN(
      String logString, Exception logException, boolean elevateExceptionLogLevel) {
    LOG(LOG_LEVEL_WARN, logString);
    LOG_EXCEPTION(elevateExceptionLogLevel ? LOG_LEVEL_WARN : LOG_LEVEL_TRACE, logException);
  }

  /**
   * Log a string with the serious log level.
   *
   * @param logString string to log
   */
  public static void LOG_SERIOUS(String logString) {
    LOG(LOG_LEVEL_SERIOUS, logString);
  }

  /**
   * Log a string with the serious log level at most once (tracked by specified log key).
   *
   * <p>The specified log key must be unique for each log event that should only be logged once. If
   * the same log key is used for multiple log events, only the first log event will be logged,
   * regardless of the message.
   *
   * @param logKey key for the log event
   * @param logString string to log
   * @since 1.15.9
   */
  public static void LOG_SERIOUS_ONCE(String logKey, String logString) {
    if (!getLoggedOnce(logKey)) {
      setLoggedOnce(logKey, true);
      LOG(LOG_LEVEL_SERIOUS, logString);
    }
  }

  /**
   * Log a string and exception with the serious log level.
   *
   * @param logString string to log
   * @param logException exception to log
   * @since 1.15.0
   */
  public static void LOG_SERIOUS(String logString, Exception logException) {
    LOG(LOG_LEVEL_SERIOUS, logString);
    LOG_EXCEPTION(LOG_LEVEL_SERIOUS, logException);
  }

  /**
   * Log a string and exception with the serious log level if {@code elevateExceptionLogLevel} is
   * {@code true}, otherwise log the exception with the trace log level.
   *
   * @param logString string to log
   * @param logException exception to log
   * @param elevateExceptionLogLevel elevates the log level used for the specified exception to
   *     serious
   * @since 1.15.0
   */
  public static void LOG_SERIOUS(
      String logString, Exception logException, boolean elevateExceptionLogLevel) {
    LOG(LOG_LEVEL_SERIOUS, logString);
    LOG_EXCEPTION(elevateExceptionLogLevel ? LOG_LEVEL_SERIOUS : LOG_LEVEL_TRACE, logException);
  }

  /**
   * Log a string with the critical log level.
   *
   * @param logString string to log
   */
  public static void LOG_CRITICAL(String logString) {
    LOG(LOG_LEVEL_CRITICAL, logString);
  }

  /**
   * Log a string with the critical log level at most once (tracked by specified log key).
   *
   * <p>The specified log key must be unique for each log event that should only be logged once. If
   * the same log key is used for multiple log events, only the first log event will be logged,
   * regardless of the message.
   *
   * @param logKey key for the log event
   * @param logString string to log
   * @since 1.15.9
   */
  public static void LOG_CRITICAL_ONCE(String logKey, String logString) {
    if (!getLoggedOnce(logKey)) {
      setLoggedOnce(logKey, true);
      LOG(LOG_LEVEL_CRITICAL, logString);
    }
  }

  /**
   * Log a string and exception with the critical log level.
   *
   * @param logString string to log
   * @param logException exception to log
   * @since 1.15.0
   */
  public static void LOG_CRITICAL(String logString, Exception logException) {
    LOG(LOG_LEVEL_CRITICAL, logString);
    LOG_EXCEPTION(LOG_LEVEL_CRITICAL, logException);
  }

  /**
   * Log a string and exception with the critical log level if {@code elevateExceptionLogLevel} is
   * {@code true}, otherwise log the exception with the trace log level.
   *
   * @param logString string to log
   * @param logException exception to log
   * @param elevateExceptionLogLevel elevates the log level used for the specified exception to
   *     critical
   * @since 1.15.0
   */
  public static void LOG_CRITICAL(
      String logString, Exception logException, boolean elevateExceptionLogLevel) {
    LOG(LOG_LEVEL_CRITICAL, logString);
    LOG_EXCEPTION(elevateExceptionLogLevel ? LOG_LEVEL_CRITICAL : LOG_LEVEL_TRACE, logException);
  }

  /** Output all queued logs at critical level. */
  public static void DUMP_LOG_QUEUE() {
    while (!logQueue.isEmpty()) {
      LOG(LOG_LEVEL_CRITICAL, logQueue.poll());
    }
  }

  /**
   * Sets whether a specific log has been outputted. This is used to prevent spamming the log with
   * the same message.
   *
   * <p>The specified log key must be unique for each log event that should only be logged once. If
   * the same log key is used for multiple log events, only the first log event will be logged,
   * regardless of the message.
   *
   * @param logKey key for the log event
   * @param logOutputted boolean representing whether the log has been output (true) or not (false)
   * @since 1.15.9
   */
  public static void setLoggedOnce(String logKey, boolean logOutputted) {
    if (logOutputted) {
      logOnceList.add(logKey);
    } else {
      logOnceList.remove(logKey);
    }
  }

  /**
   * Gets a boolean indicating whether a specific log has been outputted. This is used to prevent
   * spamming the log with the same message.
   *
   * <p>The specified log key must be unique for each log event that should only be logged once. If
   * the same log key is used for multiple log events, only the first log event will be logged,
   * regardless of the message.
   *
   * @param logKey key for the log event
   * @return {@code true} if the log has been outputted, {@code false} otherwise
   * @since 1.15.9
   */
  public static boolean getLoggedOnce(String logKey) {
    return logOnceList.contains(logKey);
  }
}
