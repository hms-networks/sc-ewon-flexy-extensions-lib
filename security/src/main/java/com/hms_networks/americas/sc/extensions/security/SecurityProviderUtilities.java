package com.hms_networks.americas.sc.extensions.security;

import java.security.Provider;
import java.security.Security;

/**
 * A utility class for adding and removing the {@link SCWonkaSecurityProvider} security provider (or
 * any specified {@link Provider} to the JVM.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @version 1.0.0
 * @since 1.13.0
 */
public class SecurityProviderUtilities {

  /**
   * Adds the {@link SCWonkaSecurityProvider} security provider to the JVM if it has not already
   * been added (JVM performs check).
   *
   * @since 1.0.0
   */
  public static void addSCWonkaSecurityProvider() {
    addSecurityProvider(new SCWonkaSecurityProvider());
  }

  /**
   * Adds the specified {@link Provider} security provider to the JVM if it has not already been
   * added (JVM performs check).
   *
   * @param provider the {@link Provider} to add to the JVM
   * @since 1.0.0
   */
  public static void addSecurityProvider(Provider provider) {
    Security.addProvider(provider);
  }

  /**
   * Removes the {@link SCWonkaSecurityProvider} security provider from the JVM if it has been added
   * (JVM performs check).
   *
   * @since 1.0.0
   */
  public static void removeSCWonkaSecurityProvider() {
    removeSecurityProvider(SCWonkaSecurityProvider.NAME);
  }

  /**
   * Removes the specified {@link Provider} security provider from the JVM if it has been added (JVM
   * performs check).
   *
   * @param provider the {@link Provider} to remove from the JVM
   * @since 1.0.0
   */
  public static void removeSecurityProvider(Provider provider) {
    removeSecurityProvider(provider.getName());
  }

  /**
   * Removes the specified {@link Provider} security provider from the JVM if it has been added (JVM
   * performs check).
   *
   * @param providerName the name of the {@link Provider} to remove from the JVM
   * @since 1.0.0
   */
  public static void removeSecurityProvider(String providerName) {
    Security.removeProvider(providerName);
  }
}
