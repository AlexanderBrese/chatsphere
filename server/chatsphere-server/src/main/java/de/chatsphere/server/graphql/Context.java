package de.chatsphere.server.graphql;

import de.chatsphere.feature.auth.AuthService;
import de.chatsphere.feature.config.Config;
import de.chatsphere.feature.file.FileStorageConfig;
import de.chatsphere.feature.file.FileSystemStorage;
import de.chatsphere.feature.file.storage.Base64DecoderFileStorageDecorator;
import de.chatsphere.feature.file.storage.FileStorage;
import de.chatsphere.feature.file.storage.MimeTypeDetectorFileStorageDecorator;
import de.chatsphere.feature.password.Hasher;
import de.chatsphere.feature.password.HasherConfig;
import de.chatsphere.io.database.Database;
import de.chatsphere.io.database.DatabaseConfig;
import graphql.servlet.GraphQLContext;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.HandshakeRequest;

/**
 * The GraphQL context is used to provide shared functionality among resolvers.
 */
public class Context extends GraphQLContext {

  private final Database database;
  private static final Hasher hasher = initHasher();
  private static final FileStorage fileStorage = initFileStorage();
  private static final MessageDigest messageDigest = initMessageDigest();
  private final AuthService authenticator = new AuthService();

  /**
   * Initializes a new context.
   */
  public Context(Database database) {
    super();
    this.database = database;
  }

  /**
   * Initializes a new context.
   */
  public Context(Database database, HttpServletRequest httpServletRequest) {
    super(httpServletRequest);
    this.database = database;
  }

  /**
   * Initializes a new context.
   */
  public Context(Database database, HandshakeRequest handshakeRequest) {
    super(handshakeRequest);
    this.database = database;
  }

  private static MessageDigest initMessageDigest() {
    try {
      return MessageDigest.getInstance("SHA-256");
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  private static Hasher initHasher() {
    return new Hasher(Config.getConfig(HasherConfig.class, "password"));
  }

  /**
   * Initialize the FileStorage.
   *
   * @return the filestorage
   */
  private static FileStorage initFileStorage() {
    return new Base64DecoderFileStorageDecorator(
      new MimeTypeDetectorFileStorageDecorator(
        FileSystemStorage
      .createFromConfig(Config.getConfig(FileStorageConfig.class, "webstorage"))
      )
    );
  }

  public static FileStorage getFileStorage() {
    return fileStorage;
  }

  /**
   * Gets the database instance.
   *
   * @return the database
   */
  public Database getDatabase() {
    return database;
  }

  public AuthService getAuthenticator() {
    return authenticator;
  }

  public Hasher getHasher() {
    return hasher;
  }

  public MessageDigest getMessageDigest() {
    return messageDigest;
  }
}
