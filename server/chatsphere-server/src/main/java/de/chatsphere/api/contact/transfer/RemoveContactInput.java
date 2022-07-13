package de.chatsphere.api.contact.transfer;

import de.chatsphere.api.shared.transfer.Input;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class RemoveContactInput implements Input {

  @Getter
  private String contactUsername;

  public RemoveContactInput() {
  }

  @Override
  public RemoveContactInput fromMap(Map<String, Object> removeContactInputMap) {
    String contactUsername = (String) removeContactInputMap.get("contactUsername");

    return new RemoveContactInput(contactUsername);
  }
}
