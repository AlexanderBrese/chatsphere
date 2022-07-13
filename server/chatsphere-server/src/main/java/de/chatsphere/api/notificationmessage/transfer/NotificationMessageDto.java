package de.chatsphere.api.notificationmessage.transfer;

import de.chatsphere.api.chat.transfer.AbstractChat;
import de.chatsphere.api.message.transfer.AbstractMessage;
import de.chatsphere.api.shared.transfer.NotificationDto;
import de.chatsphere.api.user.transfer.UserDto;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * An NotificationMessageDto entity.
 */
@AllArgsConstructor
@Builder
public class NotificationMessageDto implements AbstractMessage {

  @Getter
  private final Integer id;
  @Getter
  private final UserDto author;
  @Getter
  private final Date updatedAt;
  @Getter
  private final String text;
  @Getter
  private final NotificationDto notify;
  @Getter
  private final UserDto recipient;
}
