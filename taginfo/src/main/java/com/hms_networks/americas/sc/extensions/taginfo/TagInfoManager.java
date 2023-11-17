package com.hms_networks.americas.sc.extensions.taginfo;

import com.ewon.ewonitf.EWException;
import com.ewon.ewonitf.Exporter;
import com.ewon.ewonitf.IOManager;
import com.ewon.ewonitf.SysControlBlock;
import com.hms_networks.americas.sc.extensions.fileutils.FileAccessManager;
import com.hms_networks.americas.sc.extensions.json.JSONArray;
import com.hms_networks.americas.sc.extensions.json.JSONException;
import com.hms_networks.americas.sc.extensions.json.JSONObject;
import com.hms_networks.americas.sc.extensions.logging.Logger;
import com.hms_networks.americas.sc.extensions.string.QuoteSafeStringTokenizer;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class that allows the retrieval of tag information of an Ewon Flexy by generating an export block
 * descriptor call and parsing the response to store as a list of {@link TagInfo} objects.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.0.0
 */
public class TagInfoManager {

  /**
   * Tag information list. Contents are generated/populated by a call to {@link #refreshTagList()}.
   */
  private static TagInfo[] tagInfoList = null;

  /**
   * The current index for inserting tag information objects into the tag information list. This
   * index also doubles also a tracker for the tag information list actual size.
   */
  private static int tagInfoListInsertIndex = 0;

  /** The lowest tag ID seen during the previous call to {@link #refreshTagList()}. */
  private static int lowestTagIdSeen = TagConstants.UNINIT_INT_VAL;

  /** The highest tag ID seen during the previous call to {@link #refreshTagList()}. */
  private static int highestTagIdSeen = TagConstants.UNINIT_INT_VAL;

  /** Initial capacity for byte stream buffer. */
  private static final int INITIAL_CAPACITY_BYTES = 1000;

  /** Maximum capacity for byte stream buffer. */
  private static final int MAX_CAPACITY_BYTES = 5000;

  /** Flag to enable int to string tag enums. Defaults to not enabled. */
  private static boolean enableIntToStringEnums = false;

  /** ArrayList to hold the names of tags that will have int to string enumerations */
  private static ArrayList intToStringEnumerationTagList;

  /** File path to the int to string enumeration file. */
  private static final String INT_TO_STRING_ENUMERATION_FILE_NAME = "/usr/EnumerationMapping.json";

