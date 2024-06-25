package com.hms_networks.americas.sc.extensions.datapoint;

/**
 * Class object for a {@link DataPoint} with an integer mapped (enumerated) {@link String}. Used to
 * provide translated {@link String} values from an integer tag using an array of mappings.
 *
 * <p>Example (Integer Tag Value, Mapped String Value): <br>
 * 0 = "Off" <br>
 * 1 = "On, Loading" <br>
 * 2 = "On, Ready" <br>
 * 3 = "On, Processing" <br>
 * 4 = "On, Completed" <br>
 * 5 = "On, Failed" <br>
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.0.0
 */
public class DataPointIntegerMappedString extends DataPointString {

  /**
   * Constructor for an integer mapped (enumerated) {@link String} data point with specified
   * quality.
   *
   * @param tagName data point tag name
   * @param tagId data point tag ID
   * @param tagUnit data point tag unit
   * @param value data point value
   * @param timestampInt data point timestamp as an {@code int}, representing seconds since the
   *     epoch
   * @param timestampString data point timestamp as a {@link String}, in ISO-8601 format
   * @param quality data point quality
   * @param enumMapping integer enumerated {@link String} mapping(s)
   */
  public DataPointIntegerMappedString(
      String tagName,
      int tagId,
      String tagUnit,
      int value,
      int timestampInt,
      String timestampString,
      DataQuality quality,
      String[] enumMapping) {
    super(tagName, tagId, tagUnit, enumMapping[value], timestampInt, timestampString, quality);
  }

  /**
   * Constructor for an integer mapped (enumerated) {@link String} data point without specified
   * quality.
   *
   * @param tagName data point tag name
   * @param tagId data point tag ID
   * @param tagUnit data point tag Unit
   * @param value data point value
   * @param timestampInt data point timestamp as an {@code int}, representing seconds since the
   *     epoch
   * @param timestampString data point timestamp as a {@link String}, in ISO-8601 format
   * @param enumMapping integer enumerated {@link String} mapping(s)
   */
  public DataPointIntegerMappedString(
      String tagName,
      int tagId,
      String tagUnit,
      int value,
      int timestampInt,
      String timestampString,
      String[] enumMapping) {
    super(tagName, tagId, tagUnit, enumMapping[value], timestampInt, timestampString);
  }

  /**
   * Get the data point type.
   *
   * @return data point type
   */
  public DataType getType() {
    return DataType.INTEGER_MAPPED_STRING;
  }

  /**
   * Clones the data point.
   *
   * @param tagName The tag name to use for the cloned data point
   * @return cloned data point with the specified tag name
   * @throws CloneNotSupportedException if the data point cannot be cloned
   */
  public DataPoint clone(String tagName) throws CloneNotSupportedException {
    DataPoint cloned = (DataPoint) clone();
    ((DataPointIntegerMappedString) cloned).tagName = tagName;
    return cloned;
  }
}
