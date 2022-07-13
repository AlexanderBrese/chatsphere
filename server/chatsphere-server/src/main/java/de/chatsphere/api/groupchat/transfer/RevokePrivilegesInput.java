package de.chatsphere.api.groupchat.transfer;

import de.chatsphere.api.shared.transfer.Input;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Input for granting privileges.
 */
@AllArgsConstructor
public class RevokePrivilegesInput implements Input {

  @Getter
  private int id;
  @Getter
  private int participantId;

  public RevokePrivilegesInput() {
  }

  /**
   * Initializes a new RevokePrivilegesInput from a &lt;String,Object&gt; map.
   *
   * @param map contains the graphql input property fields
   *
   * @return a RevokePrivilegesInput type
   */
  public RevokePrivilegesInput fromMap(Map<String, Object> map) {
    int id = (int) map.get("id");
    int participantId = (int) map.get("participantId");

    return new RevokePrivilegesInput(id, participantId);
  }
}