  /**
   * Populate the tag information list by using an Ewon Export Block Descriptor and parsing the
   * response.
   *
   * @throws IOException if EDB fails
   * @throws TagInfoBufferException if line from var_lst exceeds max capacity
   * @throws JSONException if int to string enumeration JSON parse fails
   */
  public static synchronized void refreshTagList() throws IOException, JSONException {
    // Create tagInfoList of size = number of Flexy tags
    tagInfoList = new TagInfo[IOManager.getNbTags()];
    tagInfoListInsertIndex = 0;

    getIntStringEnumTags();

    /*
     * Create exporter
     *
     * dtTL = data type: tag list
     * ftT = file type: text
     */
    Exporter exporter = new Exporter("$dtTL$ftT");

    // Create flag to track reading header
    boolean isHeaderReceived = false;

    // Create a byte output stream with an initial capacity
    ByteArrayOutputStream receivedBytes = new ByteArrayOutputStream(INITIAL_CAPACITY_BYTES);

    // Current line number of tag list
    int currLineNumber = 0;

    // Loop through bytes in exporter result
    while (exporter.available() != 0) {

      // Read next byte from exporter
      byte currentByteRead = (byte) exporter.read();

      // If received new line, process line (disregard if header)
      if (currentByteRead == TagConstants.TAG_EBD_NEW_LINE) {

        // Process line if not header, otherwise change header read flag
        if (isHeaderReceived) {
          processTagListEBDLine(receivedBytes.toString());
        } else {
          isHeaderReceived = true;
        }

        // Reached end of line. Reset byte received buffer
        receivedBytes.reset();

        // Increment line number
        currLineNumber++;
      }

      /*
       * Add received byte to array (if not new line,
       * carriage return or end of stream)
       */
      if (currentByteRead != TagConstants.TAG_EBD_END_OF_STREAM
          && currentByteRead != TagConstants.TAG_EBD_CARRIAGE_RETURN
          && currentByteRead != TagConstants.TAG_EBD_NEW_LINE) {
        receivedBytes.write(currentByteRead);
        // Maintain a maximum limit for buffer growth
        if (receivedBytes.size() > MAX_CAPACITY_BYTES) {

          // Find the tag name of the error line
          final String delimiter = ";";
          final int indexName = 1;
          final boolean returnDelimiters = false;
          String errorLineTagName = "NotFound";
          QuoteSafeStringTokenizer quoteSafeStringTokenizer =
              new QuoteSafeStringTokenizer(receivedBytes.toString(), delimiter, returnDelimiters);

          String currentToken = "";
          while (quoteSafeStringTokenizer.hasMoreElements()
              && quoteSafeStringTokenizer.getPrevTokenIndex() < indexName) {
            currentToken = quoteSafeStringTokenizer.nextToken();
          }

          if (quoteSafeStringTokenizer.getPrevTokenIndex() == indexName) {
            errorLineTagName = currentToken;
          }

          // Log the error line number and tag name
          Logger.LOG_CRITICAL(
              "Line "
                  + currLineNumber
                  + " for tag name "
                  + errorLineTagName
                  + " from var_lst exceeds max capacity, throwing IOException.");
          throw new TagInfoBufferException("Line input exceeds max buffer capacity.");
        }
      }
    }

    // Correct tag info array for gaps
    final int tagIdDiff = highestTagIdSeen - lowestTagIdSeen + 1;
    final int numTagIdGaps = tagIdDiff - IOManager.getNbTags();
    if (numTagIdGaps > 0) {
      // Show warning if tag gaps above threshold
      if (numTagIdGaps >= TagConstants.TAG_ID_GAPS_WARNING_THRESHOLD) {
        Logger.LOG_WARN(
            "There are "
                + numTagIdGaps
                + " gaps in tag ID numbers. For optimal performance, it is recommended that there"
                + " be no more than "
                + TagConstants.TAG_ID_GAPS_WARNING_THRESHOLD
                + " gaps. To resolve tag ID number gaps, a reset of the Ewon must be performed.");
      }

      // Rebuild list with gaps
      Logger.LOG_DEBUG(
          "Tag ID gaps have been detected. Rebuilding tag information list with correct gaps...");
      rebuildInitialTagInfoListWithGaps();
      Logger.LOG_DEBUG("Finished rebuilding tag information list with correct gaps.");
    }

    // Flag for garbage collection
    System.gc();
  }

  /** Rebuilds the initial tag information list to account for indexing with tag ID number gaps. */
  private static synchronized void rebuildInitialTagInfoListWithGaps() {
    // Store original list to to iterate
    TagInfo[] originalList = tagInfoList;

    // Create new list with size
    final int tagIdDiff = highestTagIdSeen - lowestTagIdSeen;
    tagInfoList = new TagInfo[tagIdDiff + 1];

    // Move objects from original list to new list
    for (int x = 0; x < originalList.length; x++) {
      final int offsetTagId = originalList[x].getId() - lowestTagIdSeen;
      tagInfoList[offsetTagId] = originalList[x];
    }
  }

