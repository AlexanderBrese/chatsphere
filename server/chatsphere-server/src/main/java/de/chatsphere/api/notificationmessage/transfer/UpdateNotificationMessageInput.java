package de.chatsphere.api.notificationmessage.transfer;

import de.chatsphere.api.shared.transfer.Input;
import de.chatsphere.api.shared.transfer.NotificationDto;
import de.chatsphere.api.shared.transfer.NotificationPreference;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Input for updating an audio message.
 */
@AllArgsConstructor
public class UpdateNotificationMessageInput implements Input {

  @Getter
  private int id;
  @Getter
  private int authorId;
  @Getter
  private String text;
  @Getter
  private NotificationDto notify;
  @Getter
  private int recipientId;

  public UpdateNotificationMessageInput() {
  }

  /**
   * Initializes a new UpdateNotificationMessageInput from a &lt;String,Object&gt; map map.
   *
   * @param map contains the graphql input property fields
   *
   * @return a UpdateNotificationMessageInput type
   */
  @SuppressWarnings("unchecked")
  public UpdateNotificationMessageInput fromMap(Map<String, Object> map) {
    int id = (int) map.get("id");
    int authorId = (int) map.get("authorId");
    String text = (String) map.get("text");
    int recipientId = (int) map.get("recipientId");

    String notificationPreference =
      (String) ((LinkedHashMap<String, Object>) map.get("notify")).get("push");
    NotificationDto notificationDto = NotificationDto.from(notificationPreference);

    return new UpdateNotificationMessageInput(id, authorId, text, notificationDto, recipientId);
  }
}
