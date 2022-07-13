package de.chatsphere.io.database.schema.preference;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.j256.ormlite.dao.Dao;
import de.chatsphere.io.database.DatabaseTest;
import java.sql.SQLException;
import org.junit.Test;

public class ColorTest extends DatabaseTest {

  public static Color getTestColor() {
    return Color.builder()
        .hue(264)
        .saturation(64)
        .lightness(65)
        .alpha(66).build();
  }

  @Test
  public void createAndDeleteColor() throws SQLException {
    Color color = getTestColor();
    Dao<Color, ?> dao = db.getDao(Color.class);
    dao.create(color);
    assertThat(dao.queryForSameId(color), is(equalTo(color)));
    dao.delete(color);
    assertThat(dao.queryForSameId(color), is(nullValue()));
  }
}
