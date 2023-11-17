package com.hms_networks.americas.sc.extensions.ebd;

import com.ewon.ewonitf.Exporter;
import java.io.IOException;

/**
 * Class to provide utility functionality for executing EBD (Export Block Descriptor) calls on Ewon
 * Flexy.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 2.0.0
 */
public class EbdUtility {

  /**
   * Executes EBD call, waits for data return.
   *
   * @param ebdStr EBD string
   * @return EBD Exporter - caller must close exporter
   * @throws EbdTimeoutException when there is no response before timeout period
   * @throws IOException when there is an Exporter Exception
   */
  public static Exporter executeEbdCall(String ebdStr) throws IOException, EbdTimeoutException {
    final long start = System.currentTimeMillis();
    final Exporter exporter = new Exporter(ebdStr);
    int available = exporter.available();
    // check on data availability
    while (available == 0) {
      // sleep
      try {
        Thread.sleep(EbdConstants.DEFAULT_EBD_THREAD_SLEEP_MS);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      // check for timeout
      if (System.currentTimeMillis() - start > EbdConstants.MAX_EBD_WAIT_MS) {
        exporter.close();
        throw new EbdTimeoutException("Timeout waiting for Export Block Descriptor.");
      }

      // check for data available
      available = exporter.available();
    }
    return exporter;
  }
}
