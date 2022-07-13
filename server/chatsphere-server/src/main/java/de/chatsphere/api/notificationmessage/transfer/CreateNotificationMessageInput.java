package de.chatsphere.api.notificationmessage.transfer;

import de.chatsphere.api.shared.transfer.Input;
import de.chatsphere.api.shared.transfer.NotificationDto;
import de.chatsphere.api.shared.transfer.NotificationPreference;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Input for creating a notification message.
 */
@AllArgsConstructor
public class CreateNotificationMessageInput implements Input {

  @Getter
  private String text;
  @Getter
  private String recipientName;
  @Getter
  private int chatId;

  public CreateNotificationMessageInput() {
  }

  /**
   * Initializes a new CreateNotificationMessageInput from a &lt;String,Object&gt; map map.
   *
   * @param map contains the graphql input property fields
   *
   * @return a CreateNotificationMessageInput type
   */
  @SuppressWarnings("unchecked")
  public CreateNotificationMessageInput fromMap(Map<String, Object> map) {
    String text = (String) map.get("text");
    String recipientName = (String) map.get("recipientName");
    int chatId = (int) map.get("chatId");

    return new CreateNotificationMessageInput(text, recipientName, chatId);
  }
}
