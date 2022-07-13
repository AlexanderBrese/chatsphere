package de.chatsphere.api.account.model;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.UpdateBuilder;
import de.chatsphere.api.account.transfer.AccountDto;
import de.chatsphere.api.account.transfer.AuthenticationPayload;
import de.chatsphere.api.account.transfer.CreateAccountInput;
import de.chatsphere.api.account.transfer.LoginInput;
import de.chatsphere.api.account.transfer.UpdateEmailInput;
import de.chatsphere.api.account.transfer.UpdateNotificationInput;
import de.chatsphere.api.account.transfer.UpdatePasswordInput;
import de.chatsphere.api.chat.model.ChatRepository;
import de.chatsphere.api.chat.transfer.AbstractChat;
import de.chatsphere.api.shared.model.Reportable;
import de.chatsphere.io.database.Database;
import de.chatsphere.io.database.schema.Profile;
import de.chatsphere.io.database.schema.chat.ChatParticipant;
import de.chatsphere.io.database.schema.preference.Email;
import de.chatsphere.io.database.schema.preference.Language;
import de.chatsphere.io.database.schema.preference.Location;
import de.chatsphere.io.database.schema.preference.Password;
import de.chatsphere.io.database.schema.preference.enumeration.Notifiable;
import de.chatsphere.io.database.schema.preference.enumeration.Visibility;
import de.chatsphere.io.database.schema.preference.enumeration.Visibility.Level;
import de.chatsphere.io.database.schema.user.User;
import de.chatsphere.io.database.schema.user.UserPreferences;
import de.chatsphere.io.database.schema.user.UserRelationship;
import de.chatsphere.server.graphql.Context;
import graphql.GraphQLException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * The account repository handles database communication for account resolvers.
 */
public final class AccountRepository implements Reportable {

  /**
   * Retrieves the current authenticated account.
   *
   * @param context the GraphQL context
   * @return the authenticated account
   */
  public static AccountDto account(Context context) {
    final Database database = context.getDatabase();
    User currentUser = getCurrentUser(context);

    Dao<UserRelationship, Integer> contactDao = database.getDao(UserRelationship.class);
    Dao<ChatParticipant, Integer> chatParticipantDao = database.getDao(ChatParticipant.class);
    List<AbstractChat> chats = new LinkedList<>();
    List<UserRelationship> contacts;
    try {
      contacts = contactDao.queryForEq("owner", currentUser);
      List<ChatParticipant> chatParticipants =
        chatParticipantDao.queryForEq("participant", currentUser);
      chatParticipants.forEach(chatParticipant -> {
        AbstractChat chat = ChatRepository.chat(context, chatParticipant.getChat().getId());
        if (chat != null) {
          chats.add(chat);
        }
      });
    } catch (SQLException e) {
      logger.error(e.getMessage());
      throw new GraphQLException(INTERNAL_ERROR);
    }
    Dao<UserPreferences, Integer> userPreferencesDao = database.getDao(UserPreferences.class);
    Notifiable notifiable;

    try {
      notifiable = userPreferencesDao.queryForEq("user", currentUser)
        .get(0)
        .getNotification();
    } catch (SQLException e) {
      logger.error(e.getMessage());
      throw new GraphQLException(INTERNAL_ERROR);
    }

    return AccountDto.from(context, currentUser, notifiable, contacts, chats);
  }

  /**
   * Creates a new account by storing the account in the database and additionally authenticates the
   * newly created account.
   *
   * @param context the GraphQL context
   * @param credentials the credentials containing username, email and password
   * @return authentication payload that contains the JWS
   * @throws GraphQLException internal error | email is already in use
   */
  public static AuthenticationPayload createAccount(Context context, CreateAccountInput credentials)
    throws GraphQLException {
    if (AccountRepository.isEmailTaken(context, credentials.getEmail())) {
      throw new GraphQLException("Email is already in use");
    }

    storeAccount(context, credentials);

    return authenticate(context, credentials.getUsername());
  }

