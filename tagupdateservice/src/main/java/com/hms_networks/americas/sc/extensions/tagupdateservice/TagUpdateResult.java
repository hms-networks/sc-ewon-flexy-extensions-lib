package com.hms_networks.americas.sc.extensions.tagupdateservice;

/**
 * Class to represent a tag update result in a similar fashion to enums in Java 1.5+.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.0.0
 */
public class TagUpdateResult {

  /** Constant integer value to represent the initial tag update result value. */
  public static final int TAG_UPDATE_RESULT_INITIAL = 0;

  /** Constant integer value to represent the tag update result value of 'HTTP started'. */
  public static final int TAG_UPDATE_RESULT_HTTP_STARTED = 1;

  /** Constant integer value to represent the tag update result value of 'success'. */
  public static final int TAG_UPDATE_RESULT_SUCCESS = 2;

  /** Constant integer value to represent the tag update result value of 'connection error'. */
  public static final int TAG_UPDATE_RESULT_CONNECTION_ERROR = 3;

  /** Constant integer value to represent the tag update result value of 'Ewon error'. */
  public static final int TAG_UPDATE_RESULT_EWON_ERROR = 4;

  /** Constant integer value to represent the tag update result value of 'verification failure'. */
  public static final int TAG_UPDATE_RESULT_VERIFY_FAILURE = 5;

  /** Constant integer value to represent the tag update result value of 'apply failure'. */
  public static final int TAG_UPDATE_RESULT_APPLY_FAILURE = 6;

  /** Constant integer value to represent the tag update result value of 'missing tags'. */
  public static final int TAG_UPDATE_RESULT_MISSING_TAGS = 7;

  /** Constant integer value to represent the tag update result value of 'mismatched tag types'. */
  public static final int TAG_UPDATE_RESULT_MISMATCHED_TAG_TYPES = 8;

  /**
   * Public instance of {@link TagUpdateResult} representing the initial tag update result value.
   */
  public static final TagUpdateResult INTITIAL = new TagUpdateResult(TAG_UPDATE_RESULT_INITIAL);

  /**
   * Public instance of {@link TagUpdateResult} representing the 'HTTP started' tag update result
   * value.
   */
  public static final TagUpdateResult HTTP_STARTED =
      new TagUpdateResult(TAG_UPDATE_RESULT_HTTP_STARTED);

  /**
   * Public instance of {@link TagUpdateResult} representing the 'success' tag update result value.
   */
  public static final TagUpdateResult SUCCESS = new TagUpdateResult(TAG_UPDATE_RESULT_SUCCESS);

  /**
   * Public instance of {@link TagUpdateResult} representing the 'connection error' tag update
   * result value.
   */
  public static final TagUpdateResult CONNECTION_ERROR =
      new TagUpdateResult(TAG_UPDATE_RESULT_CONNECTION_ERROR);

  /**
   * Public instance of {@link TagUpdateResult} representing the 'Ewon error' tag update result
   * value.
   */
  public static final TagUpdateResult EWON_ERROR =
      new TagUpdateResult(TAG_UPDATE_RESULT_EWON_ERROR);

  /**
   * Public instance of {@link TagUpdateResult} representing the 'verification failure' tag update
   * result value.
   */
  public static final TagUpdateResult VERIFY_FAILURE =
      new TagUpdateResult(TAG_UPDATE_RESULT_VERIFY_FAILURE);

  /**
   * Public instance of {@link TagUpdateResult} representing the 'apply failure' tag update result
   * value.
   */
  public static final TagUpdateResult APPLY_FAILURE =
      new TagUpdateResult(TAG_UPDATE_RESULT_APPLY_FAILURE);

  /**
   * Public instance of {@link TagUpdateResult} representing the 'missing tags' tag update result
   * value.
   */
  public static final TagUpdateResult MISSING_TAGS =
      new TagUpdateResult(TAG_UPDATE_RESULT_MISSING_TAGS);

  /**
   * Public instance of {@link TagUpdateResult} representing the 'mismatched tag types' tag update
   * result value.
   */
  public static final TagUpdateResult MISMATCHED_TAG_TYPES =
      new TagUpdateResult(TAG_UPDATE_RESULT_MISMATCHED_TAG_TYPES);

  /** Instance tag update result integer. */
  private final int result;

  /**
   * Private (internal) constructor for creating an instance of {@link TagUpdateResult} with a tag
   * update result integer.
   *
   * <p>Note: Tag update result integers shall be unique.
   *
   * @param result integer to represent tag update result.
   */
  public TagUpdateResult(int result) {
    this.result = result;
  }

  /**
   * Gets the integer value which represents the tag update result.
   *
   * @return tag update result integer value
   */
  public int getIntegerResultValue() {
    return result;
  }
}
