package de.chatsphere.io.database.schema.preference;

import static de.chatsphere.io.database.schema.preference.EmailTest.getTestEmail;
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

public class EmailVerificationTest extends DatabaseTest {

  private Visibility visibility = getTestVisibility();
  private Email email = getTestEmail(visibility);

  public static EmailVerification getTestEmailVerification(Email email) {
    return EmailVerification.builder()
        .email(email)
        .lifetime(604800)
        .token("rq4FjEu3a1GPTEu8crZa6SLl3o4PXN6U\n")
        .build();
  }

  @After
  public void removeEmailEntry() throws SQLException {
    db.getDao(Email.class).delete(email);
  }

  @Test
  public void testCreateAndDeleteEmailVerification() throws SQLException {
    EmailVerification emailVerification = getTestEmailVerification(email);
    Dao<EmailVerification, ?> dao = db.getDao(EmailVerification.class);
    dao.create(emailVerification);
    assertThat(dao.queryForSameId(emailVerification).getId(),
        is(equalTo(emailVerification.getId())));
    dao.delete(emailVerification);
    assertThat(dao.queryForSameId(emailVerification), is(nullValue()));
  }

}
