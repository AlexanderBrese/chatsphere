package de.chatsphere.api.shared.transfer;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class SubscriptionInput implements Input {

  @Getter
  private String channelName;
  @Getter
  private Integer channelId;

  public SubscriptionInput() {
  }

  @Override
  public Input fromMap(Map<String, Object> inputMap) {
    String channelName = (String) inputMap.get("channelName");
    Integer channelId = (Integer) inputMap.get("channelId");

    return new SubscriptionInput(channelName, channelId);
  }
}
