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

public class UserRelationshipTest extends UserTableTest {

  @After
  public void removeUser() throws SQLException {
    db.getDao(User.class).delete(user);
  }

  @Test
  public void testCreateAndDeleteUserRelationship() throws SQLException {
    UserRelationship userRelationship = UserRelationship.builder()
        .owner(user)
        .refers(user)
        .petName("Ich")
        .blocked(new GregorianCalendar(2020, 2, 29, 23, 59, 59).getTime())
        .build();

    Dao<UserRelationship, ?> dao = db.getDao(UserRelationship.class);
    dao.create(userRelationship);
    assertThat(dao.queryForSameId(userRelationship).getId(), is(equalTo(userRelationship.getId())));
    dao.delete(userRelationship);
    assertThat(dao.queryForSameId(userRelationship), is(nullValue()));
  }

}
