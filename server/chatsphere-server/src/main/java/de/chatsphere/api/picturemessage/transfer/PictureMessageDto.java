package de.chatsphere.api.picturemessage.transfer;

import de.chatsphere.api.message.transfer.AbstractMessage;
import de.chatsphere.api.user.transfer.UserDto;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * An AudioMessage entity.
 */
@AllArgsConstructor
@Builder
public class PictureMessageDto implements AbstractMessage {

  @Getter
  private final Integer id;
  @Getter
  private final UserDto author;
  @Getter
  private final Date updatedAt;
  @Getter
  private final String text;
  @Getter
  private final String url;
}
