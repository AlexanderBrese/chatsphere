package de.chatsphere.feature.file.storage;

import de.chatsphere.io.database.schema.preference.File;
import org.junit.Test;
import org.mockito.Mockito;

public class Base64DecoderFileStorageDecoratorTest {

  private static final File FILE = new File();

  private static final byte[] ENCODED = "U29tZSByYW5kb20gdGV4dC4=".getBytes();
  private static final byte[] DECODED = "Some random text.".getBytes();

  public Base64DecoderFileStorageDecoratorTest() {
  }

  @Test
  public void testDecodeBase64() {
    FileStorage fileStorageMock = Mockito.mock(FileStorage.class);

    FileStorage decoder = new Base64DecoderFileStorageDecorator(fileStorageMock);

    decoder.createFile(FILE, ENCODED);

    Mockito.verify(fileStorageMock).createFile(FILE, DECODED);
  }

}
