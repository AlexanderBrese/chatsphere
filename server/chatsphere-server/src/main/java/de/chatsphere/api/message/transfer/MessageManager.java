package de.chatsphere.api.message.transfer;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import de.chatsphere.api.chat.transfer.AbstractChat;
import de.chatsphere.api.privatechat.transfer.PrivateChatDto;
import de.chatsphere.api.shared.model.Reportable;
import de.chatsphere.api.user.transfer.UserDto;
import de.chatsphere.io.database.schema.Profile;
import de.chatsphere.io.database.schema.chat.Chat;
import de.chatsphere.io.database.schema.chat.ChatLine;
import de.chatsphere.io.database.schema.preference.File;
import de.chatsphere.io.database.schema.user.User;
import de.chatsphere.server.graphql.Context;
import graphql.GraphQLException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.apache.tika.Tika;
import org.seedstack.coffig.Coffig;
import org.seedstack.coffig.provider.JacksonProvider;


/**
 * Let subclasses (AudioManager, VideoManager, ...) decide which class to instantiate
 */
public abstract class MessageManager implements Reportable {

  /**
   * Require access to configuration files where the supported MIME-Type is listed.
   */
  protected final MimeSupportConfig config;
  /**
   * Required to gain insight about the username.
   */
  private final Context context;
  /**
   * Used to infer the MIME-Type of File and check for support.
   */
  private final Tika tika;

  public MessageManager(Context context) {
    this.context = context;
    this.tika = new Tika();

    Coffig coffig = Coffig.builder().withProviders(new JacksonProvider().addSource(
      getClass().getClassLoader().getResource("properties.yaml"))).build();

    this.config = coffig.get(MimeSupportConfig.class, "mimetypes");
  }


  /**
   * <p>Fetches required data from the database
   * and creates a finished DTO for transferal.</p>
   *
   * <p>One of the three template method patterns. Fills in the
   * following field of the `Message` interface.</p>
   *
   * <p>
   * id: Int!, author: User!, date: Date, text: String! to: Chat!
   * </p>
   *
   * <p>Should be overridden in each subclass to cast and
   * return a concrete type (e.g. AudioMessageDto)</p>
   *
   * @param id creates a message of the data saved in the specified row in `ChatLine`
   *
   * @return a Message
   */
  public final AbstractMessage getMessage(int id) {
    logQuery();
    ChatLine queried = query(id);
    sanitizeGet(queried);
    UserDto author = author(queried);
    Date date = date(queried);
    String text = text(queried);
    AbstractMessage msg = create(id, author, date, text);

    return msg;
  }

  /**
   * <p>One of the three template method patterns. Creates a new message with an attachment.</p>
   *
   * <p>Although the name might suggest this may be part of a factory method pattern, the actual
   * factory method is {@link MessageManager#create(int, UserDto, Date, String)}</p>
   *
   * @param input graphql input type
   *
   * @return the new message
   * @throws SQLException on errors while reading/writing to the database
   */
  public final AbstractChat createMessage(CreateMessageInput input) {
    logCreate();
    sanitizeCreate(input);
    File file = buildFile(input);
    de.chatsphere.io.database.schema.chat.Message message = buildMessage(input);
    ChatLine line = buildChatLine(input, message, file);

    // Commit
    try {
      context.getDatabase().getDao(File.class).create(file);
      context.getDatabase()
        .getDao(de.chatsphere.io.database.schema.chat.Message.class).create(message);
      context.getDatabase().getDao(ChatLine.class).create(line);
    } catch (SQLException e) {
      logger.error(e.getMessage());
      throw new GraphQLException(INTERNAL_ERROR);
    }

    return createResponse(line);
  }

