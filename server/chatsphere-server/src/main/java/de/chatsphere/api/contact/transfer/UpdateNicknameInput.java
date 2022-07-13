package de.chatsphere.api.contact.transfer;

import de.chatsphere.api.shared.transfer.Input;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class UpdateNicknameInput implements Input {

  @Getter
  private String contactUsername;
  @Getter
  private String nickname;

  public UpdateNicknameInput() {
  }

  @Override
  public UpdateNicknameInput fromMap(Map<String, Object> updateNicknameInputMap) {
    String contactUsername = (String) updateNicknameInputMap.get("contactUsername");
    String nickname = (String) updateNicknameInputMap.get("nickname");

    return new UpdateNicknameInput(contactUsername, nickname);
  }
}
