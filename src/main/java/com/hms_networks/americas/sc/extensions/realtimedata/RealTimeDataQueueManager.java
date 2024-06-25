package com.hms_networks.americas.sc.extensions.realtimedata;

import com.hms_networks.americas.sc.extensions.datapoint.DataPoint;
import com.hms_networks.americas.sc.extensions.logging.Logger;
import com.hms_networks.americas.sc.extensions.taginfo.TagGroup;
import com.hms_networks.americas.sc.extensions.taginfo.TagInfo;
import com.hms_networks.americas.sc.extensions.taginfo.TagInfoManager;
import java.util.ArrayList;

/**
 * This class is used to retrieve real time data from the Flexy.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.0.0
 */
public class RealTimeDataQueueManager {

  /** Holds an array of lists of data points for every tag in group. */
  private static ArrayList tagManagers;

  /** Holds an array of the lists of what tags are in each group. */
  private static ArrayList tagGroupList;

  /**
   * Lists of iterator for each tag group used to get a single point at a time from a tag group
   * while retrieving data points.
   */
  private static int[] tagManagerIterators;

  /** Index of information for tag group A in class ArrayLists. */
  public static final int GROUP_A = 0;

  /** Index of information for tag group B in class ArrayLists. */
  public static final int GROUP_B = 1;

  /** Index of information for tag group C in class ArrayLists. */
  public static final int GROUP_C = 2;

  /** Index of information for tag group D in class ArrayLists. */
  public static final int GROUP_D = 3;

  /** The number of tag groups. */
  public static final int NUM_TAG_GROUPS = 4;

  /** Store the tag groups in an ArrayList for access by tag group index. */
  private static void initTagGroups() {
    tagGroupList = new ArrayList();
    tagGroupList.add(TagInfoManager.getTagInfoListFiltered(TagGroup.A));
    tagGroupList.add(TagInfoManager.getTagInfoListFiltered(TagGroup.B));
    tagGroupList.add(TagInfoManager.getTagInfoListFiltered(TagGroup.C));
    tagGroupList.add(TagInfoManager.getTagInfoListFiltered(TagGroup.D));
  }

  /**
   * Initialize a tag manager object for each tag in the tag's group.
   *
   * @throws Exception If the TagControl object fails to initialize for a given tag.
   */
  private static void initTagManagers() throws Exception {
    tagManagers = new ArrayList();
    for (int tagGroupIndex = 0; tagGroupIndex < NUM_TAG_GROUPS; tagGroupIndex++) {
      ArrayList tagManagerTmp = new ArrayList();
      final int tagGroupListSize = ((ArrayList) tagGroupList.get(tagGroupIndex)).size();
      for (int tagListIndex = 0; tagListIndex < tagGroupListSize; tagListIndex++) {
        TagInfo currentTag =
            ((TagInfo) ((ArrayList) tagGroupList.get(tagGroupIndex)).get(tagListIndex));
        tagManagerTmp.add(new RealTimeTagDataPointManager(currentTag.getName()));
      }
      if (tagManagerTmp.isEmpty()) {
        tagManagers.add(null);
      } else {
        tagManagers.add(tagManagerTmp);
      }
    }
  }

  /**
   * Tag iterators are used to retrieve a single data point from each tag before going to the next
   * tag in a tag group list. Initialize all tag iterators to 0 to start at the beginning of the
   * lists.
   */
  private static void initTagManageIterators() {
    tagManagerIterators = new int[NUM_TAG_GROUPS];
    for (int i = 0; i < NUM_TAG_GROUPS; i++) {
      tagManagerIterators[i] = 0;
    }
  }

  /**
   * Initializes RealTimeDataQueueManager.
   *
   * @throws Exception If the TagControl object fails to initialize for a given tag.
   */
  public static void init() throws Exception {
    initTagGroups();
    initTagManagers();
    initTagManageIterators();
  }

  /**
   * getGroupNextData will group a single data point object from the tags in a group, rotate
   * iterators to look at the next tag in the list of that group's tags, and remove the retrieved
   * tag from the list of tags.
   *
   * <p>If there are no DataPoints left, null will be returned.
   *
   * @param tagGroup The tag group to select a datapoint from.
   * @return a DataPoint object.
   */
  public static DataPoint getGroupNextData(int tagGroup) {
    DataPoint data = null;
    RealTimeTagDataPointManager dataPoints =
        ((RealTimeTagDataPointManager)
            ((ArrayList) tagManagers.get(tagGroup)).get(tagManagerIterators[tagGroup]));
    if (dataPoints.getSize() > 0) {
      data = dataPoints.removeDataPoint();
      tagManagerIterators[tagGroup]++;
      tagManagerIterators[tagGroup] =
          (tagManagerIterators[tagGroup] % ((ArrayList) tagGroupList.get(tagGroup)).size());
    }
    return data;
  }

  /**
   * Gets the number of data points retrieved from tag a group.
   *
   * @param tagGroup The tag group to get the number of data points from.
   * @return returns the number of data points in specified group.
   */
  public static int getNumGroupDataPoints(int tagGroup) {
    int numDataPoints = 0;
    ArrayList tagGroupList = (ArrayList) tagManagers.get(tagGroup);
    if (tagGroupList != null) {
      for (int i = 0; i < tagGroupList.size(); i++) {
        numDataPoints += ((RealTimeTagDataPointManager) tagGroupList.get(i)).getSize();
      }
    }
    return numDataPoints;
  }

  /**
   * Gets a new data point for each tag in a specified group and then adds it to an internal queue
   * in the tag's tag Manager.
   *
   * @param tagGroup The tag group to fetch data from.
   */
  public static void dataFetcher(int tagGroup) throws Exception {
    ArrayList tagGroupArray = (ArrayList) tagManagers.get(tagGroup);
    if (tagGroupArray != null) {
      // for each tag in the tag group, record a new value
      for (int tagNum = 0; tagNum < tagGroupArray.size(); tagNum++) {
        TagInfo currentTag = ((TagInfo) ((ArrayList) tagGroupList.get(tagGroup)).get(tagNum));
        ((RealTimeTagDataPointManager) tagGroupArray.get(tagNum)).recordCurentTagValue(currentTag);
        final int numTagsBeforeSleep = 1;
        final int sleepMilliseconds = 1;
        if ((tagNum % numTagsBeforeSleep) == 0) {
          try {
            Thread.sleep(sleepMilliseconds);
          } catch (InterruptedException e) {
            Logger.LOG_WARN("Unable to sleep thread between real time data reads.");
            Logger.LOG_EXCEPTION(e);
          }
        }
      }
    }
  }
}
