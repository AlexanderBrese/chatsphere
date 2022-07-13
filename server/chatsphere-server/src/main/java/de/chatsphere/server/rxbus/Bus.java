package de.chatsphere.server.rxbus;

import de.chatsphere.api.shared.transfer.SubscriptionInput;
import de.chatsphere.util.SubscriptionInputParser;
import graphql.servlet.internal.GraphQLRequest;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Event Bus manages subscription channels built upon RxJava Relays and all subscriptions in
 * general (any user).
 */
public class Bus {

  public static final ChannelKey MAIN_CHANNEL_KEY = new ChannelKey("Main");
  private static final Logger log = LoggerFactory.getLogger(Bus.class);
  private static volatile Bus INSTANCE = null;
  private final ConcurrentHashMap<ChannelKey, Channel> channels = new ConcurrentHashMap<>();

  /**
   * Initializes a new event bus by registering the main channel. Any remote endpoint (client) is a
   * potential subscriber to the main channel.
   */
  private Bus() {
    registerChannel(MAIN_CHANNEL_KEY);
  }

  /**
   * Gets a thread safe singleton instance of the event bus.
   *
   * @return singleton instance
   */
  public static Bus getInstance() {
    if (INSTANCE == null) {
      synchronized (Bus.class) {
        if (INSTANCE == null) {
          INSTANCE = new Bus();
        }
      }
    }
    return INSTANCE;
  }

  public ChannelKey createKey(String channelName, Integer channelId) {
    return new ChannelKey(channelName, channelId);
  }

  /**
   * Creates a key with the provided recipient, operation id and channel arguments.
   *
   * @param username    the recipient
   * @param request     the channel arguments
   * @param operationId the operation id
   *
   * @return the key
   * @throws IllegalArgumentException wrong channel arguments provided
   */
  public SubscriptionKey createSubscriptionKey(
    String username,
    GraphQLRequest request,
    String operationId
  ) throws IllegalArgumentException {
    SubscriptionInput subscriptionInput = SubscriptionInputParser.parse(request);

    Integer channelId = Bus.MAIN_CHANNEL_KEY.getChannelId();
    String channelName = Bus.MAIN_CHANNEL_KEY.getChannelName();

    if (subscriptionInput != null) {
      channelId = subscriptionInput.getChannelId();
      channelName = subscriptionInput.getChannelName();

      ChannelKey channelKey = new ChannelKey(channelName, channelId);
      Channel channel = Bus.getInstance().getChannel(channelKey);
      if (channel == null) {
        registerChannel(channelKey);
        log.warn("Channel could not be found. " + channelName + ":" + channelId);
      }
    }

    return new SubscriptionKey(channelName, channelId, username, operationId);
  }

  /**
   * Gets a key with the provided recipient and operation id.
   *
   * @param operationId the operation id
   * @param username    the recipient
   *
   * @return the key
   */
  public SubscriptionKey getSubscriptionKey(String username, String operationId) {
    for (Channel channel : channels.values()) {
      SubscriptionKey key = channel.getSubscriptionManager().getKey(username, operationId);
      if (key != null) {
        return key;
      }
    }

    return null;
  }

  /**
   * Adds a new subscription mapping to the list of subscriptions.
   *
   * @param subscriptionKey asdasd
   * @param subscription    asdasd
   */
  public synchronized void subscribe(SubscriptionKey subscriptionKey, Subscription subscription) {
    ChannelKey channelKey =
      createKey(subscriptionKey.getChannelName(), subscriptionKey.getChannelId());
    Channel channel = channels.get(channelKey);
    channel.getSubscriptionManager().add(subscriptionKey, subscription);
  }

  /**
   * Unsubscribes a channel by removing and cancelling every subscription matching the channel and
   * unregistering the channel.
   *
   * @param key the channel key
   */
  public synchronized void unsubscribeChannel(ChannelKey key) {
    Channel channel = channels.get(key);
    channel.getSubscriptionManager().unsubscribe(key);

    if (!channel.hasListeners()) {
      unregisterChannel(key);
    }
  }

  /**
   * Removes and cancels a subscription mapping from the list of subscriptions and additionally
   * unregisters the matching channel if it doesn't contain any subscriptions.
   *
   * @param subscriptionKey the subscription key
   */
  public synchronized void unsubscribe(SubscriptionKey subscriptionKey) {
    ChannelKey channelKey =
      new ChannelKey(subscriptionKey.getChannelName(), subscriptionKey.getChannelId());
    Channel channel = channels.get(channelKey);
    channel.getSubscriptionManager().unsubscribe(subscriptionKey);

    if (!channel.hasListeners()) {
      unregisterChannel(subscriptionKey);
    }
  }

  /**
   * Unsubscribes any channel subscription made by the user with the provided username.
   *
   * @param username the username
   */
  public void unsubscribe(String username) {
    channels
      .forEach((channelKey, channel) -> channel.getSubscriptionManager().unsubscribe(username));
  }

  /**
   * Sends an event through the main channel.
   *
   * @param event the event
   */
  public synchronized void postMainChannel(Event event) {
    post(MAIN_CHANNEL_KEY, event);
  }

  /**
   * Sends an event through a channel by key and additionally checks if it contains any
   * subscribers.
   *
   * @param key   the channel key
   * @param event the event
   */
  public synchronized void post(ChannelKey key, Event event) {
    Channel channel = getChannel(key);
    Objects.requireNonNull(channel);

    if (!channel.hasListeners()) {
      return;
    }
    channel.post(event);
  }

  /**
   * Adds a new channel by key to the list.
   *
   * @param key the channel key
   */
  public synchronized void registerChannel(ChannelKey key) {
    Channel channel = getChannel(key);

    if (channel == null) {
      channel = new Channel();
      channels.put(key, channel);
    }
  }

  /**
   * Gets the main channel.
   *
   * @return the main channel.
   */
  public synchronized Channel getMainChannel() {
    return getChannel(MAIN_CHANNEL_KEY);
  }

  /**
   * Gets a channel by key and additionally checks if it exists.
   *
   * @param key the channel key
   *
   * @return the channel
   */
  public synchronized Channel getChannel(ChannelKey key) {
    if (channels.isEmpty() || !channels.containsKey(key)) {
      return null;
    }

    return channels.get(key);
  }

  /**
   * Removes a channel by key from the list and additionally checks if it has subscribers.
   *
   * @param key the channel key
   */
  private synchronized void unregisterChannel(ChannelKey key) {
    channels.remove(key);
  }

}
