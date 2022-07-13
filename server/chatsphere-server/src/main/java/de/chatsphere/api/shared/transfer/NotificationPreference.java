package de.chatsphere.api.shared.transfer;

import de.chatsphere.io.database.schema.preference.enumeration.Notifiable;

public enum NotificationPreference {
  INHERIT(Notifiable.Level.INHERIT),
  NOTIFY(Notifiable.Level.NOTIFY),
  MUTE(Notifiable.Level.MUTE);

  private final Notifiable.Level level;

  /**
   * <p>Constructor required to map ENUM values of Level to
   * NotificationPreference ENUM.</p>
   *
   * <p>TODO: Do not use NotificationPreference at all.</p>
   *
   * @param level equivalent level value
   */
  NotificationPreference(Notifiable.Level level) {
    this.level = level;
  }

  public Notifiable.Level toLevel() {
    return level;
  }
}
