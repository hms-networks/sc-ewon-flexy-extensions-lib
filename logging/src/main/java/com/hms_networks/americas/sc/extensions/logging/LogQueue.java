package com.hms_networks.americas.sc.extensions.logging;

/**
 * LogQueue.java
 *
 * <p>Provides an queue mechanism for log entries which minimizes dynamic memory allocation.
 *
 * @since 1.0.0
 * @author HMS Networks, MU Americas Solution Center
 */
public class LogQueue {

  /** Constant for max string size */
  private static final int DEFAULT_STRING_MAX_SIZE_CHARS = 255;

  /** 2D char array to represent a queue of char arrays */
  private char[][] logQueue;

  /** Array containing the size of each log entry in the queue */
  private int[] logQueueLengths;

  /** Number of entries the queue can hold */
  private int queueDepth;

  /** Current tail index of queue */
  private int currTailIndex;

  /** Current number of entries in queue */
  private int currQueueSize;

  /**
   * Creates a new LogQueue
   *
   * @param queueSize number of elements the queue can hold
   */
  public LogQueue(int queueSize) {
    currQueueSize = 0;
    currTailIndex = -1;
    queueDepth = queueSize;
    logQueue = new char[queueDepth][DEFAULT_STRING_MAX_SIZE_CHARS];
    logQueueLengths = new int[queueDepth];
  }

  /**
   * Queues a new string
   *
   * @param logEntry the string to be enqueued
   */
  public void addLogEntry(String logEntry) {

    // Increment queue index
    currTailIndex++;

    // Rotate around to beginning of array if the end is reached
    if (currTailIndex >= queueDepth) {
      currTailIndex = 0;
    }

    // Increment queue size if not full
    if (currQueueSize < queueDepth) {
      currQueueSize++;
    }

    // Store the length of string
    logQueueLengths[currTailIndex] = logEntry.length();

    // Limit log entry to max size
    if (logQueueLengths[currTailIndex] > DEFAULT_STRING_MAX_SIZE_CHARS) {
      logQueueLengths[currTailIndex] = DEFAULT_STRING_MAX_SIZE_CHARS;
      logEntry = logEntry.substring(0, DEFAULT_STRING_MAX_SIZE_CHARS);
    }

    logQueue[currTailIndex] = logEntry.toCharArray();
  }

  /**
   * Method to get head string of queue
   *
   * @return the head string of the queue
   */
  public String poll() {

    String res = null;
    if (currQueueSize > 0) {
      // Compute the head index
      int headIndex = (currTailIndex + queueDepth - currQueueSize + 1) % queueDepth;

      res = String.valueOf(logQueue[headIndex]).substring(0, logQueueLengths[headIndex]);
      currQueueSize--;
    }
    return res;
  }

  /**
   * Method to check if the queue is empty
   *
   * @return true if queue is empty
   */
  public boolean isEmpty() {
    boolean res = false;
    if (currQueueSize == 0) {
      res = true;
    }
    return res;
  }

  /**
   * Method to check if the queue is fully loaded
   *
   * @return true if queue is full
   */
  public boolean isFull() {
    boolean res = false;
    if (currQueueSize == queueDepth) {
      res = true;
    }
    return res;
  }
}
