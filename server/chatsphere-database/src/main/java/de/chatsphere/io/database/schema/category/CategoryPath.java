package de.chatsphere.io.database.schema.category;

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
 * A close table that stores a tree structure of categories.
 */
@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
@DatabaseTable(tableName = "CategoryPath")
public class CategoryPath implements Storeable {

  /**
   * Primary Key of the table.
   */
  @DatabaseField(generatedId = true)
  private Integer id;

  /**
   * The parent category called <i>ancestor</i>.
   *
   * @throws SQLException When inserting a duplicate {@link #ancestor}x{@link #descendant} pair, the
   * corresponding DAO will throw an exception.
   */
  @NonNull
  @DatabaseField(columnName = "ancestor", uniqueCombo = true, foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
  private Category ancestor;

  /**
   * The child category called <i>descendant</i>.
   *
   * @throws SQLException When inserting a duplicate {@link #ancestor}x{@link #descendant} pair, the
   * corresponding DAO will throw an exception.
   */
  @NonNull
  @DatabaseField(columnName = "descendant", uniqueCombo = true, foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
  private Category descendant;

  /**
   * Depth level of the entry measured from the root.
   */
  @NonNull
  @DatabaseField
  private Integer depth;
}
