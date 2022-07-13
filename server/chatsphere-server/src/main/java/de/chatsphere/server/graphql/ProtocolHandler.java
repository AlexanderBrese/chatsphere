package de.chatsphere.server.graphql;

import static graphql.servlet.internal.ApolloSubscriptionProtocolHandler.OperationMessage.Type.GQL_COMPLETE;
import static graphql.servlet.internal.ApolloSubscriptionProtocolHandler.OperationMessage.Type.GQL_CONNECTION_ACK;
import static graphql.servlet.internal.ApolloSubscriptionProtocolHandler.OperationMessage.Type.GQL_CONNECTION_ERROR;
import static graphql.servlet.internal.ApolloSubscriptionProtocolHandler.OperationMessage.Type.GQL_CONNECTION_KEEP_ALIVE;
import static graphql.servlet.internal.ApolloSubscriptionProtocolHandler.OperationMessage.Type.GQL_CONNECTION_TERMINATE;
import static graphql.servlet.internal.ApolloSubscriptionProtocolHandler.OperationMessage.Type.GQL_DATA;
import static graphql.servlet.internal.ApolloSubscriptionProtocolHandler.OperationMessage.Type.GQL_ERROR;

import de.chatsphere.api.account.model.AccountRepository;
import de.chatsphere.server.rxbus.Bus;
import de.chatsphere.server.rxbus.SubscriptionKey;
import de.chatsphere.util.Util;
import graphql.ExecutionResult;
import graphql.GraphQLException;
import graphql.servlet.GraphQLInvocationInput;
import graphql.servlet.internal.ApolloSubscriptionProtocolHandler;
import graphql.servlet.internal.GraphQLRequest;
import graphql.servlet.internal.SubscriptionHandlerInput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.websocket.CloseReason;
import org.eclipse.jetty.websocket.api.CloseStatus;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The protocol handler implements the GraphQL subscription protocol and also handles general
 * GraphQL query functionality.
 */
public class ProtocolHandler extends SubscriptionHandler {

  private static final Logger log = LoggerFactory.getLogger(ProtocolHandler.class);
  private static final CloseStatus TERMINATE_CLOSE_REASON =
    new CloseStatus(
      CloseReason.CloseCodes.NORMAL_CLOSURE.getCode(),
      "client requested " + GQL_CONNECTION_TERMINATE.getType());
  private final SubscriptionHandlerInput input;
  private final Context context;

  /**
   * Initializes a new protocol handler.
   *
   * @param input the subscription handler input
   */
  public ProtocolHandler(
    SubscriptionHandlerInput input) {
    this.input = input;
    GraphQLInvocationInput invocationInput = input.getInvocationInputFactory().create(
      new GraphQLRequest("", new HashMap<>(), ""));
    this.context = (Context) invocationInput.getContext();
  }

  @Override
  public void onMessage(Session session, String text) {
    ApolloSubscriptionProtocolHandler.OperationMessage operationMessage = parse(session, text);
    Object payload;
    String username;
    Context context;

    switch (Objects.requireNonNull(operationMessage).getType()) {
      case GQL_CONNECTION_INIT:
        payload = operationMessage.getPayload();
        context = getContext();

        init(session, context, payload, operationMessage.getId());
        break;

      case GQL_START:
        payload = operationMessage.getPayload();
        GraphQLRequest request = getRequest(payload);
        username = getContext()
          .getAuthenticator()
          .getUsername();
        start(session, request, operationMessage, username);
        break;

      case GQL_STOP:
        username = getContext()
          .getAuthenticator()
          .getUsername();

        stop(operationMessage.getId(), username);
        break;

      case GQL_CONNECTION_TERMINATE:
        terminate(session);
        break;

      default:
        throw new IllegalArgumentException("Unknown message type: " + operationMessage.getType());
    }
  }

  /**
   * Parses an incoming text message to a GraphQL operation message. If an error occurred it will be
   * logged and a message with a specific status code (GQL_CONNECTION_ERROR) will be sent out to the
   * client signalising that there was an connection error.
   *
   * @param session the WebSocket session
   * @param text    the incoming text message
   *
   * @return the parsed operation message
   */
  private ApolloSubscriptionProtocolHandler.OperationMessage parse(Session session, String text) {
    ApolloSubscriptionProtocolHandler.OperationMessage message = null;

    try {
      message = input.getGraphQLObjectMapper()
        .getJacksonMapper()
        .readValue(text, ApolloSubscriptionProtocolHandler.OperationMessage.class);
    } catch (IOException t) {
      log.warn("Error parsing message", t);
      signal(session, GQL_CONNECTION_ERROR, null);
    }

    return message;
  }

