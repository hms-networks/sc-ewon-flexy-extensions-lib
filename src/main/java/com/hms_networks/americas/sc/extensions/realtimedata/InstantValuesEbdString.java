package com.hms_networks.americas.sc.extensions.realtimedata;

import com.ewon.ewonitf.Exporter;
import com.hms_networks.americas.sc.extensions.datapoint.DataPoint;
import com.hms_networks.americas.sc.extensions.datapoint.DataPointBoolean;
import com.hms_networks.americas.sc.extensions.datapoint.DataPointNumber;
import com.hms_networks.americas.sc.extensions.datapoint.DataPointString;
import com.hms_networks.americas.sc.extensions.datapoint.DataQuality;
import com.hms_networks.americas.sc.extensions.historicaldata.HistoricalDataManager;
import com.hms_networks.americas.sc.extensions.string.StringUtils;
import com.hms_networks.americas.sc.extensions.system.time.SCTimeUnit;
import com.hms_networks.americas.sc.extensions.system.time.SCTimeUtils;
import com.hms_networks.americas.sc.extensions.taginfo.TagInfo;
import com.hms_networks.americas.sc.extensions.taginfo.TagInfoManager;
import com.hms_networks.americas.sc.extensions.taginfo.TagType;
import com.hms_networks.americas.sc.extensions.util.StreamUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class for working with the Ewon EBD instant values query using the string data output format.
 * This class is used to parse the EBD data and create data points for each tag value. The string
 * data output format for the instant values EBD includes tag data for all numeric and string tags.
 *
 * <p>It is important to note that the Ewon instant values EBD query does not return full tag
 * configurations. The {@link TagInfoManager} is used to get tag information for each tag ID, and
 * must be populated with tag information before using this class.
 *
 * <p>Format of the EBD data:
 *
 * <ul>
 *   <li>The values of strings are surrounded by double quotes (“).
 *   <li>Special characters (quote, double-quote, non-printable) are escaped with a ‘\’.
 *   <li>Expected Header:
 *       <ul>
 *         <li>TagId
 *         <li>TagName
 *         <li>Value
 *         <li>AlStatus
 *         <li>AlType
 *         <li>Quality
 *       </ul>
 *   <li>Followed by rows/lines for each data entry
 * </ul>
 *
 * <p>Example:
 *
 * <pre>
 * "TagId";"TagName";"Value";"AlStatus";"AlType";"Quality"
 * 1;"Float_0";5;0;0;65472
 * 2;"Float_1";6;0;0;65472
 * 3;"Float_2";7;0;0;65472
 * 2003;"Str_100";"ABCDEF\"GH\'IJKLMNOPQRSTUVWXYZ";0;0;65472
 * 2004;"Str_101";"ABCDEFGHIJKLMNOPQRSTUVWXYZ";0;0;65472
 * 2005;"Str_102";"AT char @";0;0;65472
 * </pre>
 *
 * @author HMS Networks; Americas
 * @version 1.0.0
 * @since 1.15.15
 */
public class InstantValuesEbdString {

  /**
   * The EBD instant values string used to execute the EBD call.
   *
   * @since 1.0.0
   */
  private static final String EBD_INSTANT_VALUES_STRING = "$dtIV $ftT";

  /**
   * The index of the tag ID in the parsed EBD data.
   *
   * @since 1.0.0
   */
  private static final int TAG_ID_INDEX = 0;

  /**
   * The index of the tag name in the parsed EBD data.
   *
   * @since 1.0.0
   */
  private static final int TAG_NAME_INDEX = 1;

  /**
   * The index of the value in the parsed EBD data.
   *
   * @since 1.0.0
   */
  private static final int VALUE_INDEX = 2;

  /**
   * The index of the quality in the parsed EBD data.
   *
   * @since 1.0.0
   */
  private static final int QUALITY_INDEX = 5;

  /**
   * The delimiter character used to separate values in the EBD data.
   *
   * @since 1.0.0
   */
  private static final char DELIMITER = ';';

