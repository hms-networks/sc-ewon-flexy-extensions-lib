package com.hms_networks.americas.sc.extensions.string;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/**
 * Class that extends StringTokenizer to allow for ignoring delimiters in double quoted strings.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.0.0
 */
public class QuoteSafeStringTokenizer extends StringTokenizer {

  /** <code>boolean</code> to track if tokenizer should return the delimiter as a separate token */
  private final boolean returnDelimiters;

  /** String to hold a local copy of the configured delimiter */
  private String delimiter = "";

  /** Index of the last returned token in the string */
  private int currTokenIndex = -1;

  /**
   * Creates a new QuoteSafeStringTokenizer for the string <code>str</code>, that should split on
   * the default delimiter set (space, tab, newline, return and formfeed), and which doesn't return
   * the delimiters.
   *
   * @param str The string to split
   * @throws NullPointerException if str is null
   */
  public QuoteSafeStringTokenizer(String str) throws NullPointerException {
    super(str);
    returnDelimiters = false;
  }

  /**
   * Create a new QuoteSafeStringTokenizer, that splits the given string on the given delimiter
   * characters. It doesn't return the delimiter characters.
   *
   * @param str the string to split
   * @param delim a string containing all delimiter characters
   * @throws NullPointerException if either argument is null
   */
  public QuoteSafeStringTokenizer(String str, String delim) throws NullPointerException {
    super(str, delim, false);
    delimiter = delim;
    returnDelimiters = false;
  }

  /**
   * Create a new QuoteSafeStringTokenizer, that splits the given string on the given delimiter
   * characters. If you set <code>returnDelims</code> to <code>true</code>, the delimiter characters
   * are returned as tokens of their own. The delimiter tokens always consist of a single character.
   * If you set <code>returnDelims</code> to <code>false</code>, an empty string will be returned in
   * the case of a delimiter occurring without a token before it and the previous delimiter.
   *
   * @param str the string to split
   * @param delim a string containing all delimiter characters
   * @param returnDelims tells if you want to get the delimiters
   * @throws NullPointerException if str or delim is null
   */
  public QuoteSafeStringTokenizer(String str, String delim, boolean returnDelims)
      throws NullPointerException {
    super(str, delim, true);
    delimiter = delim;
    returnDelimiters = returnDelims;
  }

  /**
   * Returns the next token of the string.
   *
   * @return the next token with respect to the current delimiter characters
   * @throws NoSuchElementException if there are no more tokens
   */
  public String nextToken() throws NoSuchElementException {
    String retStr;
    retStr = super.nextToken();
    if ((retStr.trim().startsWith("\"")) && !(retStr.trim().endsWith("\""))) {
      boolean foundEndQuote = false;
      StringBuffer retStrBuilder = new StringBuffer(retStr);
      while (!foundEndQuote) {
        String nextTokenString = super.nextToken();
        foundEndQuote = nextTokenString.trim().endsWith("\"");
        retStrBuilder.append(nextTokenString);
      }
      retStr = retStrBuilder.toString();
    }

    if (!returnDelimiters) {
      if (retStr.equals(delimiter)) {
        /* Return an empty string if token doesn't exist before delimiter */
        retStr = "";
      } else if (super.hasMoreTokens()) {
        /* grab the next delimiter and discard it */
        super.nextToken();
      }
    }

    /* Increment the current token index */
    currTokenIndex++;

    return retStr;
  }

  /**
   * Returns the next token of the string removing start and end double quotes.
   *
   * @return the next token with respect to the current delimiter characters
   * @throws NoSuchElementException if there are no more tokens
   */
  public String nextTokenIgnoreQuotes() throws NoSuchElementException {
    String retStr = nextToken().trim();

    /*
     * Strip the starting quote
     */
    if (retStr.startsWith("\"")) {
      retStr = retStr.substring(1);
    }

    /*
     * Strip the ending quote
     */
    if (retStr.endsWith("\"")) {
      retStr = retStr.substring(0, retStr.length() - 1);
    }

    return retStr;
  }

  /**
   * This does the same as nextToken(). This is the {@link java.util.Enumeration} interface method.
   *
   * @return the next token with respect to the current delimiter characters
   * @throws NoSuchElementException if there are no more tokens
   * @see #nextToken()
   */
  public Object nextElement() throws NoSuchElementException {
    return nextToken();
  }

  /**
   * Returns the index of the last returned token
   *
   * @return the index of the last returned token
   */
  public int getPrevTokenIndex() {
    return currTokenIndex;
  }
}
