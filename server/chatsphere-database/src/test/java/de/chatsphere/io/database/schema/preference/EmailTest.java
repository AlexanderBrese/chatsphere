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

public class EmailTest extends DatabaseTest {

  private Visibility visibility = getTestVisibility();

  public static Email getTestEmail(Visibility visibility) {
    return Email.builder()
        .mail("max.mustermann@beispiel.de")
        .hash(validSha256)
        .visibility(visibility)
        .build();
  }

  @Test
  public void testCreateAndDeleteEmail() throws SQLException {
    Email email = getTestEmail(visibility);
    Dao<Email, ?> dao = db.getDao(Email.class);
    dao.create(email);
    assertThat(dao.queryForSameId(email).getId(), is(equalTo(email.getId())));
    dao.delete(email);
    assertThat(dao.queryForSameId(email), is(nullValue()));
  }
}