  /**
   * The delimiter character used to separate lines in the EBD data.
   *
   * @since 1.0.0
   */
  private static final String NEW_LINE_DELIMITER = "\n";

  /**
   * The encoding used to read the EBD data.
   *
   * @since 1.0.0
   */
  private static final String EBD_ENCODING = "UTF-8";

  /**
   * The timestamp of the EBD data.
   *
   * @since 1.0.0
   */
  private final String iso8601Timestamp;

  /**
   * Map to store the data points from the EBD data. The key is the tag name and the value is the
   * data point.
   *
   * <p>Parameterized type: Map&lt;String, DataPoint&gt;
   *
   * @since 1.0.0
   */
  private final Map dataPoints;

  /**
   * Constructor for the InstantValuesEbdString class. This constructor parses the EBD data and
   * creates data points for each tag.
   *
   * @param date {@link Date} object of the EBD data
   * @param ebdData the EBD data to parse
   * @throws Exception when unable to get UTC export value
   * @throws IllegalStateException when the {@link TagInfoManager} has not been initialized with
   *     {@link TagInfoManager#refreshTagList()}
   * @throws IllegalArgumentException if one of the following occurs:
   *     <ul>
   *       <li>a tag type cannot be decoded from its tag info and value
   *       <li>the number of values on a line does not match the number of headings
   *       <li>a value cannot be parsed as a string, integer, float, long, double, or boolean
   *     </ul>
   *
   * @throws Exception if unable to get UTC export value
   * @since 1.0.0
   */
  public InstantValuesEbdString(Date date, String ebdData)
      throws Exception, IllegalArgumentException {
    // Store timestamp
    this.iso8601Timestamp = SCTimeUtils.getIso8601FormattedTimestampForDate(date);
    String epochSec = Long.toString(date.getTime() / SCTimeUnit.SECONDS.toMillis(1));

    // Parse the EBD data
    Object[][] parsedData = parse(ebdData);
    dataPoints = buildDataPoints(iso8601Timestamp, epochSec, parsedData);
  }

  /**
   * Method to execute an EBD call and parse the data into data points for each tag.
   *
   * @return an {@link InstantValuesEbdString} object containing the parsed data points
   * @throws IllegalStateException when the {@link TagInfoManager} has not been initialized with
   *     {@link TagInfoManager#refreshTagList()}
   * @throws IllegalArgumentException if one of the following occurs:
   *     <ul>
   *       <li>a tag type cannot be decoded from its tag info and value
   *       <li>the number of values on a line does not match the number of headings
   *       <li>a value cannot be parsed as a string, integer, float, long, double, or boolean
   *     </ul>
   *
   * @throws Exception if an error occurs while executing the EBD call
   * @since 1.0.0
   */
  public static InstantValuesEbdString doEbd() throws Exception {
    return doEbd(new Date());
  }

  /**
   * Method to execute an EBD call and parse the data into data points for each tag.
   *
   * @param date {@link Date} object to associate with the EBD data
   * @return an {@link InstantValuesEbdString} object containing the parsed data points
   * @throws IllegalStateException when the {@link TagInfoManager} has not been initialized with
   *     {@link TagInfoManager#refreshTagList()}
   * @throws IllegalArgumentException if one of the following occurs:
   *     <ul>
   *       <li>a tag type cannot be decoded from its tag info and value
   *       <li>the number of values on a line does not match the number of headings
   *       <li>a value cannot be parsed as a string, integer, float, long, double, or boolean
   *     </ul>
   *
   * @throws Exception if an error occurs while executing the EBD call
   * @since 1.0.0
   */
  public static InstantValuesEbdString doEbd(Date date) throws Exception {
    // Store timestamp and execute EBD call
    Exporter ebdInstantValuesExporter =
        HistoricalDataManager.executeEbdCall(EBD_INSTANT_VALUES_STRING);

    // Pull in bytes from Exporter (input stream)
    String ebdData = StreamUtils.getStringFromInputStream(ebdInstantValuesExporter, EBD_ENCODING);
    return new InstantValuesEbdString(date, ebdData);
  }

