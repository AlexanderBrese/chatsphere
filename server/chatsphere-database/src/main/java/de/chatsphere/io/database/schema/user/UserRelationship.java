package de.chatsphere.io.database.schema.user;

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
 * Stores information about the relationship between users.
 */
@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
@DatabaseTable(tableName = "UserRelationship")
public class UserRelationship implements Storeable {

  /**
   * Primary Key of the table.
   */
  @DatabaseField(generatedId = true)
  private Integer id;

  /**
   * Owner of the entry.
   *
   * @throws SQLException When inserting a duplicate {@link #owner}×{@link #refers} pair, the corresponding DAO will
   * throw an exception.
   */
  @NonNull
  @DatabaseField(columnName = "owner", foreign = true, uniqueCombo = true, foreignAutoCreate = true, foreignAutoRefresh = true)
  private User owner;

  /**
   * User to whom the entry refers.
   *
   * @throws SQLException When inserting a duplicate {@link #owner}×{@link #refers} pair, the corresponding DAO will
   * throw an exception.
   */
  @NonNull
  @DatabaseField(columnName = "refers", foreign = true, foreignAutoRefresh = true, uniqueCombo = true, maxForeignAutoRefreshLevel = 3)
  private User refers;

  /**
   * The pet name with whitespaces, markup and formatting, which overwrites the name from the user
   * profile {@link #refers} for the user  {@link #owner}. May store up to 256 unicode characters.
   *
   * @see de.chatsphere.io.database.schema.Profile#displayName
   */
  @DatabaseField
  private String petName;

  /**
   * Not set if the user is a contact. Otherwise the time since which {@link #owner} ignores the
   * user {@link #refers}.
   */
  @DatabaseField(dataType = DataType.DATE)
  private Date blocked;

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
