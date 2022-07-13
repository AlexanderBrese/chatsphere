package de.chatsphere.io.database.schema.user;

import static de.chatsphere.io.database.schema.preference.LanguageTest.getTestLanguage;
import static de.chatsphere.io.database.schema.preference.LocationTest.getTestLocation;
import static de.chatsphere.io.database.schema.preference.enumeration.NotifiableTest.getTestNotifiable;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.j256.ormlite.dao.Dao;
import de.chatsphere.io.database.schema.preference.Language;
import de.chatsphere.io.database.schema.preference.Location;
import de.chatsphere.io.database.schema.preference.enumeration.Notifiable;
import java.sql.SQLException;
import org.junit.After;
import org.junit.Test;

public class UserPreferencesTest extends UserTableTest {

  private Language language = getTestLanguage();
  private Location location = getTestLocation();
  private Notifiable notifiable = getTestNotifiable();

  @After
  public void removeLanguageEntry() throws SQLException {
    db.getDao(Language.class).delete(language);
  }

  @After
  public void removeLocationEntry() throws SQLException {
    db.getDao(Location.class).delete(location);
  }

  @After
  public void removeUserEntry() throws SQLException {
    db.getDao(User.class).delete(user);
  }

  @Test
  public void testCreateAndDeletePreferences() throws SQLException {
    UserPreferences userPreferences = UserPreferences.builder()
        .user(user)
        .language(language)
        .location(location)
        .notification(notifiable)
        .linkPreview(true)
        .retention(365)
        .build();

    Dao<UserPreferences, ?> dao = db.getDao(UserPreferences.class);
    dao.create(userPreferences);
    assertThat(dao.queryForSameId(userPreferences).getId(), is(equalTo(userPreferences.getId())));
    dao.delete(userPreferences);
    assertThat(dao.queryForSameId(userPreferences), is(nullValue()));
  }

}
