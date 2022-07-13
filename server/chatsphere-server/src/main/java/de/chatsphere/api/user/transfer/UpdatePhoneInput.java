package de.chatsphere.api.user.transfer;

import de.chatsphere.api.shared.transfer.Input;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class UpdatePhoneInput implements Input {

  @Getter
  private String phone;

  public UpdatePhoneInput() {
  }

  @Override
  public UpdatePhoneInput fromMap(Map<String, Object> updatePhoneInputMap) {
    String phone = (String) updatePhoneInputMap.get("phone");

    return new UpdatePhoneInput(phone);
  }
}
