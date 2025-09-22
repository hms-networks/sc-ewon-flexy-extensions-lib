package com.hms_networks.americas.sc.extensions;

import com.hms_networks.americas.sc.extensions.datapoint.DataPoint;
import com.hms_networks.americas.sc.extensions.historicaldata.HistoricalDataQueueManager;
import java.util.ArrayList;

/**
 * Main class for the project. This class does not perform any functions, as it is contained in a
 * library.
 *
 * @since 1.0.0
 * @author HMS Networks, MU Americas Solution Center
 */
public class ExtensionsMain {

  public static void runTest() {
    int i = 0;
    boolean startNewTimeTracker = true;
    ArrayList datapointsReadFromQueue = null;
    try {
      for (i = 0; i < 20; i++) {

        datapointsReadFromQueue =
            HistoricalDataQueueManager.getFifoNextSpanDataAllGroups(startNewTimeTracker);
        i++;
        startNewTimeTracker = false;
        for (int j = 0; j < datapointsReadFromQueue.size(); j++) {
          DataPoint dp = (DataPoint) datapointsReadFromQueue.get(j);
          System.out.println(
              "Datapoint "
                  + dp.getTagName()
                  + " timestamp: "
                  + dp.getIso8601Timestamp()
                  + " IsInit value: "
                  + dp.isInitValue());
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /*
   * Main method for the project main class.
   *
   * @param args program arguments
   */
  public static void main(String[] args) {
    System.out.println("Running test version that will demonstrate the new IsInit value.");
  }
}
