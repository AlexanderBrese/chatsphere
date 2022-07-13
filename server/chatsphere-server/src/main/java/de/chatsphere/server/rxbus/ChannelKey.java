package de.chatsphere.server.rxbus;

import java.util.Objects;
import java.util.StringJoiner;
import lombok.Getter;

/**
 * The key is used to identify a channel.
 */
public class ChannelKey {

  @Getter
  private final String channelName;
  @Getter
  private final Integer channelId;
  @Getter
  private final String separator;

  /**
   * Initializes a new channel key.
   *
   * @param channelName the channelName of channel, e.g. chat
   * @param channelId   the id of channel, e.g. 123203
   */
  public ChannelKey(String channelName, Integer channelId) {
    this.channelName = channelName;
    this.separator = ":";
    this.channelId = channelId;
  }

  /**
   * Initializes a new channel key that is identified by channelName only, e.g. main.
   *
   * @param channelName the channelName
   */
  ChannelKey(String channelName) {
    this.channelName = channelName;
    this.separator = ":";
    this.channelId = 0;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ChannelKey)) {
      return false;
    }
    ChannelKey key = (ChannelKey) o;
    return Objects.equals(getChannelName(), key.getChannelName())
      && Objects.equals(getChannelId(), key.getChannelId());
  }

  @Override
  public int hashCode() {

    return Objects.hash(getChannelName(), getChannelId(), getSeparator());
  }

  @Override
  public String toString() {
    StringJoiner joiner = new StringJoiner(";");
    joiner
      .add("channelName" + getSeparator() + getChannelName())
      .add("channelId" + getSeparator() + getChannelId());

    return joiner.toString();
  }
}
