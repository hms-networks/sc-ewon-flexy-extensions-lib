package com.hms_networks.americas.sc.extensions.tagupdateservice;

import com.ewon.ewonitf.TagControl;
import com.hms_networks.americas.sc.extensions.json.JSONArray;
import com.hms_networks.americas.sc.extensions.json.JSONException;
import com.hms_networks.americas.sc.extensions.json.JSONObject;
import com.hms_networks.americas.sc.extensions.json.JSONTokener;
import com.hms_networks.americas.sc.extensions.logging.Logger;
import com.hms_networks.americas.sc.extensions.taginfo.TagInfo;
import com.hms_networks.americas.sc.extensions.taginfo.TagInfoManager;
import com.hms_networks.americas.sc.extensions.taginfo.TagType;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * Class for handling triggered tag updates from a remote service in a defined JSON format.
 *
 * @since 1.0.0
 * @author HMS Networks, MU Americas Solution Center
 */
public class TagUpdateHandler {

  /**
   * Constant integer value used to indicate a non-found or missing tag ID when checking if a tag is
   * present.
   */
  private static final int TAG_UPDATE_MESSAGE_TAG_ID_EMPTY = -1;

  /** Constant string value used to access the message ID field in tag update messages. */
  private static final String TAG_UPDATE_MESSAGE_ID_KEY = "id";

  /** Constant string value used to access the JSON RPC field in tag update messages. */
  private static final String TAG_UPDATE_MESSAGE_JSON_RPC_KEY = "jsonrpc";

  /**
   * Constant string value used to indicate the supported and expected JSON RPC version for tag
   * update messages.
   */
  private static final String TAG_UPDATE_MESSAGE_JSON_RPC_VERSION = "2.0";

  /** Constant string value used to access the error field in tag update messages. */
  private static final String TAG_UPDATE_MESSAGE_ERROR_KEY = "error";

  /** Constant string value used to access the result field in tag update messages. */
  private static final String TAG_UPDATE_MESSAGE_RESULT_KEY = "result";

  /**
   * Constant string value used to access the restore previous values on fault field in tag update
   * messages.
   */
  private static final String TAG_UPDATE_MESSAGE_RESTORE_PREVIOUS_ON_FAULT_KEY =
      "restorePreviousTagValuesOnUpdateFailure";

  /** Constant string value used to access the tags field in tag update messages. */
  private static final String TAG_UPDATE_MESSAGE_TAGS_KEY = "tagUpdates";

  /** Constant string value used to access the error code field in tag update messages. */
  private static final String TAG_UPDATE_MESSAGE_ERROR_CODE_KEY = "code";

  /** Constant string value used to access the error message field in tag update messages. */
  private static final String TAG_UPDATE_MESSAGE_ERROR_MESSAGE_KEY = "message";

  /** Constant string value used to access the tag name field in tag update messages. */
  private static final String TAG_UPDATE_MESSAGE_TAG_NAME_KEY = "name";

  /** Constant string value used to access the tag type field in tag update messages. */
  private static final String TAG_UPDATE_MESSAGE_TAG_TYPE_KEY = "type";

  /** Constant string value used to access the tag value field in tag update messages. */
  private static final String TAG_UPDATE_MESSAGE_TAG_VALUE_KEY = "value";

  /**
   * Default boolean value of the restore previous values on fault field in tag update messages, if
   * not present.
   */
  private static final boolean TAG_UPDATE_MESSAGE_RESTORE_PREVIOUS_ON_FAULT_DEFAULT = false;

  /** Constant string value used to indicate the integer tag type. */
  private static final String TAG_UPDATE_MESSAGE_TAG_TYPE_INTEGER_STRING = "integer";

  /** Constant string value used to indicate the float tag type. */
  private static final String TAG_UPDATE_MESSAGE_TAG_TYPE_FLOAT_STRING = "float";

  /** Constant string value used to indicate the string tag type. */
  private static final String TAG_UPDATE_MESSAGE_TAG_TYPE_STRING_STRING = "string";

