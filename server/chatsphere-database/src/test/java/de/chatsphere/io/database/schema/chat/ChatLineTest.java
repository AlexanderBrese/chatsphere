package de.chatsphere.io.database.schema.chat;

import static de.chatsphere.io.database.schema.chat.ChatTest.getTestChat;
import static de.chatsphere.io.database.schema.chat.MessageTest.getTestMessage;
import static de.chatsphere.io.database.schema.preference.FileTest.getTestFile;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.j256.ormlite.dao.Dao;
import de.chatsphere.io.database.schema.preference.File;
import de.chatsphere.io.database.schema.user.User;
import de.chatsphere.io.database.schema.user.UserTableTest;
import java.sql.SQLException;
import org.junit.After;
import org.junit.Test;

public class ChatLineTest extends UserTableTest {

  private Chat chat = getTestChat();
  private File asset = getTestFile();
  private Message message = getTestMessage();

  public static ChatLine getTestChatLine(User user, Chat chat, Message message) {
    return ChatLine.builder()
        .author(user)
        .chat(chat)
        .message(message)
        .build();
  }

  @After
  public void removeAssetEntry() throws SQLException {
    db.getDao(File.class).delete(asset);
  }

  @After
  public void removeChatEntry() throws SQLException {
    db.getDao(Chat.class).delete(chat);
  }

  @After
  public void removeMessageEntry() throws SQLException {
    db.getDao(Message.class).delete(message);
  }

  @After
  public void removeUser() throws SQLException {
    db.getDao(User.class).delete(user);
  }

  @Test
  public void testCreateAndDeleteChatLine() throws SQLException {
    ChatLine chatLine = ChatLine.builder()
        .author(user)
        .chat(chat)
        .asset(asset)
        .message(message)
        .build();

    Dao<ChatLine, ?> dao = db.getDao(ChatLine.class);
    dao.create(chatLine);
    assertThat(dao.queryForSameId(chatLine).getId(), is(equalTo(chatLine.getId())));
    dao.delete(chatLine);
    assertThat(dao.queryForSameId(chatLine), is(nullValue()));
  }

}
