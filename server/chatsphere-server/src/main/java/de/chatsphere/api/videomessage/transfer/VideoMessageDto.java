package de.chatsphere.api.videomessage.transfer;

import de.chatsphere.api.message.transfer.AbstractMessage;
import de.chatsphere.api.user.transfer.UserDto;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * A VideoMessage entity.
 */
@AllArgsConstructor
@Builder
public class VideoMessageDto implements AbstractMessage {

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
