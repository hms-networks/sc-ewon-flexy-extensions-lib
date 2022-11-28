package com.hms_networks.americas.sc.extensions.security;

import com.hms_networks.americas.sc.extensions.fileutils.FileAccessManager;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * A utility class for getting the hash of a files, byte arrays, and strings using the {@link
 * SCWonkaSecurityProvider} security provider.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @version 1.0.0
 * @since 1.13.0
 */
public class HashUtilities {

  /**
   * Boolean to track if the {@link SCWonkaSecurityProvider} security provider has been added to the
   * JVM. There are no side effects to adding the provider multiple times, but it is unnecessary and
   * adds additional overhead (code executed by JVM to check if already loaded).
   *
   * @since 1.0.0
   */
  private static boolean isProviderAdded = false;

  /**
   * Gets the MD5 hash of the specified {@link File}.
   *
   * @param fileToHash the {@link File} to get the MD5 hash of
   * @return the MD5 hash of the specified {@link File}
   * @throws IOException if an error occurs while reading the file
   * @throws NoSuchAlgorithmException if the MD5 algorithm is not available
   * @since 1.0.0
   */
  public static String getFileHashMd5(File fileToHash)
      throws IOException, NoSuchAlgorithmException {
    return getByteArrayHashMd5(FileAccessManager.readFileToByteArray(fileToHash));
  }

  /**
   * Gets the MD5 hash of the file with the specified file name.
   *
   * @param fileName the file name of the file to get the MD5 hash of
   * @return the MD5 hash of the specified {@link File}
   * @throws IOException if an error occurs while reading the file
   * @throws NoSuchAlgorithmException if the MD5 algorithm is not available
   * @since 1.0.0
   */
  public static String getFileHashMd5(String fileName)
      throws IOException, NoSuchAlgorithmException {
    return getFileHashMd5(new File(fileName));
  }

  /**
   * Gets the SHA-1 hash of the specified {@link File}.
   *
   * @param fileToHash the {@link File} to get the SHA-1 hash of
   * @return the SHA-1 hash of the specified {@link File}
   * @throws IOException if an error occurs while reading the file
   * @throws NoSuchAlgorithmException if the SHA-1 algorithm is not available
   * @since 1.0.0
   */
  public static String getFileHashSha1(File fileToHash)
      throws IOException, NoSuchAlgorithmException {
    return getByteArrayHashSha1(FileAccessManager.readFileToByteArray(fileToHash));
  }

  /**
   * Gets the SHA-1 hash of the file with the specified file name.
   *
   * @param fileName the file name of the file to get the SHA-1 hash of
   * @return the SHA-1 hash of the specified {@link File}
   * @throws IOException if an error occurs while reading the file
   * @throws NoSuchAlgorithmException if the SHA-1 algorithm is not available
   * @since 1.0.0
   */
  public static String getFileHashSha1(String fileName)
      throws IOException, NoSuchAlgorithmException {
    return getFileHashSha1(new File(fileName));
  }

  /**
   * Gets the MD5 hash of the specified {@link String}.
   *
   * @param stringToHash the {@link String} to get the MD5 hash of
   * @return the MD5 hash of the specified {@link String}
   * @throws NoSuchAlgorithmException if the MD5 algorithm is not available
   * @since 1.0.0
   */
  public static String getStringHashMd5(String stringToHash) throws NoSuchAlgorithmException {
    return getByteArrayHashMd5(stringToHash.getBytes());
  }

  /**
   * Gets the SHA-1 hash of the specified {@link String}.
   *
   * @param stringToHash the {@link String} to get the SHA-1 hash of
   * @return the SHA-1 hash of the specified {@link String}
   * @throws NoSuchAlgorithmException if the SHA-1 algorithm is not available
   * @since 1.0.0
   */
  public static String getStringHashSha1(String stringToHash) throws NoSuchAlgorithmException {
    return getByteArrayHashSha1(stringToHash.getBytes());
  }

  /**
   * Gets the MD5 hash of the specified {@link byte[]} (byte array).
   *
   * @param byteArrayToHash the {@link byte[]} (byte array) to get the MD5 hash of
   * @return the MD5 hash of the specified {@link byte[]} (byte array)
   * @throws NoSuchAlgorithmException if the MD5 algorithm is not available
   * @since 1.0.0
   */
  public static String getByteArrayHashMd5(byte[] byteArrayToHash) throws NoSuchAlgorithmException {
    return getByteArrayHash(byteArrayToHash, "MD5");
  }

  /**
   * Gets the SHA-1 hash of the specified {@link byte[]} (byte array).
   *
   * @param byteArrayToHash the {@link byte[]} (byte array) to get the SHA-1 hash of
   * @return the SHA-1 hash of the specified {@link byte[]} (byte array)
   * @throws NoSuchAlgorithmException if the SHA-1 algorithm is not available
   * @since 1.0.0
   */
  public static String getByteArrayHashSha1(byte[] byteArrayToHash)
      throws NoSuchAlgorithmException {
    return getByteArrayHash(byteArrayToHash, "SHA");
  }

  /**
   * Gets the hash of the specified {@link byte[]} (byte array) using the specified algorithm.
   *
   * @param byteArrayToHash the {@link byte[]} (byte array) to get the hash of
   * @param algorithm the algorithm to use to get the hash
   * @return the hash of the specified {@link byte[]} (byte array) using the specified algorithm
   * @throws NoSuchAlgorithmException if the specified algorithm is not available
   * @since 1.0.0
   */
  public static String getByteArrayHash(byte[] byteArrayToHash, String algorithm)
      throws NoSuchAlgorithmException {
    // Add provider if not already added
    if (!isProviderAdded) {
      SecurityProviderUtilities.addSCWonkaSecurityProvider();
      isProviderAdded = true;
    }

    // Calculate hash as bytes
    MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
    messageDigest.update(byteArrayToHash);
    byte[] hashBytes = messageDigest.digest();

    // Convert hash bytes to hex string
    final int hexRadix = 16;
    StringBuffer hashStringBuffer = new StringBuffer(hashBytes.length * 2);
    for (int i = 0; i < hashBytes.length; i++) {
      hashStringBuffer.append(Character.forDigit((hashBytes[i] >> 4) & 0xF, hexRadix));
      hashStringBuffer.append(Character.forDigit(hashBytes[i] & 0xF, hexRadix));
    }
    return hashStringBuffer.toString();
  }
}
