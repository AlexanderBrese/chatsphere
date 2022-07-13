package de.chatsphere.io.database.schema.preference;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.j256.ormlite.dao.Dao;
import de.chatsphere.io.database.DatabaseTest;
import java.sql.SQLException;
import org.junit.Test;

public class LanguageTest extends DatabaseTest {

  public static Language getTestLanguage() {
    return Language.builder()
        .code("fr")
        .build();
  }

  @Test
  public void testCreateAndDeleleteLanguage() throws SQLException {
    Language language = getTestLanguage();
    Dao<Language, ?> dao = db.getDao(Language.class);
    dao.create(language);
    assertThat(dao.queryForSameId(language), is(equalTo(language)));
    dao.delete(language);
    assertThat(dao.queryForSameId(language), is(nullValue()));
  }

}
