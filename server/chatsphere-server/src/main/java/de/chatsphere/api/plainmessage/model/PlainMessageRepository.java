package de.chatsphere.api.plainmessage.model;

import com.j256.ormlite.dao.Dao;
import de.chatsphere.api.account.model.AccountRepository;
import de.chatsphere.api.chat.model.ChatRepository;
import de.chatsphere.api.message.transfer.CreateMessageInput;
import de.chatsphere.api.message.transfer.UpdateMessageInput;
import de.chatsphere.api.plainmessage.transfer.PlainMessageDto;
import de.chatsphere.api.shared.model.Reportable;
import de.chatsphere.api.user.transfer.UserDto;
import de.chatsphere.io.database.Database;
import de.chatsphere.io.database.schema.chat.Chat;
import de.chatsphere.io.database.schema.chat.ChatLine;
import de.chatsphere.io.database.schema.chat.Message;
import de.chatsphere.io.database.schema.user.User;
import de.chatsphere.server.graphql.Context;
import graphql.GraphQLException;
import java.sql.SQLException;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlainMessageRepository implements Reportable {

  /**
   * asdasd.
   *
   * @param context            asdasd.
   * @param createMessageInput asdasd.
   *
   * @return asdasd.
   */
  public static PlainMessageDto createPlainMessage(
    Context context, CreateMessageInput createMessageInput
  ) {
    Database database = context.getDatabase();
    User author = AccountRepository.getCurrentUser(context);
    Chat chat = ChatRepository.getChat(context, createMessageInput.getChatId());
    Message message = Message.builder()
      .content(createMessageInput.getText())
      .markup(false)
      .build();

    Dao<ChatLine, Integer> chatLineDao = database.getDao(ChatLine.class);
    ChatLine newChatLine = ChatLine.builder()
      .chat(chat)
      .author(author)
      .message(message)
      .build();

    try {
      chatLineDao.create(newChatLine);
    } catch (SQLException e) {
      logger.error(e.getMessage());
      throw new GraphQLException(INTERNAL_ERROR);
    }

    return PlainMessageDto.builder()
      .id(message.getId())
      .text(message.getContent())
      .updatedAt(new Date())
      .author(UserDto.from(author))
      .build();
  }

  public static ChatLine updatePlainMessage(
    Context context, UpdateMessageInput updateMessageInput
  ) {
    Database database = context.getDatabase();
    Dao<Message, Integer> messageDao = database.getDao(Message.class);
    Message message;
    try {
      message = messageDao.queryForId(updateMessageInput.getMessageId());
    } catch (SQLException e) {
      logger.error(e.getMessage());
      throw new GraphQLException(INTERNAL_ERROR);
    }
    message.setContent(updateMessageInput.getText());
    try {
      messageDao.update(message);
    } catch (SQLException e) {
      logger.error(e.getMessage());
      throw new GraphQLException(INTERNAL_ERROR);
    }

    Dao<ChatLine, Integer> chatLineDao = database.getDao(ChatLine.class);
    ChatLine chatLine;
    try {
      chatLine = chatLineDao.queryForEq("message", message).get(0);
    } catch (SQLException e) {
      logger.error(e.getMessage());
      throw new GraphQLException(INTERNAL_ERROR);
    }

    return chatLine;
  }
}
