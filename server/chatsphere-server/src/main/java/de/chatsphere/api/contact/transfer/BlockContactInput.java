package de.chatsphere.api.contact.transfer;

import de.chatsphere.api.shared.transfer.Input;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class BlockContactInput implements Input {

  @Getter
  private String contactUsername;

  public BlockContactInput() {
  }

  @Override
  public BlockContactInput fromMap(Map<String, Object> blockAccountInputMap) {
    String contactUsername = (String) blockAccountInputMap.get("contactUsername");

    return new BlockContactInput(contactUsername);
  }
}
