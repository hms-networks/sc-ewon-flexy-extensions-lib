package com.hms_networks.americas.sc.extensions.taginfo;

import java.util.ArrayList;

/**
 * Class to hold information about a tag and its configuration when the {@link TagType} is {@link
 * TagType#INTEGER_MAPPED_STRING}.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.0.0
 */
public class TagInfoEnumeratedIntToString extends TagInfo {

  /** Array of {@link String} values which are mapped to tag values by the array index. */
  private final String[] enumeratedStringValueMapping;

  /**
   * Constructor for tag class with group {@code boolean}s and enumerated {@code int} to {@link
   * String} value mappings.
   *
   * @param id tag ID
   * @param name tag name
   * @param description tag description
   * @param historicalLogEnabled boolean if historical logging enabled
   * @param realTimeLogEnabled boolean if real-time logging enabled
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
   * @param enumeratedStringValueMapping array of {@link String}s where the {@code int} value of the
   *     tag represents the {@link String} array index
   */
  public TagInfoEnumeratedIntToString(
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
      float alarmLevelDeadBand,
      String[] enumeratedStringValueMapping) {
    super(
        id,
        name,
        description,
        historicalLogEnabled,
        realTimeLogEnabled,
        isInGroupA,
        isInGroupB,
        isInGroupC,
        isInGroupD,
        type,
        unit,
        alarmHint,
        alarmLow,
        alarmHigh,
        alarmLowLow,
        alarmHighHigh,
        alarmTimeDeadBand,
        alarmLevelDeadBand);
    this.enumeratedStringValueMapping = enumeratedStringValueMapping;
  }

  /**
   * Constructor for tag class with supplied tag groups and enumerated {@code int} to {@link String}
   * value mappings.
   *
   * @param id tag ID
   * @param name tag name
   * @param description tag description
   * @param historicalLogEnabled boolean if historical logging enabled
   * @param realTimeLogEnabled boolean if real-time logging enabled
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
   * @param enumeratedStringValueMapping array of {@link String}s where the {@code int} value of the
   *     tag represents the {@link String} array index
   */
  public TagInfoEnumeratedIntToString(
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
      float alarmLevelDeadBand,
      String[] enumeratedStringValueMapping) {
    super(
        id,
        name,
        description,
        historicalLogEnabled,
        realTimeLogEnabled,
        tagGroups,
        type,
        unit,
        alarmHint,
        alarmLow,
        alarmHigh,
        alarmLowLow,
        alarmHighHigh,
        alarmTimeDeadBand,
        alarmLevelDeadBand);
    this.enumeratedStringValueMapping = enumeratedStringValueMapping;
  }

  /**
   * Get the enumerated int to {@link String} value mapping for this tag.
   *
   * @return the enumerated int to {@link String} value mapping for this tag.
   */
  public String[] getEnumeratedStringValueMapping() {
    return enumeratedStringValueMapping;
  }
}
