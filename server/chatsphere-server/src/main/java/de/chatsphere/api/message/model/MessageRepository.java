package de.chatsphere.api.message.model;

import com.j256.ormlite.dao.Dao;
import de.chatsphere.api.message.transfer.RemoveMessagesInput;
import de.chatsphere.api.shared.model.Reportable;
import de.chatsphere.io.database.Database;
import de.chatsphere.io.database.schema.chat.ChatLine;
import de.chatsphere.io.database.schema.chat.Message;
import de.chatsphere.server.graphql.Context;
import graphql.GraphQLException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handle access to database. All the groupchat-specific data needs and queries are resolved here.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MessageRepository implements Reportable {

  private static final Logger log = LoggerFactory.getLogger(MessageRepository.class);

  /**
   * Remove sent messages (if they are your own or you're an admin).
   *
   * @param context {@link Context}
   *
   * @return updated chat
   */
  public static List<ChatLine> removeMessages(
    Context context,
    RemoveMessagesInput removeMessagesInput) {
    Database database = context.getDatabase();
    Dao<Message, Integer> messageDao = database.getDao(Message.class);
    Dao<ChatLine, Integer> chatLineDao = database.getDao(ChatLine.class);

    List<ChatLine> chatLines = new LinkedList<>();
    removeMessagesInput.getMessageIds().forEach(messageId -> {
      try {
        ChatLine chatLine = chatLineDao.queryForEq("message", messageId).get(0);
        chatLines.add(chatLine);
      } catch (SQLException e) {
        log.error(e.getMessage());
        throw new GraphQLException(INTERNAL_ERROR);
      }
      try {
        messageDao.deleteById(messageId);
      } catch (SQLException e) {
        log.error(e.getMessage());
        throw new GraphQLException(INTERNAL_ERROR);
      }
    });

    return chatLines;
  }

}
