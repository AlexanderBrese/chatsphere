package de.chatsphere.api.audiomessage.transfer;

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


public class AudioManager extends MessageManager {

  public AudioManager(Context context) {
    super(context);
  }

  @Override
  public void logQuery() {
    logger.info("Executing audioMessage(id: Int!): AudioMessage");
  }

  @Override
  public void logCreate() {
    logger.info("Executing createAudioMessage(createMessageInput: CreateMessageInput!): Chat");
  }

  @Override
  public void logUpdate() {
    String message =
      "Executing updateAudioMessage(updateMessageInput: UpdateMessageInput!): AudioMessage";
    logger.info(message);
  }

  @Override
  public void sanitizeCreate(CreateMessageInput input) throws GraphQLException {
    if (input.getChatId() < 0) {
      throw new GraphQLException("chatId is negative");
    }

    if (!isSupported(input.getEncoded().getBytes(), input.getFileName(), config.getAudio())) {
      throw new GraphQLException("MIME type not suitable for audio");
    }

    // TODO: Check that given file is a REAL audio file
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
        "Queried for AudioMessage but did not find a reference to an asset (asset is NULL)");
    }
    if (!Arrays.asList(config.getAudio()).contains(line.getAsset().getType())) {
      throw new GraphQLException(
        "Queried for audio but asset was of type " + line.getAsset().getType());
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
      .type("audio/mpeg") // TODO: waiting for file storage
      .build();
  }

  @Override
  public AbstractMessage create(int id, UserDto author, Date date, String text) {

    // TODO: `url` waiting for filestorage feature
    String url = "http://resamvi.de";

    return new AudioMessageDto(id, author, date, text, url);

  }
}
