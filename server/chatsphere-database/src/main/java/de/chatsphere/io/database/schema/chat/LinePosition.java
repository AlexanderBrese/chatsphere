package de.chatsphere.io.database.schema.chat;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import de.chatsphere.io.database.Storeable;
import java.sql.SQLException;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Saves the position up to which messages were read.
 */
@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
@DatabaseTable(tableName = "LinePosition")
public class LinePosition implements Storeable {

  /**
   * Primary Key of the table.
   */
  @DatabaseField(generatedId = true)
  private Integer id;

  /**
   * Subscriber who has already read the messages.
   *
   * @throws SQLException When inserting a duplicate, the corresponding DAO will throw an exception.
   */
  @NonNull
  @DatabaseField(columnName = "participant", unique = true, foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
  private ChatParticipant participant;

  /**
   * Time of the most recently read message. Is automatically set once the entry is created.
   */
  @DatabaseField(dataType = DataType.DATE)
  private Date cursor;
}
