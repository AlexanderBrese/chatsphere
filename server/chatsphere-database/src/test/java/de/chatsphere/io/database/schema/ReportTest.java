package de.chatsphere.io.database.schema;

import static de.chatsphere.io.database.schema.chat.ChatLineTest.getTestChatLine;
import static de.chatsphere.io.database.schema.chat.ChatTest.getTestChat;
import static de.chatsphere.io.database.schema.chat.MessageTest.getTestMessage;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.j256.ormlite.dao.Dao;
import de.chatsphere.io.database.schema.chat.Chat;
import de.chatsphere.io.database.schema.chat.ChatLine;
import de.chatsphere.io.database.schema.chat.Message;
import de.chatsphere.io.database.schema.user.User;
import de.chatsphere.io.database.schema.user.UserTableTest;
import java.sql.SQLException;
import org.junit.After;
import org.junit.Test;

public class ReportTest extends UserTableTest {

  private Message reason = getTestMessage();
  private Chat chat = getTestChat();
  private ChatLine line = getTestChatLine(user, chat, reason);

  @After
  public void removeChatLine() throws SQLException {
    db.getDao(ChatLine.class).delete(line);
  }

  @After
  public void removeUser() throws SQLException {
    db.getDao(User.class).delete(user);
  }

  @After
  public void removeChat() throws SQLException {
    db.getDao(Chat.class).delete(chat);
  }

  @After
  public void removeReason() throws SQLException {
    db.getDao(Message.class).delete(reason);
  }

  @Test
  public void testCreateAndDeleteReport() throws SQLException {
    Report report = Report.builder()
        .reporter(user)
        .reported(user)
        .reason(reason)
        .line(line)
        .build();

    Dao<Report, ?> dao = db.getDao(Report.class);
    dao.create(report);
    assertThat(dao.queryForSameId(report).getId(), is(equalTo(report.getId())));
    dao.delete(report);
    assertThat(dao.queryForSameId(report), is(nullValue()));
  }
}
