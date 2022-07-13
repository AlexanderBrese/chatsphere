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
 * <a href="https://en.wikipedia.org/wiki/HSL_and_HSV">HSLA Color Values</a> are an alternative
 * representation of colors of the <a href="https://en.wikipedia.org/wiki/RGB_color_model">RGBA Color
 * Model</a>. Whenever a duplicate is about to be inserted the DAO will throw a {@link java.sql.SQLException}.
 */
@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
@DatabaseTable(tableName = "Color")
public class Color implements Storeable {

  /**
   * Primary Key of the table.
   */
  @DatabaseField(generatedId = true)
  private Integer id;

  /**
   * Hue value between 0° and 360° as {@link Integer}.
   * 
   * @throws SQLException When inserting a duplicate {@link #hue}×{@link #saturation}×{@link #lightness}×{@link #alpha}
   * tuple, the corresponding DAO will throw an exception.
   */
  @NonNull
  @DatabaseField(uniqueCombo = true)
  private Integer hue;

  /**
   * Saturation value between 0% (gray) and 100% as {@link Integer}.
   * 
   * @throws SQLException When inserting a duplicate {@link #hue}×{@link #saturation}×{@link #lightness}×{@link #alpha}
   * tuple, the corresponding DAO will throw an exception.
   */
  @NonNull
  @DatabaseField(uniqueCombo = true)
  private Integer saturation;

  /**
   * Lightness value between 0% (black) and 100% (white) as {@link Integer}.
   * 
   * @throws SQLException When inserting a duplicate {@link #hue}×{@link #saturation}×{@link #lightness}×{@link #alpha}
   * tuple, the corresponding DAO will throw an exception.
   */
  @NonNull
  @DatabaseField(uniqueCombo = true)
  private Integer lightness;

  /**
   * Alpha value between 0% and 100% (transparent) as {@link Integer}.
   * 
   * @throws SQLException When inserting a duplicate {@link #hue}×{@link #saturation}×{@link #lightness}×{@link #alpha}
   * tuple, the corresponding DAO will throw an exception.
   */
  @NonNull
  @DatabaseField(uniqueCombo = true)
  private Integer alpha;
}