  /** Constant string value used to indicate the boolean tag type. */
  private static final String TAG_UPDATE_MESSAGE_TAG_TYPE_BOOLEAN_STRING = "boolean";

  /** Constant string value used to indicate the DWORD tag type. */
  private static final String TAG_UPDATE_MESSAGE_TAG_TYPE_DWORD_STRING = "dword";

  /**
   * Applies the tag values from the specified {@link JSONArray} of tag values, and restores the
   * original tag values if a failure occurs as specified by the parameter <code>
   * restorePreviousTagValuesOnUpdateFailure</code>.
   *
   * @param tagValuesArray array of tag values to update/apply
   * @param restorePreviousTagValuesOnUpdateFailure boolean indicating if original tag values should
   *     be restored on error
   * @return integer enumeration indicating result (apply failure, type mismatch, etc)
   */
  private static TagUpdateResult applyTagValuesFromJsonArray(
      JSONArray tagValuesArray, boolean restorePreviousTagValuesOnUpdateFailure) {
    // Create map to store previous values in case of restore, if enabled
    HashMap previousValsMap = null;
    if (restorePreviousTagValuesOnUpdateFailure) {
      previousValsMap = new HashMap();
    }

    // Loop through each tag in array
    TagUpdateResult tagUpdateResult = TagUpdateResult.SUCCESS;
    String currTagName = "";
    String currTagValueString = "";
    for (int tagIndex = 0; tagIndex < tagValuesArray.length(); tagIndex++) {
      try {
        // Get tag object
        JSONObject tagObject = tagValuesArray.getJSONObject(tagIndex);
        String currTagType = tagObject.getString(TAG_UPDATE_MESSAGE_TAG_TYPE_KEY);
        currTagName = tagObject.getString(TAG_UPDATE_MESSAGE_TAG_NAME_KEY);

        // Create tag control object
        TagControl currTagControl = new TagControl();
        currTagControl.setTagName(currTagName);

        // Backup previous value (if enabled) and set new value with proper data type
        if (currTagType.equals(TAG_UPDATE_MESSAGE_TAG_TYPE_INTEGER_STRING)) {
          // Backup previous value
          if (restorePreviousTagValuesOnUpdateFailure) {
            previousValsMap.put(currTagName, new Integer(currTagControl.getTagValueAsInt()));
          }

          // Update tag value
          int currTagValue = tagObject.getInt(TAG_UPDATE_MESSAGE_TAG_VALUE_KEY);
          currTagValueString = String.valueOf(currTagValue);
          currTagControl.setTagValueAsInt(currTagValue);
        } else if (currTagType.equals(TAG_UPDATE_MESSAGE_TAG_TYPE_FLOAT_STRING)) {
          // Backup previous value
          if (restorePreviousTagValuesOnUpdateFailure) {
            previousValsMap.put(currTagName, new Double(currTagControl.getTagValueAsDouble()));
          }

          // Update tag value
          double currTagValue = tagObject.getDouble(TAG_UPDATE_MESSAGE_TAG_VALUE_KEY);
          currTagValueString = String.valueOf(currTagValue);
          currTagControl.setTagValueAsDouble(currTagValue);
        } else if (currTagType.equals(TAG_UPDATE_MESSAGE_TAG_TYPE_STRING_STRING)) {
          // Backup previous value
          if (restorePreviousTagValuesOnUpdateFailure) {
            previousValsMap.put(currTagName, currTagControl.getTagValueAsString());
          }

          // Update tag value
          String currTagValue = tagObject.getString(TAG_UPDATE_MESSAGE_TAG_VALUE_KEY);
          currTagValueString = String.valueOf(currTagValue);
          currTagControl.setTagValueAsString(currTagValue);
        } else if (currTagType.equals(TAG_UPDATE_MESSAGE_TAG_TYPE_BOOLEAN_STRING)) {
          // Backup previous value
          if (restorePreviousTagValuesOnUpdateFailure) {
            previousValsMap.put(currTagName, new Integer(currTagControl.getTagValueAsInt()));
          }

          // Update tag value
          boolean currTagValue = tagObject.getBoolean(TAG_UPDATE_MESSAGE_TAG_VALUE_KEY);
          int currTagValueBooleanInt = currTagValue ? 1 : 0;
          currTagValueString = String.valueOf(currTagValueBooleanInt);
          currTagControl.setTagValueAsInt(currTagValueBooleanInt);
        } else if (currTagType.equals(TAG_UPDATE_MESSAGE_TAG_TYPE_DWORD_STRING)) {
          // Backup previous value
          if (restorePreviousTagValuesOnUpdateFailure) {
            previousValsMap.put(currTagName, new Long(currTagControl.getTagValueAsLong()));
          }

          // Update tag value
          long currTagValue = tagObject.getLong(TAG_UPDATE_MESSAGE_TAG_VALUE_KEY);
          currTagValueString = String.valueOf(currTagValue);
          currTagControl.setTagValueAsLong(currTagValue);
        }
      } catch (Exception e) {
        Logger.LOG_SERIOUS(
            "The value of the tag ["
                + currTagName
                + "] could not be updated to ["
                + currTagValueString
                + "] as specified in a tag update request response!");
        Logger.LOG_EXCEPTION(e);
        tagUpdateResult = TagUpdateResult.APPLY_FAILURE;

        // Break out of loop to prevent more values that will be restored from being set
        if (restorePreviousTagValuesOnUpdateFailure) {
          break;
        }
      }
    }

    // Check if error occurred during apply
    if (tagUpdateResult != TagUpdateResult.SUCCESS && restorePreviousTagValuesOnUpdateFailure) {
      // Log restore start
      Logger.LOG_SERIOUS(
          "Tag values are being restored due to previous failure while applying tag "
              + "values from a tag update request response...");

      // Perform restore of each previous value from map
      boolean isPartialRestore = false;
      final Iterator previousValsEntryIterator = previousValsMap.entrySet().iterator();
      while (previousValsEntryIterator.hasNext()) {
        // Get previous tag name and value
        final Entry previousValEntry = (Entry) previousValsEntryIterator.next();
        final String previousTagName = (String) previousValEntry.getKey();
        final Object previousTagValue = previousValEntry.getValue();

        // Create tag control object
        TagControl previousTagControl = new TagControl();
        previousTagControl.setTagName(previousTagName);

        // Restore previous tag value according to type (note: boolean is stored as integer 0/1)
        try {
          if (previousTagValue instanceof Integer) {
            Integer previousTagValueInteger = (Integer) previousTagValue;
            previousTagControl.setTagValueAsInt(previousTagValueInteger.intValue());
          } else if (previousTagValue instanceof Double) {
            Double previousTagValueDouble = (Double) previousTagValue;
            previousTagControl.setTagValueAsDouble(previousTagValueDouble.doubleValue());
          } else if (previousTagValue instanceof String) {
            String previousTagValueString = (String) previousTagValue;
            previousTagControl.setTagValueAsString(previousTagValueString);
          } else if (previousTagValue instanceof Long) {
            Long previousTagValueLong = (Long) previousTagValue;
            previousTagControl.setTagValueAsLong(previousTagValueLong.longValue());
          } else {
            Logger.LOG_SERIOUS(
                "An unknown data type was encountered while restoring the previous value of ["
                    + previousTagName
                    + "] after tag update failure. Value not restored!");
            isPartialRestore = true;
          }
        } catch (Exception e) {
          Logger.LOG_SERIOUS(
              "An error occurred while restoring the previous value of ["
                  + previousTagName
                  + "] to ["
                  + previousTagValue.toString()
                  + "] after tag update failure.");
          Logger.LOG_EXCEPTION(e);
          isPartialRestore = true;
        }
      }

      // Log completion
      if (isPartialRestore) {
        Logger.LOG_SERIOUS(
            "Tag values have been partially restored! See previous logs for specific "
                + "tag details.");
      } else {
        Logger.LOG_SERIOUS("Tag values have been restored successfully!");
      }
    }

    return tagUpdateResult;
  }

