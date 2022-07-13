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
 * A database file table entry is an asset of stored meta data.
 */
@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
@DatabaseTable(tableName = "File")
public class File implements Storeable {

  /**
   * Primary Key of the table.
   */
  @DatabaseField(generatedId = true)
  private Integer id;

  /**
   * The original file name of the file as it was sent with the upload. May store up to 256 unicode characters.
   */
  @NonNull
  @DatabaseField
  private String name;

  /**
   * An <a href="https://tools.ietf.org/html/rfc4288">RFC 4288 Mime Type</a>. May store up to 127 unicode characters.
   * @see <a href="https://www.iana.org/assignments/media-types/media-types.xhtml">IANA Mime Type Registry</a>
   */
  @NonNull
  @DatabaseField(canBeNull = false)
  private String type;

  /**
   * The file size in bytes. Valid range from 0 B (inclusive) up to 2 GiB (exclusive).
   */
  @NonNull
  @DatabaseField(canBeNull = false)
  private Integer size;

  /**
   * A SHA-256 hash of the file content. May store up to 32 bytes (a sha256 hash).
   */
  @NonNull
  @DatabaseField(dataType = DataType.BYTE_ARRAY, canBeNull = false)
  private byte[] hash;

  /**
   * Time the entry was created (automatically set by the database).
   */
  @DatabaseField(dataType = DataType.DATE_STRING)
  private Date createdAt;
}
