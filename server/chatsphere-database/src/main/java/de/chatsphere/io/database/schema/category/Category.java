package de.chatsphere.io.database.schema.category;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import de.chatsphere.io.database.Storeable;
import de.chatsphere.io.database.schema.Profile;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * A section that is organized with others as a tree, and in which chats can be classified.
 */
@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
@DatabaseTable(tableName = "Category")
public class Category implements Storeable {

  /**
   * Primary Key of the table.
   */
  @DatabaseField(generatedId = true)
  private Integer id;

  /**
   * The profile of the category.
   */
  @NonNull
  @DatabaseField(
    columnName = "profile", foreign = true,
    foreignAutoCreate = true, foreignAutoRefresh = true
  )
  private Profile profile;

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
