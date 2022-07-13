package de.chatsphere.io.database.schema.preference.enumeration;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.j256.ormlite.dao.Dao;
import de.chatsphere.io.database.DatabaseTest;
import java.sql.SQLException;
import org.junit.Ignore;
import org.junit.Test;

public class VisibilityTest extends DatabaseTest {

  public static Visibility getTestVisibility() {
    Visibility element;
    try {
      Dao<Visibility, ?> dao = db.getDao(Visibility.class);
      element = dao.queryForEq("description", Visibility.Level.INHERIT).get(0);
    } catch (IndexOutOfBoundsException | SQLException e) {
      element = Visibility.builder()
        .description(Visibility.Level.INHERIT)
        .build();
    }
    return element;
  }

  @Ignore
  @Test
  public void testCreateAndDeleteVisibility() throws SQLException {
    Visibility visibility = getTestVisibility();
    Dao<Visibility, ?> dao = db.getDao(Visibility.class);
    dao.create(visibility);
    assertThat(dao.queryForSameId(visibility), is(equalTo(visibility)));
    dao.delete(visibility);
    assertThat(dao.queryForSameId(visibility), is(nullValue()));
  }
}
