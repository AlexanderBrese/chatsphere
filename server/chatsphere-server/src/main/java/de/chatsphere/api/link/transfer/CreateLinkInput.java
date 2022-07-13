package de.chatsphere.api.link.transfer;

import de.chatsphere.api.shared.transfer.Input;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * A GraphQL query input type. Those are arguments to create a link.
 * (@see <a href="https://graphql.org/graphql-js/mutations-and-input-types/">INPUT_TYPES.md</a>)
 */
@AllArgsConstructor
public class CreateLinkInput implements Input {

  @Getter
  private String url;
  @Getter
  private String description;

  public CreateLinkInput() {
  }

  @Override
  public CreateLinkInput fromMap(Map<String, Object> linkInputMap) {
    String url = (String) linkInputMap.get("url");
    String description = (String) linkInputMap.get("description");

    return new CreateLinkInput(url, description);
  }
}