  /**
   * Parse the specified line from the tag information EBD data generated in {@link
   * #refreshTagList()}. Add the parse tag information to the tag information list.
   *
   * @param line EBD line string
   * @throws JSONException if int to string enumeration JSON parse fails
   * @throws IOException if int to string enumeration file read fails
   */
  private static synchronized void processTagListEBDLine(String line)
      throws IOException, JSONException {
    /*
     * Token indices
     * index 0 - tag ID
     * index 1 - name
     * index 2 - description
     * index 8 - historical logging enabled
     * index 15 - real time logging enabled
     * index 25 - in tag group A
     * index 26 - in tag group B
     * index 27 - in tag group C
     * index 28 - in tag group D
     * index 55 - tag type
     * index 56 - tag unit
     * index 61 - end of line
     */
    final int indexTagId = 0;
    final int indexName = 1;
    final int indexDescription = 2;
    final int indexHistoricalLogging = 8;
    final int indexRealTimeLogging = 15;
    final int indexAlarmHint = 20;
    final int indexAlarmHigh = 21;
    final int indexAlarmLow = 22;
    final int indexAlarmTimeDeadBand = 23;
    final int indexAlarmLevelDeadBand = 24;
    final int indexGroupA = 25;
    final int indexGroupB = 26;
    final int indexGroupC = 27;
    final int indexGroupD = 28;
    final int indexAlarmLowLow = 34;
    final int indexAlarmHighHigh = 35;
    final int indexType = 55;
    final int indexUnit = 56;
    final int indexEnd = indexType + 1;

    // Tag information
    String tagName = "";
    String tagDescription = "";
    int tagId = TagConstants.UNINIT_INT_VAL;
    TagType tagType = null;
    boolean tagInGroupA = false;
    boolean tagInGroupB = false;
    boolean tagInGroupC = false;
    boolean tagInGroupD = false;
    boolean tagHistoricalLoggingEnabled = false;
    boolean tagRealTimeLoggingEnabled = false;
    String alarmHint = "";
    float alarmLow = TagConstants.UNINIT_INT_VAL;
    float alarmHigh = TagConstants.UNINIT_INT_VAL;
    float alarmLowLow = TagConstants.UNINIT_INT_VAL;
    float alarmHighHigh = TagConstants.UNINIT_INT_VAL;
    int alarmTimeDeadBand = TagConstants.UNINIT_INT_VAL;
    float alarmLevelDeadBand = TagConstants.UNINIT_INT_VAL;

    // Tokenize line
    final boolean returnDelimiters = false;
    final String delimiter = ";";
    QuoteSafeStringTokenizer quoteSafeStringTokenizer =
        new QuoteSafeStringTokenizer(line, delimiter, returnDelimiters);

    // Loop through tokens
    while (quoteSafeStringTokenizer.hasMoreElements()
        && quoteSafeStringTokenizer.getPrevTokenIndex() < indexEnd) {

      // Read next token
      String currentToken = quoteSafeStringTokenizer.nextToken();

      // Read info from token
      switch (quoteSafeStringTokenizer.getPrevTokenIndex()) {
        case indexTagId:
          // Store tag id
          tagId = Integer.parseInt(currentToken);

          // Store tag id if lowest or highest seen
          if (lowestTagIdSeen == TagConstants.UNINIT_INT_VAL) {
            lowestTagIdSeen = tagId;
          } else if (tagId < lowestTagIdSeen) {
            lowestTagIdSeen = tagId;
          }

          if (highestTagIdSeen == TagConstants.UNINIT_INT_VAL) {
            highestTagIdSeen = tagId;
          } else if (tagId > highestTagIdSeen) {
            highestTagIdSeen = tagId;
          }
          break;
        case indexName:
          // Remove double quotes from returned tag name and store
          tagName = currentToken.substring(1, currentToken.length() - 1);
          break;
        case indexDescription:
          // Remove double quotes from returned tag description and store
          tagDescription = currentToken.substring(1, currentToken.length() - 1);
          break;
        case indexHistoricalLogging:
          tagHistoricalLoggingEnabled = convertStrToBool(currentToken);
          break;
        case indexRealTimeLogging:
          tagRealTimeLoggingEnabled = convertStrToBool(currentToken);
          break;
        case indexGroupA:
          tagInGroupA = convertStrToBool(currentToken);
          break;
        case indexGroupB:
          tagInGroupB = convertStrToBool(currentToken);
          break;
        case indexGroupC:
          tagInGroupC = convertStrToBool(currentToken);
          break;
        case indexGroupD:
          tagInGroupD = convertStrToBool(currentToken);
          break;
        case indexAlarmHint:
          alarmHint = currentToken;
          break;
        case indexAlarmLow:
          if (currentToken.length() > 0) {
            alarmLow = Float.parseFloat(currentToken);
          } else {
            alarmLow = TagConstants.UNINIT_INT_VAL;
          }
          break;
        case indexAlarmHigh:
          if (currentToken.length() > 0) {
            alarmHigh = Float.parseFloat(currentToken);
          } else {
            alarmHigh = TagConstants.UNINIT_INT_VAL;
          }
          break;
        case indexAlarmLowLow:
          if (currentToken.length() > 0) {
            alarmLowLow = Float.parseFloat(currentToken);
          } else {
            alarmLowLow = TagConstants.UNINIT_INT_VAL;
          }
          break;
        case indexAlarmHighHigh:
          if (currentToken.length() > 0) {
            alarmHighHigh = Float.parseFloat(currentToken);
          } else {
            alarmHighHigh = TagConstants.UNINIT_INT_VAL;
          }
          break;
        case indexAlarmTimeDeadBand:
          if (currentToken.length() > 0) {
            alarmTimeDeadBand = Integer.parseInt(currentToken);
          } else {
            alarmTimeDeadBand = TagConstants.UNINIT_INT_VAL;
          }
          break;
        case indexAlarmLevelDeadBand:
          if (currentToken.length() > 0) {
            alarmLevelDeadBand = Float.parseFloat(currentToken);
          } else {
            alarmLevelDeadBand = TagConstants.UNINIT_INT_VAL;
          }
          break;
        case indexType:
          int tagTypeInteger = Integer.parseInt(currentToken);

          // Convert tag type integer to object
          tagType = TagType.getTagTypeFromInt(tagTypeInteger);
          break;
        case indexUnit:
          String tagUnit = currentToken;

          // Remove wrapping quotes if present
          final char doubleQuoteChar = '"';
          final int firstCharIndex = 0;
          final int secondCharIndex = 1;
          final int lastCharIndex = tagUnit.length() - 1;

          if (tagUnit.charAt(firstCharIndex) == doubleQuoteChar
              && tagUnit.charAt(lastCharIndex) == doubleQuoteChar) {
            tagUnit = tagUnit.substring(secondCharIndex, lastCharIndex);
          }

          // Unit is the last index, form TagInfo object
          createTagInfoObject(
              tagId,
              tagName,
              tagDescription,
              tagHistoricalLoggingEnabled,
              tagRealTimeLoggingEnabled,
              tagInGroupA,
              tagInGroupB,
              tagInGroupC,
              tagInGroupD,
              tagType,
              tagUnit,
              alarmHint,
              alarmLow,
              alarmHigh,
              alarmLowLow,
              alarmHighHigh,
              alarmTimeDeadBand,
              alarmLevelDeadBand);
          break;
      }
    }
  }

