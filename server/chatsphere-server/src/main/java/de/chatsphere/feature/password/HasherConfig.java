package de.chatsphere.feature.password;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@AllArgsConstructor
@Data
@Builder
public class HasherConfig {

  /**
   * String that is appended to all passwords. If this is changed, old passwords can no longer be
   * verified.
   */
  @NonNull
  private final String pepper = "";

  /**
   * Time in milliseconds to execute the key derivation function. The key derivation function
   * parameters are automatically adjusted accordingly.
   *
   * @param duration Changes are only applied to new hashes.
   */
  @NonNull
  private final Integer duration = 1000;

  /**
   * Memory usage per execution.
   *
   * @param memory Changes are only applied to new hashes.
   */
  @NonNull
  private final Integer memory = 65536;

  /**
   * Number of threads per execution.
   *
   * @param parallelism Changes are only applied to new hashes.
   */
  @NonNull
  private final Integer parallelism = 1;
}
