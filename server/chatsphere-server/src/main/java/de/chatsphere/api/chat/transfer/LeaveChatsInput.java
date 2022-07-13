package de.chatsphere.api.chat.transfer;

import de.chatsphere.api.shared.transfer.Input;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Input for leaving/removing a chat.
 */
@AllArgsConstructor
public class LeaveChatsInput implements Input {

  @Getter
  private List<Integer> chatIds;

  public LeaveChatsInput() {
  }

  /**
   * Initializes a new LeaveChatsInput from a &lt;String,Object&gt; map.
   *
   * @param map contains the graphql input property fields
   * @return a LeaveChatsInput type
   */
  @Override
  public LeaveChatsInput fromMap(Map<String, Object> map) {
    // Convert List of "unknown" objects to List of integers
    List<Integer> chatIds = new ArrayList<>();

    @SuppressWarnings("unchecked")
    ArrayList<Object> converted = (ArrayList<Object>) map.get("chatIds");

    for (Object obj : converted) {
      chatIds.add((Integer) obj);
    }

    return new LeaveChatsInput(chatIds);
  }
}
