package de.chatsphere.io.database.schema.user;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import de.chatsphere.io.database.Storeable;
import de.chatsphere.io.database.schema.Profile;
import de.chatsphere.io.database.schema.chat.Message;
import de.chatsphere.io.database.schema.preference.Email;
import de.chatsphere.io.database.schema.preference.Password;
import de.chatsphere.io.database.schema.preference.Phone;
import java.sql.SQLException;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * A user of the application.
 */
@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
@DatabaseTable(tableName = "User")
public class User implements Storeable {

  /**
   * Primary Key of the table.
   */
  @DatabaseField(generatedId = true)
  private Integer id;

  /**
   * The Profile.
   * 
   * @throws SQLException When inserting a duplicate, the corresponding DAO will throw an exception.
   */
  @NonNull
  @DatabaseField(columnName = "profile", foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
  private Profile profile;

  /**
   * The email address.
   */
  @DatabaseField(columnName = "email", foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
  private Email email;

  /**
   * The authentication information.
   */
  @DatabaseField(columnName = "authentication", foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
  private Password authentication;

  /**
   * The phone number.
   */
  @DatabaseField(columnName = "phone", foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
  private Phone phone;

  /**
   * The status message.
   */
  @DatabaseField(columnName = "status", foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
  private Message status;

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