  /**
   * Initializes a GraphQL connection by sending a message back with a specific status code
   * (GQL_CONNECTING_ACK) identifying that the connection could be successful built. Also
   * authenticates the client if connection parameters were provided. Additionally sends a keep
   * alive message periodically to keep the WebSocket connection steady.
   *
   * @param session          the WebSocket session
   * @param context          the GraphQL context
   * @param connectionParams the incoming connection parameters
   * @param operationId      the operation id
   */
  private void init(
    Session session,
    Context context,
    Object connectionParams,
    String operationId) {

    sendKeepAliveMessage(session, operationId);
    try {
      authenticate(context, connectionParams);
    } catch (GraphQLException e) {
      send(
        session,
        GQL_CONNECTION_ERROR,
        operationId,
        e.getMessage());
      return;
    }
    signal(session, GQL_CONNECTION_ACK, operationId);
  }

  /**
   * Starts a GraphQL transaction by either querying or subscribing the incoming request. Sends back
   * a data message eventually followed by an completion message in case of a GraphQL Subscription
   * or instantly in case of a GraphQL query.
   *
   * @param session          the WebSocket session
   * @param request          the GraphQL request
   * @param operationMessage the GraphQL operation message
   * @param username         the client's username useful for subscription
   */
  private void start(
    Session session, GraphQLRequest request,
    ApolloSubscriptionProtocolHandler.OperationMessage operationMessage, String username)
    throws IllegalArgumentException, IllegalStateException {
    String query = request.getQuery();
    String operationId = operationMessage.getId();
    ExecutionResult executionResult = getResult(request);

    if (query.contains("subscription")) {
      SubscriptionKey subscriptionKey =
        Bus.getInstance().createSubscriptionKey(username, request, operationId);
      subscription(
        session,
        subscriptionKey,
        executionResult
      );
    } else {
      query(session, executionResult, operationId);
    }
  }

  /**
   * Stops a GraphQL Subscription by unsubscribing it with the provided parameters used to get the
   * matching subscription key.
   *
   * @param operationId the subscription id
   * @param username    the client's user name
   */
  private void stop(String operationId, String username) {
    SubscriptionKey subscriptionKey =
      Bus.getInstance().getSubscriptionKey(username, operationId);
    if (subscriptionKey != null) {
      unsubscribe(subscriptionKey);
    }
  }

  /**
   * Terminates a GraphQL connection by closing the WebSocket session.
   *
   * @param session the WebSocket session
   */
  private void terminate(Session session) {
    session.close(TERMINATE_CLOSE_REASON);
  }

  /**
   * Authenticates a client's user using the connection parameters provided.
   *
   * @param context          the GraphQL context
   * @param connectionParams the connection parameters
   *
   * @throws GraphQLException wrong connection parameters | couldn't decrypt token | wrong token
   */
  private void authenticate(Context context, Object connectionParams)
    throws GraphQLException {
    if (
      connectionParams == null
        || !Util.hasObject(Util.toMap(connectionParams), "token")
    ) {
      throw new GraphQLException("Wrong connection parameters supplied.");
    }
    String token = (String) Util.toMap(connectionParams).get("token");
    String username;
    try {
      username = context.getAuthenticator().decrypt(token);
    } catch (IllegalArgumentException e) {
      throw new GraphQLException("Couldn't decrypt token.");
    }
    if (!AccountRepository.usernameTaken(context, username)) {
      throw new GraphQLException("Wrong token supplied.");
    }
    context
      .getAuthenticator()
      .setUsername(username);
  }

  /**
   * Handles a GraphQL subscription by subscribing to the execution result if no errors are present.
   * If there are errors it will send a sanitized message to the client without any subscription.
   *
   * @param session         the session to send messages through
   * @param subscriptionKey the subscription key to add the subscription
   * @param executionResult the execution result to subscribe to
   */
  private void subscription(
    Session session,
    SubscriptionKey subscriptionKey,
    ExecutionResult executionResult) {
    executionResult =
      sanitizeResult(session, executionResult, subscriptionKey.getOperationId());
    if (executionResult == null) {
      return;
    }

    subscribe(session, executionResult, subscriptionKey);
  }

  /**
   * Handles a GraphQl query by sending a data message with the execution result to the client
   * followed by a completion message if no errors are present. If there are any errors it will send
   * a sanitized message.
   *
   * @param session         the session to send messages through
   * @param executionResult the execution result to subscribe to
   * @param operationId     the query id
   */
  private void query(Session session, ExecutionResult executionResult, String operationId) {
    executionResult = sanitizeResult(session, executionResult, operationId);
    if (executionResult == null) {
      return;
    }

    sendDataMessage(session, operationId, executionResult.toSpecification());
    sendCompleteMessage(session, operationId);
  }

