import com.hms_networks.americas.sc.extensions.taginfo.TagGroup;
import com.hms_networks.americas.sc.extensions.taginfo.TagInfo;
import com.hms_networks.americas.sc.extensions.taginfo.TagType;
import java.util.Random;
import junit.framework.TestCase;

/**
 * Library test class for Ewon Flexy Extensions Library.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.0.0
 * @version 0.0.1
 */
public class LibraryTest extends TestCase {

  /** Random generator for creating random test values. */
  private Random randomGenerator = new Random();

  /** Object used for testing in {@link #testBasicTagInfo()}. */
  protected TagInfo basicTagInfoTestObject = null;

  /** Tag ID used for testing in {@link #testBasicTagInfo()}. */
  protected int basicTagInfoTestId = randomGenerator.nextInt();

  /** Tag name used for testing in {@link #testBasicTagInfo()}. */
  protected String basicTagInfoTestName = String.valueOf(randomGenerator.nextFloat());

  /** Tag description used for testing in {@link #testBasicTagInfo()}. */
  protected String basicTagInfoTestDescription = String.valueOf(randomGenerator.nextFloat());

  /** Tag historical log enabled flag used for testing in {@link #testBasicTagInfo()}. */
  protected boolean basicTagInfoTestHistoricalLogEnabled = randomGenerator.nextBoolean();

  /** Tag realtime log enabled flag used for testing in {@link #testBasicTagInfo()}. */
  protected boolean basicTagInfoTestRealtimeLogEnabled = randomGenerator.nextBoolean();

  /** Tag type used for testing in {@link #testBasicTagInfo()}. */
  protected TagType basicTagInfoTestTagType = TagType.getTagTypeFromInt(randomGenerator.nextInt(3));

  /** Tag in group A boolean flag used for testing in {@link #testBasicTagInfo()}. */
  protected boolean basicTagInfoTestInGroupA = randomGenerator.nextBoolean();

  /** Tag in group B boolean flag used for testing in {@link #testBasicTagInfo()}. */
  protected boolean basicTagInfoTestInGroupB = randomGenerator.nextBoolean();

  /** Tag in group C boolean flag used for testing in {@link #testBasicTagInfo()}. */
  protected boolean basicTagInfoTestInGroupC = randomGenerator.nextBoolean();

  /** Tag in group D boolean flag used for testing in {@link #testBasicTagInfo()}. */
  protected boolean basicTagInfoTestInGroupD = randomGenerator.nextBoolean();

  /** Tag unit used for testing in {@link #testBasicTagInfo()}. */
  protected String basicTagInfoTestUnit = String.valueOf(randomGenerator.nextFloat());

  /** Tag alarm hint used for testing in {@link #testBasicTagInfo()}. */
  protected String basicTagInfoTestAlarmHint = String.valueOf(randomGenerator.nextFloat());

  /** Tag alarm low threshold used for testing in {@link #testBasicTagInfo()}. */
  protected float basicTagInfoTestAlarmLow = randomGenerator.nextFloat();

  /** Tag alarm high threshold used for testing in {@link #testBasicTagInfo()}. */
  protected float basicTagInfoTestAlarmHigh = randomGenerator.nextFloat();

  /** Tag alarm low/low threshold used for testing in {@link #testBasicTagInfo()}. */
  protected float basicTagInfoTestAlarmLowLow = randomGenerator.nextFloat();

  /** Tag alarm high/high threshold used for testing in {@link #testBasicTagInfo()}. */
  protected float basicTagInfoTestAlarmHighHigh = randomGenerator.nextFloat();

  /** Tag alarm time dead band (delay) used for testing in {@link #testBasicTagInfo()}. */
  protected int basicTagInfoTestAlarmTimeDeadBand = randomGenerator.nextInt();

  /** Tag alarm level dead band (value) used for testing in {@link #testBasicTagInfo()}. */
  protected float basicTagInfoTestAlarmLevelDeadBand = randomGenerator.nextFloat();

  /**
   * Main test method. Invokes all test methods with a name starting with 'test' and no required
   * parameters/arguments.
   *
   * @param args test arguments (ignored)
   */
  public static void main(String[] args) {
    junit.textui.TestRunner.run(LibraryTest.class);
  }

  /**
   * Set up required variables, classes or other resources before testing is run.
   *
   * @throws Exception if unable to perform setup
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
   */
  protected void tearDown() throws Exception {
    // Clean up test variables/classes/etc
    basicTagInfoTestObject = null;

    super.tearDown();
  }

  /**
   * Basic test method for {@link com.hms_networks.americas.sc.extensions.taginfo.TagInfo} objects.
   *
   * <p>This test performs basic checks to ensure that {@link
   * com.hms_networks.americas.sc.extensions.taginfo.TagInfo} objects store data properly.
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
