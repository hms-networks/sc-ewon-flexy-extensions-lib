package com.hms_networks.americas.sc.extensions.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * Utility class for working with byte data, including byte arrays and streams.
 *
 * @author HMS Networks; Americas
 * @since 1.15.14
 */
public class StreamUtils {

  /**
   * The size of the buffer used when reading from an input stream.
   *
   * @since 1.15.14
   */
  private static final int INPUT_STREAM_BUFFER_SIZE_BYTES = 1024;

  /**
   * The value returned when an input stream has reached the end of the stream.
   *
   * @since 1.15.14
   */
  private static final int INPUT_STREAM_END_OF_STREAM_VALUE = -1;

  /**
   * Get the bytes from an input stream.
   *
   * @param inputStream input stream to read
   * @return byte array of input stream data
   * @throws IOException if an error occurs while reading the input stream
   * @since 1.15.14
   */
  public static byte[] getBytes(InputStream inputStream) throws IOException {
    // Create output stream for result
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    // Create buffer for reading input stream
    byte[] buffer = new byte[INPUT_STREAM_BUFFER_SIZE_BYTES];

    // Read from input stream and write to output stream
    final int offset = 0;
    for (int length; (length = inputStream.read(buffer)) != INPUT_STREAM_END_OF_STREAM_VALUE; ) {
      outputStream.write(buffer, offset, length);
    }

    // Cleanup streams and return result
    byte[] result = outputStream.toByteArray();
    outputStream.close();
    inputStream.close();
    return result;
  }

  /**
   * Get the string from an input stream using the specified encoding.
   *
   * @param inputStream input stream to read
   * @param encoding encoding to use
   * @return string contents of input stream
   * @throws IOException if an error occurs while reading the input stream
   * @throws UnsupportedEncodingException if the encoding is not supported
   * @since 1.15.14
   */
  public static String getStringFromInputStream(InputStream inputStream, String encoding)
      throws IOException {
    // Get bytes from input stream
    byte[] bytes = getBytes(inputStream);

    // Convert bytes to string
    return new String(bytes, encoding);
  }
}
