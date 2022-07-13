package de.chatsphere.api.user.transfer;

import de.chatsphere.server.rxbus.Event;
import java.util.List;
import lombok.Getter;

public class UserAddedEventDto extends Event {

  @Getter
  private final UserDto user;

  public UserAddedEventDto(String sender, List<String> recipients, UserDto userDto) {
    super(sender, recipients);
    this.user = userDto;
  }
}