  /**
   * Method to build data points from the parsed data.
   *
   * @param iso8601Timestamp the timestamp of the EBD data in ISO 8601 format
   * @param epochSecondsTimestamp the timestamp of the EBD data in epoch seconds
   * @param parsedData the parsed data
   * @return a map of data points (Parameterized type: Map&lt;String, DataPoint&gt;)
   * @throws IllegalArgumentException if a tag type cannot be decoded from its tag info and value
   * @throws IllegalStateException when the {@link TagInfoManager} has not been initialized with
   *     {@link TagInfoManager#refreshTagList()}
   * @since 1.0.0
   */
  private static Map buildDataPoints(
      String iso8601Timestamp, String epochSecondsTimestamp, Object[][] parsedData) {
    // Create a map to store the data points
    Map dataPoints = new HashMap(); // Map<String, DataPoint>

    // Loop through each row of the parsed data (except headings)
    for (int i = 1; i < parsedData.length; i++) {
      // Get the values for the row
      Object[] values = parsedData[i];

      // Extract the values from the row
      int tagId = ((Integer) values[TAG_ID_INDEX]).intValue();
      String tagName = (String) values[TAG_NAME_INDEX];
      Object value = values[VALUE_INDEX];
      int quality = ((Integer) values[QUALITY_INDEX]).intValue();

      // Build required values
      TagInfo tagInfo = TagInfoManager.getTagInfoFromTagId(tagId);
      String tagUnit = tagInfo.getUnit();
      DataQuality dataQuality = DataQuality.fromOpcuaQuality(quality);

      // Create a data point for the tag
      DataPoint dataPoint;

      // Check if string
      if (tagInfo.getType() == TagType.STRING) {
        String valueString = value instanceof String ? (String) value : value.toString();
        dataPoint =
            new DataPointString(
                tagName,
                tagId,
                tagUnit,
                valueString,
                epochSecondsTimestamp,
                iso8601Timestamp,
                dataQuality);
      } else if (tagInfo.getType() == TagType.BOOLEAN && value instanceof Boolean) {
        boolean valueBoolean = ((Boolean) value).booleanValue();
        dataPoint =
            new DataPointBoolean(
                tagName,
                tagId,
                tagUnit,
                valueBoolean,
                epochSecondsTimestamp,
                iso8601Timestamp,
                dataQuality);
      } else {
        Number valueNumber = (Number) value;
        if (tagInfo.getType() == TagType.BOOLEAN) {
          boolean valueBoolean = valueNumber.intValue() != 0;
          dataPoint =
              new DataPointBoolean(
                  tagName,
                  tagId,
                  tagUnit,
                  valueBoolean,
                  epochSecondsTimestamp,
                  iso8601Timestamp,
                  dataQuality);
        } else if (tagInfo.getType() == TagType.INTEGER) {
          dataPoint =
              new DataPointNumber(
                  tagName,
                  tagId,
                  tagUnit,
                  valueNumber,
                  epochSecondsTimestamp,
                  iso8601Timestamp,
                  dataQuality);
        } else if (tagInfo.getType() == TagType.FLOAT) {
          dataPoint =
              new DataPointNumber(
                  tagName,
                  tagId,
                  tagUnit,
                  valueNumber,
                  epochSecondsTimestamp,
                  iso8601Timestamp,
                  dataQuality);
        } else if (tagInfo.getType() == TagType.DWORD) {
          dataPoint =
              new DataPointNumber(
                  tagName,
                  tagId,
                  tagUnit,
                  valueNumber,
                  epochSecondsTimestamp,
                  iso8601Timestamp,
                  dataQuality);
        } else {
          throw new IllegalArgumentException(
              "Failed to create data point for tag ["
                  + tagName
                  + "]. Value type cannot be decoded from ["
                  + value
                  + "]");
        }
      }

      // Add the data point to the map
      dataPoints.put(tagName, dataPoint);
    }

    return dataPoints;
  }

