package de.chatsphere.api.groupchat.transfer;

import de.chatsphere.api.shared.transfer.Input;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Input for removing a participant.
 */
@AllArgsConstructor
public class RemoveParticipantInput implements Input {

  @Getter
  private int participantId;
  @Getter
  private int id;

  public RemoveParticipantInput() {
  }

  /**
   * Initializes a new RemoveParticipantInput from a &lt;String,Object&gt; map.
   *
   * @param map contains the graphql input property fields
   *
   * @return a RemoveParticipantInput type
   */
  public RemoveParticipantInput fromMap(Map<String, Object> map) {
    int participantId = (int) map.get("participantId");
    int id = (int) map.get("id");

    return new RemoveParticipantInput(participantId, id);
  }
}
