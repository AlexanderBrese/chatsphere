package de.chatsphere.io.database.schema.preference;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.j256.ormlite.dao.Dao;
import de.chatsphere.io.database.DatabaseTest;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import org.junit.Test;

public class FileTest extends DatabaseTest {

  public static File getTestFile() {
    return File.builder()
        .name(testString + ".png")
        .size(67108864)
        .hash(validSha256)
        .type("image/png")
        .createdAt(new GregorianCalendar(2020, 2, 29, 23, 59, 59).getTime())
        .build();
  }

  @Test
  public void createAndDeleteColor() throws SQLException {
    File file = getTestFile();
    Dao<File, ?> dao = db.getDao(File.class);
    dao.create(file);
    assertThat(dao.queryForSameId(file), is(equalTo(file)));
    dao.delete(file);
    assertThat(dao.queryForSameId(file), is(nullValue()));
  }
}
