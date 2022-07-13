package de.chatsphere.api.message.transfer;

import de.chatsphere.api.audiomessage.transfer.AudioMessageDto;
import de.chatsphere.api.documentmessage.transfer.DocumentMessageDto;
import de.chatsphere.api.picturemessage.transfer.PictureMessageDto;
import de.chatsphere.api.plainmessage.transfer.PlainMessageDto;
import de.chatsphere.api.user.transfer.UserDto;
import de.chatsphere.io.database.schema.chat.ChatLine;
import graphql.GraphQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface AbstractMessage {

  Integer id = 0;
  UserDto author = UserDto.builder().build();
  Date updatedAt = new Date();
  String text = "";
  String url = "";


  /**
   * <p>Used to resolve `log`.</p>
   * <p>Infer concrete message types of a list of abstract ChatLines.</p>
   *
   * <p>When generating log, no `to` field is resolved to stop the recursive loop
   * (because `to` is of type `Chat` which requires `log` of type `Message`. But each `Message` has
   * a `to` field of its own - continue ad absurdum)</p>
   *
   * <p>TODO: currently Message requires knowledge of all its deviates</p>
   *
   * @param lines - list of messages to a chat
   * @return list of Messages
   */
  static List<AbstractMessage> from(List<ChatLine> lines) {
    List<AbstractMessage> result = new LinkedList<>();
    lines.forEach(line -> result.add(from(line)));
    return result;
  }

  /**
   * asasdasd.
   *
   * @param line asdasd
   * @return asdasd
   */
  static AbstractMessage from(ChatLine line) {
    List<AbstractMessage> result = new LinkedList<>();

    // Extract "image" and "png" of "image/png"
    Pattern p = Pattern.compile("^([a-zA-Z]+)/([[a-zA-Z]+])");

    if (line.getAsset() != null) {

      Matcher m = p.matcher(line.getAsset().getType());
      if (!m.find()) {
        throw new GraphQLException("Could not parse MIME type: " + line.getAsset().getType());
      }

      // TODO: Waiting for feature to get url (meanwhile return null)

      switch (m.group(1)) {
        case "audio":
          return new AudioMessageDto(line.getId(), UserDto.from(line.getAuthor()),
            line.getMessage().getUpdatedAt(), line.getMessage().getContent(), null);
        case "application":
          return new DocumentMessageDto(line.getId(), UserDto.from(line.getAuthor()),
            line.getMessage().getUpdatedAt(), line.getMessage().getContent(), null);
        case "video":
          return new DocumentMessageDto(line.getId(), UserDto.from(line.getAuthor()),
            line.getMessage().getUpdatedAt(), line.getMessage().getContent(), null);
        case "image":
          return new PictureMessageDto(line.getId(), UserDto.from(line.getAuthor()),
            line.getMessage().getUpdatedAt(), line.getMessage().getContent(), null);
        default:
          throw new GraphQLException("Unknown/Unsupported type: " + line.getAsset().getType());
      }

    } else {
      // PlainMessageDto
      return PlainMessageDto.from(line);
    }
  }
}
