package de.chatsphere.feature.password;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import de.mkammerer.argon2.Argon2Helper;

/**
 * A Hashing Helper class using <a href="https://en.wikipedia.org/wiki/Argon2">Argon2</a> Key
 * Derivation Function.
 *
 * <p>Encapsulates the utilization and initialization of a specific key derivation function
 */
public class Hasher {

  private final HasherConfig config;
  private final Argon2 argon2 = Argon2Factory.create();
  private final int iterations;

  /**
   * Calculates the optimal iteration number (calculation-intensive) and creates a Hasher object.
   *
   * @deprecated The hasher should now be initialized with a configuration object that defines the
   * internal parameters.
   */
  @Deprecated
  public Hasher() {
    this(new HasherConfig());
  }

  /**
   * Calculates the optimal iteration number (calculation-intensive) and creates a Hasher object.
   */
  public Hasher(HasherConfig config) {
    this.config = config;
    int iterations = Argon2Helper.findIterations(
      argon2,
      config.getDuration(),
      config.getMemory(),
      config.getParallelism());
    this.iterations = iterations > 0 ? iterations : 1;
  }

  /**
   * Hashes a password using UTF-8 encoding.
   *
   * @param password Password
   *
   * @return Hashed password
   * @see de.mkammerer.argon2.Argon2#hash(int, int, int, String)
   */
  public String hash(String password) {
    return argon2
      .hash(iterations, config.getMemory(), config.getParallelism(), preparePassword(password));
  }

  /**
   * Verify a password against a hash using UTF-8 encoding.
   *
   * @param hash     Hash
   * @param password Password
   *
   * @return True if the password matches the hash, false otherwise
   * @see de.mkammerer.argon2.Argon2#verify(String, String)
   */
  public boolean verify(String hash, String password) {
    return argon2.verify(hash, preparePassword(password));
  }

  private String preparePassword(String password) {
    return password + config.getPepper();
  }
}
