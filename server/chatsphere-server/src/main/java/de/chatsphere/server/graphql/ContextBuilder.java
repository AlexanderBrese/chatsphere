package de.chatsphere.server.graphql;

import de.chatsphere.io.database.Database;
import graphql.servlet.GraphQLContext;
import graphql.servlet.GraphQLContextBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.HandshakeRequest;

public class ContextBuilder implements GraphQLContextBuilder {

  private final Context context;

  public ContextBuilder(Database database) {
    this.context = new Context(database);
  }

  /**
   * Save context and associated state for the duration of multiple requests (multiple
   * ContextBuilder calls) processed within a WebSocket. The state is initialized externally by the
   * {@link ProtocolHandler}.
   */

  @Override
  public GraphQLContext build(HttpServletRequest httpServletRequest) {
    return null;
  }

  @Override
  public GraphQLContext build(HandshakeRequest handshakeRequest) {
    return null;
  }

  @Override
  public GraphQLContext build() {
    return this.context;
  }
}
