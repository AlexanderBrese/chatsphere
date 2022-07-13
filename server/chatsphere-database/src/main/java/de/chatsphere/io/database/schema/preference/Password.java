package de.chatsphere.io.database.schema.preference;

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
 * Password storage with <a href="https://en.wikipedia.org/wiki/Argon2">Argon2</a>
 * key-derivation-function hashes.
 */
@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
@DatabaseTable(tableName = "Password")
public class Password implements Storeable {

  /**
   * Primary Key of the table.
   */
  @DatabaseField(generatedId = true)
  private Integer id;

  /**
   * Complete <a href="https://en.wikipedia.org/wiki/Argon2">Argon2</a> hash string of the password. This string
   * includes all data needed to verify the password, including type, version, memory, lanes, passes, salt and hash. May
   * store up to 256 unicode characters.
   */
  @NonNull
  @DatabaseField
  private String hash;

  /**
   * Time the entry was updated (automatically maintained by the database).
   */
  @DatabaseField(dataType = DataType.DATE)
  private Date updatedAt;
}
