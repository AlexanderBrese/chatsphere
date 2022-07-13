package de.chatsphere.api.account.transfer;

import de.chatsphere.api.shared.transfer.Input;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class UpdatePasswordInput implements Input {

  @Getter
  private String password;

  public UpdatePasswordInput() {
  }

  @Override
  public UpdatePasswordInput fromMap(Map<String, Object> updatePasswordInputMap) {
    String password = (String) updatePasswordInputMap.get("password");

    return new UpdatePasswordInput(password);
  }
}
