package de.chatsphere.feature.file.hash;

import org.junit.Assert;
import org.junit.Test;

public class Sha256HashingAlgorithmTest {

  private static final byte[] DATA = "Some random text.".getBytes();
  private static final byte[] HASH = {
    (byte) 0x15, (byte) 0x7c, (byte) 0x09, (byte) 0xfe, (byte) 0x22, (byte) 0xc7, (byte) 0xb5,
    (byte) 0x9c,
    (byte) 0x98, (byte) 0x80, (byte) 0x9d, (byte) 0xa6, (byte) 0x54, (byte) 0xdc, (byte) 0x81,
    (byte) 0x9f,
    (byte) 0x9a, (byte) 0xbe, (byte) 0x94, (byte) 0x5b, (byte) 0x94, (byte) 0xf0, (byte) 0xe8,
    (byte) 0xd1,
    (byte) 0x38, (byte) 0xd6, (byte) 0x07, (byte) 0xde, (byte) 0x3d, (byte) 0xb7, (byte) 0x50,
    (byte) 0xfc};

  public Sha256HashingAlgorithmTest() {
  }

  @Test
  public void testHash() {
    HashingAlgorithm sha256 = new Sha256HashingAlgorithm();
    Assert.assertArrayEquals(HASH, sha256.hash(DATA));
  }

}
