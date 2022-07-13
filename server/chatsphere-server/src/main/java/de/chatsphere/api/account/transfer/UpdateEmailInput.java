package de.chatsphere.api.account.transfer;

import de.chatsphere.api.shared.transfer.Input;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class UpdateEmailInput implements Input {

  @Getter
  private String email;

  public UpdateEmailInput() {
  }

  @Override
  public UpdateEmailInput fromMap(Map<String, Object> updateEmailInputMap) {
    String email = (String) updateEmailInputMap.get("email");

    return new UpdateEmailInput(email);
  }
}
