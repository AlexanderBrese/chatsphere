package de.chatsphere.feature.websocket;

import de.chatsphere.server.graphql.ProtocolHandler;
import de.chatsphere.server.rxbus.Bus;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.websocket.CloseReason;
import org.eclipse.jetty.websocket.api.CloseStatus;
import org.eclipse.jetty.websocket.api.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A session manager manages all sessions made during WebSocket connection initialization and maps
 * GraphQL protocol handler to it.
 */
public class SessionManager {

  private static final CloseStatus SHUTDOWN_CLOSE_REASON =
    new CloseStatus(CloseReason.CloseCodes.UNEXPECTED_CONDITION.getCode(), "Server Shut Down");
  private static final Logger log = LoggerFactory.getLogger(SessionManager.class);
  private final Map<Session, ProtocolHandler> sessionHandlers = new HashMap<>();
  private final AtomicBoolean isShuttingDown = new AtomicBoolean(false);
  private final AtomicBoolean isShutDown = new AtomicBoolean(false);
  private final Object cacheLock = new Object();

  /**
   * Adds a session mapped to a protocol handler thread safe.
   *
   * @param session         key
   * @param protocolHandler value
   */
  public void add(Session session, ProtocolHandler protocolHandler) {
    synchronized (cacheLock) {
      if (isShuttingDown.get()) {
        throw new IllegalStateException("Server is shutting down!");
      }

      sessionHandlers.put(session, protocolHandler);
    }
  }

  /**
   * Removes an entry from the map with the session as the key thread safe.
   *
   * @param session the key
   *
   * @return the removed value
   */
  private ProtocolHandler remove(Session session) {
    ProtocolHandler protocolHandler;
    synchronized (cacheLock) {
      protocolHandler = sessionHandlers.remove(session);
    }
    return protocolHandler;
  }

  /**
   * Gets a protocol handler mapped to the provided session.
   *
   * @param session the key
   *
   * @return the value
   */
  public ProtocolHandler get(Session session) {
    return sessionHandlers.get(session);
  }

  /**
   * Closes and removes all channels subscriptions made in the session.
   *
   * @param session the key
   */
  public void close(Session session) {
    ProtocolHandler protocolHandler = remove(session);
    String username = protocolHandler.getContext().getAuthenticator().getUsername();
    Bus.getInstance().unsubscribe(username);
  }

  /**
   * Closes and removes all sessions and its subscriptions. Prevents comodification exception since
   * #onClose() is called during session.close(), but we can't necessarily rely on that happening so
   * we close subscriptions here anyway.
   */
  public void beginShutDown() {
    synchronized (cacheLock) {
      isShuttingDown.set(true);
      Map<Session, ProtocolHandler> copy = new HashMap<>(sessionHandlers);

      copy.forEach((session, protocolHandler) -> {
        close(session);
        session.close(SHUTDOWN_CLOSE_REASON);
      });

      copy.clear();

      if (!sessionHandlers.isEmpty()) {
        log.error("GraphQLWebsocketServlet did not shut down cleanly!");
        sessionHandlers.clear();
      }
    }

    isShutDown.set(true);
  }

  /**
   * Checks if all sessions with its subscriptions are closed and removed.
   *
   * @return true/false
   */
  public boolean isShutDown() {
    return isShutDown.get();
  }
}
