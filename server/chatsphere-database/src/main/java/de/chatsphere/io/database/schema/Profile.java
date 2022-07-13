package de.chatsphere.io.database.schema;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import de.chatsphere.io.database.Storeable;
import de.chatsphere.io.database.schema.preference.Color;
import de.chatsphere.io.database.schema.preference.File;
import java.sql.SQLException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * A <i>Profile</i> or <i>Stamp</i> encapsulates the searchable elements as well as the information
 * and attributes of various complex elements displayed in a list.
 */
@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
@DatabaseTable(tableName = "Profile")
public class Profile implements Storeable {

  /**
   * Primary Key of the table.
   */
  @DatabaseField(generatedId = true)
  private Integer id;

  /**
   * Unique human readable identifier. May store up to 32 unicode characters.
   *
   * @throws SQLException When inserting a duplicate, the corresponding DAO will throw an exception.
   */
  @NonNull
  @DatabaseField(unique = true)
  private String name;

  /**
   * The {@link #name} attribute with whitespaces, markup and formatting applied. May store up to 256 unicode
   * characters.
   */
  @NonNull
  @DatabaseField
  private String displayName;

  /**
   * Background color of the entry.
   */
  @DatabaseField(columnName = "backgroundColor", foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
  private Color backgroundColor;

  /**
   * Avatar of the Profile.
   */
  @DatabaseField(columnName = "icon", foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
  private File icon;
}
