package com.hms_networks.americas.sc.extensions.alarms;

import com.ewon.ewonitf.EvtTagAlarmListener;
import com.hms_networks.americas.sc.extensions.taginfo.TagInfoManager;
import com.hms_networks.americas.sc.extensions.taginfo.TagType;
import com.hms_networks.americas.sc.extensions.time.LocalTimeOffsetCalculator;

import java.util.Date;

/**
 * Abstract class for monitoring and handling tag alarms.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.0.0
 */
public abstract class AlarmMonitor extends EvtTagAlarmListener {

  /** Tag alarm event method. This method is invoked for each tag alarm that occurs. */
  public void callTagChanged() {
    // Get tag alarm information
    long utcTime = System.currentTimeMillis();
    long localTime = utcTime - LocalTimeOffsetCalculator.getLocalTimeOffsetMilliseconds();
    String alarmUtcTimestamp = new Date(utcTime).toString();
    String alarmLocalTimestamp = new Date(localTime).toString();
    String alarmedTagName = getTagName();
    String alarmedTagValue = getTagValueAsString();
    String alarmStatus = getAlarmStatusStringFromInt(getAlarmStatus());
    String alarmType = getAlarmTypeStringFromInt(getAlarmType());
    int alarmedTagId = getTagId();

    // TagInfo objects are stored with the "current ID" - "lowest ID seen" used as the index
    int offset = TagInfoManager.getLowestTagIdSeen();
    TagType alarmedTagType = TagInfoManager.getTagInfoArray()[alarmedTagId - offset].getType();

    // Call alarm handler with information
    onTagAlarm(
        alarmedTagName,
        alarmedTagId,
        alarmedTagType,
        alarmedTagValue,
        alarmType,
        alarmStatus,
        alarmUtcTimestamp,
        alarmLocalTimestamp);
  }

  /**
   * Abstract method to be implemented to handle tag alarms using the specified information.
   *
   * @param alarmedTagName alarmed tag name
   * @param alarmedTagId alarmed tag ID
   * @param alarmedTagType alarmed tag type
   * @param alarmedTagValue alarmed tag value
   * @param alarmType alarm type
   * @param alarmStatus alarm status
   * @param alarmUtcTimestamp alarm timestamp (UTC)
   * @param alarmLocalTimestamp alarm timestamp (local time)
   */
  public abstract void onTagAlarm(
      String alarmedTagName,
      int alarmedTagId,
      TagType alarmedTagType,
      String alarmedTagValue,
      String alarmType,
      String alarmStatus,
      String alarmUtcTimestamp,
      String alarmLocalTimestamp);

  /**
   * Gets a string representation of the specified alarm status integer.
   *
   * @param alarmStatusInt alarm status integer
   * @return alarm status string
   */
  public static String getAlarmStatusStringFromInt(int alarmStatusInt) {
    String alarmStatus = AlarmConstants.ALARM_STATUS_UNKNOWN_STRING;
    if (alarmStatusInt == ALARM_STATUS_ACK) {
      alarmStatus = AlarmConstants.ALARM_STATUS_ACK_STRING;
    } else if (alarmStatusInt == ALARM_STATUS_ALM) {
      alarmStatus = AlarmConstants.ALARM_STATUS_ALM_STRING;
    } else if (alarmStatusInt == ALARM_STATUS_NONE) {
      alarmStatus = AlarmConstants.ALARM_STATUS_NONE_STRING;
    } else if (alarmStatusInt == ALARM_STATUS_RTN) {
      alarmStatus = AlarmConstants.ALARM_STATUS_RTN_STRING;
    }
    return alarmStatus;
  }

  /**
   * Gets a string representation of the specified alarm type integer.
   *
   * @param alarmTypeInt alarm type integer
   * @return alarm type string
   */
  public static String getAlarmTypeStringFromInt(int alarmTypeInt) {
    String alarmType = AlarmConstants.ALARM_TYPE_UNKNOWN_STRING;
    if (alarmTypeInt == ALARM_TYPE_NONE) {
      alarmType = AlarmConstants.ALARM_TYPE_NONE_STRING;
    } else if (alarmTypeInt == ALARM_TYPE_LOW_LOW) {
      alarmType = AlarmConstants.ALARM_TYPE_LOW_LOW_STRING;
    } else if (alarmTypeInt == ALARM_TYPE_LOW) {
      alarmType = AlarmConstants.ALARM_TYPE_LOW_STRING;
    } else if (alarmTypeInt == ALARM_TYPE_LEVEL) {
      alarmType = AlarmConstants.ALARM_TYPE_LEVEL_STRING;
    } else if (alarmTypeInt == ALARM_TYPE_HIGH) {
      alarmType = AlarmConstants.ALARM_TYPE_HIGH_STRING;
    } else if (alarmTypeInt == ALARM_TYPE_HIGH_HIGH) {
      alarmType = AlarmConstants.ALARM_TYPE_HIGH_HIGH_STRING;
    }
    return alarmType;
  }
}
