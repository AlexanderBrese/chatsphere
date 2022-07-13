package de.chatsphere.api.chat.model;

import com.j256.ormlite.dao.Dao;
import de.chatsphere.api.account.model.AccountRepository;
import de.chatsphere.api.chat.transfer.AbstractChat;
import de.chatsphere.api.chat.transfer.LeaveChatsInput;
import de.chatsphere.api.chat.transfer.UpdateChatNotificationSettingsInput;
import de.chatsphere.api.groupchat.model.GroupChatRepository;
import de.chatsphere.api.groupchat.transfer.GroupChatDto;
import de.chatsphere.api.privatechat.model.PrivateChatRepository;
import de.chatsphere.api.privatechat.transfer.PrivateChatDto;
import de.chatsphere.api.shared.model.Reportable;
import de.chatsphere.io.database.schema.chat.Chat;
import de.chatsphere.io.database.schema.chat.ChatLine;
import de.chatsphere.io.database.schema.chat.ChatParticipant;
import de.chatsphere.io.database.schema.chat.Message;
import de.chatsphere.io.database.schema.preference.enumeration.Notifiable;
import de.chatsphere.io.database.schema.preference.enumeration.Visibility;
import de.chatsphere.io.database.schema.user.User;
import de.chatsphere.server.graphql.Context;
import graphql.GraphQLException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handle access to database. All the chat-specific data needs and queries are resolved here.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ChatRepository implements Reportable {

  private static final Logger log = LoggerFactory.getLogger(ChatRepository.class);

  /**
   * asdasd.
   *
   * @param context asdasd.
   * @param id asdasd.
   * @return asdasd.
   */
  public static AbstractChat chat(Context context, Integer id) {
    Chat chat = getChat(context, id);
    return chat(context, chat);
  }

  public static AbstractChat chat(Context context, Chat chat) {
    return chat.isPrivateChat()
      ? PrivateChatDto.from(chat, context)
      : GroupChatDto.from(chat, context);
  }

  public static AbstractChat chat(Context context, Chat chat, String displayName) {
    return chat.isPrivateChat()
      ? PrivateChatDto.from(chat, context, displayName)
      : GroupChatDto.from(chat, context);
  }

  /**
   * Return all chats, this user is participating in with `name` as prefix
   *
   * @param context {@link Context}
   * @return modified chat
   */
  public static List<AbstractChat> searchChat(Context context, String name) {

    try {
      // Get a list of all chats this user is participating in
      Dao<ChatParticipant, Integer> participantDao = context.getDatabase()
        .getDao(ChatParticipant.class);
      User user = AccountRepository.getUser(context, context.getAuthenticator().getUsername());
      List<ChatParticipant> participating = participantDao.queryForEq("participant", user.getId());

      // Extract chats
      List<AbstractChat> result = new ArrayList<>();
      for (ChatParticipant p : participating) {
        if (!p.getVisibility()) {
          continue;
        }
        AbstractChat chat = ChatRepository.chat(context, p.getChat());
        // Check if the user's name is prefix of this chat's name
        if (chat.getStamp().getDisplayName().toLowerCase().startsWith(name.toLowerCase())) {
          result.add(chat);
        }
      }

      return result;

    } catch (SQLException e) {
      e.printStackTrace();
      log.error(e.getMessage());
      throw new GraphQLException(INTERNAL_ERROR);
    }
  }


  /**
   * Apply settings to the database.
   *
   * @param context {@link Context}
   * @return modified chat
   */
  public static Boolean updateChatNotificationSettings(
    Context context,
    UpdateChatNotificationSettingsInput updateChatNotificationSettingsInput) {

    Dao<Notifiable, Integer> notifiableDao = context.getDatabase().getDao(Notifiable.class);
    Notifiable notifiable;
    try {
      notifiable = notifiableDao.queryForEq("description", updateChatNotificationSettingsInput
        .getNotify()
        .getPush()
        .toLevel()).get(0);
    } catch (SQLException e) {
      log.error(e.getMessage());
      throw new GraphQLException(INTERNAL_ERROR);
    }

    Dao<ChatParticipant, Integer> chatParticipantDao =
      context.getDatabase().getDao(ChatParticipant.class);
    try {
      ChatParticipant toUpdate = chatParticipantDao.queryBuilder()
        .where()
        .eq("chat", updateChatNotificationSettingsInput.getChatId())
        .and()
        .eq("participant", AccountRepository.getCurrentUser(context))
        .queryForFirst();
      toUpdate.setSubscription(notifiable);
      chatParticipantDao.update(toUpdate);
    } catch (SQLException e) {
      log.error(e.getMessage());
      throw new GraphQLException(INTERNAL_ERROR);
    }

    return true;
  }

  public static void updateVisibility(Context context, ChatParticipant chatParticipant,
    Boolean visibility) {
    Dao<ChatParticipant, Integer> chatParticipantDao = context.getDatabase()
      .getDao(ChatParticipant.class);
    Dao<Visibility, Integer> visibilityDao = context.getDatabase().getDao(Visibility.class);
    chatParticipant.setVisibility(visibility);
    try {
      chatParticipantDao.update(chatParticipant);
    } catch (SQLException e) {
      logger.error(e.getMessage());
      throw new GraphQLException(INTERNAL_ERROR);
    }
  }

  public static List<Message> getMessages(Context context, Integer chatId) {
    Dao<ChatLine, Integer> chatLineDao = context.getDatabase().getDao(ChatLine.class);
    List<Message> messages = new LinkedList<>();
    try {
      List<ChatLine> chatLines = chatLineDao.queryForEq("chat", chatId);
      chatLines.forEach(chatLine -> {
        messages.add(chatLine.getMessage());
      });
    } catch (SQLException e) {
      logger.error(e.getMessage());
      throw new GraphQLException(INTERNAL_ERROR);
    }
    return messages;
  }

  /**
   * Remove the participant out of the appropriate database tables.
   *
   * @param context {@link Context}
   * @return TODO: which user is given back?
   */
  public static List<AbstractChat> leaveChats(Context context, LeaveChatsInput leaveChatsInput) {
    Dao<ChatParticipant, Integer> chatParticipantDao =
      context.getDatabase().getDao(ChatParticipant.class);

    List<AbstractChat> left = new LinkedList<>();
    leaveChatsInput.getChatIds().forEach(chatId -> {
      ChatParticipant toRemove;
      try {
        toRemove = chatParticipantDao.queryBuilder()
          .where()
          .eq("chat", chatId)
          .and()
          .eq("participant", AccountRepository.getCurrentUser(context))
          .queryForFirst();
      } catch (SQLException e) {
        log.error(e.getMessage());
        throw new GraphQLException(INTERNAL_ERROR);
      }
      AbstractChat chat;
      if (toRemove.getChat().isPrivateChat()) {
        chat = PrivateChatRepository.leavePrivateChat(context, toRemove);
      } else {
        chat = GroupChatRepository.leaveGroupChat(context, toRemove);
      }
      if (chat != null) {
        left.add(chat);
      }
    });
    return left;
  }

  /**
   * asdasd.
   *
   * @param context asdasd.
   * @param id asdasd.
   * @return asdasd.
   */
  public static Chat getChat(Context context, Integer id) {
    Dao<Chat, Integer> chatDao = context.getDatabase().getDao(Chat.class);

    try {
      return chatDao.queryForId(id);
    } catch (SQLException e) {
      log.error(e.getMessage());
      throw new GraphQLException(INTERNAL_ERROR);
    }
  }

  public static List<ChatParticipant> getChatParticipants(Context context, Integer chatId) {
    Dao<ChatParticipant, Integer> chatParticipantDao =
      context.getDatabase().getDao(ChatParticipant.class);

    try {
      return chatParticipantDao.queryForEq("chat", chatId);
    } catch (SQLException e) {
      log.error(e.getMessage());
      throw new GraphQLException(INTERNAL_ERROR);
    }
  }

  public static List<ChatParticipant> getChatParticipants(Context context,
    List<Integer> participantIds) {
    Dao<ChatParticipant, Integer> chatParticipantDao =
      context.getDatabase().getDao(ChatParticipant.class);

    List<ChatParticipant> chatParticipants = new LinkedList<>();

    participantIds.forEach(participantId -> {
      try {
        List<ChatParticipant> result = chatParticipantDao.queryForEq("participant", participantId);
        if (result.size() > 0) {
          chatParticipants.add(result.get(0));
        }
      } catch (SQLException e) {
        log.error(e.getMessage());
        throw new GraphQLException(INTERNAL_ERROR);
      }
    });

    return chatParticipants;
  }

  public static boolean isHidden(ChatParticipant chatParticipant) {
    return !chatParticipant.getVisibility();
  }

  public static List<ChatLine> getChatLines(Context context, Chat chat) {
    Dao<ChatLine, Integer> chatLineDao = context.getDatabase().getDao(ChatLine.class);
    try {
      return chatLineDao.queryForEq("chat", chat);
    } catch (SQLException e) {
      log.error(e.getMessage());
      throw new GraphQLException(INTERNAL_ERROR);
    }
  }

  public static ChatParticipant createChatParticipant(
    Context context,
    Chat chat,
    User participant,
    Boolean isAdmin,
    Notifiable notifiable
  ) {
    Dao<ChatParticipant, Integer> chatParticipantDao =
      context.getDatabase().getDao(ChatParticipant.class);
    Dao<Visibility, Integer> visibilityDao = context.getDatabase().getDao(Visibility.class);

    ChatParticipant newChatParticipant = ChatParticipant.builder()
      .chat(chat)
      .participant(participant)
      .admin(isAdmin)
      .visibility(true)
      .subscription(notifiable)
      .build();
    try {
      chatParticipantDao.create(newChatParticipant);
      return newChatParticipant;
    } catch (SQLException e) {
      log.error(e.getMessage());
      throw new GraphQLException(INTERNAL_ERROR);
    }
  }
}
