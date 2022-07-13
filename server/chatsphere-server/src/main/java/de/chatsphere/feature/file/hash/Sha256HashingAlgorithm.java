package de.chatsphere.feature.file.hash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * sha256 implementation of the hashing algorithm.
 */
public class Sha256HashingAlgorithm implements HashingAlgorithm {

  private MessageDigest sha256;

  /**
   * Create a sha256 hasher instance. One instance may be shared inside of one thread, but is not
   * multithreading safe.
   */
  public Sha256HashingAlgorithm() {
    try {
      sha256 = MessageDigest.getInstance("SHA-256");
    } catch (NoSuchAlgorithmException ex) {
      throw new UnsupportedOperationException("JVM doesn't support sha256.", ex);
    }
  }

  @Override
  public byte[] hash(byte[] data) {
    return sha256.digest(data);
  }

}
