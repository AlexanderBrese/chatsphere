package de.chatsphere.io.database.schema.user;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import de.chatsphere.io.database.Storeable;
import de.chatsphere.io.database.schema.preference.Language;
import de.chatsphere.io.database.schema.preference.Location;
import de.chatsphere.io.database.schema.preference.enumeration.Notifiable;
import java.sql.SQLException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Settings of a user.
 */
@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
@DatabaseTable(tableName = "UserPreferences")
public class UserPreferences implements Storeable {

  /**
   * Primary Key of the table.
   */
  @DatabaseField(generatedId = true)
  private Integer id;

  /**
   * User to whom the settings refer.
   * 
   * @throws SQLException When inserting a duplicate, the corresponding DAO will throw an exception.
   */
  @NonNull
  @DatabaseField(columnName = "user", foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
  private User user;

  /**
   * Language of the {@link #user}.
   */
  @NonNull
  @DatabaseField(columnName = "language", foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
  private Language language;

  /**
   * Location of the {@link #user}.
   */
  @DatabaseField(columnName = "location", foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
  private Location location;

  /**
   * Global notification level of the {@link #user}, which is inherited.
   */
  @DatabaseField(columnName = "notification", foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
  private Notifiable notification;

  /**
   * Whether link preview images are activated for the {@link #user}.
   */
  @DatabaseField
  private Boolean linkPreview;

  /**
   * Retention period of the {@link #user} account in days.
   *
   * <p>A {@link UserLegal#locked} account shall not be deleted automatically.
   */
  @DatabaseField
  private Integer retention;
}
