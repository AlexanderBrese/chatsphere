package de.chatsphere.api.contact.transfer;

import de.chatsphere.api.shared.transfer.Input;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class CreateContactInput implements Input {

  @Getter
  private String contactUsername;

  public CreateContactInput() {
  }

  @Override
  public CreateContactInput fromMap(Map<String, Object> createContactInputMap) {
    String contactUsername = (String) createContactInputMap.get("contactUsername");

    return new CreateContactInput(contactUsername);
  }
}