  /**
   * Does a login by retrieving the user from the database with the provided credentials and
   * additionally authenticates the retrieved user.
   *
   * @param context the GraphQl context
   * @param credentials the credentials to retrieve the user
   * @return authentication payload that contains the JWS
   * @throws GraphQLException internal error | wrong username | wrong password
   */
  public static AuthenticationPayload login(Context context, LoginInput credentials)
    throws GraphQLException {
    final String username = credentials.getUsername();

    Profile profile = getProfile(context, username);
    if (profile == null) {
      throw new GraphQLException("Wrong username entered.");
    }

    User user = getUser(context, profile.getId());
    String hash = user.getAuthentication().getHash();
    if (!isValidPassword(context, hash, credentials.getPassword())) {
      throw new GraphQLException("Wrong password entered.");
    }
    return authenticate(context, username);
  }

  /**
   * Checks if a username is already taken.
   *
   * @param context the GraphQL context
   * @param username the username to check
   * @return true/false
   */
  public static boolean usernameTaken(Context context, String username) {
    return getProfile(context, username) != null;
  }

  /**
   * Gets the currently authenticated user database object.
   *
   * @param context the GraphQL context
   * @return the user database object
   */
  public static User getCurrentUser(Context context) {
    return getUser(context, context.getAuthenticator().getUsername());
  }

  /**
   * <p>Changes the password of an existing account.</p>
   *
   * <p>TODO: Check if password strong enough</p>
   *
   * @param context to gain read/write access to the database
   * @param input contains new password
   * @return newly updated account
   */
  public static Boolean updatePassword(Context context, UpdatePasswordInput input) {

    String hash = context.getHasher().hash(input.getPassword());

    // Get user from name
    Profile profile = getProfile(context, context.getAuthenticator().getUsername());
    User user = getUser(context, profile.getId());

    // Update
    try {
      UpdateBuilder<Password, ?> updatePassword = context.getDatabase().getDao(Password.class)
        .updateBuilder();
      updatePassword.updateColumnValue("hash", hash);
      updatePassword.where().eq("id", user.getAuthentication().getId());
      updatePassword.update();
    } catch (SQLException e) {
      logger.error(e.getMessage());
      throw new GraphQLException(INTERNAL_ERROR);
    }

    return true;
  }

  /**
   * Updates E-mail of an existing account. Sets verified to zero.
   *
   * @param context to gain read/write access to the database
   * @param input contains new email address
   * @return newly updated account
   */
  public static Boolean updateEmail(Context context, UpdateEmailInput input) {

    byte[] hash = context.getMessageDigest()
      .digest(input.getEmail().toLowerCase().getBytes(StandardCharsets.UTF_8));

    // Get user from name
    Profile profile = getProfile(context, context.getAuthenticator().getUsername());
    User user = getUser(context, profile.getId());

    // Update
    try {
      UpdateBuilder<Email, ?> updateEmail = context.getDatabase().getDao(Email.class)
        .updateBuilder();
      updateEmail.updateColumnValue("mail", input.getEmail());
      updateEmail.updateColumnValue("hash", hash);
      updateEmail.updateColumnValue("verified", false);
      updateEmail.where().eq("id", user.getEmail().getId());
      updateEmail.update();
    } catch (SQLException e) {
      logger.error(e.getMessage());
      throw new GraphQLException(INTERNAL_ERROR);
    }

    return true;
  }

  /**
   * Updates global notification settings.
   *
   * @param context to gain read/write access to the database
   * @param input contains the new notification level
   * @return updated user
   */
  public static Boolean updateNotificationSettings(Context context, UpdateNotificationInput input) {

    // Get user from name
    Profile profile = getProfile(context, context.getAuthenticator().getUsername());
    User user = getUser(context, profile.getId());

    Dao<Notifiable, Integer> notifiableDao = context.getDatabase().getDao(Notifiable.class);
    ;

    // Query appropriate ENUM entry in Notificationlevel table
    Notifiable notify;
    try {
      List<Notifiable> notifies =
        notifiableDao.queryForEq("description", input.getNotify().getPush().toLevel());
      if (!notifies.isEmpty()) {
        notify = notifies.get(0);
      } else {
        throw new GraphQLException(INTERNAL_ERROR);
      }
    } catch (SQLException e) {
      logger.error(e.getMessage());
      throw new GraphQLException(INTERNAL_ERROR);
    }

    // Update
    try {
      UpdateBuilder<UserPreferences, ?> updateNotify = context.getDatabase()
        .getDao(UserPreferences.class).updateBuilder();
      updateNotify.updateColumnValue("notification", notify);
      updateNotify.where().eq("user", user.getId());
      updateNotify.update();
    } catch (SQLException e) {
      logger.error(e.getMessage());
      throw new GraphQLException(INTERNAL_ERROR);
    }

    return true;

  }

