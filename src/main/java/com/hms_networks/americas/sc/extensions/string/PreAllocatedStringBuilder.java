package com.hms_networks.americas.sc.extensions.string;

/**
 * PreAllocatedStringBuilder class. As of version 2.0.0, this class is deprecated and its underlying
 * functionality redirects to the {@link StringBuffer} class.
 *
 * <p>Class for building large pre-allocated strings
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.0.0
 * @version 2.0.0
 * @deprecated Use {@link StringBuffer} directly instead.
 */
public class PreAllocatedStringBuilder {

  /**
   * The underlying {@link StringBuffer} for building the string.
   *
   * @since 2.0.0
   */
  private final StringBuffer stringBuffer;

  /**
   * Create a new <code>PreAllocatedStringBuilder</code> with the specified number of characters as
   * the maximum length. As of version 2.0.0, this class is deprecated and its underlying
   * functionality redirects to the {@link StringBuffer} class. The equivalent constructor for
   * {@link StringBuffer} is {@link StringBuffer#StringBuffer(int)}.
   *
   * @param numChars maximum string length
   * @since 1.0.0
   * @deprecated Implement using {@link StringBuffer} directly and use {@link
   *     StringBuffer#StringBuffer(int)}.
   */
  public PreAllocatedStringBuilder(int numChars) {
    stringBuffer = new StringBuffer(numChars);
  }

  /**
   * Get the current length of the string that is being formed. As of version 2.0.0, this class is
   * deprecated and its underlying functionality redirects to the {@link StringBuffer} class. The
   * equivalent method for {@link StringBuffer} is {@link StringBuffer#length()}.
   *
   * @return current string length
   * @since 1.0.0
   * @deprecated Implement using {@link StringBuffer} directly and use {@link
   *     StringBuffer#length()}.
   */
  public int getLength() {
    return stringBuffer.length();
  }

  /**
   * Clear the contents of the string. As of version 2.0.0, this class is deprecated and its
   * underlying functionality redirects to the {@link StringBuffer} class. There is no equivalent
   * method for {@link StringBuffer}, but the equivalent functionality can be achieved by using
   * {@link StringBuffer#setLength(int)} with the argument 0.
   *
   * @since 1.0.0
   * @deprecated Implement using {@link StringBuffer} directly and use {@link
   *     StringBuffer#setLength(int)} with the argument 0.
   */
  public void clearString() {
    stringBuffer.setLength(0);
  }

  /**
   * Get the contents of the string that is being formed. As of version 2.0.0, this class is
   * deprecated and its underlying functionality redirects to the {@link StringBuffer} class. The
   * equivalent method for {@link StringBuffer} is {@link StringBuffer#toString()}.
   *
   * @return string contents
   * @since 1.0.0
   * @deprecated Implement using {@link StringBuffer} directly and use {@link
   *     StringBuffer#toString()}.
   */
  public String toString() {
    return stringBuffer.toString();
  }

  /**
   * Append a specified string to the string that is being formed. As of version 2.0.0, this class
   * is deprecated and its underlying functionality redirects to the {@link StringBuffer} class. The
   * equivalent method for {@link StringBuffer} is {@link StringBuffer#append(String)}.
   *
   * @param s string to append
   * @return true if successful, otherwise false
   * @since 1.0.0
   * @deprecated Implement using {@link StringBuffer} directly and use {@link
   *     StringBuffer#append(String)}. Note that the return value for {@link
   *     StringBuffer#append(String)} is different, and is a reference to the {@link StringBuffer}.
   *     The result of the append operation can be verified by comparing {@link
   *     StringBuffer#length()} before and after the append operation.
   */
  public boolean append(String s) {
    int origLength = stringBuffer.length();
    int expectedLength = origLength + s.length();
    stringBuffer.append(s);
    int newLength = stringBuffer.length();
    return newLength == expectedLength;
  }
}