  /**
   * Call this function to enable int to string enumerations. Int to string enumeration file will be
   * expected to exist in Flexy usr directory. The file contents are expected in the following
   * format: <code>
   * {
   * "enumeratedTagList": ["tagName1", "tagName2"],
   * "tags": {
   *     "tagName1": [{
   *         "0": "example string 0 for tagName1"
   *     }, {
   *         "6": "example string 6 for tagName1"
   *     }, {
   *         "4": "example string 4 for tagName1"
   *     }],
   *     "tagName2": [{
   *         "0": "example string 0 for tagName2"
   *     }, {
   *         "1": "example string 1 for tagName2"
   *     }, {
   *         "2": "example string 2 for tagName2"
   *     }]
   *    }
   *  }
   * </code> <img src="../../../../../../example/IntToStringEnumerationExample.PNG" alt="Example int
   * to string enumeration format">
   */
  public static void enableIntToStringEnums() {
    enableIntToStringEnums = true;
  }

  /**
   * Creates a tag info object either with or without the tag int to string enumeration.
   *
   * @param tagId the ID of a tag
   * @param tagName the name of a tag
   * @param tagDescription the description of a tag
   * @param tagHistoricalLoggingEnabled true if historical logging is enabled for the tag
   * @param tagRealTimeLoggingEnabled true if real time logging is enabled for the tag
   * @param tagInGroupA true if tag is added to group A
   * @param tagInGroupB true if tag is added to group B
   * @param tagInGroupC true if tag is added to group C
   * @param tagInGroupD true if tag is added to group D
   * @param tagTypeObj TagType object associated with this tag
   * @param tagUnit the unit of a tag
   * @param alarmHint tag alarm hint
   * @param alarmLow tag alarm low threshold
   * @param alarmHigh tag alarm high threshold
   * @param alarmLowLow tag alarm low/low threshold
   * @param alarmHighHigh tag alarm high/high threshold
   * @param alarmTimeDeadBand tag alarm time dead band (delay)
   * @param alarmLevelDeadBand tag alarm level dead band (value)
   * @throws JSONException if int to string enumeration JSON parse fails
   * @throws IOException if in to string enumeration file read fails
   * @throws NumberFormatException if the key defined in int to string enum mappings is not an
   *     integer
   */
  private static void createTagInfoObject(
      int tagId,
      String tagName,
      String tagDescription,
      boolean tagHistoricalLoggingEnabled,
      boolean tagRealTimeLoggingEnabled,
      boolean tagInGroupA,
      boolean tagInGroupB,
      boolean tagInGroupC,
      boolean tagInGroupD,
      TagType tagTypeObj,
      String tagUnit,
      String alarmHint,
      float alarmLow,
      float alarmHigh,
      float alarmLowLow,
      float alarmHighHigh,
      int alarmTimeDeadBand,
      float alarmLevelDeadBand)
      throws IOException, JSONException {
    String[] tagIntToStringMappings = null;
    boolean enumTag = false;
    if (enableIntToStringEnums) {
      for (int tagListIndex = 0;
          tagListIndex < intToStringEnumerationTagList.size();
          tagListIndex++) {
        if (((String) intToStringEnumerationTagList.get(tagListIndex)).equals(tagName)) {
          enumTag = true;
          tagIntToStringMappings = getIntStringEnumTagMappings(tagName);
          break;
        }
      }
    }

    TagInfo currentTagInfo;
    if (enumTag) {
      tagTypeObj = TagType.INTEGER_MAPPED_STRING;
      currentTagInfo =
          new TagInfoEnumeratedIntToString(
              tagId,
              tagName,
              tagDescription,
              tagHistoricalLoggingEnabled,
              tagRealTimeLoggingEnabled,
              tagInGroupA,
              tagInGroupB,
              tagInGroupC,
              tagInGroupD,
              tagTypeObj,
              tagUnit,
              alarmHint,
              alarmLow,
              alarmHigh,
              alarmLowLow,
              alarmHighHigh,
              alarmTimeDeadBand,
              alarmLevelDeadBand,
              tagIntToStringMappings);
    } else {
      currentTagInfo =
          new TagInfo(
              tagId,
              tagName,
              tagDescription,
              tagHistoricalLoggingEnabled,
              tagRealTimeLoggingEnabled,
              tagInGroupA,
              tagInGroupB,
              tagInGroupC,
              tagInGroupD,
              tagTypeObj,
              tagUnit,
              alarmHint,
              alarmLow,
              alarmHigh,
              alarmLowLow,
              alarmHighHigh,
              alarmTimeDeadBand,
              alarmLevelDeadBand);
    }
    tagInfoList[tagInfoListInsertIndex] = currentTagInfo;
    tagInfoListInsertIndex++;
  }

