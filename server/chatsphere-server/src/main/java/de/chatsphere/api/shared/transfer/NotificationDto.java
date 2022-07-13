package de.chatsphere.api.shared.transfer;

import de.chatsphere.io.database.schema.preference.enumeration.Notifiable.Level;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * A notification entity. Used to set preferences about notifaction settings.
 */
@AllArgsConstructor
public class NotificationDto {

  @Getter
  private final NotificationPreference push;

  /**
   * A convenient helper method to convert from a notification level into a notification DTO.
   *
   * @param level the notification level
   *
   * @return the notification DTO
   */
  public static NotificationDto from(Level level) {
    switch (level) {
      case INHERIT:
        return new NotificationDto(NotificationPreference.INHERIT);
      case MUTE:
        return new NotificationDto(NotificationPreference.MUTE);
      case NOTIFY:
        return new NotificationDto(NotificationPreference.NOTIFY);
      default:
        assert false : "Unexpected Level '" + level + "'. "
          + "Error while trying to resolve 'notify' field";
        return null;
    }
  }

  public static NotificationDto from(String notificationPreference) {
    NotificationDto notify = null;
    switch (notificationPreference) {
      case "NOTIFY":
        notify = new NotificationDto(NotificationPreference.NOTIFY);
        break;
      case "MUTE":
        notify = new NotificationDto(NotificationPreference.MUTE);
        break;
      case "INHERIT":
        notify = new NotificationDto(NotificationPreference.INHERIT);
        break;
      default:
        System.err.println("Error while parsing notify field");
        break;
    }
    return notify;
  }
}
