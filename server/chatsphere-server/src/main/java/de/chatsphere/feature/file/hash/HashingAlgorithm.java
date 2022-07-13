package de.chatsphere.feature.file.hash;

/**
 * The hashing algorithm to use. This Interface is part of the strategy pattern.
 */
public interface HashingAlgorithm {

  /**
   * Method to create a hash from binary data. As hashes are represented by hex-strings, they can be
   * converted to raw bytes and stored inside of an array.
   *
   * @param data the data to create a hash from
   *
   * @return the hash as byte array
   */
  byte[] hash(byte[] data);

}