  /**
   * Gets the GraphQL context from the subscription handler input.
   *
   * @return the GraphQL context
   */
  public Context getContext() {
    return context;
  }

  /**
   * Gets the GraphQL request from the operation payload.
   *
   * @param payload the operation payload
   *
   * @return the GraphQL request
   */
  private GraphQLRequest getRequest(Object payload) {
    return input.getGraphQLObjectMapper()
      .getJacksonMapper()
      .convertValue(payload, GraphQLRequest.class);
  }

  /**
   * Gets the execution result from a GraphQL request.
   *
   * @param request the GraphQL request
   *
   * @return the execution result
   */
  private ExecutionResult getResult(GraphQLRequest request) {
    ExecutionResult result = null;
    try {
      result = input.getQueryInvoker()
        .query(input.getInvocationInputFactory().create(request));
    } catch (RuntimeException e) {
      log.error(e.getMessage());
    }
    return result;
  }

  @Override
  protected void sendKeepAliveMessage(Session session, String operationId) {
    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
      public void run() {
        if (session.isOpen()) {
          signal(session, GQL_CONNECTION_KEEP_ALIVE, operationId);
        } else {
          timer.cancel();
        }
      }
    };
    timer.schedule(task, 0L, 30L);
  }

  @Override
  protected void sendDataMessage(Session session, String operationId, Object data) {
    send(session, GQL_DATA, operationId, data);
  }

  @Override
  protected void sendErrorMessage(Session session, String operationId) {
    signal(session, GQL_ERROR, operationId);
  }

  @Override
  protected void sendCompleteMessage(Session session, String operationId) {
    signal(session, GQL_COMPLETE, operationId);
  }

  /**
   * Sanitizes errors in a execution result and sends out a sanitized message if they are any errors
   * present.
   *
   * @param session         the session to send the sanitized message through
   * @param executionResult the execution result to be sanitized
   * @param operationId     the operation id
   *
   * @return the sanitized execution result
   */
  private ExecutionResult sanitizeResult(
    Session session,
    ExecutionResult executionResult,
    String operationId) {
    executionResult = input.getGraphQLObjectMapper().sanitizeErrors(executionResult);

    if (input.getGraphQLObjectMapper().areErrorsPresent(executionResult)) {
      sendSanitizedMessage(session, executionResult, operationId);
      return null;
    }

    return executionResult;
  }

  /**
   * Sends out an error message to the client with the sanitized execution result.
   *
   * @param session         the session to send the error message through
   * @param executionResult the sanitized execution result
   * @param operationId     the operation id
   */
  private void sendSanitizedMessage(Session session, ExecutionResult executionResult,
    String operationId) {
    Map<String, Object> sanitizedExecutionResult =
      input.getGraphQLObjectMapper().convertSanitizedExecutionResult(
        executionResult,
        false);
    send(
      session, GQL_ERROR,
      operationId,
      sanitizedExecutionResult);
  }

  /**
   * Sends a message to the client without an operation payload.
   *
   * @param session the session to send the signal through
   * @param type    the operation message type
   * @param id      the operation id
   */
  private void signal(
    Session session,
    ApolloSubscriptionProtocolHandler.OperationMessage.Type type,
    String id) {
    send(session, type, id, null);
  }

  /**
   * Sends a message to the client asynchronously.
   *
   * @param session     the session to send the message through
   * @param type        the operation message type
   * @param operationId the operation id
   * @param data        the resulting data
   */
  private void send(
    Session session,
    ApolloSubscriptionProtocolHandler.OperationMessage.Type type,
    String operationId,
    Object data) {
    String message;
    try {
      message = input.getGraphQLObjectMapper().getJacksonMapper().writeValueAsString(
        new ApolloSubscriptionProtocolHandler.OperationMessage(type, operationId, data)
      );
    } catch (IOException e) {
      log.error("Error sending subscription response", e);
      return;
    }
    if (!session.isOpen()) {
      return;
    }
    RemoteEndpoint remote = session.getRemote();
    sendAsync(remote, message);
  }

  /**
   * Sends a text asynchronously to the specified remote endpoint.
   *
   * @param remote the client
   * @param text   the text to send
   */
  private void sendAsync(RemoteEndpoint remote, String text) {
    Future<Void> fut = null;
    try {
      fut = remote.sendStringByFuture(text);
      // wait for completion (timeout)
      fut.get(2, TimeUnit.SECONDS);
    } catch (ExecutionException | InterruptedException e) {
      // Send failed
      e.printStackTrace();
    } catch (TimeoutException e) {
      // timeout
      e.printStackTrace();
      if (fut != null) {
        // cancel the message
        fut.cancel(true);
      }
    }
  }
}
