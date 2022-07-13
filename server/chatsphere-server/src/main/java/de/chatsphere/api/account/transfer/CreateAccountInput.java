package de.chatsphere.api.account.transfer;

import de.chatsphere.api.shared.transfer.Input;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class CreateAccountInput implements Input {

  @Getter
  private String email;
  @Getter
  private String username;
  @Getter
  private String password;

  public CreateAccountInput() {
  }

  @Override
  public CreateAccountInput fromMap(Map<String, Object> createAccountInputMap) {
    String email = (String) createAccountInputMap.get("email");
    String username = (String) createAccountInputMap.get("username");
    String password = (String) createAccountInputMap.get("password");

    return new CreateAccountInput(email, username, password);
  }
}
