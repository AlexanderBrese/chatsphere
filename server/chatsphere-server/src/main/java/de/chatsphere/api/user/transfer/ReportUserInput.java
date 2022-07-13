package de.chatsphere.api.user.transfer;

import de.chatsphere.api.shared.transfer.Input;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class ReportUserInput implements Input {

  @Getter
  private String reportedUsername;
  @Getter
  private String reason;

  public ReportUserInput() {
  }

  @Override
  public ReportUserInput fromMap(Map<String, Object> reportUsernameInputMap) {
    String reportedUsername = (String) reportUsernameInputMap.get("reportedUsername");
    String reason = (String) reportUsernameInputMap.get("reason");

    return new ReportUserInput(reportedUsername, reason);
  }
}