  /**
   * Check if the tag with the specified tag ID on the Ewon matches the specified expected tag type.
   *
   * @param tagId Ewon tag ID
   * @param expectedTagType expected tag type
   * @return true/false indicating if tag type matches
   */
  private static boolean doesTagTypeMatch(int tagId, String expectedTagType) {
    final TagInfo tagInfo = TagInfoManager.getTagInfoFromTagId(tagId);
    boolean matches = true;
    if (expectedTagType.equals(TAG_UPDATE_MESSAGE_TAG_TYPE_INTEGER_STRING)
        && tagInfo.getType() != TagType.INTEGER
        && tagInfo.getType() != TagType.INTEGER_MAPPED_STRING) {
      matches = false;
    } else if (expectedTagType.equals(TAG_UPDATE_MESSAGE_TAG_TYPE_FLOAT_STRING)
        && tagInfo.getType() != TagType.FLOAT) {
      matches = false;
    } else if (expectedTagType.equals(TAG_UPDATE_MESSAGE_TAG_TYPE_STRING_STRING)
        && tagInfo.getType() != TagType.STRING) {
      matches = false;
    } else if (expectedTagType.equals(TAG_UPDATE_MESSAGE_TAG_TYPE_BOOLEAN_STRING)
        && tagInfo.getType() != TagType.BOOLEAN) {
      matches = false;
    } else if (expectedTagType.equals(TAG_UPDATE_MESSAGE_TAG_TYPE_DWORD_STRING)
        && tagInfo.getType() != TagType.DWORD) {
      matches = false;
    }
    return matches;
  }