  /**
   * Checks if an email is already taken.
   *
   * @param context the GraphQL context
   * @param email the email to check
   * @return true/false
   * @throws GraphQLException internal error
   */
  private static boolean isEmailTaken(Context context, String email) {
    Dao<Email, Integer> emailDao = context.getDatabase().getDao(Email.class);

    byte[] emailHash = getEmailHash(context, email);
    List<Email> emails;
    try {
      emails = emailDao.queryForEq("hash", emailHash);
    } catch (SQLException e) {
      logger.error(e.getMessage());
      throw new GraphQLException(INTERNAL_ERROR);
    }
    return !emails.isEmpty();
  }

  /**
   * Checks if a password is valid.
   *
   * @param context the GraphQL context
   * @param hash the hashed password
   * @param password the password to verify
   * @return true/false
   */
  private static boolean isValidPassword(Context context, String hash, String password) {
    return context.getHasher().verify(hash, password);
  }

  /**
   * Gets an ORM profile by name.
   *
   * @param context the context used to get database connection
   * @param username the name used to get the profile
   * @return the ORM profile or null if username wasn't found
   * @throws GraphQLException internal error
   */
  private static Profile getProfile(Context context, String username) {
    final Database database = context.getDatabase();
    Dao<Profile, Integer> profileDao = database.getDao(Profile.class);

    List<Profile> profiles;
    try {
      profiles = profileDao.queryForEq("name", username);
    } catch (SQLException e) {
      logger.error("Profile with name " + username + " could not be found.\n" + e.getMessage());
      throw new GraphQLException(INTERNAL_ERROR);
    }
    if (profiles.isEmpty()) {
      return null;
    }
    return profiles.get(0);
  }

  /**
   * Gets a user database object by profile id.
   *
   * @param context the GraphQL context
   * @param profileId the profile id
   * @return the user database object
   * @throws GraphQLException internal error
   */
  public static User getUser(Context context, Integer profileId) {
    final Database database = context.getDatabase();
    Dao<User, Integer> userDao = database.getDao(User.class);
    try {
      List<User> userList = userDao.queryForEq("profile", profileId);
      return userList.size() != 0 ? userList.get(0) : null;
    } catch (SQLException e) {
      logger.error("User with profileId" + profileId + "could not be found.\n" + e.getMessage());
      throw new GraphQLException(INTERNAL_ERROR);
    }
  }

  /**
   * Gets a user database object for the provided username.
   *
   * @param context the GraphQL context
   * @param username the username
   * @return the user database object
   */
  public static User getUser(Context context, String username) {
    Profile profile = getProfile(context, username);
    return getUser(context, profile.getId());
  }

  public static UserPreferences getPreferences(Context context, User user) {
    final Database database = context.getDatabase();
    Dao<UserPreferences, Integer> preferenceDao = database.getDao(UserPreferences.class);

    List<UserPreferences> preferences;
    try {
      preferences = preferenceDao.queryForEq("user", user.getId());
    } catch (SQLException e) {
      logger.error("Preferences of id " + user.getId() + " could not be found.\n" + e.getMessage());
      throw new GraphQLException(INTERNAL_ERROR);
    }
    if (preferences.isEmpty()) {
      return null;
    }
    return preferences.get(0);
  }

  /**
   * Gets the hash of an email as specified for database usage.
   *
   * @param context the GraphQL context
   * @param email the email to hash
   * @return the email hash
   */
  private static byte[] getEmailHash(Context context, String email) {
    return context.getMessageDigest()
      .digest(email.toLowerCase().getBytes(StandardCharsets.UTF_8));
  }

  /**
   * Builds an email database object by providing hashed email and an inheritance visibility.
   *
   * @param context the GraphQL context
   * @param email the email
   * @return the email database object
   * @throws GraphQLException internal error
   */
  private static Email buildEmail(Context context, String email) {
    Dao<Visibility, Integer> visibilityDao = context.getDatabase().getDao(Visibility.class);
    Visibility visibility;
    try {
      visibility = visibilityDao.queryForEq("description", Level.INHERIT).get(0);
    } catch (SQLException e) {
      logger.error(e.getMessage());
      throw new GraphQLException(INTERNAL_ERROR);
    }
    byte[] emailHash = getEmailHash(context, email);

    return Email.builder()
      .mail(email)
      .hash(emailHash)
      .verified(false)
      .visibility(visibility)
      .build();
  }

