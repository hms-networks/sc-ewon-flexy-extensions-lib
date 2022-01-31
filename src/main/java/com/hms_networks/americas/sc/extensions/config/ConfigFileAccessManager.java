package com.hms_networks.americas.sc.extensions.config;

import com.hms_networks.americas.sc.extensions.fileutils.FileAccessManager;
import com.hms_networks.americas.sc.extensions.json.JSONException;
import com.hms_networks.americas.sc.extensions.json.JSONObject;
import com.hms_networks.americas.sc.extensions.json.JSONTokener;
import java.io.IOException;

/**
 * Class for reading and writing {@link JSONObject}s from file.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.0.0
 */
public class ConfigFileAccessManager {
  /**
   * Read a {@link JSONObject} from the specified file name.
   *
   * @param filename file to read from
   * @return JSON object read from file
   * @throws IOException if unable to read from file
   * @throws JSONException if unable to convert file contents to JSON
   */
  public static JSONObject getJsonObjectFromFile(String filename)
      throws IOException, JSONException {
    // Read file contents to string
    String jsonObjectAsString = FileAccessManager.readFileToString(filename);

    // Create JSON tokener using file contents
    JSONTokener jsonTokener = new JSONTokener(jsonObjectAsString);

    // Create and return JSON object from tokener
    return new JSONObject(jsonTokener);
  }

  /**
   * Write the specified {@link JSONObject} to the specified file name using the supplied indent
   * factor.
   *
   * @param filename file to write to
   * @param indentFactor JSON file indent factor
   * @param jsonObject JSON object to write to file
   * @throws IOException if unable to write to file
   * @throws JSONException if unable to convert JSON to string
   */
  public static void writeJsonObjectToFile(String filename, int indentFactor, JSONObject jsonObject)
      throws JSONException, IOException {
    // Convert JSON object to string
    String jsonObjectAsString = jsonObject.toString(indentFactor);

    // Write converted JSON string to file
    FileAccessManager.writeStringToFile(filename, jsonObjectAsString);
  }
}