  /**
   * <p>One of the three template method patterns. Updates text contents of an existing message.
   * Note: The attachment cannot be edited.</p>
   *
   * <p>Should be overridden in each subclass to cast and
   * return a concrete type (e.g. VideoMessageDto)</p>
   *
   * @param input graphql input type
   *
   * @return the updated message
   * @throws SQLException on errors while reading/writing to the database
   */
  public final AbstractMessage updateMessage(UpdateMessageInput input) {
    logUpdate();
    sanitizeUpdate(input);
    ChatLine queried = query(input.getMessageId());

    // Prepare update query
    try {
      UpdateBuilder<de.chatsphere.io.database.schema.chat.Message, ?> updateMessage =
        context.getDatabase()
          .getDao(de.chatsphere.io.database.schema.chat.Message.class).updateBuilder();
      updateMessage.updateColumnValue("content", input.getText());
      updateMessage.where().eq("id", queried.getMessage().getId());
      updateMessage.update();
    } catch (SQLException e) {
      logger.error(e.getMessage());
      throw new GraphQLException(INTERNAL_ERROR);
    }

    return getMessage(input.getMessageId());
  }

  /**
   * First step: Log that a get-query has occured.
   */
  public abstract void logQuery();

  /**
   * (On Create) First step: Log that a create*Message query has occured.
   */
  public abstract void logCreate();

  /**
   * (On update) First step: Log that a update*Message query has occured.
   */
  public abstract void logUpdate();

  /**
   * Second step: Query for the specific ChatLine.
   *
   * @param id chatline to look for
   *
   * @return chatline
   * @throws SQLException on any sql errors
   */
  private ChatLine query(int id) {

    Dao<ChatLine, Integer> dao = context.getDatabase().getDao(ChatLine.class);
    ChatLine line = null;
    try {
      line = dao.queryForId(id);
    } catch (SQLException e) {
      logger.error(e.getMessage());
      throw new GraphQLException("Could not get line with id: " + id);
    }

    return line;
  }

  /**
   * (On create) Second step: Check input parameters for correctness (Future possibility: Modify
   * input to combat malicious queries).
   *
   * @param input graphql input type
   *
   * @throws GraphQLException on any malicious/accidental mistakes that may cause trouble
   */
  public abstract void sanitizeCreate(CreateMessageInput input) throws GraphQLException;

  /**
   * (On Update) Second step: Check input parameters for correctness.
   *
   * @param input graphql input type
   *
   * @throws GraphQLException on any malicious/accidental mistakes that may cause trouble
   */
  public abstract void sanitizeUpdate(UpdateMessageInput input) throws GraphQLException;

  /**
   * Third step: Should check for any inconsistencies that occur when seeking data (e.g. no data
   * found, multiple found)
   *
   * @param line that was queried
   *
   * @throws GraphQLException on any inconsistencies that may occur when continuing
   */
  public abstract void sanitizeGet(ChatLine line) throws GraphQLException;

  /**
   * (On create) Third step: Create entry written to `file` table Infer file size/MIME Type and
   * generate hash.
   *
   * @param input graphql input type
   *
   * @return file entry
   */
  public abstract File buildFile(CreateMessageInput input);

  /**
   * Fourth step: Extract author and convert to DTO.
   *
   * @param line that was queried
   *
   * @return author of this message
   */
  private UserDto author(ChatLine line) {
    return UserDto.from(line.getAuthor());
  }

  /**
   * (On create) Fourth step: Create entry written to `message` table.
   *
   * @param input graphql input type
   *
   * @return message entry
   */
  private de.chatsphere.io.database.schema.chat.Message buildMessage(CreateMessageInput input) {

    return de.chatsphere.io.database.schema.chat.Message.builder()
      .markup(false)
      .content(input.getText())
      .build();
  }

  /**
   * Fifth step: Extract date.
   *
   * @param line that was queried
   *
   * @return last updated
   */
  private Date date(ChatLine line) {
    return line.getMessage().getUpdatedAt();
  }

