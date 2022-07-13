package de.chatsphere.api.user.transfer;

import de.chatsphere.api.shared.transfer.Input;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class UpdateDisplayNameInput implements Input {

  @Getter
  private String displayName;

  public UpdateDisplayNameInput() {
  }

  @Override
  public UpdateDisplayNameInput fromMap(Map<String, Object> updateDisplayNameInputMap) {
    String displayName = (String) updateDisplayNameInputMap.get("displayName");

    return new UpdateDisplayNameInput(displayName);
  }
}