  /**
   * Processes and applies the specified tag update body after verifying its contents. Its contents
   * are expected in the following format: <code>
   * {
   *   "restorePreviousTagValuesOnUpdateFailure": true,
   *   "tagUpdates": [
   *     {
   *       "name": "TagName1",
   *       "type": "integer",
   *       "value": 11
   *     },
   *     {
   *       "name": "TagName2",
   *       "type": "float",
   *       "value": 22.2
   *     },
   *     {
   *       "name": "TagName3",
   *       "type": "boolean",
   *       "value": true
   *     },
   *     {
   *       "name": "TagName4",
   *       "type": "dword",
   *       "value": 33
   *     },
   *     {
   *       "name": "TagName5",
   *       "type": "string",
   *       "value": "example string value"
   *     }
   *   ]
   * }
   * </code>
   *
   * @param body tag update body (JSON)
   * @return tag update result
   * @throws JSONException if unable to parse tag update body
   */
  public static TagUpdateResult processTagUpdate(JSONObject body) throws JSONException {
    // Create variable to store result
    TagUpdateResult tagUpdateResult = TagUpdateResult.SUCCESS;

    // Check for presence of restore previous values behavior flag
    boolean restorePreviousValsOnFault = TAG_UPDATE_MESSAGE_RESTORE_PREVIOUS_ON_FAULT_DEFAULT;
    if (body.has(TAG_UPDATE_MESSAGE_RESTORE_PREVIOUS_ON_FAULT_KEY)) {
      restorePreviousValsOnFault =
          body.getBoolean(TAG_UPDATE_MESSAGE_RESTORE_PREVIOUS_ON_FAULT_KEY);
    }

    // Check for presence of tags object
    if (body.has(TAG_UPDATE_MESSAGE_TAGS_KEY)) {
      // Get tags array
      JSONArray tagsArray = body.getJSONArray(TAG_UPDATE_MESSAGE_TAGS_KEY);

      // Check that all tags are present and correct type
      for (int tagIndex = 0; tagIndex < tagsArray.length(); tagIndex++) {
        // Get tag object
        JSONObject tagObject = tagsArray.getJSONObject(tagIndex);

        if (tagObject.has(TAG_UPDATE_MESSAGE_TAG_NAME_KEY)
            && tagObject.has(TAG_UPDATE_MESSAGE_TAG_TYPE_KEY)
            && tagObject.has(TAG_UPDATE_MESSAGE_TAG_VALUE_KEY)) {
          String tagName = tagObject.getString(TAG_UPDATE_MESSAGE_TAG_NAME_KEY);
          String tagType = tagObject.getString(TAG_UPDATE_MESSAGE_TAG_TYPE_KEY);
          int tagId = TAG_UPDATE_MESSAGE_TAG_ID_EMPTY;

          // Create tag control to check if tag exists and get ID for type lookup
          try {
            TagControl tagControl = new TagControl();
            tagControl.setTagName(tagName);
            tagId = tagControl.getTagId();
          } catch (Exception e) {
            Logger.LOG_SERIOUS(
                "A tag update request response was received with a tag ("
                    + tagName
                    + ") that does not "
                    + "exist and will not be processed.");
            tagUpdateResult = TagUpdateResult.MISSING_TAGS;
          }

          // If tag exists, check that tag type matches
          if (tagId != TAG_UPDATE_MESSAGE_TAG_ID_EMPTY) {
            if (!doesTagTypeMatch(tagId, tagType)) {
              Logger.LOG_SERIOUS(
                  "A tag update request response was received with a tag ("
                      + tagName
                      + ") that has mismatched types and will not be processed.");
              tagUpdateResult = TagUpdateResult.MISMATCHED_TAG_TYPES;
            }
          }
        } else {
          Logger.LOG_SERIOUS(
              "A tag update request response was received with an improperly formatted "
                  + "tag entry. Format is unknown and will not be processed.");
          tagUpdateResult = TagUpdateResult.VERIFY_FAILURE;
        }
      }

      // Apply tag values if no previous errors
      if (tagUpdateResult == TagUpdateResult.SUCCESS) {
        tagUpdateResult = applyTagValuesFromJsonArray(tagsArray, restorePreviousValsOnFault);
      }
    } else {
      Logger.LOG_SERIOUS(
          "A tag update request response was received without any tags or an error. "
              + "Format is unknown and will not be processed.");
      tagUpdateResult = TagUpdateResult.VERIFY_FAILURE;
    }
    return tagUpdateResult;
  }

