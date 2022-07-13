package de.chatsphere.api.contact.transfer;

import de.chatsphere.api.shared.transfer.Input;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class UnblockContactInput implements Input {

  @Getter
  private String contactUsername;

  public UnblockContactInput() {
  }

  @Override
  public UnblockContactInput fromMap(Map<String, Object> unblockAccountInputMap) {
    String contactUsername = (String) unblockAccountInputMap.get("contactUsername");

    return new UnblockContactInput(contactUsername);
  }
}
