package de.chatsphere.feature.file.storage;

import de.chatsphere.io.database.schema.preference.File;
import org.apache.tika.Tika;

public class MimeTypeDetectorFileStorageDecorator extends FileStorageDecorator {

  private final Tika tika;

  public MimeTypeDetectorFileStorageDecorator(FileStorage fileStorage) {
    super(fileStorage);
    tika = new Tika();
  }

  @Override
  public void createFile(File file, byte[] data) {
    String mimeType = tika.detect(data, file.getName());
    file.setType(mimeType);
    fileStorage.createFile(file, data);
  }

}
