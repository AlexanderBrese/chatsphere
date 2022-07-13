package de.chatsphere.api.user.model;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.UpdateBuilder;
import de.chatsphere.api.account.model.AccountRepository;
import de.chatsphere.api.shared.model.Reportable;
import de.chatsphere.api.user.transfer.ReportUserInput;
import de.chatsphere.api.user.transfer.UpdateAvatarInput;
import de.chatsphere.api.user.transfer.UpdateDisplayNameInput;
import de.chatsphere.api.user.transfer.UpdatePhoneInput;
import de.chatsphere.api.user.transfer.UpdateStatusInput;
import de.chatsphere.api.user.transfer.UserDto;
import de.chatsphere.feature.file.storage.FileStorage;
import de.chatsphere.io.database.Database;
import de.chatsphere.io.database.schema.Profile;
import de.chatsphere.io.database.schema.Report;
import de.chatsphere.io.database.schema.chat.Message;
import de.chatsphere.io.database.schema.preference.File;
import de.chatsphere.io.database.schema.preference.Phone;
import de.chatsphere.io.database.schema.preference.enumeration.Visibility;
import de.chatsphere.io.database.schema.user.User;
import de.chatsphere.server.graphql.Context;
import graphql.GraphQL;
import graphql.GraphQLException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserRepository implements Reportable {

  private static final Logger log = LoggerFactory.getLogger(UserRepository.class);

  /**
   * Does a wildcard search on users for the provided username. Omits the currently authenticated
   * user.
   *
   * @param context  the GraphQL context
   * @param username the username to search for
   *
   * @return the list of users
   */
  public static List<UserDto> searchUsers(Context context, String username) {
    final Database database = context.getDatabase();
    Dao<Profile, Integer> profileDao = database.getDao(Profile.class);
    List<UserDto> users = new LinkedList<>();

    try {
      List<Profile> profiles = profileDao.queryBuilder()
        .where()
        .like("name", username + "%")
        .query();

      final String currentUsername = context.getAuthenticator().getUsername();
      profiles.forEach(profile -> {
        if (!profile.getName().equals(currentUsername)) {
          User user = AccountRepository.getUser(context, profile.getId());
          if (user != null) {
            users.add(UserDto.from(user));
          }
        }
      });
    } catch (SQLException e) {
      log.error(e.getMessage());
      throw new GraphQLException(INTERNAL_ERROR);
    }

    return users;
  }

  /**
   * Return user by username
   *
   * @param context object
   * @param username of the user
   * @return graphql user object
   */
  public static UserDto user(Context context, String username) {
    User user;
    try {
      user = AccountRepository.getUser(context, username);
    } catch (Exception e) {
      logger.error("Could not find user to username: " + username);
      return null;
    }
    return UserDto.from(user);
  }

  /**
   * Updates avatar picture.
   *
   * @param context to gain read/write access to the database
   * @param input   contains the encoded base64 image
   *
   * @return updated user
   */
  public static String updateAvatar(Context context, UpdateAvatarInput input) {

    // Get user from name
    Profile profile = getProfileByName(context, context.getAuthenticator().getUsername());

    // TODO: Check that uploaded image is *actually* an image
    // TODO: Tika gives text/plain for image/png

    FileStorage storage = Context.getFileStorage();

    // Delete old file
    if (profile.getIcon() != null) {
      File file = profile.getIcon();
      try {
        context.getDatabase().getDao(File.class).delete(file);
      } catch (SQLException e) {
        logger.error(e.getMessage());
        throw new GraphQLException(INTERNAL_ERROR);
      }
      // TODO: Delete in file storage
    }

    byte[] hash = context.getMessageDigest()
      .digest(input.getAvatar().getBytes(StandardCharsets.UTF_8));

    // Create new file
    File newAvatar = File.builder()
      .name(input.getFileName())
      .size(input.getAvatar().getBytes().length)
      .type("image/png") // TODO: Tika cannot detect
      .hash(hash)
      .build();

    storage.createFile(newAvatar, input.getAvatar().getBytes());
    try {
      context.getDatabase().getDao(File.class).create(newAvatar);
    } catch (SQLException e) {
      logger.error(e.getMessage());
      throw new GraphQLException(INTERNAL_ERROR);
    }

    // Update profile reference to new file
    UpdateBuilder<Profile, ?> updateProfile = null;
    try {
      updateProfile = context.getDatabase().getDao(Profile.class).updateBuilder();
      updateProfile.updateColumnValue("icon", newAvatar.getId());
      updateProfile.where().eq("name", profile.getName());
      updateProfile.update();
    } catch (SQLException e) {
      logger.error(e.getMessage());
      throw new GraphQLException(INTERNAL_ERROR);
    }

    return getUpdatedUser(context).getAvatarUrl();
  }

  /**
   * Updates phone number.
   *
   * @param context to gain read/write access to the database
   * @param input   contains the new phone number
   *
   * @return updated user
   */
  public static Boolean updatePhone(Context context, UpdatePhoneInput input) {

    // Get user from name
    User user = AccountRepository.getUser(context, context.getAuthenticator().getUsername());
    Dao<Phone, Integer> phoneDao = context.getDatabase().getDao(Phone.class);
    Dao<Visibility, Integer> visibilityDao = context.getDatabase().getDao(Visibility.class);

    Visibility visibility;
    try {
      List<Visibility> visibilities =
        visibilityDao.queryForEq("description", Visibility.Level.PUBLIC);
      if (!visibilities.isEmpty()) {
        visibility = visibilities.get(0);
      } else {
        visibility = Visibility.builder().description(Visibility.Level.PUBLIC).build();
        visibilityDao.create(visibility);
      }
    } catch (SQLException e) {
      logger.error(e.getMessage());
      throw new GraphQLException(INTERNAL_ERROR);
    }

    // Check if phone number exists
    Phone phone;
    try {
      if (user.getPhone() == null) {
        phone = Phone.builder()
          .number(input.getPhone())
          .hash(
            context.getMessageDigest().digest(input.getPhone().getBytes(StandardCharsets.UTF_8)))
          .visibility(visibility)
          .build();
        phoneDao.create(phone);
      } else {
        List<Phone> phoneNumbers = phoneDao.queryForEq("id", user.getPhone().getId());
        phone = phoneNumbers.get(0);
        UpdateBuilder<Phone, ?> updatePhone = context.getDatabase().getDao(Phone.class)
          .updateBuilder();
        updatePhone.updateColumnValue("number", input.getPhone());
        updatePhone.where().eq("id", user.getPhone().getId());
        updatePhone.update();
      }
    } catch (SQLException e) {
      logger.error(e.getMessage());
      throw new GraphQLException(INTERNAL_ERROR);
    }

    // Update user
    try {
      UpdateBuilder<User, ?> updateUser = context.getDatabase().getDao(User.class).updateBuilder();
      updateUser.updateColumnValue("phone", phone.getId());
      updateUser.where().eq("profile", user.getProfile().getId());
      updateUser.update();
    } catch (SQLException e) {
      logger.error(e.getMessage());
      throw new GraphQLException(INTERNAL_ERROR);
    }

    return true;
  }

  /**
   * Updates the name displayed to other users.
   *
   * @param context to gain read/write access to the database
   * @param input   contains the new display name
   *
   * @return updated user
   */
  public static Boolean updateDisplayName(Context context, UpdateDisplayNameInput input) {

    // Get user from name
    User user = AccountRepository.getUser(context, context.getAuthenticator().getUsername());

    // Update display name
    UpdateBuilder<Profile, ?> updateProfile = null;
    try {
      updateProfile = context.getDatabase().getDao(Profile.class).updateBuilder();
      updateProfile.updateColumnValue("displayName", input.getDisplayName());
      updateProfile.where().eq("id", user.getProfile().getId());
      updateProfile.update();
    } catch (SQLException e) {
      logger.error(e.getMessage());
      throw new GraphQLException(INTERNAL_ERROR);
    }

    return true;

  }

  /**
   * Updates the status message.
   *
   * @param context to gain read/write access to the database
   * @param input   contains the new status message
   *
   * @return updated user
   */
  public static Boolean updateStatus(Context context, UpdateStatusInput input) {

    // Get user from name
    User user = AccountRepository.getUser(context, context.getAuthenticator().getUsername());
    Dao<Message, Integer> messageDao = context.getDatabase().getDao(Message.class);

    // Check if status already exists
    Message status;
    try {
      if (user.getStatus() == null) {
        status = Message.builder()
          .markup(false)
          .content(input.getStatus())
          .build();
        messageDao.create(status);
      } else {
        List<Message> messages = messageDao.queryForEq("id", user.getStatus().getId());
        status = messages.get(0);
        UpdateBuilder<Message, ?> updateMessage = context.getDatabase().getDao(Message.class)
          .updateBuilder();
        updateMessage.updateColumnValue("content", input.getStatus());
        updateMessage.where().eq("id", user.getStatus().getId());
        updateMessage.update();
      }
    } catch (SQLException e) {
      logger.error(e.getMessage());
      throw new GraphQLException(INTERNAL_ERROR);
    }

    // Update user
    try {
      UpdateBuilder<User, ?> updateUser = context.getDatabase().getDao(User.class).updateBuilder();
      updateUser.updateColumnValue("status", status.getId());
      updateUser.where().eq("id", user.getId());
      updateUser.update();
    } catch (SQLException e) {
      logger.error(e.getMessage());
      throw new GraphQLException(INTERNAL_ERROR);
    }

    return true;
  }

  /**
   * Reports an account by appending him to the list of reported users.
   *
   * @param context to gain read/write access to the database
   * @param input   contains the reported username and reason for reporting
   *
   * @return true if report has been received and saved
   */
  public static Boolean reportUser(Context context, ReportUserInput input) {
    // Get reporter
    User reporter = AccountRepository.getCurrentUser(context);
    // Get reported user
    User reported = AccountRepository.getUser(context, input.getReportedUsername());

    // Create report message
    Message message = Message.builder()
      .content(input.getReason())
      .markup(false)
      .build();

    // Create report entry
    Report entry = Report.builder()
      .reporter(reporter)
      .reported(reported)
      .reason(message)
      .build();

    try {
      context.getDatabase().getDao(Message.class).create(message);
      context.getDatabase().getDao(Report.class).create(entry);
      return true;
    } catch (SQLException e) {
      logger.error(e.getMessage());
      throw new GraphQLException(INTERNAL_ERROR);
    }
  }

  /**
   * Gets an ORM profile by name.
   *
   * @param context the context used to get database connection
   * @param name    the name used to get the profile
   *
   * @return the ORM profile or null if username wasn't found
   */
  private static Profile getProfileByName(Context context, String name) {
    Database database = context.getDatabase();
    Dao<Profile, Integer> profileDao = database.getDao(Profile.class);

    try {
      List<Profile> profiles = profileDao.queryForEq("name", name);
      if (profiles.isEmpty()) {
        return null;
      } else {
        return profiles.get(0);
      }

    } catch (SQLException e) {
      logger.error(e.getMessage());
      throw new GraphQLException(INTERNAL_ERROR);
    }
  }

  private static UserDto getUpdatedUser(Context context) {
    return UserDto
      .from(AccountRepository.getUser(context, context.getAuthenticator().getUsername()));
  }
}
