package de.chatsphere.io.database.schema.chat;

import static de.chatsphere.io.database.schema.chat.ChatTest.getTestChat;
import static de.chatsphere.io.database.schema.preference.enumeration.NotifiableTest.getTestNotifiable;
import static de.chatsphere.io.database.schema.preference.enumeration.VisibilityTest.getTestVisibility;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.j256.ormlite.dao.Dao;
import de.chatsphere.io.database.schema.preference.enumeration.Notifiable;
import de.chatsphere.io.database.schema.preference.enumeration.Visibility;
import de.chatsphere.io.database.schema.user.User;
import de.chatsphere.io.database.schema.user.UserTableTest;
import java.sql.SQLException;
import org.junit.After;
import org.junit.Test;

public class ChatParticipantTest extends UserTableTest {

  private Chat chat = getTestChat();
  private Notifiable notifiable = getTestNotifiable();
  private Visibility visibility = getTestVisibility();
  protected ChatParticipant chatParticipant = ChatParticipant.builder()
      .chat(chat)
      .participant(user)
      .admin(true)
      .subscription(notifiable)
      .visibility(true)
      .build();

  @After
  public void removeChatEntry() throws SQLException {
    db.getDao(Chat.class).delete(chat);
  }

  @After
  public void removeUser() throws SQLException {
    db.getDao(User.class).delete(user);
  }

  @Test
  public void testCreateAndDeleteChatParticipant() throws SQLException {
    Dao<ChatParticipant, ?> dao = db.getDao(ChatParticipant.class);
    dao.create(chatParticipant);
    assertThat(dao.queryForSameId(chatParticipant).getId(), is(equalTo(chatParticipant.getId())));
    dao.delete(chatParticipant);
    assertThat(dao.queryForSameId(chatParticipant), is(nullValue()));
  }

}
