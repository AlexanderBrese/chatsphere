package de.chatsphere.io.database.schema.preference;

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
 * Stores the Verification Codes of an email.
 */
@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
@DatabaseTable(tableName = "EmailVerification")
public class EmailVerification implements Storeable {

  /**
   * Primary Key of the table.
   */
  @DatabaseField(generatedId = true)
  private Integer id;

  /**
   * Email to verify.
   *
   * @throws SQLException When inserting a duplicate, the corresponding DAO will throw an exception.
   */
  @NonNull
  @DatabaseField(columnName = "email", unique = true, foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
  private Email email;

  /**
   * 32 characters verification token. May store up to 32 unicode characters.
   */
  @NonNull
  @DatabaseField
  private String token;

  /**
   * Lifetime of the token in seconds.
   */
  @NonNull
  @DatabaseField
  private Integer lifetime;

  /**
   * Time the entry was created (automatically set by the database).
   */
  @DatabaseField(dataType = DataType.DATE)
  private Date createdAt;
}
