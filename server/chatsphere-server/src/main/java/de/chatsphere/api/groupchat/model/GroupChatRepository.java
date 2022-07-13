package de.chatsphere.api.groupchat.model;

import com.j256.ormlite.dao.Dao;
import de.chatsphere.api.account.model.AccountRepository;
import de.chatsphere.api.chat.model.ChatRepository;
import de.chatsphere.api.chatstamp.transfer.ChatStampDto;
import de.chatsphere.api.groupchat.transfer.AddParticipantInput;
import de.chatsphere.api.groupchat.transfer.CreateGroupChatInput;
import de.chatsphere.api.groupchat.transfer.GrantPrivilegesInput;
import de.chatsphere.api.groupchat.transfer.GroupChatDto;
import de.chatsphere.api.groupchat.transfer.RemoveParticipantInput;
import de.chatsphere.api.groupchat.transfer.RevokePrivilegesInput;
import de.chatsphere.api.groupchat.transfer.UpdateNameInput;
import de.chatsphere.api.groupchat.transfer.UpdatePictureInput;
import de.chatsphere.api.shared.model.Reportable;
import de.chatsphere.api.shared.transfer.NotificationDto;
import de.chatsphere.api.user.transfer.ParticipantDto;
import de.chatsphere.api.user.transfer.UserDto;
import de.chatsphere.io.database.Database;
import de.chatsphere.io.database.schema.Profile;
import de.chatsphere.io.database.schema.chat.Chat;
import de.chatsphere.io.database.schema.chat.ChatParticipant;
import de.chatsphere.io.database.schema.chat.Message;
import de.chatsphere.io.database.schema.preference.File;
import de.chatsphere.io.database.schema.preference.enumeration.Notifiable;
import de.chatsphere.io.database.schema.preference.enumeration.Notifiable.Level;
import de.chatsphere.io.database.schema.user.User;
import de.chatsphere.server.graphql.Context;
import graphql.GraphQLException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Handle access to database. All the groupchat-specific data needs and queries are resolved here.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GroupChatRepository implements Reportable {

  /**
   * Creates a new groupchat in the database and adds the creator to the table.
   *
   * @param context {@link Context}
   * @return TODO: Add public check
   */
  public static GroupChatDto createGroupChat(
    Context context, CreateGroupChatInput createGroupChatInput) {

    Dao<Profile, Integer> profileDao = context.getDatabase().getDao(Profile.class);
    try {
      List<Profile> result =
        profileDao.queryForEq("name", createGroupChatInput.getDisplayName());
      if (result.size() > 0) {
        throw new GraphQLException("Groupchat name already exists!");
      }
    } catch (SQLException e) {
      logger.error(e.getMessage());
      throw new GraphQLException(INTERNAL_ERROR);
    }

    Dao<Notifiable, Integer> notificationDao = context.getDatabase().getDao(Notifiable.class);
    Notifiable notifiable;
    try {
      notifiable = notificationDao.queryForEq("description", Level.INHERIT).get(0);
    } catch (SQLException e) {
      logger.error(e.getMessage());
      throw new GraphQLException(INTERNAL_ERROR);
    }

    String icon = createGroupChatInput.getIcon();
    File newIcon = null;
    if (!icon.isEmpty()) {
      byte[] hash = context.getMessageDigest()
        .digest(icon.getBytes(StandardCharsets.UTF_8));

      // Create new file
      newIcon = File.builder()
        .name(createGroupChatInput.getFileName())
        .size(icon.getBytes().length)
        .type("image/png") // TODO: Tika cannot detect
        .hash(hash)
        .build();

      Context.getFileStorage().createFile(newIcon, icon.getBytes());
    }

    List<ParticipantDto> participantDtoList = new LinkedList<>();
    Profile groupChatProfile = Profile.builder()
      .name(createGroupChatInput.getDisplayName())
      .displayName(createGroupChatInput.getDisplayName())
      .icon(newIcon)
      .build();
    Chat groupChat = Chat.builder()
      .profile(groupChatProfile)
      .build();

    User recipientUser = AccountRepository.getCurrentUser(context);
    ParticipantDto recipientDto = ParticipantDto.builder()
      .hasPrivileges(true)
      .user(UserDto.from(recipientUser))
      .build();
    participantDtoList.add(recipientDto);
    ChatParticipant recipient =
      ChatRepository.createChatParticipant(context, groupChat, recipientUser, true, notifiable);

    createGroupChatInput.getParticipantUsernames().forEach(participantUsername -> {
      User participantUser = AccountRepository.getUser(context, participantUsername);
      ParticipantDto participantDto = ParticipantDto.builder()
        .hasPrivileges(false)
        .user(UserDto.from(participantUser))
        .build();
      participantDtoList.add(participantDto);
      ChatRepository.createChatParticipant(context, groupChat, participantUser, false, notifiable);
    });

    NotificationDto notify = NotificationDto.from(recipient.getSubscription().getDescription());
    ChatStampDto stamp = ChatStampDto.from(groupChat, null);

    return GroupChatDto.builder()
      .id(groupChat.getId())
      .participants(participantDtoList)
      .stamp(stamp)
      .notify(notify)
      .build();
  }

  public static GroupChatDto leaveGroupChat(Context context, ChatParticipant toRemove) {
    final Database database = context.getDatabase();
    Dao<ChatParticipant, Integer> chatParticipantDao =
      context.getDatabase().getDao(ChatParticipant.class);
    Chat chat = toRemove.getChat();
    List<ChatParticipant> chatParticipants =
      ChatRepository.getChatParticipants(context, chat.getId());

    assert chatParticipants.size() > 0;
    try {
      chatParticipantDao.delete(toRemove);
    } catch (SQLException e) {
      logger.error(e.getMessage());
      throw new GraphQLException(INTERNAL_ERROR);
    }
    if (chatParticipants.size() == 1) {
      List<Message> messages = ChatRepository.getMessages(context, chat.getId());
      Dao<Profile, Integer> profileDao = context.getDatabase().getDao(Profile.class);
      try {
        profileDao.deleteById(chat.getProfile().getId());
      } catch (SQLException e) {
        logger.error(e.getMessage());
        throw new GraphQLException(INTERNAL_ERROR);
      }
      Dao<Message, Integer> messageDao = database.getDao(Message.class);
      try {
        messageDao.delete(messages);
      } catch (SQLException e) {
        logger.error(e.getMessage());
        throw new GraphQLException(INTERNAL_ERROR);
      }
      return null;
    }
    return GroupChatDto.from(chat, context);
  }

  /**
   * Add an entry mapping the participant to the appropriate groupchat in the database.
   *
   * @param context {@link Context}
   * @return groupchat with updated memberlist
   */
  public static Boolean addParticipant(
    Context context, AddParticipantInput addParticipantInput) {

    // TODO: Implement

    return true;
  }

  /**
   * Add an entry mapping the participant to the appropriate groupchat in the database.
   *
   * @param context {@link Context}
   * @return groupchat with updated memberlist
   */
  public static Boolean removeParticipant(
    Context context, RemoveParticipantInput removeParticipantInput) {

    // TODO: Implement

    return true;
  }

  /**
   * Update the name in the table.
   *
   * @param context {@link Context}
   * @return a stamp
   */
  public static Boolean updateName(
    Context context,
    UpdateNameInput updateNameInput) {
    System.out.println(updateNameInput);

    // TODO: Implement

    return true;
  }

  /**
   * Update the picture.
   *
   * @param context {@link Context}
   * @return a stamp
   */
  public static Boolean updatePicture(
    Context context, UpdatePictureInput updatePictureInput) {

    // TODO: Implement

    return true;
  }

  /**
   * Set the flag for administrator rights.
   *
   * @param context {@link Context}
   * @return groupchat with recently granted privileges
   */
  public static Boolean grantPrivileges(
    Context context, GrantPrivilegesInput removeParticipantInput) {

    // TODO: Implement

    return true;
  }

  /**
   * Unset the flag for administrator rights.
   *
   * @param context {@link Context}
   * @return groupchat with recently revoked privileges
   */
  public static Boolean revokePrivileges(
    Context context, RevokePrivilegesInput revokePrivilegesInput) {

    // TODO: Implement

    return true;
  }

}
