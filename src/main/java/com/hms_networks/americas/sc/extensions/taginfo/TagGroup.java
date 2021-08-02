package com.hms_networks.americas.sc.extensions.taginfo;

/**
 * Class to represent a tag group in a similar fashion to enums in Java 1.5+.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.0.0
 */
public class TagGroup {

  /** String assigned to represent tag group A. */
  private static final String GROUP_A_STR = "A";

  /** String assigned to represent tag group B. */
  private static final String GROUP_B_STR = "B";

  /** String assigned to represent tag group C. */
  private static final String GROUP_C_STR = "C";

  /** String assigned to represent tag group D. */
  private static final String GROUP_D_STR = "D";

  /** Public instance of {@link TagGroup} representing tag group A. */
  public static final TagGroup A = new TagGroup(GROUP_A_STR);

  /** Public instance of {@link TagGroup} representing tag group B. */
  public static final TagGroup B = new TagGroup(GROUP_B_STR);

  /** Public instance of {@link TagGroup} representing tag group C. */
  public static final TagGroup C = new TagGroup(GROUP_C_STR);

  /** Public instance of {@link TagGroup} representing tag group D. */
  public static final TagGroup D = new TagGroup(GROUP_D_STR);

  /** Instance tag group string */
  private final String groupLetter;

  /**
   * Private (internal) constructor for creating an instance of {@link TagGroup} with a tag group
   * letter.
   *
   * <p>Note: Tag group letter shall be unique.
   *
   * @param groupLetter letter to represent tag group
   */
  private TagGroup(String groupLetter) {
    this.groupLetter = groupLetter;
  }

  /**
   * Get the tag group letter as a string.
   *
   * @return tag group string
   */
  public String toString() {
    return groupLetter;
  }
}
