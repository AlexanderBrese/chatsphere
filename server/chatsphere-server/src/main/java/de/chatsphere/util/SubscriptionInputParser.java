package de.chatsphere.util;

import de.chatsphere.api.shared.transfer.SubscriptionInput;
import graphql.servlet.internal.GraphQLRequest;
import java.util.Map;

public class SubscriptionInputParser {

  /**
   * asdasd.
   *
   * @param request asdasd.
   *
   * @return asdasd.
   */
  public static SubscriptionInput parse(GraphQLRequest request) {
    Map<String, Object> variables = request.getVariables();
    if (variables == null || !Util.hasObject(variables, "subscriptionInput")) {
      return null;
    }

    return Util.convertMap(variables, SubscriptionInput.class);
  }
}