  /**
   * Processes the specified tag update body by converting to a JSON object and handling it using
   * {@link #processTagUpdate(JSONObject)}. Its contents are expected in the following format:
   * <code>
   * {
   *   "restorePreviousTagValuesOnUpdateFailure": true,
   *   "tagUpdates": [
   *     {
   *       "name": "TagName1",
   *       "type": "integer",
   *       "value": 11
   *     },
   *     {
   *       "name": "TagName2",
   *       "type": "float",
   *       "value": 22.2
   *     },
   *     {
   *       "name": "TagName3",
   *       "type": "boolean",
   *       "value": true
   *     },
   *     {
   *       "name": "TagName4",
   *       "type": "dword",
   *       "value": 33
   *     },
   *     {
   *       "name": "TagName5",
   *       "type": "string",
   *       "value": "example string value"
   *     }
   *   ]
   * }
   * </code>
   *
   * @param body tag update body (string)
   * @return tag update result
   * @throws JSONException if unable to parse tag update body
   */
  public static TagUpdateResult processTagUpdate(String body) throws JSONException {
    // Get JSON object from response body
    JSONTokener jsonTokener = new JSONTokener(body);
    JSONObject responseBodyJson = new JSONObject(jsonTokener);

    return processTagUpdate(responseBodyJson);
  }

