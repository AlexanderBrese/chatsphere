package de.chatsphere.api.shared.transfer;

import de.chatsphere.server.rxbus.Event;
import java.util.List;

public class DefaultEvent extends Event {

  /**
   * Initializes a default event implementation.
   *
   * @param sender     the event sender
   * @param recipients the event recipient
   */
  public DefaultEvent(String sender, List<String> recipients) {
    super(sender, recipients);
  }
}
