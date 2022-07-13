package de.chatsphere.io.database.schema.preference;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.j256.ormlite.dao.Dao;
import de.chatsphere.io.database.DatabaseTest;
import java.sql.SQLException;
import org.junit.Test;

public class LocationTest extends DatabaseTest {

  public static Location getTestLocation() {
    return Location.builder()
        .country("de")
        .province("bw").build();
  }

  @Test
  public void testCreateAndDeleteLocation() throws SQLException {
    Location location = getTestLocation();
    Dao<Location, ?> dao = db.getDao(Location.class);
    dao.create(location);
    assertThat(dao.queryForSameId(location), is(equalTo(location)));
    dao.delete(location);
    assertThat(dao.queryForSameId(location), is(nullValue()));
  }
}
