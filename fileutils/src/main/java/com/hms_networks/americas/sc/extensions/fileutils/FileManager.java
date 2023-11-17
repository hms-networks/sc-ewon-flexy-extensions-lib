package com.hms_networks.americas.sc.extensions.fileutils;

import java.io.File;

/**
 * Class for managing files on the Ewon Flexy file system.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.0.0
 */
public class FileManager {

  /**
   * Deletes the specified file/folder and all of its contents recursively.
   *
   * @param file file/folder to delete
   * @return true if deleted
   */
  public static boolean recursivelyDeleteFileFolder(File file) {
    boolean didDelete = false;
    if (file.exists()) {
      if (file.isDirectory()) {
        final File[] files = file.listFiles();
        if (files != null) {
          for (int x = 0; x < files.length; x++) {
            didDelete |= recursivelyDeleteFileFolder(files[x]);
          }
        }
      }
      didDelete |= file.delete();
    }
    return didDelete;
  }
}
