package de.chatsphere.io.database.schema.chat;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import de.chatsphere.io.database.Storeable;
import de.chatsphere.io.database.schema.Profile;
import de.chatsphere.io.database.schema.category.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Save meta information and the existence of a chat.
 */
@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
@DatabaseTable(tableName = "Chat")
public class Chat implements Storeable {

  /**
   * Primary Key of the table.
   */
  @DatabaseField(generatedId = true)
  private Integer id;

  /**
   * Saves the category of a public group chat. If not set, the chat is not public.
   *
   * <p>Setting {@link #category} implits setting a {@link #profile}.
   */
  @DatabaseField(columnName = "category", foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
  private Category category;

  /**
   * Saves the profile of a group chat. If not set, it is a private chat.
   */
  @DatabaseField(columnName = "profile", foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
  private Profile profile;


  public boolean isPrivateChat() {
    return this.profile == null;
  }

  public boolean isGroupChat() {
    return !isPrivateChat();
  }

  public boolean isPublic() {
    return this.category != null;
  }
}
