package de.chatsphere.api.link.transfer;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * A data transmitter object for links. (@see <a href="https://en.wikipedia.org/wiki/Data_transfer_object">DATA_TRANSFER_OBJECT.md</a>)
 */
@AllArgsConstructor
public class LinkDto {
  @Getter
  private final Integer id;
  @Getter
  private final String url;
  @Getter
  private final String description;
}
