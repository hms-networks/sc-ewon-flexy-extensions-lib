package com.hms.flexyosisoftconnector.demo;
import com.ewon.ewonitf.EWException;
import com.ewon.ewonitf.TagControl;
import com.hms_networks.americas.sc.extensions.logging.Logger;
import com.hms_networks.americas.sc.extensions.taginfo.TagInfo;
import com.hms_networks.americas.sc.extensions.taginfo.TagInfoManager;
import java.util.List;

public class DataSimulator{

  /**
   * Counter used in data simulation.
   */
  private int simulationCounter = 0;

  /**
   * The list of tags to run data simulations on.
   */
  private List tagNameList;

  /**
   * Sets the array of tags to manipulate with fake data to all tags on the Ewon in the tag name list.
   *
   * @param tagNameList list of tag names to manipulate with fake data.
   */
  public void setTagListByIncludeTags(List tagNameList){
    this.tagNameList = tagNameList;
  }

  /**
   * Sets the array of tags to manipulate with fake data to all tags on the Ewon not in the excluded tag list.
   *
   * @param excludedTagNames list of tag names to not manipulate with fake data.
   */
  public void setTagListByExcludeTags(List excludedTagNames){

    // for each tag on the Ewon
    for(int indexTagInfo = 0; indexTagInfo < TagInfoManager.getTagInfoList().size(); indexTagInfo++) {

      String currentTag = ((TagInfo) TagInfoManager.getTagInfoList().get(indexTagInfo)).getName();

      // add to tag list if not excluded
      if(!excludedTagNames.contains(currentTag)) {
        tagNameList.add(currentTag);
      }
    }
  }

  /**
   * * @param minutesBetweenData
   */
  public void runSimulation() {

    // seconds * milliseconds
    long waitTime = 5 * 1000;

    // loop forever
    while (true) {
      simulationCounter++;


      for (int i = 0; i < tagNameList.size(); i++) {
        String currentTagName = (String) tagNameList.get(i);
        int numberSimFunctions = 4;

        if(i % numberSimFunctions == 3){
          randomValues(currentTagName);
        }
        if ( i % numberSimFunctions ==  2) {
          sinWave(currentTagName);
        }
        if ( i % numberSimFunctions ==  1) {
          triangles(currentTagName);
        }
        if ( i % numberSimFunctions ==  0) {
          slantedLines(currentTagName);
        }

        // sleep in between each individual tag manipulation to not lock up Ewon
        try {
          int minimumSleepTimeMilliseconds = 1;
          Thread.sleep(minimumSleepTimeMilliseconds);
        } catch (InterruptedException e) {
          Logger.LOG_EXCEPTION(e);
        }
      }

      // sleep between each round of tag manipulation for specified wait time
      try {
        Logger.LOG_INFO("sim counter set to "+simulationCounter);
        Thread.sleep(waitTime);
      } catch (InterruptedException e) {
        Logger.LOG_EXCEPTION(e);
      }
    }
  }

  private void randomValues(String tagName){
    try {
      TagControl tc = new TagControl(tagName);
      tc.setTagValueAsDouble(Math.random()); // make this random and get edge cases per type.
    } catch (EWException e) {
      Logger.LOG_EXCEPTION(e);
    }
  }

  /**
   * Takes in a tag to manipulate and transforms the tag value according to a preset rule.
   */
  private void sinWave(String tagName){
    try {
      TagControl tc = new TagControl(tagName);
      double sinWave = 100 * Math.sin( (2*Math.PI)*(simulationCounter/60));
      tc.setTagValueAsDouble(sinWave);
    } catch (EWException e) {
      Logger.LOG_EXCEPTION(e);
    }
  }

  /**
   * Takes in a tag to manipulate and transforms the tag value according to a preset rule.
   */
  private void slantedLines(String tagName){
    int maxSlantedLinesCounter = 20;
    int slantedLinesCounter = simulationCounter % maxSlantedLinesCounter;
    try {
      TagControl tc = new TagControl(tagName);
      tc.setTagValueAsDouble(slantedLinesCounter);
    } catch (EWException e) {
      Logger.LOG_EXCEPTION(e);
    }
  }

  private void triangles(String tagName) {
    int maxTriangleCounter = 20;
    int triangleCounter = simulationCounter % maxTriangleCounter;

    try {
      TagControl tc = new TagControl(tagName);
      if(triangleCounter < (maxTriangleCounter / 2)){
        tc.setTagValueAsDouble(maxTriangleCounter - triangleCounter);
      } else {
        tc.setTagValueAsDouble(triangleCounter);
      }
    } catch (EWException e) {
      Logger.LOG_EXCEPTION(e);
    }
  }
}
