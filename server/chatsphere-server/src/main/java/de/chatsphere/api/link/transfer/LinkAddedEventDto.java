package de.chatsphere.api.link.transfer;

import de.chatsphere.server.rxbus.Event;
import java.util.ArrayList;
import java.util.Arrays;
import lombok.Getter;

public class LinkAddedEventDto extends Event {

  @Getter
  private final LinkDto link;

  /**
   * Initializes a new link added event.
   *
   * @param sender    the event sender
   * @param recipient the event recipient
   * @param link      the link
   */
  public LinkAddedEventDto(String sender, String recipient, LinkDto link) {
    super(sender, new ArrayList<>(Arrays.asList(recipient)));
    this.link = link;
  }
}
