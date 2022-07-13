package de.chatsphere.io.database.schema.chat;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import de.chatsphere.io.database.Storeable;
import de.chatsphere.io.database.schema.preference.File;
import de.chatsphere.io.database.schema.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * One line within a chat.
 */
@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
@DatabaseTable(tableName = "ChatLine")
public class ChatLine implements Storeable {

  /**
   * Primary Key of the table.
   */
  @DatabaseField(generatedId = true)
  private Integer id;

  /**
   * Chat to which the entry belongs.
   */
  @NonNull
  @DatabaseField(columnName = "chat", foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
  private Chat chat;

  /**
   * Author who wrote the line.
   */
  @NonNull
  @DatabaseField(columnName = "author", foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true, maxForeignAutoRefreshLevel = 3)
  private User author;

  /**
   * Message that was written.
   */
  @NonNull
  @DatabaseField(columnName = "message", foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
  private Message message;

  /**
   * Optional file attachments.
   */
  @DatabaseField(columnName = "asset", foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
  private File asset;
}

