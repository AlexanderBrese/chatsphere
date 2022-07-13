package de.chatsphere.api.account.transfer;

import de.chatsphere.api.shared.transfer.Input;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class LoginInput implements Input {

  @Getter
  private String username;
  @Getter
  private String password;

  public LoginInput() {
  }

  @Override
  public LoginInput fromMap(Map<String, Object> loginInputMap) {
    String username = (String) loginInputMap.get("username");
    String password = (String) loginInputMap.get("password");

    return new LoginInput(username, password);
  }
}
