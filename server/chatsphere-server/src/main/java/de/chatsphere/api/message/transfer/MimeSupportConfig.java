package de.chatsphere.api.message.transfer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MimeSupportConfig {

  @Getter
  private String[] video = {"video/mp4", "video/webm"};
  @Getter
  private String[] picture = {"image/jpg", "image/jpeg", "image/gif", "image/png"};
  @Getter
  private String[] document = {"application/pdf"};
  @Getter
  private String[] audio = {"audio/wav", "audio/mpeg"};
}
