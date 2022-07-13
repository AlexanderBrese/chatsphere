package de.chatsphere.io.database.schema.preference;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import de.chatsphere.io.database.Storeable;
import de.chatsphere.io.database.schema.preference.enumeration.Visibility;
import java.sql.SQLException;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * A simple mail address.
 */
@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
@DatabaseTable(tableName = "Email")
public class Email implements Storeable {

  /**
   * Primary Key of the table.
   */
  @DatabaseField(generatedId = true)
  private Integer id;

  /**
   * The full email address. May store up to 512 ascii characters.
   * 
   * <p>Use the {@link #hash} attribute for searching of an mail address.
   *
   * @throws SQLException When inserting a duplicate, the corresponding DAO will throw an exception.
   */
  @NonNull
  @DatabaseField(unique = true)
  private String mail;

  /**
   * A <i>SHA256</i> hash of the <i>lower case</i> {@link #mail} address. May store up to 32 bytes (a sha256 hash).
   * 
   * <p>This attribute is suitable for searching by address.
   * 
   * @throws SQLException When inserting a duplicate, the corresponding DAO will throw an exception.
   */
  @NonNull
  @DatabaseField(dataType = DataType.BYTE_ARRAY, canBeNull = false, unique = true)
  private byte[] hash;

  /**
   * Indicates whether the email address has been successfully verified.
   */
  @DatabaseField
  private Boolean verified;

  /**
   * The visibility of the address.
   */
  @NonNull
  @DatabaseField(columnName = "visibility", foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
  private Visibility visibility;

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
