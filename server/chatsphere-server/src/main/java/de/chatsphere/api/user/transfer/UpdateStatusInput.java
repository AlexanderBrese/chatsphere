package de.chatsphere.api.user.transfer;

import de.chatsphere.api.shared.transfer.Input;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class UpdateStatusInput implements Input {

  @Getter
  private String status;

  public UpdateStatusInput() {
  }

  @Override
  public UpdateStatusInput fromMap(Map<String, Object> updateStatusInputMap) {
    String status = (String) updateStatusInputMap.get("status");

    return new UpdateStatusInput(status);
  }
}
