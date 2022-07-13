package de.chatsphere.api.message.transfer;

import de.chatsphere.api.shared.transfer.Input;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Input for removing a message.
 */
@AllArgsConstructor
public class RemoveMessagesInput implements Input {

  @Getter
  private List<Integer> messageIds;

  public RemoveMessagesInput() {
  }

  /**
   * Initializes a new RemoveMessagesInput from a &lt;String,Object&gt; map.
   *
   * @param map contains the graphql input property fields
   *
   * @return a RemoveMessagesInput type
   */
  public RemoveMessagesInput fromMap(Map<String, Object> map) {
    // Convert List of "unknown" objects to List of integers
    List<Integer> messageIds = new ArrayList<>();

    @SuppressWarnings("unchecked")
    ArrayList<Object> converted = (ArrayList<Object>) map.get("messageIds");

    for (Object obj : converted) {
      messageIds.add((Integer) obj);
    }

    return new RemoveMessagesInput(messageIds);
  }
}
