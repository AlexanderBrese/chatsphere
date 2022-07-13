package de.chatsphere.io.database.schema.user;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.j256.ormlite.dao.Dao;
import java.sql.SQLException;
import org.junit.Test;

public class UserTest extends UserTableTest {

  @Test
  public void testCreateAndDeleteUser() throws SQLException {
    Dao<User, ?> dao = db.getDao(User.class);
    dao.create(user);
    assertThat(dao.queryForSameId(user).getId(), is(equalTo(user.getId())));
    dao.delete(user);
    assertThat(dao.queryForSameId(user), is(nullValue()));
  }

}
