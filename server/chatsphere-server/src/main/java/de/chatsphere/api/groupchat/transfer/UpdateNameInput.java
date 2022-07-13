package de.chatsphere.api.groupchat.transfer;

import de.chatsphere.api.shared.transfer.Input;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Input for changing the name.
 */
@AllArgsConstructor
public class UpdateNameInput implements Input {

  @Getter
  private int id;
  @Getter
  private String name;

  public UpdateNameInput() {
  }

  /**
   * Initializes a new UpdateNameInput from a &lt;String,Object&gt; map.
   *
   * @param map contains the graphql input property fields
   *
   * @return a UpdateNameInput type
   */
  public UpdateNameInput fromMap(Map<String, Object> map) {
    int id = (int) map.get("id");
    String name = (String) map.get("name");

    return new UpdateNameInput(id, name);
  }
}
