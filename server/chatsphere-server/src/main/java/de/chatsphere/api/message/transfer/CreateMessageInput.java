package de.chatsphere.api.message.transfer;

import de.chatsphere.api.shared.transfer.Input;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Input for removing a message.
 */
@AllArgsConstructor
public class CreateMessageInput implements Input {

  @Getter
  private String text;
  @Getter
  private String fileName;
  @Getter
  private String encoded;
  @Getter
  private Integer chatId;

  public CreateMessageInput() {
  }

  /**
   * Initializes a new CreateMessageInput from a &lt;String,Object&gt; map.
   *
   * @param map contains the graphql input property fields
   *
   * @return a CreateMessageInput type
   */
  public CreateMessageInput fromMap(Map<String, Object> map) {
    String text = (String) map.get("text");
    String fileName = (String) map.get("fileName");
    String encoded = (String) map.get("encoded");
    Integer chatId = (Integer) map.get("chatId");

    return new CreateMessageInput(text, fileName, encoded, chatId);
  }
}
