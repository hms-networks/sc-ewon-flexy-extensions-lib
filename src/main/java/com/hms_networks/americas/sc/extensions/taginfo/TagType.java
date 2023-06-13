package com.hms_networks.americas.sc.extensions.taginfo;

/**
 * Class to represent tag type in a similar fashion to enums in Java 1.5+.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.0.0
 */
public class TagType {

  /** Ewon integer value for boolean type. */
  private static final int BOOLEAN_INT = 0;

  /** Ewon integer value for floating point type. */
  private static final int FLOAT_INT = 1;

  /** Ewon integer value for integer type. */
  private static final int INTEGER_INT = 2;

  /** Ewon integer value for DWORD type. */
  private static final int DWORD_INT = 3;

  /** Ewon integer value for string type. */
  private static final int STRING_INT = 6;

  /** Ewon integer value for integer mapped string type. */
  public static final int INTEGER_MAPPED_STRING_INT = 7;

  /** Public instance of {@link TagType} representing boolean type. */
  public static final TagType BOOLEAN = new TagType(BOOLEAN_INT);

  /** Public instance of {@link TagType} representing floating point type. */
  public static final TagType FLOAT = new TagType(FLOAT_INT);

  /** Public instance of {@link TagType} representing integer type. */
  public static final TagType INTEGER = new TagType(INTEGER_INT);

  /** Public instance of {@link TagType} representing DWORD type. */
  public static final TagType DWORD = new TagType(DWORD_INT);

  /** Public instance of {@link TagType} representing string type. */
  public static final TagType STRING = new TagType(STRING_INT);

  /** Public instance of {@link TagType} representing integer mapped string type. */
  public static final TagType INTEGER_MAPPED_STRING = new TagType(INTEGER_MAPPED_STRING_INT);

  /** Instance tag type integer. */
  private final int typeInt;

  /**
   * Private (internal) constructor for creating an instance of {@link TagType} with a tag type
   * integer.
   *
   * <p>Note: Tag type integers shall be unique.
   *
   * @param typeInt integer to represent tag type.
   */
  private TagType(int typeInt) {
    this.typeInt = typeInt;
  }

  /**
   * Returns the integer value of the tag type.
   *
   * @return tag type integer
   */
  public int getTypeInt() {
    return typeInt;
  }

  /**
   * Helper method to get the equivalent {@link TagType} for the supplied Ewon tag type integer. If
   * the specified tag type integer is not valid, null will be returned.
   *
   * @param tagType Ewon tag type integer
   * @return tag type for specified int
   */
  public static TagType getTagTypeFromInt(int tagType) {
    if (tagType == BOOLEAN_INT) {
      return BOOLEAN;
    } else if (tagType == FLOAT_INT) {
      return FLOAT;
    } else if (tagType == INTEGER_INT) {
      return INTEGER;
    } else if (tagType == DWORD_INT) {
      return DWORD;
    } else if (tagType == STRING_INT) {
      return STRING;
    } else if (tagType == INTEGER_MAPPED_STRING_INT) {
      return INTEGER_MAPPED_STRING;
    } else {
      return null;
    }
  }
}