  /**
   * Reads the enumeration file to get a list of tags that have int to string enumerations and
   * stores the list. This must be done once at the start of the application and is handled when the
   * TagInfoList is created.
   *
   * @throws IOException if int to string enumeration file read fails
   * @throws JSONException if int to string enumeration JSON parse fails
   */
  private static void getIntStringEnumTags() throws IOException, JSONException {
    if (enableIntToStringEnums) {
      ArrayList tagList = new ArrayList();
      JSONObject allTagIntToStringMappingsJson;
      JSONArray tagIntToStringMappingsJsonArray;
      allTagIntToStringMappingsJson =
          new JSONObject(FileAccessManager.readFileToString(INT_TO_STRING_ENUMERATION_FILE_NAME));
      tagIntToStringMappingsJsonArray =
          (JSONArray) allTagIntToStringMappingsJson.get("enumeratedTagList");
      final int tagListJsonArrayLength = tagIntToStringMappingsJsonArray.length();
      for (int i = 0; i < tagListJsonArrayLength; i++) {
        String tagValueAsString = tagIntToStringMappingsJsonArray.getString(i);
        tagList.add(tagValueAsString);
      }
      intToStringEnumerationTagList = tagList;
    }
  }

  /**
   * For a single tag, get the array of string mappings corresponding to the given integer tag.
   *
   * @param tagName the name of the Ewon tag
   * @return an array of string mappings corresponding to the current integer tag
   * @throws IOException if int to string enumeration file read fails
   * @throws JSONException if int to string enumeration JSON parse fails
   * @throws NumberFormatException if the key defined in int to string enum mappings is not an
   *     integer
   */
  private static String[] getIntStringEnumTagMappings(String tagName)
      throws IOException, JSONException {
    String[] tagStringMappings = null;
    JSONObject tagIntToStringMappingsFileJson;
    JSONObject tagIntToStringMappingsJson;
    JSONArray tagIntToStringMappingsJsonArray;
    if (enableIntToStringEnums) {
      tagIntToStringMappingsFileJson =
          new JSONObject(FileAccessManager.readFileToString(INT_TO_STRING_ENUMERATION_FILE_NAME));
      tagIntToStringMappingsJson = (JSONObject) tagIntToStringMappingsFileJson.get("tags");
      tagIntToStringMappingsJsonArray = (JSONArray) tagIntToStringMappingsJson.get(tagName);
      final int length = tagIntToStringMappingsJsonArray.length();
      int biggestIndex = 0;

      /* Int to string enumerations are held in a JSON array of one key/value pair for each index in the array.
       * To get the key of a key value pair, get the first(only) item returned by JSONObject.names()
       * for the JSON object index.
       */
      final int tagIntToStringEnumStringKeyIndex = 0;
      for (int jsonArrayIndex = 0; jsonArrayIndex < length; jsonArrayIndex++) {
        int currentIndex =
            Integer.parseInt(
                (String)
                    ((JSONObject) tagIntToStringMappingsJsonArray.get(jsonArrayIndex))
                        .names()
                        .get(tagIntToStringEnumStringKeyIndex));

        biggestIndex = currentIndex > biggestIndex ? currentIndex : biggestIndex;
      }
      tagStringMappings = new String[biggestIndex + 1];
      for (int i = 0; i < length; i++) {
        JSONObject jsonObject = (JSONObject) tagIntToStringMappingsJsonArray.get(i);
        String mappingKey = (String) jsonObject.names().get(tagIntToStringEnumStringKeyIndex);
        String tagValueAsString = (String) jsonObject.get(mappingKey);
        tagStringMappings[Integer.parseInt(mappingKey)] = tagValueAsString;
      }
    }
    return tagStringMappings;
  }

