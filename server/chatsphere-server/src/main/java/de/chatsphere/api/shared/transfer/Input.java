package de.chatsphere.api.shared.transfer;

import java.util.Map;

/**
 * Defines graphql input types.
 */
public interface Input {

  /**
   * Creates a new Input object from an input map object.
   *
   * @param inputMap the input map
   *
   * @return the input
   */
  Input fromMap(Map<String, Object> inputMap);
}
