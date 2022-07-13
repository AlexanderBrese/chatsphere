package de.chatsphere.io.database.schema.preference;

import static de.chatsphere.io.database.schema.preference.enumeration.VisibilityTest.getTestVisibility;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.j256.ormlite.dao.Dao;
import de.chatsphere.io.database.DatabaseTest;
import de.chatsphere.io.database.schema.preference.enumeration.Visibility;
import java.sql.SQLException;
import org.junit.After;
import org.junit.Test;

public class PhoneTest extends DatabaseTest {

  private Visibility visibility = getTestVisibility();

  public static Phone getTestPhone(Visibility visibility) {
    return Phone.builder()
        .number("+49 (30) 71 712 71")
        .hash(validSha256)
        .visibility(visibility)
        .build();
  }

  @Test
  public void testCreateAndDeleteVisibility() throws SQLException {
    Phone phone = getTestPhone(visibility);
    Dao<Phone, ?> dao = db.getDao(Phone.class);
    dao.create(phone);
    assertThat(dao.queryForSameId(phone).getId(), is(equalTo(phone.getId())));
    dao.delete(phone);
    assertThat(dao.queryForSameId(phone), is(nullValue()));
  }

}
