package de.chatsphere.io.database.schema.preference;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import de.chatsphere.io.database.Storeable;
import java.sql.SQLException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Stores the language.
 */
@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
@DatabaseTable(tableName = "Language")
public class Language implements Storeable {

  /**
   * Primary Key of the table.
   */
  @DatabaseField(generatedId = true)
  private Integer id;

  /**
   * Two Letter ISO 639-1 language code. May store up to 2 unicode characters.
   *
   * @throws SQLException When inserting a duplicate, the corresponding DAO will throw an exception.
   */
  @NonNull
  @DatabaseField(unique = true)
  private String code;
}
