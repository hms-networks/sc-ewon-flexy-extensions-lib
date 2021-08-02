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
      TagType type) {
    this.type = type;
    this.historicalLogEnabled = historicalLogEnabled;
    this.realTimeLogEnabled = realTimeLogEnabled;
    this.id = id;
    this.name = name;
    this.description = description;
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
   * @param tagGroups list of tag groups
   * @param type tag type
   */
  public TagInfo(
      int id,
      String name,
      String description,
      boolean historicalLogEnabled,
      boolean realTimeLogEnabled,
      ArrayList tagGroups,
      TagType type) {
    this.type = type;
    this.historicalLogEnabled = historicalLogEnabled;
    this.realTimeLogEnabled = realTimeLogEnabled;
    this.id = id;
    this.name = name;
    this.description = description;
    this.tagGroups = tagGroups;
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
}
