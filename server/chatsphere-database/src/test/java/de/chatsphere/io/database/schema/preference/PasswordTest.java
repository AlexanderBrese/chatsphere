package de.chatsphere.io.database.schema.preference;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.j256.ormlite.dao.Dao;
import de.chatsphere.io.database.DatabaseTest;
import java.sql.SQLException;
import org.junit.Test;

public class PasswordTest extends DatabaseTest {

  public static Password getTestPassword() {
    return Password.builder()
        .hash("$argon2i$v=19$m=65536,t=11,p=1$ZM4gr8iHTFHrS6KpWQUQ5Q$"
            + "tcK/oCg82RnAJUBqS9lLOZQx/9XAyfZbmiLtxCoexAU")
        .build();
  }

  @Test
  public void testCreateAndDeletePassword() throws SQLException {
    Password password = getTestPassword();
    Dao<Password, ?> dao = db.getDao(Password.class);
    dao.create(password);
    assertThat(dao.queryForSameId(password).getId(), is(equalTo(password.getId())));
    dao.delete(password);
    assertThat(dao.queryForSameId(password), is(nullValue()));
  }
}
