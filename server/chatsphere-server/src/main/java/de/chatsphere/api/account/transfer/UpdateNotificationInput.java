package de.chatsphere.api.account.transfer;

import de.chatsphere.api.shared.transfer.Input;
import de.chatsphere.api.shared.transfer.NotificationDto;
import de.chatsphere.api.shared.transfer.NotificationPreference;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class UpdateNotificationInput implements Input {

  @Getter
  private NotificationDto notify;

  public UpdateNotificationInput() {
  }

  @Override
  public UpdateNotificationInput fromMap(Map<String, Object> map) {

    @SuppressWarnings("unchecked")
    String notificationPreference =
      (String) ((LinkedHashMap<String, Object>) map.get("notify")).get("push");
    NotificationDto notificationDto = NotificationDto.from(notificationPreference);

    return new UpdateNotificationInput(notificationDto);
  }
}
