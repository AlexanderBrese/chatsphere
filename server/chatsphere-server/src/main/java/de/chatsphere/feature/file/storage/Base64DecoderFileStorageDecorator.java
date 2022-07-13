package de.chatsphere.feature.file.storage;

import de.chatsphere.io.database.schema.preference.File;
import java.util.Base64;
import java.util.Base64.Decoder;

/**
 * This FileStorageDecorator takes base64 encoded data and decodes it.
 */
public class Base64DecoderFileStorageDecorator extends FileStorageDecorator {

  private final Decoder decoder = Base64.getDecoder();

  public Base64DecoderFileStorageDecorator(FileStorage fileStorage) {
    super(fileStorage);
  }

  @Override
  public void createFile(File file, byte[] encodedData) {
    byte[] decodedData = decoder.decode(encodedData);
    fileStorage.createFile(file, decodedData);
  }

}
