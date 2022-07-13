package de.chatsphere.io.database.schema.chat;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import de.chatsphere.io.database.Storeable;
import de.chatsphere.io.database.schema.preference.enumeration.Notifiable;
import de.chatsphere.io.database.schema.preference.enumeration.Visibility;
import de.chatsphere.io.database.schema.user.User;
import java.sql.SQLException;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Stores a participant of a chat.
 */
@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
@DatabaseTable(tableName = "ChatParticipant")
public class ChatParticipant implements Storeable {

  /**
   * Primary Key of the table.
   */
  @DatabaseField(generatedId = true)
  private Integer id;

  /**
   * The chat where the {@link #participant} is a participant.
   *
   * @throws SQLException When inserting a duplicate {@link #chat}×{@link #participant} pair, the corresponding DAO will
   * throw an exception.
   */
  @NonNull
  @DatabaseField(columnName = "chat", uniqueCombo = true, foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
  private Chat chat;

  /**
   * The participant of the chat.
   *
   * @throws SQLException When inserting a duplicate {@link #chat}×{@link #participant} pair, the corresponding DAO will
   * throw an exception.
   */
  @NonNull
  @DatabaseField(columnName = "participant", uniqueCombo = true, foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true,  maxForeignAutoRefreshLevel = 3)
  private User participant;

  /**
   * The level that determines the scope of notifications for the {@link #participant}.
   */
  @DatabaseField(columnName = "subscription", foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
  private Notifiable subscription;

  /**
   * Whether the {@link #participant} is an administrator in the {@link #chat}.
   */
  @NonNull
  @DatabaseField
  private Boolean admin;

  /**
   * The visibility of the participant.
   */
  @NonNull
  @DatabaseField
  private Boolean visibility;

  /**
   * Time the entry was created (automatically set by the database).
   */
  @DatabaseField(dataType = DataType.DATE)
  private Date createdAt;

  /**
   * Time the entry was updated (automatically maintained by the database).
   */
  @DatabaseField(dataType = DataType.DATE)
  private Date updatedAt;
}
