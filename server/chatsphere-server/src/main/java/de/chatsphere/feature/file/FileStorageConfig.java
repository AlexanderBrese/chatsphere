package de.chatsphere.feature.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FileStorageConfig {

  @Getter
  private String downloadUrl = "http://localhost/files/{0}";
  @Getter
  private String workingDirectory = "files/";
  @Getter
  private String hashingAlgorithm = "de.chatsphere.feature.file.hash.Sha256HashingAlgorithm";
}
