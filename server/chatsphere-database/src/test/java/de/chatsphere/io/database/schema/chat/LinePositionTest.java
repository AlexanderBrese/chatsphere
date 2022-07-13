package de.chatsphere.io.database.schema.chat;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.j256.ormlite.dao.Dao;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import org.junit.After;
import org.junit.Test;

public class LinePositionTest extends ChatParticipantTest {

  @After
  public void removeChatParticipantEntry() throws SQLException {
    db.getDao(ChatParticipant.class).delete(chatParticipant);
  }

  @Test
  public void testCreateAndDeleteChatParticipant() throws SQLException {
    LinePosition linePosition = LinePosition.builder()
        .participant(chatParticipant)
        .cursor(new GregorianCalendar(2020, 2, 29, 23, 59, 59).getTime())
        .build();

    Dao<LinePosition, ?> dao = db.getDao(LinePosition.class);
    dao.create(linePosition);
    assertThat(dao.queryForSameId(linePosition).getId(), is(equalTo(linePosition.getId())));
    dao.delete(linePosition);
    assertThat(dao.queryForSameId(linePosition), is(nullValue()));
  }

}