  /**
   * (On create) Fourth step: Create entry written to `message` table with reference to `file` and
   * `message`.
   *
   * @param input graphql input type
   *
   * @return message entry
   */
  private ChatLine buildChatLine(CreateMessageInput input,
    de.chatsphere.io.database.schema.chat.Message message,
    File audio) {

    // Require read access
    Dao<Chat, Integer> chatDao = context.getDatabase().getDao(Chat.class);
    // Get the chat this message was sent to
    Chat chat = null;
    try {
      chat = chatDao.queryForId(input.getChatId());
    } catch (SQLException e) {
      logger.error(e.getMessage());
      throw new GraphQLException(INTERNAL_ERROR);
    }

    // Sender of this query is the author of the message
    User author = getUserByName(context.getAuthenticator().getUsername());

    // Build entry to `chatline` with reference to `file` and `message`
    return ChatLine.builder()
      .chat(chat)
      .author(author)
      .message(message)
      .asset(audio)
      .build();
  }

  /**
   * Sixth step: Get text contents.
   *
   * @param line that was queried
   *
   * @return text contents
   */
  private String text(ChatLine line) {
    return line.getMessage().getContent();
  }

  /**
   * (On create): Sixth step: Client wants data returned on mutation. Resolve this request
   *
   * @param line that was recently created
   *
   * @return chat to which this line was added
   * @throws SQLException on any errors while reading from database
   */
  private AbstractChat createResponse(ChatLine line) {
    if (line.getChat().isPrivateChat()) {
      return PrivateChatDto.from(line.getChat(), context);
    } else if (line.getChat().isGroupChat()) {
      throw new GraphQLException("`createResponse` for GroupChats: Not implemented");
    } else {
      throw new GraphQLException("Could not determine type of chat");
    }
  }

  /**
   * Seventh step: The chat the message was sent to.
   *
   * @param line that was queried
   *
   * @return chat
   * @throws GraphQLException when chat type is unknown
   */
  private AbstractChat to(ChatLine line) {
    if (line.getChat().isPrivateChat()) {
      return PrivateChatDto.from(line.getChat(), this.context);
    } else if (line.getChat().isGroupChat()) {
      throw new GraphQLException("`to` for GroupChats: Not implemented");
    } else {
      throw new GraphQLException("Could not determine type of chat");
    }
  }

  /**
   * <p>Factory method pattern.</p>
   * <p>Overriden by subclasses to return their conrete type (e.g. Video, Audio, ...) message</p>
   *
   * @param id     of chatline
   * @param author of chat message
   * @param date   when it was last updated
   * @param text   contents
   *
   * @return message object
   */
  public abstract AbstractMessage create(int id, UserDto author, Date date, String text);

  /**
   * <p>Determines what file types can be used
   * for this media type.</p>
   *
   * @param data           sha-256 encoded data
   * @param name           of file
   * @param supportedTypes list of types supported (listed in configs)
   *
   * @return true when MIME type is recognized
   */
  public boolean isSupported(byte[] data, String name, String[] supportedTypes) {
    String mimeType = tika.detect(data, name);

    if (!Arrays.asList(supportedTypes).contains(mimeType)) {
      return false;
    }
    return true;
  }


  /**
   * Map username => user object.
   *
   * @param username of user
   *
   * @return user object
   */
  private User getUserByName(String username) {

    List<User> result;

    // INNER JOIN + WHERE
    try {
      QueryBuilder<User, ?> userQb = context.getDatabase().getDao(User.class).queryBuilder();
      QueryBuilder<Profile, ?> profileQb = context.getDatabase().getDao(Profile.class)
        .queryBuilder();
      profileQb.where().eq("name", username);
      result = userQb.join(profileQb).query();
    } catch (SQLException e) {
      logger.error(e.getMessage());
      throw new GraphQLException(INTERNAL_ERROR);
    }

    if (result.size() != 1) {
      throw new GraphQLException("Found none/multiple users to single username: " + username);
    }

    return result.get(0);
  }
}
