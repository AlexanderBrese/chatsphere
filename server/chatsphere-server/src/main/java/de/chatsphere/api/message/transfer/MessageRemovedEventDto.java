package de.chatsphere.api.message.transfer;

import de.chatsphere.server.rxbus.Event;
import java.util.List;
import lombok.Getter;

public class MessageRemovedEventDto extends Event {

  @Getter
  private final Integer messageId;

  public MessageRemovedEventDto(String sender, List<String> recipients, Integer messageId) {
    super(sender, recipients);
    this.messageId = messageId;
  }
}
