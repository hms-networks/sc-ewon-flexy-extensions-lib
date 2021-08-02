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
 */
public class FileAccessManager {
  /**
   * Reads the specified file into a string.
   *
   * @param filename file to read
   * @return file contents
   * @throws IOException if unable to access or read file
   */
  public static String readFileToString(String filename) throws IOException {
    // If file does not exist, return null
    File file = new File(filename);
    String returnVal = null;

    if (file.exists()) {
      // Create input stream and buffered reader
      InputStream inputStream = new FileInputStream(filename);
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
