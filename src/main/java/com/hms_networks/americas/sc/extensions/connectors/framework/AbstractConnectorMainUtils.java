package com.hms_networks.americas.sc.extensions.connectors.framework;

import com.ewon.ewonitf.EWException;
import com.ewon.ewonitf.TagControl;
import com.hms_networks.americas.sc.extensions.logging.Logger;
import com.hms_networks.americas.sc.extensions.system.tags.SCTagUtils;

/**
 * Class with utility methods for the {@link AbstractConnectorMain} class.
 *
 * <p>This class is intended to provide utility methods that are used by the {@link
 * AbstractConnectorMain} class, but in a separate class to avoid cluttering the main class.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.15.2
 * @version 1.15.2
 */
public class AbstractConnectorMainUtils {

  /**
   * Sets up the connector control tag. This method will attempt to load the tag from the Flexy, and
   * if it does not exist, it will create it.
   *
   * @return the {@link TagControl} object for the connector control tag, or null if the tag could
   *     not be loaded or created
   * @since 1.15.2
   */
  protected static TagControl setUpConnectorControlTag() {
    // Load connector control tag or create new one if it doesn't exist
    TagControl connectorControlTag = null;
    try {
      connectorControlTag = new TagControl(AbstractConnectorMainConstants.CONNECTOR_HALT_TAG_NAME);
    } catch (Exception e1) {
      Logger.LOG_INFO(
          "Unable to create tag object to track connector control tag! Attempting to create `"
              + AbstractConnectorMainConstants.CONNECTOR_HALT_TAG_NAME
              + "` tag.");
      Logger.LOG_EXCEPTION(e1);
      try {
        SCTagUtils.createTag(
            AbstractConnectorMainConstants.CONNECTOR_HALT_TAG_NAME,
            AbstractConnectorMainConstants.CONNECTOR_HALT_TAG_DESCRIPTION,
            AbstractConnectorMainConstants.CONNECTOR_HALT_TAG_IO_SERVER_NAME,
            AbstractConnectorMainConstants.CONNECTOR_HALT_TAG_TYPE);
        connectorControlTag =
            new TagControl(AbstractConnectorMainConstants.CONNECTOR_HALT_TAG_NAME);
        Logger.LOG_INFO(
            "Created `" + AbstractConnectorMainConstants.CONNECTOR_HALT_TAG_NAME + "` tag.");
      } catch (Exception e2) {
        Logger.LOG_WARN(
            "Unable to create tag `"
                + AbstractConnectorMainConstants.CONNECTOR_HALT_TAG_NAME
                + "`! To control this connector, create a boolean tag with the name `"
                + AbstractConnectorMainConstants.CONNECTOR_HALT_TAG_NAME
                + "`.");
        Logger.LOG_EXCEPTION(e2);
      }
    }

    // Reset value
    if (connectorControlTag != null) {
      try {
        connectorControlTag.setTagValueAsInt(
            AbstractConnectorMainConstants.CONNECTOR_CONTROL_TAG_RUN_VALUE);
      } catch (EWException e) {
        Logger.LOG_WARN(
            "Unable to reset tag `"
                + AbstractConnectorMainConstants.CONNECTOR_HALT_TAG_NAME
                + "` to "
                + AbstractConnectorMainConstants.CONNECTOR_CONTROL_TAG_RUN_VALUE
                + "! The connector may shut down if the halt tag value indicates.");
        Logger.LOG_EXCEPTION(e);
      }
    }

    return connectorControlTag;
  }

  /**
   * Sets up the connector data polling disable tag. This method will attempt to load the tag from
   * the Flexy, and if it does not exist, it will create it.
   *
   * @return the {@link TagControl} object for the connector data polling disable tag, or null if
   *     the tag could not be loaded or created
   * @since 1.15.2
   */
  protected static TagControl setupConnectorDataPollingDisableControlTag() {
    // Load connector control tag or create new one if it doesn't exist
    TagControl connectorDataPollingDisableTag = null;
    try {
      connectorDataPollingDisableTag =
          new TagControl(AbstractConnectorMainConstants.CONNECTOR_DATA_POLLING_DISABLE_TAG_NAME);
    } catch (Exception e1) {
      Logger.LOG_INFO(
          "Unable to create tag object to track connector data polling disable tag! "
              + "Attempting to create `"
              + AbstractConnectorMainConstants.CONNECTOR_DATA_POLLING_DISABLE_TAG_NAME
              + "` tag.");
      Logger.LOG_EXCEPTION(e1);
      try {
        SCTagUtils.createPersistentMemTag(
            AbstractConnectorMainConstants.CONNECTOR_DATA_POLLING_DISABLE_TAG_NAME,
            AbstractConnectorMainConstants.CONNECTOR_DATA_POLLING_DISABLE_TAG_DESCRIPTION,
            AbstractConnectorMainConstants.CONNECTOR_DATA_POLLING_DISABLE_TAG_TYPE);
        connectorDataPollingDisableTag =
            new TagControl(AbstractConnectorMainConstants.CONNECTOR_DATA_POLLING_DISABLE_TAG_NAME);
        Logger.LOG_INFO(
            "Created `"
                + AbstractConnectorMainConstants.CONNECTOR_DATA_POLLING_DISABLE_TAG_NAME
                + "` tag.");
      } catch (Exception e2) {
        Logger.LOG_WARN(
            "Unable to create tag `"
                + AbstractConnectorMainConstants.CONNECTOR_DATA_POLLING_DISABLE_TAG_NAME
                + "`! To enable/disable data polling, create a boolean tag with the name `"
                + AbstractConnectorMainConstants.CONNECTOR_DATA_POLLING_DISABLE_TAG_NAME
                + "`.");
        Logger.LOG_EXCEPTION(e2);
      }
    }

    return connectorDataPollingDisableTag;
  }
}