  /**
   * Method to parse the EBD data into a 2D array of objects.
   *
   * @param ebdData the EBD data to parse
   * @return a 2D array of objects
   * @throws IllegalArgumentException if the number of values on a line does not match the number of
   *     headings, or if a value cannot be parsed as a string, integer, float, long, double, or
   *     boolean
   * @since 1.0.0
   */
  public Object[][] parse(String ebdData) {
    // Split the EBD data into lines
    List lines = StringUtils.split(ebdData, NEW_LINE_DELIMITER);

    // Read the header line
    final int headerLineIndex = 0;
    String header = (String) lines.get(headerLineIndex);
    Object[] headings = parseLine(header);

    // Get line count (not including last line if empty)
    int lastLineIndex = lines.size() - 1;
    String lastLine = (String) lines.get(lastLineIndex);
    int lineCount = lastLine.trim().length() > 0 ? lines.size() : lines.size() - 1;

    // Create a 2D array to store the parsed data
    Object[][] parsedData = new Object[lineCount][headings.length];

    // Loop through each line of the EBD data
    for (int i = 0; i < lineCount; i++) {
      // Split the line into individual values
      String line = (String) lines.get(i);
      if (line.trim().length() > 0) {
        Object[] values = parseLine(line);

        // Check if the number of values is equal to the number of headings
        if (values.length != headings.length) {
          int lineNumber = i + 1;
          throw new IllegalArgumentException(
              "Number of values on line " + lineNumber + " does not match number of headings");
        }

        // Store the values in the parsed data array
        parsedData[i] = values;
      }
    }

    return parsedData;
  }

  /**
   * Get the delimiter indices for the specified line. This method will return an array of integers
   * representing the indices of the delimiter character in the specified line. It will not include
   * delimiters that are enclosed within a quoted string, or escaped with a backslash.
   *
   * @param line line to get delimiter indices for
   * @return array of delimiter indices
   * @since 1.0.0
   */
  public int[] getDelimiterIndicesForLine(String line) {
    List delimiterIndices = new ArrayList(); // List<Integer>
    boolean inQuotedString = false;
    boolean escaped = false;

    // Loop through each character in the line
    for (int i = 0; i < line.length(); i++) {
      char c = line.charAt(i);

      // Check if the character is a delimiter
      if (c == DELIMITER && !inQuotedString && !escaped) {
        delimiterIndices.add(new Integer(i));
      }

      // Check if the character is a quote
      if (c == '"') {
        inQuotedString = !inQuotedString;
      }

      // Check if the character is an escape character
      if (c == '\\') {
        escaped = !escaped;
      } else {
        escaped = false;
      }
    }

    // Return the list of delimiter indices converted to an array (List<Integer> -> int[])
    int[] delimiterIndicesArray = new int[delimiterIndices.size()];
    for (int i = 0; i < delimiterIndices.size(); i++) {
      delimiterIndicesArray[i] = ((Integer) delimiterIndices.get(i)).intValue();
    }
    return delimiterIndicesArray;
  }

  /**
   * Parse the specified line into an array of objects. This method will split the line into
   * individual values using the delimiter character, and return an array of objects representing
   * the values.
   *
   * @param line line to parse
   * @return array of objects representing the values
   * @throws IllegalArgumentException if a value cannot be parsed as a string, integer, float, long,
   *     double, or boolean
   * @since 1.0.0
   */
  public Object[] parseLine(String line) {
    // Get the delimiter indices for the line
    int[] delimiterIndices = getDelimiterIndicesForLine(line);

    // Split the line into individual values
    return parseLine(line, delimiterIndices);
  }

