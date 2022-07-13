package de.chatsphere.feature.websocket;

import de.chatsphere.io.database.Database;
import de.chatsphere.server.graphql.ContextBuilder;
import de.chatsphere.server.graphql.ProtocolHandler;
import de.chatsphere.server.graphql.schema.AnnotatedSchema;
import graphql.GraphQLException;
import graphql.servlet.DefaultGraphQLRootObjectBuilder;
import graphql.servlet.DefaultGraphQLSchemaProvider;
import graphql.servlet.GraphQLInvocationInputFactory;
import graphql.servlet.GraphQLObjectMapper;
import graphql.servlet.GraphQLQueryInvoker;
import graphql.servlet.internal.SubscriptionHandlerInput;
import javax.websocket.CloseReason;
import lombok.AllArgsConstructor;
import org.eclipse.jetty.websocket.api.CloseStatus;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A GraphQL WebSocket to facilitate GraphQL queries, mutations and subscriptions over apollo's
 * specified websocket subprotocol (graphql-ws).
 */
@WebSocket
@AllArgsConstructor
public class ChatSphereWebSocket {

  private static final CloseStatus ERROR_CLOSE_REASON =
    new CloseStatus(
      CloseReason.CloseCodes.UNEXPECTED_CONDITION.getCode(),
      "Internal Server Error");
  private static final Logger log = LoggerFactory.getLogger(ChatSphereWebSocket.class);
  private static final SessionManager sessionManager = new SessionManager();
  private final AnnotatedSchema annotatedSchema;
  private final GraphQLQueryInvoker queryInvoker;
  private final GraphQLObjectMapper objectMapper;
  private final Database database;


  /**
   * Handles WebSocket connection initialization by setting up a new protocol handler and mapping it
   * to the session provided.
   *
   * @param session the session
   */
  @OnWebSocketConnect
  public void onConnect(Session session) {
    GraphQLInvocationInputFactory invocationInputFactory =
        GraphQLInvocationInputFactory.newBuilder(
          new DefaultGraphQLSchemaProvider(annotatedSchema.getSchema()))
            .withGraphQLContextBuilder(new ContextBuilder(this.database))
            .withGraphQLRootObjectBuilder(new DefaultGraphQLRootObjectBuilder())
            .build();
    SubscriptionHandlerInput handlerInput =
      new SubscriptionHandlerInput(invocationInputFactory, queryInvoker, objectMapper);
    ProtocolHandler protocolHandler =
      new ProtocolHandler(handlerInput);

    sessionManager.add(session, protocolHandler);
    log.debug("Session opened: {}", session.getLocalAddress().getHostString());
  }

  /**
   * Handles WebSocket connection termination by closing the session and its subscriptions
   * respectively.
   *
   * @param session    the session
   * @param statusCode the status code for termination
   * @param reason     the reason for termination
   */
  @OnWebSocketClose
  public void onClose(Session session, int statusCode, String reason) {
    log.debug("Session closed: {}, {}", session.getLocalAddress().getHostString(), reason);
    sessionManager.close(session);
  }

  /**
   * Handles WebSocket errors by closing the session with a provided reason.
   *
   * @param session the session
   * @param cause   the error cause
   */
  @OnWebSocketError
  public void onError(Session session, Throwable cause) {
    log.error("Error in websocket session: {}", session.getLocalAddress().getHostString());
    closeUnexpectedly(session);
  }

  /**
   * Closes a session by providing an error reason.
   *
   * @param session the session
   */
  private void closeUnexpectedly(Session session) {
    session.close(ERROR_CLOSE_REASON);
  }

  /**
   * Handles WebSocket communication by executing the GraphQL protocol.
   *
   * @param session the session made
   * @param text    the GraphQL operation
   */
  @OnWebSocketMessage
  public void onText(Session session, String text) {
    ProtocolHandler protocolHandler = sessionManager.get(session);

    try {
      protocolHandler.onMessage(session, text);
    } catch (NullPointerException | IllegalStateException | IllegalArgumentException e) {
      log.error(
        "Error executing websocket query for session: {}",
        session.getLocalAddress().getHostString(),
        e);
      closeUnexpectedly(session);
    }
  }
}
