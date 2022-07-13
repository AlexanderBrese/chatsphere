package de.chatsphere.io.database.schema.user;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.j256.ormlite.dao.Dao;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import org.junit.After;
import org.junit.Test;

public class UserLegalTest extends UserTableTest {

  @After
  public void removeUserEntry() throws SQLException {
    db.getDao(User.class).delete(user);
  }

  @Test
  public void testCreateAndDeleteUserLegal() throws SQLException {
    UserLegal userLegal = UserLegal.builder()
        .user(user)
        .agb(new GregorianCalendar(2020, 2, 29, 23, 59, 59).getTime())
        .privacy(new GregorianCalendar(2020, 2, 29, 23, 59, 59).getTime())
        .locked(new GregorianCalendar(2020, 2, 29, 23, 59, 59).getTime())
        .build();

    Dao<UserLegal, ?> dao = db.getDao(UserLegal.class);
    dao.create(userLegal);
    assertThat(dao.queryForSameId(userLegal).getId(), is(equalTo(userLegal.getId())));
    dao.delete(userLegal);
    assertThat(dao.queryForSameId(userLegal), is(nullValue()));
  }

}
