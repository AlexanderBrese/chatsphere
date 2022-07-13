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

public class NotifiableTest extends DatabaseTest {

  public static Notifiable getTestNotifiable() {
    Notifiable element;
    try {
      Dao<Notifiable, ?> dao = db.getDao(Notifiable.class);
      element = dao.queryForEq("description", Notifiable.Level.INHERIT).get(0);
    } catch (IndexOutOfBoundsException | SQLException e) {
      element = Notifiable.builder()
        .description(Notifiable.Level.INHERIT)
        .build();
    }
    return element;
  }

  @Ignore
  @Test
  public void testCreateAndDeleteVisibility() throws SQLException {
    Notifiable notifiable = getTestNotifiable();
    Dao<Notifiable, ?> dao = db.getDao(Notifiable.class);
    dao.create(notifiable);
    assertThat(dao.queryForSameId(notifiable), is(equalTo(notifiable)));
    dao.delete(notifiable);
    assertThat(dao.queryForSameId(notifiable), is(nullValue()));
  }
}