  /**
   * Processes the specified tag update request response body and verifies that it matches the
   * expected ID and format (RPC 2.0). Its contents are expected in the following format: <code>
   * {
   *   "id": 1,
   *   "jsonrpc": "2.0",
   *   "result": {
   *     "restorePreviousTagValuesOnUpdateFailure": true,
   *     "tagUpdates": [
   *       {
   *         "name": "TagName1",
   *         "type": "integer",
   *         "value": 11
   *       },
   *       {
   *         "name": "TagName2",
   *         "type": "float",
   *         "value": 22.2
   *       },
   *       {
   *         "name": "TagName3",
   *         "type": "boolean",
   *         "value": true
   *       },
   *       {
   *         "name": "TagName4",
   *         "type": "dword",
   *         "value": 33
   *       },
   *       {
   *         "name": "TagName5",
   *         "type": "string",
   *         "value": "example string value"
   *       }
   *     ]
   *   }
   * }
   * </code>
   *
   * @param responseBody tag update request response body
   * @param expectedId expected ID of tag update request response
   * @return tag update result
   * @throws JSONException if unable to parse tag update request response body
   */
  public static TagUpdateResult processTagUpdateRequestResponse(String responseBody, int expectedId)
      throws JSONException {
    // Create variable to store result
    TagUpdateResult tagUpdateResult = TagUpdateResult.SUCCESS;

    // Get JSON object from response body
    JSONTokener jsonTokener = new JSONTokener(responseBody);
    JSONObject responseBodyJson = new JSONObject(jsonTokener);

    // Check if message ID matches
    boolean messageIdMatches =
        responseBodyJson.has(TAG_UPDATE_MESSAGE_ID_KEY)
            && responseBodyJson.getInt(TAG_UPDATE_MESSAGE_ID_KEY) == expectedId;
    if (!messageIdMatches) {
      Logger.LOG_SERIOUS(
          "A tag update request response was received with a missing or mismatched "
              + "identifier (ID) and will not be processed. Expected: "
              + expectedId
              + " Got: "
              + responseBodyJson.getString(TAG_UPDATE_MESSAGE_ID_KEY));
      tagUpdateResult = TagUpdateResult.VERIFY_FAILURE;
    }

    // Check that JSON RPC version matches expected
    boolean jsonRpcVersionMatches =
        responseBodyJson.has(TAG_UPDATE_MESSAGE_JSON_RPC_KEY)
            && responseBodyJson
                .getString(TAG_UPDATE_MESSAGE_JSON_RPC_KEY)
                .equals(TAG_UPDATE_MESSAGE_JSON_RPC_VERSION);
    if (tagUpdateResult == TagUpdateResult.SUCCESS && !jsonRpcVersionMatches) {
      Logger.LOG_SERIOUS(
          "A tag update request response was received with a missing or mismatched JSON "
              + "RPC version and will not be processed. Expected: "
              + TAG_UPDATE_MESSAGE_JSON_RPC_VERSION
              + " Got: "
              + responseBodyJson.getString(TAG_UPDATE_MESSAGE_JSON_RPC_KEY));
      tagUpdateResult = TagUpdateResult.VERIFY_FAILURE;
    }

    // Check if a result or error was returned as response
    if (responseBodyJson.has(TAG_UPDATE_MESSAGE_RESULT_KEY)
        && tagUpdateResult == TagUpdateResult.SUCCESS) {
      // Get result JSON object and process
      JSONObject resultJsonObject = responseBodyJson.getJSONObject(TAG_UPDATE_MESSAGE_RESULT_KEY);
      processTagUpdate(resultJsonObject);

    } else if (responseBodyJson.has(TAG_UPDATE_MESSAGE_ERROR_KEY)) {
      // Get error JSON object
      JSONObject errorJsonObject = responseBodyJson.getJSONObject(TAG_UPDATE_MESSAGE_ERROR_KEY);

      // Check for error code
      if (errorJsonObject.has(TAG_UPDATE_MESSAGE_ERROR_CODE_KEY)) {
        tagUpdateResult =
            new TagUpdateResult(errorJsonObject.getInt(TAG_UPDATE_MESSAGE_ERROR_CODE_KEY));
      } else {
        Logger.LOG_SERIOUS(
            "A tag update request response returned an error but did not "
                + "include an error code!");
      }

      // Check for error message
      String errorMessage = "";
      if (errorJsonObject.has(TAG_UPDATE_MESSAGE_ERROR_MESSAGE_KEY)) {
        errorMessage = errorJsonObject.getString(TAG_UPDATE_MESSAGE_ERROR_MESSAGE_KEY);
      }

      Logger.LOG_SERIOUS(
          "A tag update request response returned the following error ("
              + tagUpdateResult
              + "): "
              + errorMessage);
    } else {
      Logger.LOG_SERIOUS(
          "A tag update request response was received without a result or an error. Format "
              + "is unknown and will not be processed.");
      tagUpdateResult = TagUpdateResult.VERIFY_FAILURE;
    }

    return tagUpdateResult;
  }
}
