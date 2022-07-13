package de.chatsphere.feature.file.storage;

import de.chatsphere.io.database.schema.preference.File;

/**
 * A FileStorage stores files in a way that they can be accessed by external programs via URL. It is
 * the main interface for communication with this module. To alter the behaviour the decorator
 * pattern can be utilized by extending {@link FileStorageDecorator}.
 */
public interface FileStorage {

  /**
   * Store given files content for given file. After returning, the given files content can be
   * accessed by using {@link #getDownloadUri(de.chatsphere.io.database.schema.preference.File)}.
   * The given file argument will be modified to match the given data.
   *
   * <p>If the same file is saved twice or more, the implementation may do nothing. If the files
   * contents are equal to another file, it may not store the data twice but link to the already
   * stored data instead.
   *
   * @param file the files known metadata before creation, may be modified
   * @param data the files content
   *
   * @throws NullPointerException if name or data is null
   */
  void createFile(File file, byte[] data);

  /**
   * Create the file access URI to read the file. The returned String should be safe to be converted
   * into an {@link java.net.URI}.
   *
   * @param file the file to access
   *
   * @return the files uri as String
   * @throws NullPointerException if file is null
   */
  String getDownloadUri(File file);
}
