package de.chatsphere.feature.websocket;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import de.chatsphere.io.database.Database;
import de.chatsphere.server.graphql.ModifiedDataFetcherExceptionHandler;
import de.chatsphere.server.graphql.schema.AnnotatedSchema;
import graphql.execution.AsyncExecutionStrategy;
import graphql.execution.AsyncSerialExecutionStrategy;
import graphql.execution.DataFetcherExceptionHandler;
import graphql.execution.SubscriptionExecutionStrategy;
import graphql.execution.preparsed.PreparsedDocumentEntry;
import graphql.servlet.DefaultExecutionStrategyProvider;
import graphql.servlet.DefaultGraphQLErrorHandler;
import graphql.servlet.ExecutionStrategyProvider;
import graphql.servlet.GraphQLObjectMapper;
import graphql.servlet.GraphQLQueryInvoker;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A WebSocketCreator that bases the WebSocket it creates off of information present in the
 * UpgradeRequest object. The ChatSphereWebSocketCrator only creates a ChatSphereWebSocket if the
 * 'graphql-ws' SubProtocol is defined in the UpgradeRequest.
 */
public class WebSocketProvider implements WebSocketCreator {

  private static final Logger log = LoggerFactory.getLogger(WebSocketProvider.class);
  private final ChatSphereWebSocket socket;

  /**
   * Initializes a new ChatSphereWebSocket.
   */
  public WebSocketProvider(Database database) {
    final AnnotatedSchema annotatedSchema = new AnnotatedSchema();

    final Cache<String, PreparsedDocumentEntry> cache =
      Caffeine.newBuilder().maximumSize(10_000).build();
    final DataFetcherExceptionHandler exceptionHandler = new ModifiedDataFetcherExceptionHandler();
    final ExecutionStrategyProvider executionStrategyProvider =
      new DefaultExecutionStrategyProvider(
        new AsyncExecutionStrategy(exceptionHandler),
        new AsyncSerialExecutionStrategy(exceptionHandler),
        new SubscriptionExecutionStrategy(exceptionHandler)
      );

    GraphQLQueryInvoker queryInvoker = GraphQLQueryInvoker.newBuilder()
      .withExecutionStrategyProvider(executionStrategyProvider)
      .withPreparsedDocumentProvider(cache::get)
      .build();
    GraphQLObjectMapper objectMapper = GraphQLObjectMapper.newBuilder()
      .withGraphQLErrorHandler(new DefaultGraphQLErrorHandler())
      .build();

    socket = new ChatSphereWebSocket(annotatedSchema, queryInvoker, objectMapper, database);
  }

  @Override
  public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
    for (String subprotocol : req.getSubProtocols()) {
      if ("graphql-ws".equals(subprotocol)) {
        resp.setAcceptedSubProtocol(subprotocol);
        return socket;
      }
      log.info("Undefined subprotocol: " + subprotocol);
    }

    return null;
  }
}
