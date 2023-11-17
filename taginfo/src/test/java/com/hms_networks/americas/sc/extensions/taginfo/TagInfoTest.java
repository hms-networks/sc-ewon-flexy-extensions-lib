package com.hms_networks.americas.sc.extensions.taginfo;

import java.util.Random;
import junit.framework.TestCase;

/**
 * Library test class for the {@link TagInfo} class in the Ewon Flexy Extensions Library.
 *
 * <p>The {@link #testBasicTagInfo()} method was migrated from the root {@code LibraryTest} class in
 * {@code v1.15.7} to better organize the test suite classes and methods.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.15.7
 * @version 1.0.1
 */
public class TagInfoTest extends TestCase {

  /**
   * Random generator for creating random test values.
   *
   * @since 1.0.0
   */
  private static final Random RANDOM = new Random();

  /**
   * Object used for testing in {@link #testBasicTagInfo()}.
   *
   * @since 1.0.0
   */
  protected TagInfo basicTagInfoTestObject = null;

  /**
   * Tag ID used for testing in {@link #testBasicTagInfo()}.
   *
   * @since 1.0.0
   */
  protected int basicTagInfoTestId = RANDOM.nextInt();

  /**
   * Tag name used for testing in {@link #testBasicTagInfo()}.
   *
   * @since 1.0.0
   */
  protected String basicTagInfoTestName = String.valueOf(RANDOM.nextFloat());

  /**
   * Tag description used for testing in {@link #testBasicTagInfo()}.
   *
   * @since 1.0.0
   */
  protected String basicTagInfoTestDescription = String.valueOf(RANDOM.nextFloat());

  /**
   * Tag historical log enabled flag used for testing in {@link #testBasicTagInfo()}.
   *
   * @since 1.0.0
   */
  protected boolean basicTagInfoTestHistoricalLogEnabled = RANDOM.nextBoolean();

  /**
   * Tag realtime log enabled flag used for testing in {@link #testBasicTagInfo()}.
   *
   * @since 1.0.0
   */
  protected boolean basicTagInfoTestRealtimeLogEnabled = RANDOM.nextBoolean();

  /**
   * Tag type used for testing in {@link #testBasicTagInfo()}.
   *
   * @since 1.0.0
   */
  protected TagType basicTagInfoTestTagType = TagType.getTagTypeFromInt(RANDOM.nextInt(3));

  /**
   * Tag in group A boolean flag used for testing in {@link #testBasicTagInfo()}.
   *
   * @since 1.0.0
   */
  protected boolean basicTagInfoTestInGroupA = RANDOM.nextBoolean();

  /**
   * Tag in group B boolean flag used for testing in {@link #testBasicTagInfo()}.
   *
   * @since 1.0.0
   */
  protected boolean basicTagInfoTestInGroupB = RANDOM.nextBoolean();

  /**
   * Tag in group C boolean flag used for testing in {@link #testBasicTagInfo()}.
   *
   * @since 1.0.0
   */
  protected boolean basicTagInfoTestInGroupC = RANDOM.nextBoolean();

  /**
   * Tag in group D boolean flag used for testing in {@link #testBasicTagInfo()}.
   *
   * @since 1.0.0
   */
  protected boolean basicTagInfoTestInGroupD = RANDOM.nextBoolean();

  /**
   * Tag unit used for testing in {@link #testBasicTagInfo()}.
   *
   * @since 1.0.0
   */
  protected String basicTagInfoTestUnit = String.valueOf(RANDOM.nextFloat());

  /**
   * Tag alarm hint used for testing in {@link #testBasicTagInfo()}.
   *
   * @since 1.0.0
   */
  protected String basicTagInfoTestAlarmHint = String.valueOf(RANDOM.nextFloat());

  /**
   * Tag alarm low threshold used for testing in {@link #testBasicTagInfo()}.
   *
   * @since 1.0.0
   */
  protected float basicTagInfoTestAlarmLow = RANDOM.nextFloat();

  /**
   * Tag alarm high threshold used for testing in {@link #testBasicTagInfo()}.
   *
   * @since 1.0.0
   */
  protected float basicTagInfoTestAlarmHigh = RANDOM.nextFloat();

