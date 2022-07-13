package de.chatsphere.io.database.schema.category;

import static de.chatsphere.io.database.schema.ProfileTest.getTestProfile;
import static de.chatsphere.io.database.schema.category.CategoryTest.getTestCategory;
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

public class CategoryPathTest extends DatabaseTest {

  private Profile profile = getTestProfile();
  private Category category = getTestCategory(profile);

  @After
  public void removeProfileEntry() throws SQLException {
    db.getDao(Profile.class).delete(profile);
  }

  @After
  public void removeCategoryEntry() throws SQLException {
    db.getDao(Category.class).delete(category);
  }

  @Test
  public void testCreateAndDeleteCategoryPath() throws SQLException {
    CategoryPath categoryPath = CategoryPath.builder()
        .ancestor(category)
        .descendant(category)
        .depth(0)
        .build();

    Dao<CategoryPath, ?> categoryPathDao = db.getDao(CategoryPath.class);
    categoryPathDao.create(categoryPath);
    assertThat(categoryPathDao.queryForSameId(categoryPath).getDepth(),
        is(equalTo(categoryPath.getDepth())));
    categoryPathDao.delete(categoryPath);
    assertThat(categoryPathDao.queryForSameId(categoryPath), is(nullValue()));
  }
}
