package de.chatsphere.server.rxbus;

import java.util.List;
import java.util.StringJoiner;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The event is used to be passed around a channel of subscribers.
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Event {

  /**
   * This flag indicates that everybody is a valid recipient.
   */
  public static final String EVERYBODY = "";
  private static final Logger log = LoggerFactory.getLogger(Event.class);
  @Getter
  private final String sender;
  @Getter
  private final List<String> recipients;

  /**
   * Compares another sender for identity.
   *
   * @param other the other sender
   *
   * @return true/false
   */
  public boolean isIdenticalSender(String other) {
    return sender != null
      && sender.equals(other);
  }

  /**
   * Compares another recipient for identity.
   *
   * @param other the other recipient
   *
   * @return true/false
   */
  public boolean isRecipient(String other) {
    return !recipients.isEmpty()
      && recipients.contains(other);
  }

  @Override
  public String toString() {
    StringJoiner joiner = new StringJoiner("\n");
    joiner
      .add("\tSender: " + getSender())
      .add("\tRecipient: " + getRecipients());

    return joiner.toString();
  }
}
