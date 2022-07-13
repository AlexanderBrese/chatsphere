package de.chatsphere.api.privatechat.transfer;

import de.chatsphere.api.shared.transfer.Input;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Input for creating a group chat.
 */
@AllArgsConstructor
public class CreatePrivateChatInput implements Input {

  @Getter
  private String participantUsername;

  public CreatePrivateChatInput() {
  }

  /**
   * Initializes a new CreateGroupChatInput from a &lt;String,Object&gt; map.
   *
   * @param map contains the graphql input property fields
   *
   * @return a CreateGroupChatInput type
   */
  @Override
  @SuppressWarnings("unchecked")
  public CreatePrivateChatInput fromMap(Map<String, Object> map) {
    String participantUsername = (String) map.get("participantUsername");
    return new CreatePrivateChatInput(participantUsername);
  }
}
