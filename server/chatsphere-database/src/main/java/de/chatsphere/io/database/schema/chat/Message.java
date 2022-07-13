package de.chatsphere.io.database.schema.chat;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import de.chatsphere.io.database.Storeable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * A written message.
 */
@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
@DatabaseTable(tableName = "Message")
public class Message implements Storeable {

  /**
   * Primary Key of the table.
   */
  @DatabaseField(generatedId = true)
  private Integer id;

  /**
   * Specifies whether the markup parser must be executed before {@link #content} is displayed.
   */
  @NonNull
  @DatabaseField
  private Boolean markup;

  /**
   * The content of the message with less than 65,535 unicode characters.
   */
  @NonNull
  @DatabaseField
  private String content;

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
