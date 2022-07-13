package de.chatsphere.api.message.transfer;

import de.chatsphere.api.shared.transfer.Input;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Input for updating a message.
 */
@AllArgsConstructor
public class UpdateMessageInput implements Input {

  @Getter
  private Integer messageId;
  @Getter
  private String text;

  public UpdateMessageInput() {
  }

  /**
   * Initializes a new UpdateMessageInput from a &lt;String,Object&gt; map.
   *
   * @param map contains the graphql input property fields
   *
   * @return a UpdateMessageInput type
   */
  public UpdateMessageInput fromMap(Map<String, Object> map) {
    Integer messageId = (Integer) map.get("messageId");
    String text = (String) map.get("text");

    return new UpdateMessageInput(messageId, text);
  }
}