  /**
   * Tag alarm low/low threshold used for testing in {@link #testBasicTagInfo()}.
   *
   * @since 1.0.0
   */
  protected float basicTagInfoTestAlarmLowLow = RANDOM.nextFloat();

  /**
   * Tag alarm high/high threshold used for testing in {@link #testBasicTagInfo()}.
   *
   * @since 1.0.0
   */
  protected float basicTagInfoTestAlarmHighHigh = RANDOM.nextFloat();

  /**
   * Tag alarm time dead band (delay) used for testing in {@link #testBasicTagInfo()}.
   *
   * @since 1.0.0
   */
  protected int basicTagInfoTestAlarmTimeDeadBand = RANDOM.nextInt();

  /**
   * Tag alarm level dead band (value) used for testing in {@link #testBasicTagInfo()}.
   *
   * @since 1.0.0
   */
  protected float basicTagInfoTestAlarmLevelDeadBand = RANDOM.nextFloat();

  /**
   * Set up required variables, classes or other resources before testing is run.
   *
   * @throws Exception if unable to perform setup
   * @since 1.0.0
   */
  protected void setUp() throws Exception {
    // Set up basic tag info test
    basicTagInfoTestObject =
        new TagInfo(
            basicTagInfoTestId,
            basicTagInfoTestName,
            basicTagInfoTestDescription,
            basicTagInfoTestHistoricalLogEnabled,
            basicTagInfoTestRealtimeLogEnabled,
            basicTagInfoTestInGroupA,
            basicTagInfoTestInGroupB,
            basicTagInfoTestInGroupC,
            basicTagInfoTestInGroupD,
            basicTagInfoTestTagType,
            basicTagInfoTestUnit,
            basicTagInfoTestAlarmHint,
            basicTagInfoTestAlarmLow,
            basicTagInfoTestAlarmHigh,
            basicTagInfoTestAlarmLowLow,
            basicTagInfoTestAlarmHighHigh,
            basicTagInfoTestAlarmTimeDeadBand,
            basicTagInfoTestAlarmLevelDeadBand);

    super.setUp();
  }

  /**
   * Cleanup required variables, classes, or other resources after testing has run.
   *
   * @throws Exception if unable to perform cleanup
   * @since 1.0.0
   */
  protected void tearDown() throws Exception {
    // Clean up test variables/classes/etc
    basicTagInfoTestObject = null;

    super.tearDown();
  }

  /**
   * Basic test method for {@link TagInfo} objects.
   *
   * <p>This test performs basic checks to ensure that {@link
   * TagInfo} objects store data properly.
   *
   * @since 1.0.0
   */
  public void testBasicTagInfo() {
    // Check that basic tag info object set up
    assertNotNull(basicTagInfoTestObject);

    // Check that field contents match in object
    assertEquals(basicTagInfoTestObject.getId(), basicTagInfoTestId);
    assertEquals(basicTagInfoTestObject.getName(), basicTagInfoTestName);
    assertEquals(basicTagInfoTestObject.getDescription(), basicTagInfoTestDescription);
    assertEquals(
        basicTagInfoTestObject.isHistoricalLogEnabled(), basicTagInfoTestHistoricalLogEnabled);
    assertEquals(basicTagInfoTestObject.isRealTimeLogEnabled(), basicTagInfoTestRealtimeLogEnabled);
    assertEquals(basicTagInfoTestObject.getType(), basicTagInfoTestTagType);

    if (basicTagInfoTestInGroupA) {
      assertTrue(basicTagInfoTestObject.getTagGroups().contains(TagGroup.A));
    }

    if (basicTagInfoTestInGroupB) {
      assertTrue(basicTagInfoTestObject.getTagGroups().contains(TagGroup.B));
    }

    if (basicTagInfoTestInGroupC) {
      assertTrue(basicTagInfoTestObject.getTagGroups().contains(TagGroup.C));
    }

    if (basicTagInfoTestInGroupD) {
      assertTrue(basicTagInfoTestObject.getTagGroups().contains(TagGroup.D));
    }
  }
}
