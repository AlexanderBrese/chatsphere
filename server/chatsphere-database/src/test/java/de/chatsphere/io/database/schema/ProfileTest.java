package de.chatsphere.io.database.schema;

import static de.chatsphere.io.database.schema.preference.ColorTest.getTestColor;
import static de.chatsphere.io.database.schema.preference.FileTest.getTestFile;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.j256.ormlite.dao.Dao;
import de.chatsphere.io.database.DatabaseTest;
import de.chatsphere.io.database.schema.preference.Color;
import de.chatsphere.io.database.schema.preference.File;
import java.sql.SQLException;
import org.junit.After;
import org.junit.Test;

public class ProfileTest extends DatabaseTest {

  private File file = getTestFile();
  private Color color = getTestColor();

  public static Profile getTestProfile() {
    return Profile.builder()
        .name("UnitTestProfile")
        .displayName(testString)
        .build();
  }

  @After
  public void removeFileEntry() throws SQLException {
    db.getDao(File.class).delete(file);
  }

  @After
  public void removeColorEntry() throws SQLException {
    db.getDao(Color.class).delete(color);
  }

  @Test
  public void createAndDeleteProfile() throws SQLException {
    Profile profile = Profile.builder()
        .name("ProfileTestUnit.4")
        .displayName(testString)
        .backgroundColor(color)
        .icon(file)
        .build();

    Dao<Profile, ?> dao = db.getDao(Profile.class);
    dao.create(profile);
    assertThat(dao.queryForSameId(profile), is(equalTo(profile)));
    dao.delete(profile);
    assertThat(dao.queryForSameId(profile), is(nullValue()));
  }
}