  /**
   * Convert a string boolean ("0"/"1") to boolean.
   *
   * @param stringBool string boolean ("0"/"1")
   * @return converted boolean
   */
  private static boolean convertStrToBool(String stringBool) {
    return stringBool.equals("1");
  }

  /**
   * Gets the tag info array populated by calling {@link #refreshTagList()}. If this method is
   * called before {@link #refreshTagList()}, an {@link IllegalStateException} will be thrown.
   *
   * <p>Warning: TagInfo array has tags indexed by ID with an offset of the lowest tag ID. When
   * accessing elements, the offset must be accounted for ( e.g. index = desiredID - lowestID).
   *
   * @throws IllegalStateException when tag list has not been populated with {@link
   *     #refreshTagList()}
   * @return populated tag information array
   */
  public static synchronized TagInfo[] getTagInfoArray() throws IllegalStateException {
    // Verify tag info list has been populated
    if (tagInfoList == null) {
      throw new IllegalStateException(
          "Cannot get tag information list before calling refreshTagList()");
    }

    return tagInfoList;
  }

  /**
   * Gets a {@link TagInfo} object from the tag info array populated by calling {@link
   * #refreshTagList()}. If this method is called before {@link #refreshTagList()}, an {@link
   * IllegalStateException} will be thrown.
   *
   * @throws IllegalStateException when tag list has not been populated with {@link
   *     #refreshTagList()}
   * @throws IndexOutOfBoundsException when (tagId - {@link #lowestTagIdSeen}) is not an index
   *     within the TagInfo array bounds.
   * @param tagId the Ewon tag ID
   * @return {@link TagInfo} object associated with the given tag ID
   */
  public static synchronized TagInfo getTagInfoFromTagId(int tagId)
      throws IllegalStateException, IndexOutOfBoundsException {
    return ((TagInfo) getTagInfoArray()[tagId - lowestTagIdSeen]);
  }