  /**
   * Parse the specified line into an array of objects. This method will split the line into
   * individual values using the delimiter character, and return an array of objects representing
   * the values.
   *
   * @param line line to parse
   * @param delimiterIndices array of delimiter indices
   * @return array of objects representing the values
   * @throws IllegalArgumentException if a value cannot be parsed as a string, integer, float, long,
   *     double, or boolean
   * @since 1.0.0
   */
  public Object[] parseLine(String line, int[] delimiterIndices) {
    // Create an array to store the parsed values
    Object[] values = new Object[delimiterIndices.length + 1];

    // Loop through each delimiter index
    for (int i = 0; i < delimiterIndices.length; i++) {
      // Get the start and end indices for the value
      int start = i == 0 ? 0 : delimiterIndices[i - 1] + 1;
      int end = delimiterIndices[i];

      // Extract the value from the line and sanitize
      String value = line.substring(start, end);
      values[i] = getValueObject(value.trim());
    }

    // Extract the final value from the line
    values[delimiterIndices.length] =
        getValueObject(line.substring(delimiterIndices[delimiterIndices.length - 1] + 1).trim());

    return values;
  }

  /**
   * Get the object representation of the specified value. This method will attempt to parse the
   * value as an integer, float, long, or boolean, and default to a string if parsing fails.
   * Additionally, this method will unescape special characters in the value.
   *
   * @param value value to parse
   * @return object representation of the value
   * @throws IllegalArgumentException if the value cannot be parsed as a string, integer, float,
   *     long, double, or boolean
   * @since 1.0.0
   */
  public Object getValueObject(String value) {
    Object result;

    // Check if the value is a string (enclosed in quotes)
    if (value.startsWith("\"") && value.endsWith("\"")) {
      // Remove the quotes
      value = value.substring(1, value.length() - 1);

      // Unescape special characters
      value = StringUtils.replace(value, "\\\"", "\"");
      value = StringUtils.replace(value, "\\'", "'");
      value = StringUtils.replace(value, "\\n", "\n");
      value = StringUtils.replace(value, "\\r", "\r");
      value = StringUtils.replace(value, "\\t", "\t");

      result = value;
    } else {
      // Check if the value is an integer
      try {
        int rawInt = Integer.parseInt(value);
        result = new Integer(rawInt);
      } catch (NumberFormatException e) {
        // Check if the value is a float
        try {
          float rawFloat = Float.parseFloat(value);
          result = new Float(rawFloat);
        } catch (NumberFormatException e1) {
          // Check if the value is a long
          try {
            long rawLong = Long.parseLong(value);
            result = new Long(rawLong);
          } catch (NumberFormatException e2) {
            // Check if the value is a double
            try {
              double rawDouble = Double.parseDouble(value);
              result = new Double(rawDouble);
            } catch (NumberFormatException e3) {
              // Check if the value is a boolean
              if (value.equalsIgnoreCase("true")) {
                result = Boolean.TRUE;
              } else if (value.equalsIgnoreCase("false")) {
                result = Boolean.FALSE;
              } else {
                throw new IllegalArgumentException(
                    "Failed to parse value ["
                        + value
                        + "] as a String, Integer, Float, Double, Long, or Boolean!");
              }
            }
          }
        }
      }
    }

    return result;
  }

  /**
   * Get the data points from the EBD data.
   *
   * @return a map of data points (Parameterized type: Map&lt;String, DataPoint&gt;)
   * @since 1.0.0
   */
  public Map getDataPoints() {
    return dataPoints;
  }

  /**
   * Get the timestamp of the EBD data.
   *
   * @return the timestamp of the EBD data
   * @since 1.0.0
   */
  public String getTimestamp() {
    return iso8601Timestamp;
  }

  /**
   * Get the data point for the specified tag name.
   *
   * <p>NOTE: This method will return null if the tag name is not found.
   *
   * @param tagName the tag name to get the data point for
   * @return the data point for the specified tag name
   */
  public DataPoint getDataPoint(String tagName) {
    return (DataPoint) getDataPoints().get(tagName);
  }
}
