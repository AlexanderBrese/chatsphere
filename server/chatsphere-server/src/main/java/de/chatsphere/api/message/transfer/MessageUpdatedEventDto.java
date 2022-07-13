package de.chatsphere.api.message.transfer;

import de.chatsphere.server.rxbus.Event;
import java.util.List;
import lombok.Getter;

public class MessageUpdatedEventDto extends Event {

  @Getter
  private final AbstractMessage message;

  public MessageUpdatedEventDto(String sender, List<String> recipients, AbstractMessage message) {
    super(sender, recipients);
    this.message = message;
  }
}
