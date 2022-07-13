package de.chatsphere.api.privatechat.model;

import com.j256.ormlite.dao.Dao;
import de.chatsphere.api.account.model.AccountRepository;
import de.chatsphere.api.chat.model.ChatRepository;
import de.chatsphere.api.chatstamp.transfer.ChatStampDto;
import de.chatsphere.api.privatechat.transfer.CreatePrivateChatInput;
import de.chatsphere.api.privatechat.transfer.PrivateChatDto;
import de.chatsphere.api.shared.model.Reportable;
import de.chatsphere.api.shared.transfer.NotificationDto;
import de.chatsphere.api.user.transfer.ParticipantDto;
import de.chatsphere.api.user.transfer.UserDto;
import de.chatsphere.io.database.Database;
import de.chatsphere.io.database.schema.chat.Chat;
import de.chatsphere.io.database.schema.chat.ChatLine;
import de.chatsphere.io.database.schema.chat.ChatParticipant;
import de.chatsphere.io.database.schema.chat.Message;
import de.chatsphere.io.database.schema.preference.enumeration.Notifiable;
import de.chatsphere.io.database.schema.preference.enumeration.Notifiable.Level;
import de.chatsphere.io.database.schema.preference.enumeration.Visibility;
import de.chatsphere.io.database.schema.user.User;
import de.chatsphere.server.graphql.Context;
import graphql.GraphQLException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class PrivateChatRepository implements Reportable {

  /**
   * asdasd.
   *
   * @param context asdasd.
   * @param createPrivateChatInput asdasd.
   * @return asdasd.
   */
  public static PrivateChatDto createPrivateChat(
    Context context,
    CreatePrivateChatInput createPrivateChatInput
  ) {
    User participantUser =
      AccountRepository.getUser(context, createPrivateChatInput.getParticipantUsername());

    List<ParticipantDto> participantDtoList = new LinkedList<>();
    Chat chat = Chat.builder().build();
    Dao<Notifiable, Integer> notificationDao = context.getDatabase().getDao(Notifiable.class);
    Notifiable notifiable;
    try {
      notifiable = notificationDao.queryForEq("description", Level.INHERIT).get(0);
    } catch (SQLException e) {
      logger.error(e.getMessage());
      throw new GraphQLException(INTERNAL_ERROR);
    }
    ParticipantDto participantDto = ParticipantDto.builder()
      .hasPrivileges(false)
      .user(UserDto.from(participantUser))
      .build();
    participantDtoList.add(participantDto);
    ChatParticipant participant =
      ChatRepository.createChatParticipant(context, chat, participantUser, false, notifiable);

    User recipientUser = AccountRepository.getCurrentUser(context);
    ParticipantDto recipientDto = ParticipantDto.builder()
      .hasPrivileges(true)
      .user(UserDto.from(recipientUser))
      .build();
    participantDtoList.add(recipientDto);
    ChatParticipant recipient =
      ChatRepository.createChatParticipant(context, chat, recipientUser, true, notifiable);

    NotificationDto notify = NotificationDto.from(recipient.getSubscription().getDescription());
    ChatStampDto stamp = ChatStampDto.from(participant.getParticipant().getProfile(), null);

    return PrivateChatDto.builder()
      .id(chat.getId())
      .participants(participantDtoList)
      .stamp(stamp)
      .notify(notify)
      .build();
  }

  public static ChatParticipant findChatIntersection(Context context, User other) {
    Dao<ChatParticipant, Integer> chatParticipantDao =
      context.getDatabase().getDao(ChatParticipant.class);
    List<ChatParticipant> result;
    try {
      result = chatParticipantDao.queryForEq("participant", other);
    } catch (SQLException e) {
      logger.error(e.getMessage());
      throw new GraphQLException(INTERNAL_ERROR);
    }

    User currentUser = AccountRepository.getCurrentUser(context);
    ChatParticipant intersect = null;
    for (ChatParticipant chatParticipant : result) {
      Chat chat = chatParticipant.getChat();
      if (!chat.isPrivateChat()) {
        break;
      }
      Integer chatId = chat.getId();
      try {
        intersect = chatParticipantDao.queryBuilder()
          .where()
          .eq("chat", chatId)
          .and()
          .eq("participant", currentUser)
          .queryForFirst();
      } catch (SQLException e) {
        logger.error(e.getMessage());
        throw new GraphQLException(INTERNAL_ERROR);
      }
    }

    return intersect;
  }

  public static PrivateChatDto leavePrivateChat(Context context, ChatParticipant toRemove) {
    final Database database = context.getDatabase();
    Dao<ChatParticipant, Integer> chatParticipantDao =
      database.getDao(ChatParticipant.class);
    Integer chatId = toRemove.getChat().getId();
    List<ChatParticipant> chatParticipants =
      ChatRepository.getChatParticipants(context, chatId);

    assert chatParticipants.size() == 2;
    if (ChatRepository.isHidden(chatParticipants.get(0))
      || ChatRepository.isHidden(chatParticipants.get(1))) {
      Dao<ChatLine, Integer> chatLineDao = database.getDao(ChatLine.class);
      List<Message> messages = ChatRepository.getMessages(context, chatId);
      Dao<Chat, Integer> chatDao = database.getDao(Chat.class);
      try {
        chatDao.deleteById(chatId);
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
    } else {
      toRemove.setVisibility(false);
      try {
        chatParticipantDao.update(toRemove);
      } catch (SQLException e) {
        logger.error(e.getMessage());
        throw new GraphQLException(INTERNAL_ERROR);
      }
    }
    return null;
  }
}
