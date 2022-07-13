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
 * Stores the Location.
 */
@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
@DatabaseTable(tableName = "Location")
public class Location implements Storeable {

  /**
   * Primary Key of the table.
   */
  @DatabaseField(generatedId = true)
  private Integer id;

  /**
   * ISO 3166-1 alpha 2 country code. May store up to 2 unicode characters.
   *
   * @throws SQLException When inserting a duplicate {@link #country}×{@link #province} pair, the
   * corresponding DAO will throw an exception.
   */
  @NonNull
  @DatabaseField(uniqueCombo = true)
  private String country;

  /**
   * ISO 3166-2 province code. May store up to 3 unicode characters.
   *
   * @throws SQLException When inserting a duplicate {@link #country}×{@link #province} pair, the corresponding DAO will
   * throw an exception.
   */
  @NonNull
  @DatabaseField(uniqueCombo = true)
  private String province;
}
