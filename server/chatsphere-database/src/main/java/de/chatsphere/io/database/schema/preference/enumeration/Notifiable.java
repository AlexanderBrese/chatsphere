package de.chatsphere.io.database.schema.preference.enumeration;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import de.chatsphere.io.database.Storeable;
import java.sql.SQLException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Notifiable indicates the extent to which notifications are sent.
 *
 * <p>This table stores only the possible values. Please reference the entries as foreign key.
 */
@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
@DatabaseTable(tableName = "NotificationLevel")
public class Notifiable implements Storeable {

  /**
   * Primary Key of the table.
   *
   * <p>The values in this column may differ from those in the ENUM Ordinal.
   */
  @Getter
  @DatabaseField(generatedId = true)
  private Integer level;

  /**
   * Notifiable ENUM value.
   *
   * <p>Each value may only exist once in the database. However, each entry in the table can be
   * referenced several times from other tables with the {@link #level}.
   *
   * <p>If invalid or not set, the value is {@link Visibility.Level#INHERIT}
   *
   * @throws SQLException on duplicate caused by dao.create or dao.update
   */
  @NonNull
  @DatabaseField(dataType = DataType.ENUM_TO_STRING, unique = true, unknownEnumName = "INHERIT")
  private Level description;

  /**
   * Representation of the possible values as Java-ENUM.
   */
  public enum Level {
    /**
     * The value is inherited. This should be the default.
     */
    INHERIT,
    /**
     * Notifications are sent.
     */
    NOTIFY,
    /**
     * No notifications are created.
     */
    MUTE;
  }
}