  /**
   * Gets the tag info list populated by calling {@link #refreshTagList()}. If this method is called
   * before {@link #refreshTagList()}, an {@link IllegalStateException} will be thrown.
   *
   * @return populated tag information list
   */
  public static synchronized List getTagInfoList() {
    // Verify tag info list has been populated
    if (tagInfoList == null) {
      throw new IllegalStateException(
          "Cannot get tag information list before calling refreshTagList()");
    }

    return Arrays.asList(tagInfoList);
  }

  /**
   * Gets a filtered tag info list containing only tags from the tag info list that are in the
   * specified tag group(s). If this method is called before {@link #refreshTagList()}, an {@link
   * IllegalStateException} will be thrown.
   *
   * @param tagGroups tag groups to include
   * @return filtered tag information list
   */
  public static synchronized List getTagInfoListFiltered(List tagGroups) {
    // Verify tag info list has been populated
    if (tagInfoList == null) {
      throw new IllegalStateException(
          "Cannot get tag information list before calling refreshTagList()");
    }

    /*
     * Create array list to store filtered tags.
     *
     * Use an initial capacity matching the size of full tag list.
     * This will potentially reduce the number of resizing operations
     * of the ArrayList.
     */
    ArrayList filteredTagInfoList = new ArrayList(tagInfoList.length);

    // Loop through each tag in tag info list
    for (int i = 0; i < tagInfoListInsertIndex; i++) {
      // Get tag at array index and its group
      TagInfo currentTagInfo = tagInfoList[i];
      if (currentTagInfo != null) {
        List currentTagGroups = currentTagInfo.getTagGroups();

        // Check if tag belongs to a filter
        boolean filterMatch = false;
        for (int x = 0; x < tagGroups.size(); x++) {
          if (currentTagGroups.contains(tagGroups.get(x))) {
            filterMatch = true;
          }
        }

        // If tag is in desired group(s), add to filtered tag info list
        if (filterMatch) {
          filteredTagInfoList.add(currentTagInfo);
        }
      }
    }

    // Shrink filtered tag array list to remove unused spaces and return
    filteredTagInfoList.trimToSize();
    return filteredTagInfoList;
  }

  /**
   * Gets a filtered tag info list containing only tags from the tag info list that are in the
   * specified tag group(s). If this method is called before {@link #refreshTagList()}, an {@link
   * IllegalStateException} will be thrown.
   *
   * @param tagGroups tag groups to include
   * @return filtered tag information list
   */
  public static synchronized List getTagInfoListFiltered(TagGroup[] tagGroups) {
    return getTagInfoListFiltered(Arrays.asList(tagGroups));
  }

  /**
   * Gets a filtered tag info list containing only tags from the tag info list that are in the
   * specified tag group. If this method is called before {@link #refreshTagList()}, an {@link
   * IllegalStateException} will be thrown.
   *
   * @param tagGroup tag group to include
   * @return filtered tag information list
   */
  public static synchronized List getTagInfoListFiltered(TagGroup tagGroup) {
    return getTagInfoListFiltered(new TagGroup[] {tagGroup});
  }

