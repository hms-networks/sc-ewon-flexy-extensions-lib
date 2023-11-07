import com.hms_networks.americas.sc.extensions.taginfo.TagInfoTest;
import com.hms_networks.americas.sc.extensions.util.RawNumberValueUtilsTest;

/**
 * Library test class for Ewon Flexy Extensions Library.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.0.0
 * @version 0.0.1
 */
public class LibraryTest {

  /**
   * Main test method. Invokes all test methods with a name starting with 'test' and no required
   * parameters/arguments.
   *
   * @param args test arguments (ignored)
   */
  public static void main(String[] args) {
    junit.textui.TestRunner.run(TagInfoTest.class);
    junit.textui.TestRunner.run(RawNumberValueUtilsTest.class);
  }
}
