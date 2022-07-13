package de.chatsphere.io.database.schema.user;

import static de.chatsphere.io.database.schema.ProfileTest.getTestProfile;
import static de.chatsphere.io.database.schema.chat.MessageTest.getTestMessage;
import static de.chatsphere.io.database.schema.preference.EmailTest.getTestEmail;
import static de.chatsphere.io.database.schema.preference.PasswordTest.getTestPassword;
import static de.chatsphere.io.database.schema.preference.PhoneTest.getTestPhone;
import static de.chatsphere.io.database.schema.preference.enumeration.VisibilityTest.getTestVisibility;

import de.chatsphere.io.database.DatabaseTest;
import de.chatsphere.io.database.schema.Profile;
import de.chatsphere.io.database.schema.chat.Message;
import de.chatsphere.io.database.schema.preference.Email;
import de.chatsphere.io.database.schema.preference.Password;
import de.chatsphere.io.database.schema.preference.Phone;
import de.chatsphere.io.database.schema.preference.enumeration.Visibility;
import java.sql.SQLException;
import org.junit.After;

public abstract class UserTableTest extends DatabaseTest {

  private Visibility visibility = getTestVisibility();
  private Profile profile = getTestProfile();
  private Email email = getTestEmail(visibility);
  private Password password = getTestPassword();
  private Phone phone = getTestPhone(visibility);
  private Message message = getTestMessage();
  protected User user = getTestUser(profile, email, password, phone, message);

  private User getTestUser(Profile profile, Email email, Password password, Phone phone,
      Message message) {
    return User.builder()
        .profile(profile)
        .email(email)
        .authentication(password)
        .phone(phone)
        .status(message)
        .build();
  }

  @After
  public void removeMessageEntry() throws SQLException {
    db.getDao(Message.class).delete(message);
  }

  @After
  public void removePhoneEntry() throws SQLException {
    db.getDao(Phone.class).delete(phone);
  }

  @After
  public void removePasswordEntry() throws SQLException {
    db.getDao(Password.class).delete(password);
  }

  @After
  public void removeEmailEntry() throws SQLException {
    db.getDao(Email.class).delete(email);
  }

  @After
  public void removeProfileEntry() throws SQLException {
    db.getDao(Profile.class).delete(profile);
  }

}
