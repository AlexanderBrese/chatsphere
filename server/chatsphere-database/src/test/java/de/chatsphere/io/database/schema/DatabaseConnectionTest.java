package de.chatsphere.io.database.schema;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.table.TableUtils;
import de.chatsphere.io.database.DatabaseTest;
import java.sql.SQLException;
import org.junit.Test;

public class DatabaseConnectionTest extends DatabaseTest {

  /*
  @Test
  public void testLinkCreate() throws SQLException {
    Link link = Link.builder()
        .url("https://start.duckduckgo.com")
        .description(String.valueOf(System.currentTimeMillis()))
        .build();

    Dao<Link, ?> dao = db.getDao(Link.class);
    try {
      dao.create(link);
    } catch (SQLException e) {
      TableUtils.createTable(dao);
      dao.create(link);
    }
    Link result = db.getDao(Link.class).queryForSameId(link);
    assertThat(dao.queryForSameId(link), is(equalTo(link)));
  }
  */
}
