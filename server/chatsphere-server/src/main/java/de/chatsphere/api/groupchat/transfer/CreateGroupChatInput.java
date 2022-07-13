package de.chatsphere.api.groupchat.transfer;

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
public class CreateGroupChatInput implements Input {

  @Getter
  private List<String> participantUsernames;
  @Getter
  private String displayName;
  @Getter
  private String icon;
  @Getter
  private String fileName;

  public CreateGroupChatInput() {
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
  public CreateGroupChatInput fromMap(Map<String, Object> map) {
    // Convert List of "unknown" objects to List of integers
    List<String> participantUsernames = new ArrayList<>();
    for (Object obj : (ArrayList<Object>) map.get("participantUsernames")) {
      participantUsernames.add((String) obj);
    }

    String displayName = (String) map.get("displayName");
    String icon = (String) map.get("icon");
    String fileName = (String) map.get("fileName");

    return new CreateGroupChatInput(participantUsernames, displayName, icon, fileName);
  }
}
