package de.chatsphere.io.database.schema;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import de.chatsphere.io.database.Storeable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Test object that is used for examples and basic checks of the database connection.
 *
 * <p>Not suitable for use in production environments.
 */
@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
@DatabaseTable(tableName = "links")
public class Link implements Storeable {

  /**
   * Primary Key of the table.
   */
  @DatabaseField(generatedId = true)
  private Integer id;

  /**
   * URL that is referenced.
   */
  @DatabaseField
  private String url;

  /**
   * Description text of the link.
   */
  @DatabaseField
  private String description;
}
