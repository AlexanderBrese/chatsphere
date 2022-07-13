package de.chatsphere.api.notificationmessage.transfer;

import de.chatsphere.server.rxbus.Event;
import java.util.List;
import lombok.Getter;

public class NotificationEventDto extends Event {

  @Getter
  private final String text;

  @Getter
  private final int chatId;

  public NotificationEventDto(String sender, List<String> recipients, String text, int chatId) {
    super(sender, recipients);
    this.text = text;
    this.chatId = chatId;
  }
}
