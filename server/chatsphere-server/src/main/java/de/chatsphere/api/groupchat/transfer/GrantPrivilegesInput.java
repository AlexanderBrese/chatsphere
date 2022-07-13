package de.chatsphere.api.groupchat.transfer;

import de.chatsphere.api.shared.transfer.Input;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Input for granting privileges.
 */
@AllArgsConstructor
public class GrantPrivilegesInput implements Input {

  @Getter
  private int id;
  @Getter
  private int participantId;

  public GrantPrivilegesInput() {
  }

  /**
   * Initializes a new GrantPrivilegesInput from a &lt;String,Object&gt; map.
   *
   * @param map contains the graphql input property fields
   *
   * @return a GrantPrivilegesInput type
   */
  @Override
  public GrantPrivilegesInput fromMap(Map<String, Object> map) {
    int id = (int) map.get("id");
    int participantId = (int) map.get("participantId");

    return new GrantPrivilegesInput(id, participantId);
  }
}
