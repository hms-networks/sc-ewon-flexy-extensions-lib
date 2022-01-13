package com.hms_networks.americas.sc.extensions.logging;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.Connector;
import javax.microedition.io.ServerSocketConnection;
import javax.microedition.io.SocketConnection;

/**
 * SocketLogger.java
 *
 * <p>Provides an interface for logging via socket connection.
 *
 * @since 1.0.0
 * @author HMS Networks, MU Americas Solution Center
 */
public class SocketLogger implements Runnable {

  /** Constant for socket port */
  private static final String SOCKET_PORT = "766";

  /** Constant for socket fatal error state */
  private static final int STATE_FATAL_ERROR = -1;

  /** Constant for socket disconnected state */
  private static final int STATE_DISCONNECTED = 0;

  /** Constant for socket connecting state */
  private static final int STATE_CONNECTING = 1;

  /** Constant for socket connected state */
  private static final int STATE_CONNECTED = 2;

  /** Constant for socket delay option */
  private static final byte SOCKET_DELAY_SEC = 0;

  /** Constant for socket linger option */
  private static final byte SOCKET_LINGER_SEC = 5;

  /** Constant for socket keep alive option */
  private static final byte SOCKET_KEEPALIVE_SEC = 15;

  /** Constant for socket receive buffer size option */
  private static final byte SOCKET_RCVBUF_BYTES = 1;

  /** Constant for socket send buffer size option */
  private static final byte SOCKET_SNDBUF_BYTES = 127;

  /** Server socket connection used for logging */
  private static ServerSocketConnection socketServerConnection;

  /** Underlying socket connection used for logging */
  private static SocketConnection socketConnection;

  /** Data output stream for adding log entries to */
  private static DataOutputStream dataOutputStream;

  /** Socket connection state variable */
  private static int socketConnectionState = STATE_DISCONNECTED;

  /** Socket log queue for storing logs while not connected */
  private static LogQueue socketLogQueue;

  /** Number of log entries able to be queued */
  private static final int SOCKET_LOG_QUEUE_DEPTH = 50;

  /** Open the socket server connection */
  private static void OPEN() {
    if (socketLogQueue == null) {
      socketLogQueue = new LogQueue(SOCKET_LOG_QUEUE_DEPTH);
    }

    if (socketConnectionState == STATE_DISCONNECTED) {
      socketConnectionState = STATE_CONNECTING;
      Logger.LOG_DEBUG("SocketLogger connection starting.");
      // Create the server listening socket
      try {
        socketServerConnection =
            (ServerSocketConnection) Connector.open("socket://:" + SOCKET_PORT);
      } catch (ConnectionNotFoundException e) {
        Logger.LOG_DEBUG("SocketLogger connection could not open (ConnectionNotFoundException).");
        socketConnectionState = STATE_DISCONNECTED;
      } catch (IllegalArgumentException e) {
        Logger.LOG_DEBUG("SocketLogger connection could not open (IllegalArgumentException).");
        socketConnectionState = STATE_DISCONNECTED;
      } catch (SecurityException e) {
        Logger.LOG_DEBUG("SocketLogger connection could not open (SecurityException).");
        socketConnectionState = STATE_DISCONNECTED;
      } catch (IOException e) {
        Logger.LOG_DEBUG("SocketLogger connection could not open (IOException).");
        socketConnectionState = STATE_DISCONNECTED;
      }
      if (socketConnectionState == STATE_CONNECTING) {
        SocketLogger sl = new SocketLogger();
        Thread connectThread = new Thread(sl);
        connectThread.start();
      }
    }
  }

  /** Close the socket server connection */
  private static void CLOSE() {
    socketConnectionState = STATE_DISCONNECTED;
    Logger.LOG_DEBUG("SocketLogger connection closing.");

    // Try to close all elements of connection.
    try {
      dataOutputStream.close();
    } catch (IOException e) {
      Logger.LOG_DEBUG("SocketLogger data output stream close failed.");
    }
    try {
      socketConnection.close();
    } catch (IOException e) {
      Logger.LOG_DEBUG("SocketLogger socket connection close failed.");
    }
    try {
      socketServerConnection.close();
    } catch (IOException e) {
      Logger.LOG_DEBUG("SocketLogger socket server connection close failed.");
    }
  }

  /**
   * Run method
   *
   * <p>Handles waiting for a incoming connection and setting up the socket connection
   */
  public void run() {
    try {
      // Wait for a incoming connection
      socketConnection = (SocketConnection) socketServerConnection.acceptAndOpen();

      // Set socket connection options
      socketConnection.setSocketOption(SocketConnection.DELAY, SOCKET_DELAY_SEC);
      socketConnection.setSocketOption(SocketConnection.LINGER, SOCKET_LINGER_SEC);
      socketConnection.setSocketOption(SocketConnection.KEEPALIVE, SOCKET_KEEPALIVE_SEC);
      socketConnection.setSocketOption(SocketConnection.RCVBUF, SOCKET_RCVBUF_BYTES);
      socketConnection.setSocketOption(SocketConnection.SNDBUF, SOCKET_SNDBUF_BYTES);

      // Open a data output stream to add log information to
      dataOutputStream = socketConnection.openDataOutputStream();
    } catch (IllegalArgumentException e) {
      // Socket options are incorrect, set state to fatal error.
      Logger.LOG_DEBUG("SocketLogger invalid socket options specified.");
      socketConnectionState = STATE_FATAL_ERROR;
    } catch (IOException e) {
      Logger.LOG_DEBUG("SocketLogger connection could not start.");
      socketConnectionState = STATE_DISCONNECTED;
    }

    if (socketConnectionState == STATE_CONNECTING) {
      // Connection has been established
      socketConnectionState = STATE_CONNECTED;
      outputQueue();
    }
  }

  /** Outputs all queued log entries if possible */
  private void outputQueue() {
    while (!socketLogQueue.isEmpty() && socketConnectionState == STATE_CONNECTED) {
      String logEntry = socketLogQueue.poll();
      try {
        writeStringToSocket(logEntry);
      } catch (IOException e) {

        // Put log entry back in queue
        socketLogQueue.addLogEntry(logEntry);

        CLOSE();
        OPEN();
      }
    }
  }

  /**
   * Log a string to the socket connection if connected. If no connection exists this method will
   * start one.
   *
   * @param logEntry string to log
   */
  public static void LOG(String logEntry) {
    boolean wasLogged = false;
    if (logEntry.length() > 0) {
      // Format log entries "Timestamp: logEntry\n"
      final long currentTimeMillis = new Date().getTime();
      String logEntryFormatted = currentTimeMillis + ": " + logEntry + "\n";

      if (socketConnectionState == STATE_CONNECTED) {
        try {
          writeStringToSocket(logEntryFormatted);
          wasLogged = true;
        } catch (IOException e) {
          CLOSE();
          OPEN();
        }
      } else if (socketConnectionState == STATE_DISCONNECTED) {
        // State is disconnected, open socket connection
        OPEN();
      }
      if (!wasLogged) {
        // Log entry not sent, put log entry in queue
        socketLogQueue.addLogEntry(logEntryFormatted);
      }
    }
  }

  /**
   * Outputs a string on established socket connection
   *
   * @param s string to output
   * @throws IOException if the socket connection is broken
   * @since 1.2
   */
  private static void writeStringToSocket(String s) throws IOException {
    // Convert string to byte array
    byte[] outputByteArray = s.getBytes("UTF-8");
    dataOutputStream.write(outputByteArray);
  }
}
