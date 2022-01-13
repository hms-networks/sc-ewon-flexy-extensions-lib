package com.hms_networks.americas.sc.extensions.realtimedata;

import java.util.ArrayList;

import com.ewon.ewonitf.TagControl;
import com.hms_networks.americas.sc.extensions.datapoint.DataPoint;
import com.hms_networks.americas.sc.extensions.datapoint.DataPointBoolean;
import com.hms_networks.americas.sc.extensions.datapoint.DataPointDword;
import com.hms_networks.americas.sc.extensions.datapoint.DataPointFloat;
import com.hms_networks.americas.sc.extensions.datapoint.DataPointString;
import com.hms_networks.americas.sc.extensions.logging.Logger;
import com.hms_networks.americas.sc.extensions.taginfo.TagInfo;
import com.hms_networks.americas.sc.extensions.taginfo.TagType;
import java.util.Date;

/**
 * This class will hold a list of data points for each tag. One instance of the class is made per
 * tag.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.0.0
 */
public class RealTimeTagDataPointManager {
  /** The name of the tag */
  public String tagName;

  /** A list of data points retrieved for the tag */
  private ArrayList dataPoints;

  /** The last data value retrieved for this tag */
  private DataPoint lastDataPoint;

  /** hold a tag control object to use when retrieving data points */
  public TagControl tagControl;

  /**
   * Gets the tag's name
   *
   * @return The tag name
   */
  public String getTagName() {
    return tagName;
  }

  /**
   * Default constructor for {@link RealTimeTagDataPointManager}.
   *
   * @param tagName The name of the tag that data points are coming from
   * @throws Exception when unable to create TagControl Object
   */
  public RealTimeTagDataPointManager(String tagName) throws Exception {
    this.tagName = tagName;
    this.lastDataPoint = null;
    this.dataPoints = new ArrayList();
    tagControl = new TagControl(tagName);
  }

  /**
   * Add a point to the list of data points only if it is not a duplicate data point.
   *
   * @param data the data point to add to the data point list.
   */
  public void addDataPoint(DataPoint data) {
    if (!data.equals(lastDataPoint)) {
      dataPoints.add(data);
      lastDataPoint = data;
    } else {
      Logger.LOG_INFO(
          "Duplicate data point not added. Tag: "
              + data.getTagName()
              + " value: "
              + data.getValueString());
    }
  }

  /**
   * Removes data point from the top of the list of data points.
   *
   * @return the data point that was removed
   */
  public DataPoint removeDataPoint() {
    final int lastDataPointIndex = dataPoints.size() - 1;
    DataPoint data = (DataPoint) dataPoints.get(lastDataPointIndex);
    dataPoints.remove(lastDataPointIndex);
    return data;
  }

  /**
   * Get the size of the list of data points.
   *
   * @return the size of the list of data points
   */
  public int getSize() {
    return dataPoints.size();
  }

  /**
   * Gets a tag's current value into a list of tag current values.
   *
   * @param tag the tag to inspect
   */
  public void recordCurentTagValue(TagInfo tag) {

    String tagName = tag.getName();
    TagType tagType = tag.getType();
    String tagUnit = tag.getUnit();
    int tagID = tag.getId();
    final int millisecondsInSeconds = 1000;
    final long currentTimeMilliseconds = new Date().getTime();
    String timeStampSeconds = String.valueOf(currentTimeMilliseconds / millisecondsInSeconds);
    DataPoint data = null;

    if (tagControl != null) {
      if (tagType == TagType.FLOAT) {
        float val = (float) tagControl.getTagValueAsDouble();
        data = new DataPointFloat(tagName, tagID, tagUnit, val, timeStampSeconds);
      } else if (tagType == TagType.INTEGER) {
        int val = tagControl.getTagValueAsInt();
        data = new DataPointFloat(tagName, tagID, tagUnit, val, timeStampSeconds);
      } else if (tagType == TagType.STRING) {
        String val = tagControl.getTagValueAsString();
        data = new DataPointString(tagName, tagID, tagUnit, val, timeStampSeconds);
      } else if (tagType == TagType.BOOLEAN) {
        boolean val = (tagControl.getTagValueAsLong() != 0);
        data = new DataPointBoolean(tagName, tagID, tagUnit, val, timeStampSeconds);
      } else if (tagType == TagType.DWORD) {
        long val = tagControl.getTagValueAsLong();
        data = new DataPointDword(tagName, tagID, tagUnit, val, timeStampSeconds);
      }
    } else {
      Logger.LOG_WARN(
          "Tag control initialization failed, cannot retrieve data points from " + tag.getName());
    }
    addDataPoint(data);
  }
}
