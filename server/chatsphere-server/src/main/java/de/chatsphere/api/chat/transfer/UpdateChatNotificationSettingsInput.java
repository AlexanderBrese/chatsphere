package de.chatsphere.api.chat.transfer;

import de.chatsphere.api.shared.transfer.Input;
import de.chatsphere.api.shared.transfer.NotificationDto;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * A GraphQL query input type. Those are arguments to create a complex object.
 */
@AllArgsConstructor
public class UpdateChatNotificationSettingsInput implements Input {

  @Getter
  private int chatId;
  @Getter
  private NotificationDto notify;

  public UpdateChatNotificationSettingsInput() {
  }

  /**
   * Initializes a new CreateLinkInput from a link input map.
   *
   * @param updateChatNotificationSettingsInputMap the link input map
   *
   * @return the CreateLinkInput type
   */
  @Override
  public UpdateChatNotificationSettingsInput fromMap(
    Map<String, Object> updateChatNotificationSettingsInputMap
  ) {
    int chatId = (int) updateChatNotificationSettingsInputMap.get("chatId");
    NotificationDto notify = (NotificationDto) updateChatNotificationSettingsInputMap.get("notify");

    return new UpdateChatNotificationSettingsInput(chatId, notify);
  }
}
