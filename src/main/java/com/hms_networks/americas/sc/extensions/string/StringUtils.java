package com.hms_networks.americas.sc.extensions.string;

import java.util.ArrayList;
import java.util.List;

/**
 * Class providing utilities for working with and manipulating {@link String} objects.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.0.0
 */
public class StringUtils {

  /**
   * Method to replace all instances of <code>target</code> with <code>replacement</code> in <code>
   * string
   * </code> and return the result.
   *
   * @param string string to modify
   * @param target to replace
   * @param replacement replacement
   * @return string with replacement completed
   * @since 1.0
   * @throws NullPointerException if any string parameters are null
   */
  public static String replace(String string, String target, String replacement) {
    // Throw NullPointerException if any supplied strings are null
    if (string == null || target == null || replacement == null) throw new NullPointerException();

    // Create String buffer for building the modified string
    StringBuffer stringBuffer = new StringBuffer();

    // Loop through each character in -string-
    for (int x = 0; x < string.length(); ) {
      // Search for an instance of -target- in -string-
      int indexOfTarget = string.indexOf(target, x);

      // Perform replacement if at start index
      if (x == indexOfTarget) {
        stringBuffer.append(replacement);
        x += target.length();
      } else {
        // Append character and move to next
        stringBuffer.append(string.charAt(x++));
      }
    }

    // Return modified string
    return stringBuffer.toString();
  }

  /**
   * Method to split the supplied string into smaller strings using the supplied delimiter.
   *
   * @param string string to split
   * @param delimiter string split delimiter
   * @return list of string split parts
   * @since 1.2
   */
  public static List split(String string, String delimiter) {
    // Create array to store string parts
    ArrayList stringParts = new ArrayList();

    // Create start and end index variables, starting at index 0 (string start)
    int startIndex = 0;
    int endIndex = string.indexOf(delimiter, startIndex);

    // Loop while #delimiter is found in string
    while (endIndex != -1) {
      String stringPart = string.substring(startIndex, endIndex);
      stringParts.add(stringPart);
      startIndex = endIndex + delimiter.length();
      endIndex = string.indexOf(delimiter, startIndex);
    }

    // Add remaining end of string
    String endStringPart = string.substring(startIndex);
    stringParts.add(endStringPart);
    return stringParts;
  }
}
