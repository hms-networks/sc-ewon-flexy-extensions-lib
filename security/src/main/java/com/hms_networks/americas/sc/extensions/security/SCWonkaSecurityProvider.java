package com.hms_networks.americas.sc.extensions.security;

import java.security.Provider;

/**
 * A custom security provider for the Wonka JVM which allows for the use of the Wonka MD5 and SHA
 * algorithms by the Java MessageDigest class. These algorithms are not available by default in the
 * Java MessageDigest class due to classloader restrictions in the Java JVM.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @version 1.0.0
 * @since 1.0.0
 */
public class SCWonkaSecurityProvider extends Provider {

  /**
   * The name of the provider.
   *
   * @since 1.0.0
   */
  protected static final String NAME = "SCWonkaSecurityProvider";

  /**
   * The version number of the provider.
   *
   * @since 1.0.0
   */
  protected static final double VERSION = 1.0;

  /**
   * The information string of the provider.
   *
   * @since 1.0.0
   */
  protected static final String INFO =
      "Solution Center Security Provider for Classloader Restricted Wonka JVM Security Classes";

  /**
   * The classpath of the Wonka JVM MD5 {@link java.security.MessageDigestSpi} (message digest
   * service provider interface) implementation.
   *
   * @since 1.0.0
   */
  private static final String WONKA_MD5_MESSAGE_DIGEST_SPI_CLASSPATH =
      "wonka.security.MD5MessageDigest";

  /**
   * The classpath of the Wonka JVM SHA {@link java.security.MessageDigestSpi} (message digest
   * service provider interface) implementation.
   *
   * @since 1.0.0
   */
  private static final String WONKA_SHA_MESSAGE_DIGEST_SPI_CLASSPATH =
      "wonka.security.ShaMessageDigest";

  /**
   * Constructs a new {@link SCWonkaSecurityProvider} and loads the Wonka JVM MD5 and SHA
   * algorithms.
   *
   * @since 1.0.0
   */
  protected SCWonkaSecurityProvider() {
    super(NAME, VERSION, INFO);

    // Load the MD5 and SHA algorithms
    put("MessageDigest.MD5", WONKA_MD5_MESSAGE_DIGEST_SPI_CLASSPATH);
    put("MessageDigest.SHA", WONKA_SHA_MESSAGE_DIGEST_SPI_CLASSPATH);
  }
}
