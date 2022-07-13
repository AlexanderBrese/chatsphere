package de.chatsphere.io.database.schema.chat;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.j256.ormlite.dao.Dao;
import de.chatsphere.io.database.DatabaseTest;
import java.sql.SQLException;
import org.junit.Test;

public class MessageTest extends DatabaseTest {

  public static Message getTestMessage() {
    return Message.builder()
        .content("Simple Message")
        .markup(false)
        .build();
  }

  @Test
  public void testCreateAndDeleteMessage() throws SQLException {
    Message message = getTestMessage();
    Dao<Message, ?> dao = db.getDao(Message.class);
    dao.create(message);
    assertThat(dao.queryForSameId(message).getId(), is(equalTo(message.getId())));
    dao.delete(message);
    assertThat(dao.queryForSameId(message), is(nullValue()));
  }

  // TODO: Test maximum message length
}
