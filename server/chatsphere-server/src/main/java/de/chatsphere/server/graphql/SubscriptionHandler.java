package de.chatsphere.server.graphql;

import de.chatsphere.server.rxbus.Bus;
import de.chatsphere.server.rxbus.Event;
import de.chatsphere.server.rxbus.SubscriptionKey;
import de.chatsphere.util.EventParser;
import graphql.ExecutionResult;
import java.util.concurrent.atomic.AtomicReference;
import lombok.AllArgsConstructor;
import org.eclipse.jetty.websocket.api.Session;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The subscription handler handles GraphQL subscriptions and its responses to the client.
 */
@AllArgsConstructor
public abstract class SubscriptionHandler {

  private static final Logger log = LoggerFactory.getLogger(SubscriptionHandler.class);

  /**
   * Provides the GraphQL request/response mechanisms from parsing and validating text messages to
   * executing its operations and sending back a response with the execution result.
   *
   * @param session the session to send the response/error message through
   * @param text    the text to parse and validate
   *
   * @throws Exception something went wrong during the GraphQL transaction
   */
  public abstract void onMessage(Session session, String text) throws Exception;

  /**
   * Sends a message with data to the client.
   *
   * @param session     the session to the send the message through
   * @param operationId the operation id
   * @param data        the data
   */
  protected abstract void sendDataMessage(Session session, String operationId, Object data);

  /**
   * Sends an error message to the client.
   *
   * @param session     the session to send the error message through
   * @param operationId the operation id
   */
  protected abstract void sendErrorMessage(Session session, String operationId);

  /**
   * Sends a completion message to the client.
   *
   * @param session     the session to send the completion through
   * @param operationId the operation id
   */
  protected abstract void sendCompleteMessage(Session session, String operationId);

  /**
   * Sends a keep alive message to the client.
   *
   * @param session     the session to send the keep alive message through.
   * @param operationId the operation id
   */
  protected abstract void sendKeepAliveMessage(Session session, String operationId);

  /**
   * Subscribes to a execution results data and sends incoming results to the client.
   *
   * @param session         the session to send the results through
   * @param executionResult the execution result to subscribe to
   * @param subscriptionKey the subscription key to store the subscription with
   */
  protected void subscribe(
    Session session,
    ExecutionResult executionResult,
    SubscriptionKey subscriptionKey) {
    final Object data = executionResult.getData();

    if (data instanceof Publisher) {
      @SuppressWarnings("unchecked") final Publisher<ExecutionResult> publisher =
        (Publisher<ExecutionResult>) data;
      final AtomicSubscriptionReference subscriptionReference = new AtomicSubscriptionReference();

      publisher.subscribe(new Subscriber<ExecutionResult>() {
        @Override
        public void onSubscribe(Subscription subscription) {
          subscriptionReference.set(subscription);
          subscriptionReference.get().request(1);

          Bus.getInstance().subscribe(subscriptionKey, subscriptionReference.get());
        }

        @Override
        public void onNext(ExecutionResult executionResult) {
          Event event = EventParser.parse(executionResult);
          if (!isEligibleRecipient(event, subscriptionKey.getSubscriber())) {
            subscriptionReference.get().request(1);
            return;
          }
          subscriptionReference.get().request(1);
          sendDataMessage(
            session, subscriptionKey.getOperationId(), executionResult.toSpecification());
        }

        @Override
        public void onError(Throwable throwable) {
          log.error("Subscription error", throwable);
          unsubscribe(subscriptionKey);
          sendErrorMessage(session, subscriptionKey.getOperationId());
        }

        @Override
        public void onComplete() {
          unsubscribe(subscriptionKey);
          sendCompleteMessage(session, subscriptionKey.getOperationId());
        }
      });
    }
  }

  /**
   * Unsubscribes a subscription.
   *
   * @param subscriptionKey the key mapped to the subscription
   */
  protected void unsubscribe(
    SubscriptionKey subscriptionKey) {
    Bus.getInstance().unsubscribe(subscriptionKey);
  }

  /**
   * Checks if the event's recipient is eligible for receiving the subscribed execution result.
   *
   * @param event the event holding the sender and the recipient
   * @param user  the user to check for eligibility.
   *
   * @return true/false
   */
  private boolean isEligibleRecipient(Event event, String user) {
    return !event.isIdenticalSender(user)
      && (event.isRecipient(user) || event.isRecipient(Event.EVERYBODY));
  }

  /**
   * Defines a subscription reference.
   */
  static class AtomicSubscriptionReference {

    private final AtomicReference<Subscription> reference = new AtomicReference<>(null);

    /**
     * Sets a reference to the subscription if not already set.
     *
     * @param subscription the subscription
     */
    public void set(Subscription subscription) {
      if (reference.get() != null) {
        throw new IllegalStateException("Cannot overwrite subscription!");
      }

      reference.set(subscription);
    }

    /**
     * Gets the subscription referenced if set.
     *
     * @return the subscription
     */
    public Subscription get() {
      Subscription subscription = reference.get();
      if (subscription == null) {
        throw new IllegalStateException("Subscription has not been initialized yet!");
      }

      return subscription;
    }
  }
}
