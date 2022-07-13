package de.chatsphere.api.plainmessage.transfer;

import de.chatsphere.api.message.transfer.AbstractMessage;
import de.chatsphere.api.message.transfer.CreateMessageInput;
import de.chatsphere.api.message.transfer.MessageManager;
import de.chatsphere.api.message.transfer.UpdateMessageInput;
import de.chatsphere.api.user.transfer.UserDto;
import de.chatsphere.io.database.schema.chat.ChatLine;
import de.chatsphere.io.database.schema.preference.File;
import de.chatsphere.server.graphql.Context;
import graphql.GraphQLException;
import java.util.Date;

public class PlainMessageManager extends MessageManager {

  public PlainMessageManager(Context context) {
    super(context);
  }

  @Override
  public void logQuery() {
    logger.info("Executing plainMessage(id: Int!): PlainMessage");
  }

  @Override
  public void logCreate() {
    logger.info("Executing createPlainMessage(createMessageInput: CreateMessageInput!): Chat");
  }

  @Override
  public void logUpdate() {
    logger
      .info("Executing updatePlainMessage(updateMessageInput: UpdateMessageInput!): PlainMessage");
  }

  @Override
  public void sanitizeCreate(CreateMessageInput input) throws GraphQLException {
    if (input.getChatId() < 0) {
      throw new GraphQLException("chatId is negative");
    }

    // TODO: Check that input does not contain malicious code/swear words/spam (?)
    // TODO: Check that text characters are valid
    // TODO: Check limit

  }

  @Override
  public void sanitizeUpdate(UpdateMessageInput input) throws GraphQLException {
    if (input.getMessageId() < 0) {
      throw new GraphQLException("chatId is negative");
    }

    // TODO: Check that input does not contain malicious code/swear words/spam (?)
    // TODO: Check that text characters are valid
    // TODO: Check for text limit
  }

  @Override
  public void sanitizeGet(ChatLine line) {
    if (line.getAsset() != null) {
      throw new GraphQLException(
        "Queried for PlainMessage but found a reference to an asset (asset is NOT NULL)");
    }
  }

  @Override
  public File buildFile(CreateMessageInput input) {
    return null;
  }

  @Override
  public AbstractMessage create(int id, UserDto author, Date date, String text) {
    return new PlainMessageDto(id, author, date, text);
  }
}
