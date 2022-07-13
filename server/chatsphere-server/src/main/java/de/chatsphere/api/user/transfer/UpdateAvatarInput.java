package de.chatsphere.api.user.transfer;

import de.chatsphere.api.shared.transfer.Input;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class UpdateAvatarInput implements Input {

  @Getter
  private String avatar;

  @Getter
  private String fileName;

  public UpdateAvatarInput() {
  }

  @Override
  public UpdateAvatarInput fromMap(Map<String, Object> updateAvatarInputMap) {
    String avatar = (String) updateAvatarInputMap.get("avatar");
    String fileName = (String) updateAvatarInputMap.get("fileName");

    return new UpdateAvatarInput(avatar, fileName);
  }
}
