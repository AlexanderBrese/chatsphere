package de.chatsphere.api.documentmessage.transfer;

import de.chatsphere.api.message.transfer.AbstractMessage;
import de.chatsphere.api.message.transfer.CreateMessageInput;
import de.chatsphere.api.message.transfer.MessageManager;
import de.chatsphere.api.message.transfer.UpdateMessageInput;
import de.chatsphere.api.user.transfer.UserDto;
import de.chatsphere.io.database.schema.chat.ChatLine;
import de.chatsphere.io.database.schema.preference.File;
import de.chatsphere.server.graphql.Context;
import graphql.GraphQLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

public class DocumentManager extends MessageManager {

  public DocumentManager(Context context) {
    super(context);
  }

  @Override
  public void logQuery() {
    logger.info("Executing documentMessage(id: Int!): DocumentMessage");
  }

  @Override
  public void logCreate() {
    logger.info("Executing createDocumentMessage(createMessageInput: CreateMessageInput!): Chat");
  }

  @Override
  public void logUpdate() {
    String message =
      "Executing updateDocumentMessage(updateMessageInput: UpdateMessageInput!): DocumentMessage";
    logger.info(message);
  }

  @Override
  public void sanitizeCreate(CreateMessageInput input) throws GraphQLException {
    if (input.getChatId() < 0) {
      throw new GraphQLException("chatId is negative");
    }

    if (!isSupported(input.getEncoded().getBytes(), input.getFileName(), config.getDocument())) {
      throw new GraphQLException("MIME type not suitable for document");
    }

    // TODO: Check that given file is a REAL document
  }

  @Override
  public void sanitizeUpdate(UpdateMessageInput input) throws GraphQLException {
    if (input.getMessageId() < 0) {
      throw new GraphQLException("chatId is negative");
    }

    // TODO: Check for text limit
  }

  @Override
  public void sanitizeGet(ChatLine line) {
    if (line.getAsset() == null) {
      throw new GraphQLException(
        "Queried for DocumentMessage but did not find a reference to an asset (asset is NULL)");
    }
    if (!Arrays.asList(config.getDocument()).contains(line.getAsset().getType())) {
      throw new GraphQLException(
        "Queried for document but asset was of type " + line.getAsset().getType());
    }
  }

  @Override
  public File buildFile(CreateMessageInput input) {
    byte[] hash = null;

    // (temporary) > me trying to generate a random sha-256 hash without any libs
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      hash = digest.digest(UUID.randomUUID().toString().getBytes());
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }

    return File.builder()
      .name(input.getFileName())
      .size(0) // TODO: waiting for file storage
      .hash(hash) // TODO: waiting for file storage
      .type("application/pdf") // TODO: waiting for file storage
      .build();
  }

  @Override
  public AbstractMessage create(int id, UserDto author, Date date, String text) {

    // TODO: `url` waiting for filestorage feature
    String url = "http://resamvi.de";

    return new DocumentMessageDto(id, author, date, text, url);

  }
}