  /**
   * Gets a filtered tag info list containing only tags from the tag info list that are in the
   * specified tag group and have real time logging enabled. If this method is called before {@link
   * #refreshTagList()}, an {@link IllegalStateException} will be thrown.
   *
   * @param tagGroup tag group to include
   * @return filtered real time tag information list
   */
  public static ArrayList getRealTimeTagInfoListFiltered(TagGroup tagGroup) {
    ArrayList allTags = (ArrayList) getTagInfoListFiltered(tagGroup);
    ArrayList realTimeTags = new ArrayList();
    if (allTags != null) {
      for (int i = 0; i < allTags.size(); i++) {
        TagInfo tag = (TagInfo) allTags.get(i);
        if (tag.isRealTimeLogEnabled()) {
          realTimeTags.add(tag);
        }
      }
    }
    return realTimeTags;
  }

  /**
   * Gets the lowest tag ID seen during the previous call to {@link #refreshTagList()}. If this
   * method is called before {@link #refreshTagList()}, an {@link IllegalStateException} will be
   * thrown.
   *
   * @return lowest tag ID seen
   */
  public static int getLowestTagIdSeen() {
    // Verify lowest tag ID seen variable is set
    if (lowestTagIdSeen == TagConstants.UNINIT_INT_VAL) {
      throw new IllegalStateException(
          "Cannot get lowest tag ID seen before calling refreshTagList()");
    }

    return lowestTagIdSeen;
  }

  /**
   * Gets the highest tag ID seen during the previous call to {@link #refreshTagList()}. If this
   * method is called before {@link #refreshTagList()}, an {@link IllegalStateException} will be
   * thrown.
   *
   * @return highest tag ID seen
   */
  public static int getHighestTagIdSeen() {
    // Verify lowest tag ID seen variable is set
    if (lowestTagIdSeen == TagConstants.UNINIT_INT_VAL) {
      throw new IllegalStateException(
          "Cannot get lowest tag ID seen before calling refreshTagList()");
    }

    return highestTagIdSeen;
  }

  /**
   * Get a boolean value representing if the tag info list has been populated by calling {@link
   * #refreshTagList()}.
   *
   * @return true if tag info list populated
   */
  public static synchronized boolean isTagInfoListPopulated() {
    return tagInfoList != null;
  }

  /**
   * Applies the specified log interval to the specified tag.
   *
   * @param tagName name of tag to modify
   * @param logInterval new historical log interval
   * @throws EWException if unable to apply historical log interval
   */
  public static void applyHistoricalLogRateForTag(String tagName, String logInterval)
      throws EWException {
    SysControlBlock SCB = new SysControlBlock(SysControlBlock.TAG, tagName);
    SCB.setItem("LogEnabled", "1");
    SCB.setItem("LogTimer", logInterval);
    SCB.saveBlock();
  }

  /**
   * Applies the specified historical log interval to tags in the specified tag group. To reduce the
   * time waiting for this method to return, it is recommended that this method be called from a new
   * {@link Thread}.
   *
   * @param tagGroup tag group to modify
   * @param logInterval new historical log interval
   * @throws EWException if unable to apply historical log interval
   * @throws InterruptedException if unable to sleep between updating tags
   */
  public static void applyHistoricalLogRateForTagGroup(
      final TagGroup tagGroup, final String logInterval) throws EWException, InterruptedException {
    // Define constant number of milliseconds to wait between applying each log intervals
    final long millisToWaitBetweenTags = 10;

    // Get list of tags in group
    List tagsInGroup = getTagInfoListFiltered(tagGroup);

    // For each tag in group, apply log interval and sleep
    for (int x = 0; x < tagsInGroup.size(); x++) {
      TagInfo currentTagInfo = (TagInfo) tagsInGroup.get(x);
      applyHistoricalLogRateForTag(currentTagInfo.getName(), logInterval);
      Thread.sleep(millisToWaitBetweenTags);
    }
  }
}
