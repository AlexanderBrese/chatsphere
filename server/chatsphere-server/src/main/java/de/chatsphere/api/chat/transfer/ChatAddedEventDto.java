package de.chatsphere.api.chat.transfer;

import de.chatsphere.server.rxbus.Event;
import java.util.List;
import lombok.Getter;

public class ChatAddedEventDto extends Event {

  @Getter
  private final AbstractChat chat;

  public ChatAddedEventDto(String sender, List<String> recipients, AbstractChat chat) {
    super(sender, recipients);
    this.chat = chat;
  }
}
