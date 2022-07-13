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
 * A simple phone number.
 */
@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
@DatabaseTable(tableName = "Phone")
public class Phone implements Storeable {

  /**
   * Primary Key of the table.
   */
  @DatabaseField(generatedId = true)
  private Integer id;

  /**
   * The ITU E.164 phone number formatted semantically by the user. May store up to 32 unicode characters.
   * 
   * <p>Example: +49 (30) 71 712 71
   */
  @NonNull
  @DatabaseField
  private String number;

  /**
   * A <i>SHA-256</i> hash of the normalized phone number containing only numbers and leading zeros
   * for the identification of area codes. May store up to 32 bytes (a sha256 hash).
   *
   * @throws SQLException When inserting a duplicate, the corresponding DAO will throw an
   * exception.
   */
  @NonNull
  @DatabaseField(dataType = DataType.BYTE_ARRAY, canBeNull = false, unique = true)
  private byte[] hash;

  /**
   * The visibility of the phone number.
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
