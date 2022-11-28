package com.hms_networks.americas.sc.extensions.fileutils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * Class for reading and writing files using Java strings on the Ewon Flexy filesystem.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.0.0
 * @version 1.1.0
 */
public class FileAccessManager {

  /**
   * Reads the contents of the specified {@link File} as a byte array ({@link byte[]}).
   *
   * @param file the {@link File} to get the contents of
   * @return byte array ({@link byte[]}) of the contents of the specified {@link File}
   * @throws IOException if an error occurs while reading the file
   * @since 1.1.0
   */
  public static byte[] readFileToByteArray(File file) throws IOException {
    byte[] fileBytes = new byte[(int) file.length()];
    FileInputStream fileInputStream = new FileInputStream(file);
    fileInputStream.read(fileBytes);
    fileInputStream.close();
    return fileBytes;
  }

  /**
   * Reads the contents of the file with the specified name as a byte array ({@link byte[]}).
   *
   * @param fileName the name of the file to get the contents of
   * @return byte array ({@link byte[]}) of the contents of the file with the specified name
   * @throws IOException if an error occurs while reading the file
   * @since 1.1.0
   */
  public static byte[] readFileToByteArray(String fileName) throws IOException {
    return readFileToByteArray(new File(fileName));
  }

  /**
   * Reads the contents of the specified {@link File} as a {@link String}.
   *
   * @param file the {@link File} to get the contents of
   * @return {@link String} of of the contents of the specified {@link File}
   * @throws IOException if an error occurs while reading the file
   * @since 1.1.0
   */
  public static String readFileToString(File file) throws IOException {
    // If file does not exist, return null
    String returnVal = null;

    if (file.exists()) {
      // Create input stream and buffered reader
      InputStream inputStream = new FileInputStream(file.getAbsolutePath());
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

      // Create string for file contents
      StringBuffer fileContents = new StringBuffer();

      // Loop through file and add to string
      String currentLine = bufferedReader.readLine();
      while (currentLine != null) {
        // Append current line
        fileContents.append(currentLine);

        // Read next line
        currentLine = bufferedReader.readLine();

        // If next line exists, add new line to string
        if (currentLine != null) {
          fileContents.append("\n");
        }
      }

      // Close reader and stream
      bufferedReader.close();
      inputStream.close();

      returnVal = fileContents.toString();
    }

    return returnVal;
  }

  /**
   * Reads the contents of the file with the specified name as a {@link String}.
   *
   * @param fileName the name of the file to get the contents of
   * @return {@link String} of the contents of the file with the specified name
   * @throws IOException if an error occurs while reading the file
   * @since 1.0.0
   */
  public static String readFileToString(String fileName) throws IOException {
    return readFileToString(new File(fileName));
  }

  /**
   * Writes the specified string to the specified file, appending or overwriting the existing
   * content based on the value of <code>append</code>.
   *
   * @param filename file to write to
   * @param contents file contents
   * @param append boolean indicating if specified contents appended to existing
   * @throws IOException if unable to access or write file
   */
  private static void writeStringToFile(String filename, String contents, boolean append)
      throws IOException {
    // Verify folders exist and file exists
    File file = new File(filename);
    file.getParentFile().mkdirs();
    if (!file.exists()) {
      file.createNewFile();
    }

    // Create output stream and buffered writer
    OutputStream outputStream = new FileOutputStream(filename, append);
    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));

    // Write contents to file
    bufferedWriter.write(contents);

    // Close writer and stream
    bufferedWriter.close();
    outputStream.close();
  }

  /**
   * Writes the specified string to the specified file, overwriting existing contents.
   *
   * @param filename file to write to
   * @param contents file contents
   * @throws IOException if unable to access or write file
   */
  public static void writeStringToFile(String filename, String contents) throws IOException {
    final boolean appendToExistingFile = false;
    writeStringToFile(filename, contents, appendToExistingFile);
  }

  /**
   * Appends the specified string to the specified file.
   *
   * @param filename file to write to
   * @param contents file contents
   * @throws IOException if unable to access or write file
   */
  public static void appendStringToFile(String filename, String contents) throws IOException {
    final boolean appendToExistingFile = true;
    writeStringToFile(filename, contents, appendToExistingFile);
  }
}
