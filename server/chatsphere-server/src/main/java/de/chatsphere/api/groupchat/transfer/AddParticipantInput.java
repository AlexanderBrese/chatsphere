package de.chatsphere.api.groupchat.transfer;

import de.chatsphere.api.shared.transfer.Input;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Input for adding a participant.
 */
@AllArgsConstructor
public class AddParticipantInput implements Input {

  @Getter
  private int participantId;
  @Getter
  private int id;

  public AddParticipantInput() {
  }

  /**
   * Initializes a new AddParticipantInput from a &lt;String,Object&gt; map.
   *
   * @param map contains the graphql input property fields
   *
   * @return a AddParticipantInput type
   */
  @Override
  public AddParticipantInput fromMap(Map<String, Object> map) {
    int participantId = (int) map.get("participantId");
    int id = (int) map.get("id");

    return new AddParticipantInput(participantId, id);
  }

}
