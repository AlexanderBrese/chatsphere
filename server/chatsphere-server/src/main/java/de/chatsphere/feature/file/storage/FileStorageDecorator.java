package de.chatsphere.feature.file.storage;

import de.chatsphere.io.database.schema.preference.File;

/**
 * This sub class provides an easy way for decorator implementations. Only methods that are
 * decorated need to be overwritten and unrelated methods can be safely ignored.
 */
public abstract class FileStorageDecorator implements FileStorage {

  protected final FileStorage fileStorage;

  /**
   * Create a FileStorageDecorator.
   *
   * @param fileStorage the FileStorage to decorate
   */
  public FileStorageDecorator(FileStorage fileStorage) {
    this.fileStorage = fileStorage;
  }

  @Override
  public String getDownloadUri(File file) {
    return fileStorage.getDownloadUri(file);
  }

  @Override
  public void createFile(File file, byte[] data) {
    fileStorage.createFile(file, data);
  }

}
