package de.chatsphere.io.database.schema.category;

import static de.chatsphere.io.database.schema.ProfileTest.getTestProfile;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.j256.ormlite.dao.Dao;
import de.chatsphere.io.database.DatabaseTest;
import de.chatsphere.io.database.schema.Profile;
import java.sql.SQLException;
import org.junit.After;
import org.junit.Test;

public class CategoryTest extends DatabaseTest {

  private Profile profile = getTestProfile();

  public static Category getTestCategory(Profile testProfile) {
    return Category.builder()
        .profile(testProfile)
        .build();
  }

  @After
  public void removeProfileEntry() throws SQLException {
    db.getDao(Profile.class).delete(profile);
  }

  @Test
  public void createCategory() throws SQLException {
    Category category = Category.builder()
        .profile(profile)
        .build();
    Dao<Category, ?> categoryDao = db.getDao(Category.class);
    categoryDao.create(category);
    assertThat(categoryDao.queryForSameId(category).getId(), is(equalTo(category.getId())));
    categoryDao.delete(category);
    assertThat(categoryDao.queryForSameId(category), is(nullValue()));
  }

}
