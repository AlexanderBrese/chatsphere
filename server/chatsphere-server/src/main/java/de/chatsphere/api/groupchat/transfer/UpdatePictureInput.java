package de.chatsphere.api.groupchat.transfer;

import de.chatsphere.api.shared.transfer.Input;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Input for removing a participant.
 */
@AllArgsConstructor
public class UpdatePictureInput implements Input {

  @Getter
  private int id;
  @Getter
  private String picture;

  public UpdatePictureInput() {
  }

  /**
   * Initializes a new UpdatePictureInput from a &lt;String,Object&gt; map.
   *
   * @param map contains the graphql input property fields
   *
   * @return a UpdatePictureInput type
   */
  public UpdatePictureInput fromMap(Map<String, Object> map) {
    int id = (int) map.get("id");
    String picture = (String) map.get("picture");

    return new UpdatePictureInput(id, picture);
  }
}
