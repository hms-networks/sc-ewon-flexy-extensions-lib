package com.hms_networks.americas.sc.extensions.system.tags;

import com.ewon.ewonitf.SysControlBlock;

/**
 * Utility class for creating, deleting and manipulating tags on the Ewon Flexy system.
 *
 * @since 1.0.0
 * @author HMS Networks, MU Americas Solution Center
 */
public class SCTagUtils {

  /** Key for accessing/manipulating the tag name item in a system con4trol block. */
  private static final String SCB_TAG_NAME_KEY = "Name";

  /** Key for accessing/manipulating the tag description item in a system control block. */
  private static final String SCB_TAG_DESCRIPTION_KEY = "Description";

  /** Key for accessing/manipulating the tag IO server name item in a system control block. */
  private static final String SCB_TAG_SERVER_NAME_KEY = "ServerName";

  /** Key for accessing/manipulating the tag type item in a system control block. */
  private static final String SCB_TAG_TYPE_KEY = "Type";

  /** Key for accessing/manipulating the tag do-delete item in a system control block. */
  private static final String SCB_TAG_DO_DELETE_KEY = "DoDelete";

  /** The value used for the tag do-delete item which indicates that the tag should be deleted. */
  private static final String SCB_TAG_DO_DELETE_TRUE_VALUE = "1";

  /**
   * Creates a new tag on the Ewon Flexy with the specified tag name, tag IO server name, and tag
   * type.
   *
   * @param tagName name of tag to add
   * @param ioServerName IO server name of tag to add
   * @param tagType type of tag to add (0: boolean, 1: float, 2: integer, 3: dword, 6: string)
   * @throws Exception if unable to create tag with specified information
   */
  public static void createTag(String tagName, String ioServerName, int tagType) throws Exception {
    createTag(tagName, null, ioServerName, tagType);
  }

  /**
   * Creates a new tag on the Ewon Flexy with the specified tag name, tag description, tag IO server
   * name, and tag type.
   *
   * @param tagName name of tag to add
   * @param tagDescription description of tag to add
   * @param ioServerName IO server name of tag to add
   * @param tagType type of tag to add (0: boolean, 1: float, 2: integer, 3: dword, 6: string)
   * @throws Exception if unable to create tag with specified information
   */
  public static void createTag(
      String tagName, String tagDescription, String ioServerName, int tagType) throws Exception {
    // Create tag system control block
    SysControlBlock tagSysControlBlock = new SysControlBlock(SysControlBlock.TAG);

    // Set tag name and IO server name
    tagSysControlBlock.setItem(SCB_TAG_NAME_KEY, tagName);
    tagSysControlBlock.setItem(SCB_TAG_SERVER_NAME_KEY, ioServerName);
    tagSysControlBlock.setItem(SCB_TAG_TYPE_KEY, String.valueOf(tagType));

    // Set tag description, if not null
    if (tagDescription != null) {
      tagSysControlBlock.setItem(SCB_TAG_DESCRIPTION_KEY, tagDescription);
    }

    // Save system control block
    tagSysControlBlock.saveBlock(true);
  }
}
