package de.chatsphere.server;

import com.j256.ormlite.dao.Dao;
import de.chatsphere.feature.config.Config;
import de.chatsphere.feature.websocket.WebSocketProvider;
import de.chatsphere.io.database.Database;
import de.chatsphere.io.database.DatabaseConfig;
import de.chatsphere.io.database.schema.chat.Chat;
import de.chatsphere.server.graphql.ContextBuilder;
import de.chatsphere.server.graphql.schema.AnnotatedSchema;
import de.chatsphere.server.rxbus.Bus;
import de.chatsphere.server.rxbus.ChannelKey;
import graphql.schema.GraphQLSchema;
import graphql.servlet.DefaultGraphQLRootObjectBuilder;
import graphql.servlet.DefaultGraphQLSchemaProvider;
import graphql.servlet.GraphQLInvocationInputFactory;
import graphql.servlet.SimpleGraphQLHttpServlet;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Jetty provides the ability to wire up WebSocket endpoints to Servlet Path Specs via the use of a
 * WebSocketServlet bridge servlet. Internally, Jetty manages the HTTP Upgrade to WebSocket and
 * migration from a HTTP Connection to a WebSocket Connection.
 */
@WebServlet(urlPatterns = "/graphql")
public class Servlet extends WebSocketServlet {

  private static final long serialVersionUID = -4597837975376102283L;
  private final Database database = initDatabase();
  private static final Logger log = LoggerFactory.getLogger(Servlet.class);

  private SimpleGraphQLHttpServlet graphQlServlet;

  @Override
  public void configure(WebSocketServletFactory factory) {
    factory.getPolicy().setMaxTextMessageBufferSize(1024 * 1024);
    factory.getPolicy().setIdleTimeout(30 * 1000);
    // Sending images via graphql require enough space for its base64 encoded string (else an exception may be thrown)
    factory.getPolicy().setMaxTextMessageSize(1024 * 1024);
    factory.setCreator(new WebSocketProvider(database));
  }

  @Override
  public void init() throws ServletException {
    initBusChannels();
    GraphQLSchema schema = new AnnotatedSchema().getSchema();
    GraphQLInvocationInputFactory invocationInputFactory =
      GraphQLInvocationInputFactory.newBuilder(new DefaultGraphQLSchemaProvider(schema))
        .withGraphQLContextBuilder(new ContextBuilder(database))
        .withGraphQLRootObjectBuilder(new DefaultGraphQLRootObjectBuilder())
        .build();
    graphQlServlet = SimpleGraphQLHttpServlet.newBuilder(invocationInputFactory).build();
    super.init();
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    graphQlServlet.service(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    graphQlServlet.service(request, response);
  }

  /**
   * Initializes the database connection.
   *
   * @return the database
   * @throws RuntimeException an error occurred during initialization
   */
  private Database initDatabase() throws RuntimeException {
    Database database;
    try {
      database = new Database(Config.getConfig(DatabaseConfig.class, "database"));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return database;
  }

  private void initBusChannels() {
    Dao<Chat, Integer> chatDao = database.getDao(Chat.class);
    try {
      chatDao.queryForAll().forEach(chat -> {
        ChannelKey channelKey = Bus.getInstance().createKey("chat", chat.getId());
        Bus.getInstance().registerChannel(channelKey);
      });
    } catch (SQLException e) {
      log.error(e.getMessage());
    }
  }
}