  /**
   * Builds a password database object by providing a hashed password.
   *
   * @param context the GraphQL context
   * @param password the password
   * @return the password database object
   */
  private static Password buildPassword(Context context, String password) {
    String hash = context.getHasher().hash(password);
    return Password.builder()
      .hash(hash)
      .build();
  }

  /**
   * Builds a profile database object by providing the same username both for name and display
   * name.
   *
   * @param username the username
   * @return the profile database object
   */
  private static Profile buildProfile(String username) {
    return Profile.builder()
      .name(username)
      .displayName(username)
      .build();
  }

  private static Language buildLanguage(Context context) {
    Dao<Language, Integer> languageDao = context.getDatabase().getDao(Language.class);
    Language language;
    try {
      List<Language> languages =
        languageDao.queryForEq("code", "en");
      if (!languages.isEmpty()) {
        language = languages.get(0);
      } else {
        language = Language.builder().code("en").build();
        languageDao.create(language);
      }
    } catch (SQLException e) {
      logger.error(e.getMessage());
      throw new GraphQLException(INTERNAL_ERROR);
    }
    return language;
  }

  private static Location buildLocation(Context context) {
    Dao<Location, Integer> locationDao = context.getDatabase().getDao(Location.class);
    Location location;
    try {
      List<Location> locations =
        locationDao.queryForEq("country", "--");
      if (!locations.isEmpty()) {
        location = locations.get(0);
      } else {
        location = Location.builder().country("--").province("--").build();
        locationDao.create(location);
      }
    } catch (SQLException e) {
      logger.error(e.getMessage());
      throw new GraphQLException(INTERNAL_ERROR);
    }

    return location;
  }

  private static Notifiable buildNotifiable(Context context) {
    Dao<Notifiable, Integer> notifiableDao = context.getDatabase().getDao(Notifiable.class);
    Notifiable notify;
    try {
      List<Notifiable> notifies =
        notifiableDao.queryForEq("description", Notifiable.Level.NOTIFY);
      if (!notifies.isEmpty()) {
        notify = notifies.get(0);
      } else {
        notify = Notifiable.builder().description(Notifiable.Level.NOTIFY).build();
        notifiableDao.create(notify);
      }
    } catch (SQLException e) {
      logger.error(e.getMessage());
      throw new GraphQLException(INTERNAL_ERROR);
    }

    return notify;
  }

  private static void storeUserPreferences(Context context, User user) {
    Language language = buildLanguage(context);
    Location location = buildLocation(context);
    Notifiable notify = buildNotifiable(context);

    Dao<UserPreferences, Integer> preferenceDao =
      context.getDatabase().getDao(UserPreferences.class);
    UserPreferences preferences = UserPreferences.builder()
      .user(user)
      .language(language)
      .location(location)
      .notification(notify)
      .linkPreview(false)
      .retention(365)
      .build();

    try {
      preferenceDao.create(preferences);
    } catch (SQLException e) {
      logger.error(e.getMessage());
      throw new GraphQLException(INTERNAL_ERROR);
    }
  }

  /**
   * Stores an account with the credentials provided by building a new user database object and
   * persisting it.
   *
   * @param context the GraphQL context
   * @param credentials the credentials used to build the user database object
   * @throws GraphQLException internal error
   */
  private static void storeAccount(Context context, CreateAccountInput credentials) {
    final Database database = context.getDatabase();
    Dao<User, Integer> userDao = database.getDao(User.class);

    User newUser = User.builder()
      .authentication(buildPassword(context, credentials.getPassword()))
      .email(buildEmail(context, credentials.getEmail()))
      .profile(buildProfile(credentials.getUsername()))
      .build();

    try {
      userDao.create(newUser);
    } catch (SQLException e) {
      logger.error(e.getMessage());
      throw new GraphQLException(INTERNAL_ERROR);
    }

    // default preferences
    storeUserPreferences(context, newUser);
  }

  /**
   * Authenticates with the username provided and gives back a JWS.
   *
   * @param context the GraphQL context
   * @param username the username used to authenticate
   * @return the JWS
   */
  private static AuthenticationPayload authenticate(Context context, String username) {
    context.getAuthenticator().setUsername(username);
    String jws = context.getAuthenticator().encrypt(username);
    return new AuthenticationPayload(jws);
  }
}
