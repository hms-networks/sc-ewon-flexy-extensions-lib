package com.hms_networks.americas.sc.extensions;

import com.hms_networks.americas.sc.extensions.datapoint.DataPoint;
import com.hms_networks.americas.sc.extensions.historicaldata.CorruptedTimeTrackerException;
import com.hms_networks.americas.sc.extensions.historicaldata.HistoricalDataEBDRequest;
import com.hms_networks.americas.sc.extensions.historicaldata.HistoricalDataQueueManager;
import com.hms_networks.americas.sc.extensions.historicaldata.TimeTrackerUnrecoverableException;
import com.hms_networks.americas.sc.extensions.system.time.LocalTimeOffsetCalculator;
import com.hms_networks.americas.sc.extensions.system.time.SCTimeUtils;
import com.hms_networks.americas.sc.extensions.system.time.TimeZoneManager;
import com.hms_networks.americas.sc.extensions.taginfo.TagInfoManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Main class for the project. This class does not perform any functions, as it is contained in a
 * library.
 *
 * @since 1.0.0
 * @author HMS Networks, MU Americas Solution Center
 */
public class ExtensionsMain {

  public static void getTagInfo() {
    int i = 40;
    while (i > 0) {
      i--;
      try {
        TimeZoneManager.checkUpdateTimeZone();
        ArrayList dat = HistoricalDataQueueManager.getFifoNextSpanDataAllGroups(false);
        for (int j = 0; j < dat.size(); j++) {
          DataPoint dp = (DataPoint) dat.get(j);
          if (dp.getTagId() == 7 || dp.getTagId() == 6) {
            System.out.println(dp.getValueString() + " " + dp.getIso8601Timestamp());
          }
          Thread.sleep(1000);
        }
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }

  public static void checkTime()
      throws CorruptedTimeTrackerException, IOException, TimeTrackerUnrecoverableException {
    long t1 = HistoricalDataQueueManager.getTrackingStartTime(false);
    long t2 = System.currentTimeMillis();
    long diff = t2 - t1;
    String rel = HistoricalDataEBDRequest.convertToEBDRelativeTimeFormat(t1);
    System.out.println("Time 1: " + t1);
    System.out.println("Time 2: " + t2);
    System.out.println("Time difference: " + diff);
    System.out.println("rel time difference  seconds: " + rel);
  }

  public static void t1() {
    long t1 = 1730598984000L;
    long offest = LocalTimeOffsetCalculator.getLocalTimeOffsetMilliseconds();
    Date d = new Date(t1 + offest);
    System.out.println("Date : " + d);
    long utc;
    try {
      utc = LocalTimeOffsetCalculator.convertLocalEpochMillisToUtc(t1);

      Date dutc = new Date(utc);
      System.out.println("Date utc : " + dutc);

      System.out.println("seconds ago " + (System.currentTimeMillis() - utc) / 1000);
      System.out.println(
          "seconds ago ebd"
              + HistoricalDataEBDRequest.prepareHistoricalFifoReadEBDString(
                  utc, utc + 100000, true, false, false, false, true, false, true, false, true));

    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    System.out.println("check 2024-11-03T01:56:24-04:00  or 2024-11-03T05:56:24Z");
    System.out.println("d.getTime() " + d.getTime());
  }

  /**
   * Main method for the project main class.
   *
   * @param args program arguments
   */
  public static void main(String[] args) {

    System.out.println("<h1>start</h1> ");
    try {
      SCTimeUtils.injectJvmLocalTime();
      TagInfoManager.refreshTagList();
      HistoricalDataQueueManager.setStringHistoryEnabled(true);
      getTagInfo();

    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    System.out.println("done");
  }
}
