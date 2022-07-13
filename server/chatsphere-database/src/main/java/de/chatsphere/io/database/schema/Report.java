package de.chatsphere.io.database.schema;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import de.chatsphere.io.database.Storeable;
import de.chatsphere.io.database.schema.chat.ChatLine;
import de.chatsphere.io.database.schema.chat.Message;
import de.chatsphere.io.database.schema.user.User;
import java.sql.SQLException;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Marks messages for review by moderators with reasons given by the reporting user.
 */
@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
@DatabaseTable(tableName = "Report")
public class Report implements Storeable {

  /**
   * Primary Key of the table.
   */
  @DatabaseField(generatedId = true)
  private Integer id;

  /**
   * The chat entry to review.
   *
   * @throws SQLException When inserting a duplicate {@link #line}×{@link #reporter} pair, the
   * corresponding DAO will throw an exception. Either {@link #line} xor {@link #reported} has to be NOT NULL.
   */
  @DatabaseField(columnName = "line", uniqueCombo = true, foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
  private ChatLine line;

  /**
   * The reporter who wants a review.
   *
   * @throws SQLException When inserting a duplicate {@link #line}×{@link #reporter} pair, the
   * corresponding DAO will throw an exception.
   */
  @NonNull
  @DatabaseField(columnName = "reporter", uniqueCombo = true, foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
  private User reporter;

  /**
   * The reported user. /F0520/ requires that a user can be reported
   * without associated chat line.
   *
   * @throws SQLException When inserting a duplicate {@link #line}×{@link #reporter} pair, the
   * corresponding DAO will throw an exception. Either {@link #line} xor {@link #reported} has to be NOT NULL.
   */
  @DatabaseField(columnName = "reported", uniqueCombo = true, foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
  private User reported;

  /**
   * The reason for the report.
   */
  @NonNull
  @DatabaseField(columnName = "reason", foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
  private Message reason;

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
