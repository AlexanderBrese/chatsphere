package de.chatsphere.feature.file;

import de.chatsphere.feature.file.hash.HashingAlgorithm;
import de.chatsphere.feature.file.storage.FileStorage;
import de.chatsphere.io.database.schema.preference.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.text.MessageFormat;
import org.apache.commons.codec.binary.Hex;

/**
 * This FileStorage implementation uses the operating systems file system for storing files
 * permanently.
 */
public class FileSystemStorage implements FileStorage {

  private final MessageFormat downloadUri;
  private final java.io.File workingDirectory;
  private final HashingAlgorithm hashingAlgorithm;

  /**
   * Create a FileStorage with given parameters.
   *
   * @param downloadUri      an formatter that takes the file location and creates an uri from it
   * @param workingDirectory the directory to store the files to
   * @param algorithm        the algorithm to use for creating hashes
   *
   * @see #createFromConfig(de.chatsphere.feature.file.FileStorageConfig)
   */
  public FileSystemStorage(
    MessageFormat downloadUri,
    java.io.File workingDirectory, HashingAlgorithm algorithm
  ) {
    this.downloadUri = downloadUri;
    this.workingDirectory = workingDirectory;
    this.hashingAlgorithm = algorithm;
  }

  /**
   * Parse given configuration and create a new FileSystemStorage using its content. The given
   * config has to be valid.
   *
   * @param config the configuration to use
   *
   * @return the newly created FileSystemStorage
   * @throws IllegalArgumentException if the configuration is invalid
   */
  public static FileSystemStorage createFromConfig(FileStorageConfig config) {
    MessageFormat dlUrl = new MessageFormat(config.getDownloadUrl());
    java.io.File workDir = getAndCheckWorkingDirectory(config);
    HashingAlgorithm hashAlgo = getHashingAlgorythmByName(config.getHashingAlgorithm());
    return new FileSystemStorage(dlUrl, workDir, hashAlgo);
  }

  private static java.io.File getAndCheckWorkingDirectory(FileStorageConfig config)
    throws IllegalArgumentException {
    java.io.File workDir = new java.io.File(config.getWorkingDirectory());
    workDir.mkdirs();
    if (!(workDir.isDirectory() && (workDir.canWrite() || workDir.setWritable(true)))) {
      throw new IllegalArgumentException(
        "The configuration is not valid: Could not access given working directory."
      );
    }
    return workDir;
  }

  private static HashingAlgorithm getHashingAlgorythmByName(String className)
    throws IllegalArgumentException, SecurityException {
    try {
      Class<?> clazz = Class.forName(className);
      Class<? extends HashingAlgorithm> algorithmClass = clazz.asSubclass(HashingAlgorithm.class);
      return algorithmClass.getConstructor().newInstance();
    } catch (ReflectiveOperationException | ClassCastException ex) {
      throw new IllegalArgumentException(
        "The configuration is not valid: Could not load given hashing algorithm.", ex
      );
    }
  }

  @Override
  public String getDownloadUri(File file) {
    return downloadUri.format(new String[]{file.getType(), Hex.encodeHexString(file.getHash())});
  }

  @Override
  public void createFile(File file, byte[] data) {
    byte[] hash = hashingAlgorithm.hash(data);
    file.setSize(data.length);
    file.setHash(hash);

    String internalFileName = Hex.encodeHexString(hash);
    java.io.File fileSystemFile = new java.io.File(workingDirectory, internalFileName);

    try (FileOutputStream fileOutStream = new FileOutputStream(fileSystemFile)) {
      fileOutStream.write(data);
    } catch (IOException ex) {
      throw new UncheckedIOException(ex);
    }
  }
}
