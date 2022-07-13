package de.chatsphere.server.rxbus;

import de.chatsphere.server.rxbus.ChannelKey;
import de.chatsphere.server.rxbus.SubscriptionKey;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The subscription manager manages all subscriptions for a particular protocol handler or channel.
 */
public class SubscriptionManager {

  private static final Logger log = LoggerFactory.getLogger(SubscriptionManager.class);
  private final Object lock = new Object();
  private boolean closed = false;
  private Map<SubscriptionKey, Subscription> subscriptions = new HashMap<>();

  /**
   * Adds a subscription to the map thread safe.
   *
   * @param subscriptionKey the key
   * @param subscription    the value
   */
  public void add(SubscriptionKey subscriptionKey, Subscription subscription)
    throws IllegalStateException {
    synchronized (lock) {
      if (closed) {
        throw new IllegalStateException("Websocket was already closed!");
      }
      subscriptions.put(subscriptionKey, subscription);
    }
  }

  /**
   * Cancels a given subscription mapped to the provided key thread safe.
   *
   * @param subscriptionKey the key
   */
  private void cancel(SubscriptionKey subscriptionKey) {
    Subscription subscription = subscriptions.get(subscriptionKey);
    if (subscription != null) {
      subscription.cancel();
    }
  }

  /**
   * asdasd.
   *
   * @param subscriptionKey asasd.
   */
  public void unsubscribe(SubscriptionKey subscriptionKey) {
    synchronized (lock) {
      cancel(subscriptionKey);
      subscriptions.remove(subscriptionKey);
    }
  }

  /**
   * Cancels all subscriptions matching the provided channel key thread safe.
   *
   * @param channelKey the channel key
   */
  public void unsubscribe(ChannelKey channelKey) {
    synchronized (lock) {
      List<SubscriptionKey> subscriptionsToRemove = new LinkedList<>();
      for (SubscriptionKey subscriptionKey : subscriptions.keySet()) {
        if (subscriptionKey.getChannelId().equals(channelKey.getChannelId())
          && subscriptionKey.getChannelName().equals(channelKey.getChannelName())) {
          subscriptionsToRemove.add(subscriptionKey);
          cancel(subscriptionKey);
        }
      }
      subscriptionsToRemove.forEach(subscriptionKey -> subscriptions.remove(subscriptionKey));
    }
  }

  /**
   * Cancels and removes any subscription whose key matches the username.
   *
   * @param username the username
   */
  public void unsubscribe(String username) {
    synchronized (lock) {
      List<SubscriptionKey> subscriptionsToRemove = new LinkedList<>();
      for (SubscriptionKey subscriptionKey : subscriptions.keySet()) {
        String subscriber = subscriptionKey.getSubscriber();
        if (username.equals(subscriber)) {
          subscriptionsToRemove.add(subscriptionKey);
          cancel(subscriptionKey);
        }
      }
      subscriptionsToRemove.forEach(subscriptionKey -> subscriptions.remove(subscriptionKey));
    }
  }

  /**
   * Gets a subscription key from an username and an operation id.
   *
   * @param username    the username
   * @param operationId the operation id
   *
   * @return the subscription key
   */
  public SubscriptionKey getKey(String username, String operationId) {
    for (SubscriptionKey subscriptionKey : subscriptions.keySet()) {
      String subscriber = subscriptionKey.getSubscriber();
      String id = subscriptionKey.getOperationId();

      if (Objects.equals(username, subscriber) && operationId.equals(id)) {
        return subscriptionKey;
      }
    }
    return null;
  }
}
