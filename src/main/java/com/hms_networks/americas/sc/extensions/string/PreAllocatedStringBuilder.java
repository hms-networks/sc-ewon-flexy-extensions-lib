package com.hms_networks.americas.sc.extensions.string;

/**
 * PreAllocatedStringBuilder class
 *
 * <p>Class for building large pre-allocated strings
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.0.0
 */
public class PreAllocatedStringBuilder {

  /** The length, in number of characters, of the string being formed. */
  private int length = 0;

  /**
   * The current index of the end of the string being formed. Used as a placement index for strings
   * and chars that are being appended.
   */
  private int index = 0;

  /** The character array forming the string that is being formed. */
  private final char[] strValue;

  /**
   * Create a new <code>PreAllocatedStringBuilder</code> with the specified number of characters as
   * the maximum length.
   *
   * @param numChars maximum string length
   */
  public PreAllocatedStringBuilder(int numChars) {
    strValue = new char[numChars];
  }

  /**
   * Get the current length of the string that is being formed.
   *
   * @return current string length
   */
  public int getLength() {
    return length;
  }

  /** Clear the contents of the string by setting the placement index at 0 (start). */
  public void clearString() {
    index = 0;
    length = 0;
  }

  /**
   * Get the contents of the string that is being formed.
   *
   * @return string contents
   */
  public String toString() {
    return String.valueOf(strValue, 0, index);
  }

  /**
   * Append a specified string to the string that is being formed.
   *
   * @param s string to append
   * @return true if successful, otherwise false
   */
  public boolean append(String s) {
    boolean retval = true;
    if (s == null) {
      s = "";
    }
    int len = s.length();
    length += len;
    try {
      // copy the characters in the appended sting s into the char array strValue
      s.getChars(0, len, strValue, index);
      index += len;
    } catch (IndexOutOfBoundsException e) {
      e.printStackTrace();
      retval = false;
    }
    return retval;
  }
}
