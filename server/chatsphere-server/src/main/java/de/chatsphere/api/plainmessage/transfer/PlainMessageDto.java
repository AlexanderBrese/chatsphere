package de.chatsphere.api.plainmessage.transfer;

import de.chatsphere.api.message.transfer.AbstractMessage;
import de.chatsphere.api.user.transfer.UserDto;
import de.chatsphere.io.database.schema.chat.ChatLine;
import de.chatsphere.io.database.schema.chat.Message;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * Sent to a chat. Contains text only.
 */
@AllArgsConstructor
@Builder
public class PlainMessageDto implements AbstractMessage {

  @Getter
  private final Integer id;
  @Getter
  private final UserDto author;
  @Getter
  private final Date updatedAt;
  @Getter
  private final String text;


  public static PlainMessageDto from(ChatLine chatLine) {
    Message message = chatLine.getMessage();
    return PlainMessageDto.builder()
      .id(message.getId())
      .text(message.getContent())
      .updatedAt(message.getUpdatedAt())
      .author(UserDto.from(chatLine.getAuthor()))
      .build();
  }
}
