package de.chatsphere.server.rxbus;

import java.util.Objects;
import java.util.StringJoiner;
import lombok.Getter;

/**
 * The subscription key is used to identify subscriptions made to a channel.
 */
public class SubscriptionKey extends ChannelKey {

  @Getter
  private final String subscriber;
  @Getter
  private final String operationId;

  /**
   * Initializes a new subscription key.
   *
   * @param channelName the channel name
   * @param channelId   the channel id
   * @param subscriber  the subscriber
   */
  public SubscriptionKey(
    String channelName,
    Integer channelId,
    String subscriber,
    String operationId) {
    super(channelName, channelId);
    this.subscriber = subscriber;
    this.operationId = operationId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof SubscriptionKey)) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    SubscriptionKey that = (SubscriptionKey) o;
    return Objects.equals(getSubscriber(), that.getSubscriber())
      && Objects.equals(getOperationId(), that.getOperationId());
  }

  @Override
  public int hashCode() {
    return super.hashCode() + Objects.hash(getSubscriber());
  }

  @Override
  public String toString() {
    StringJoiner joiner = new StringJoiner(";");
    joiner
      .add("subscriber" + super.getSeparator() + getSubscriber())
      .add("operationId" + super.getSeparator() + getOperationId())
      .add(super.toString());

    return joiner.toString();
  }
}
