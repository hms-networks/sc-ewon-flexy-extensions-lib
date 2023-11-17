package com.hms_networks.americas.sc.extensions.taginfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class to hold information about a tag and its configuration.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.0.0
 */
public class TagInfo {

  /** Tag type */
  private final TagType type;

  /** Boolean if historical logging enabled */
  private final boolean historicalLogEnabled;

  /** Boolean if real time logging enabled */
  private final boolean realTimeLogEnabled;

  /** Tag ID */
  private final int id;

  /** Tag name */
  private final String name;

  /** Tag description */
  private final String description;

  /** List of tag groups */
  private final ArrayList tagGroups;

  /** Tag unit */
  private final String unit;

  /** Tag alarm hint */
  private final String alarmHint;

  /** Tag alarm low threshold */
  private final float alarmLow;

  /** Tag alarm high threshold */
  private final float alarmHigh;

  /** Tag alarm low/low threshold */
  private final float alarmLowLow;

  /** Tag alarm high/high threshold */
  private final float alarmHighHigh;

  /** Tag alarm time dead band (delay) */
  private final int alarmTimeDeadBand;

  /** Tag alarm level dead band (value) */
  private final float alarmLevelDeadBand;

  /**
   * Constructor for tag class with group booleans.
   *
   * @param id tag ID
   * @param name tag name
   * @param description tag description
   * @param historicalLogEnabled boolean if historical logging enabled
   * @param realTimeLogEnabled boolean if realtime logging enabled
   * @param isInGroupA boolean if tag in group A
   * @param isInGroupB boolean if tag in group B
   * @param isInGroupC boolean if tag in group C
   * @param isInGroupD boolean if tag in group D
   * @param type tag type
   * @param unit tag unit
   * @param alarmHint tag alarm hint
   * @param alarmLow tag alarm low threshold
   * @param alarmHigh tag alarm high threshold
   * @param alarmLowLow tag alarm low/low threshold
   * @param alarmHighHigh tag alarm high/high threshold
   * @param alarmTimeDeadBand tag alarm time dead band (delay)
   * @param alarmLevelDeadBand tag alarm level dead band (value)
   */
  public TagInfo(
      int id,
      String name,
      String description,
      boolean historicalLogEnabled,
      boolean realTimeLogEnabled,
      boolean isInGroupA,
      boolean isInGroupB,
      boolean isInGroupC,
      boolean isInGroupD,
      TagType type,
      String unit,
      String alarmHint,
      float alarmLow,
      float alarmHigh,
      float alarmLowLow,
      float alarmHighHigh,
      int alarmTimeDeadBand,
      float alarmLevelDeadBand) {
    this.type = type;
    this.historicalLogEnabled = historicalLogEnabled;
    this.realTimeLogEnabled = realTimeLogEnabled;
    this.id = id;
    this.name = name;
    this.description = description;
    this.unit = unit;
    this.alarmHint = alarmHint;
    this.alarmLow = alarmLow;
    this.alarmHigh = alarmHigh;
    this.alarmLowLow = alarmLowLow;
    this.alarmHighHigh = alarmHighHigh;
    this.alarmTimeDeadBand = alarmTimeDeadBand;
    this.alarmLevelDeadBand = alarmLevelDeadBand;
    this.tagGroups = new ArrayList();

    if (isInGroupA) {
      this.tagGroups.add(TagGroup.A);
    }
    if (isInGroupB) {
      this.tagGroups.add(TagGroup.B);
    }
    if (isInGroupC) {
      this.tagGroups.add(TagGroup.C);
    }
    if (isInGroupD) {
      this.tagGroups.add(TagGroup.D);
    }
  }

