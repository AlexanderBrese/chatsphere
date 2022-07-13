package de.chatsphere.io.database.schema.chat;

import static de.chatsphere.io.database.schema.ProfileTest.getTestProfile;
import static de.chatsphere.io.database.schema.category.CategoryTest.getTestCategory;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.j256.ormlite.dao.Dao;
import de.chatsphere.io.database.DatabaseTest;
import de.chatsphere.io.database.schema.Profile;
import de.chatsphere.io.database.schema.category.Category;
import java.sql.SQLException;
import org.junit.After;
import org.junit.Test;

public class ChatTest extends DatabaseTest {

  private Profile profile = getTestProfile();
  private Category category = getTestCategory(profile);

  public static Chat getTestChat() {
    return Chat.builder().build();
  }

  @After
  public void removeCategoryEntry() throws SQLException {
    db.getDao(Category.class).delete(category);
  }

  @After
  public void removeProfileEntry() throws SQLException {
    db.getDao(Profile.class).delete(profile);
  }

  @Test
  public void testCreateAndDeleteChat() throws SQLException {
    Chat chat = Chat.builder()
        .profile(profile)
        .category(category)
        .build();

    Dao<Chat, ?> dao = db.getDao(Chat.class);
    dao.create(chat);
    assertThat(dao.queryForSameId(chat).getId(), is(equalTo(chat.getId())));
    dao.delete(chat);
    assertThat(dao.queryForSameId(chat), is(nullValue()));
  }

}