  /**
   * Constructor for tag class with supplied tag groups.
   *
   * @param id tag ID
   * @param name tag name
   * @param description tag description
   * @param historicalLogEnabled boolean if historical logging enabled
   * @param realTimeLogEnabled boolean if realtime logging enabled
   * @param unit tag unit
   * @param tagGroups list of tag groups
   * @param type tag type
   * @param unit tag unit
   * @param alarmHint tag alarm hint
   * @param alarmLow tag alarm low threshold
   * @param alarmHigh tag alarm high threshold
   * @param alarmLowLow tag alarm low/low threshold
   * @param alarmHighHigh tag alarm high/high threshold
   * @param alarmTimeDeadBand tag alarm time dead band (delay)
   * @param alarmLevelDeadBand tag alarm level dead band (value)
   */
  public TagInfo(
      int id,
      String name,
      String description,
      boolean historicalLogEnabled,
      boolean realTimeLogEnabled,
      ArrayList tagGroups,
      TagType type,
      String unit,
      String alarmHint,
      float alarmLow,
      float alarmHigh,
      float alarmLowLow,
      float alarmHighHigh,
      int alarmTimeDeadBand,
      float alarmLevelDeadBand) {
    this.type = type;
    this.historicalLogEnabled = historicalLogEnabled;
    this.realTimeLogEnabled = realTimeLogEnabled;
    this.id = id;
    this.name = name;
    this.description = description;
    this.tagGroups = tagGroups;
    this.unit = unit;
    this.alarmHint = alarmHint;
    this.alarmLow = alarmLow;
    this.alarmHigh = alarmHigh;
    this.alarmLowLow = alarmLowLow;
    this.alarmHighHigh = alarmHighHigh;
    this.alarmTimeDeadBand = alarmTimeDeadBand;
    this.alarmLevelDeadBand = alarmLevelDeadBand;
  }

  /**
   * Get the tag ID
   *
   * @return tag ID
   */
  public int getId() {
    return id;
  }

  /**
   * Get the tag type
   *
   * @return tag type
   */
  public TagType getType() {
    return type;
  }

  /**
   * Get the tag description
   *
   * @return tag description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Get the tag name
   *
   * @return tag name
   */
  public String getName() {
    return name;
  }

  /**
   * Get if the tag historical logging is enabled
   *
   * @return true if historical logging enabled
   */
  public boolean isHistoricalLogEnabled() {
    return historicalLogEnabled;
  }

  /**
   * Get if the tag real time logging is enabled
   *
   * @return true if real time logging enabled
   */
  public boolean isRealTimeLogEnabled() {
    return realTimeLogEnabled;
  }

  /**
   * Get the tag's groups as a list of {@link TagGroup}s.
   *
   * @return tag groups list
   */
  public List getTagGroups() {
    return Collections.unmodifiableList(tagGroups);
  }

  /**
   * Get the tag unit
   *
   * @return tag unit
   */
  public String getUnit() {
    return unit;
  }

  /**
   * Get the tag alarm hint
   *
   * @return tag alarm hint
   */
  public String getAlarmHint() {
    return alarmHint;
  }

  /**
   * Get the tag alarm low threshold
   *
   * @return tag alarm low threshold
   */
  public float getAlarmLow() {
    return alarmLow;
  }

  /**
   * Get the tag alarm high threshold
   *
   * @return tag alarm high threshold
   */
  public float getAlarmHigh() {
    return alarmHigh;
  }

  /**
   * Get the tag alarm low/low threshold
   *
   * @return tag alarm low/low threshold
   */
  public float getAlarmLowLow() {
    return alarmLowLow;
  }

  /**
   * Get the tag alarm high/high threshold
   *
   * @return tag alarm high/high threshold
   */
  public float getAlarmHighHigh() {
    return alarmHighHigh;
  }

  /**
   * Get the tag alarm time dead band (delay)
   *
   * @return tag alarm time dead band (delay)
   */
  public int getAlarmTimeDeadBand() {
    return alarmTimeDeadBand;
  }

  /**
   * Get the tag alarm level dead band (value)
   *
   * @return tag alarm level dead band (value)
   */
  public float getAlarmLevelDeadBand() {
    return alarmLevelDeadBand;
  }
}
