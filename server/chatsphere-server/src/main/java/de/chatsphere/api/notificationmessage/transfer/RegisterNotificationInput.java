package de.chatsphere.api.notificationmessage.transfer;

import de.chatsphere.api.shared.transfer.Input;
import java.security.Security;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * Body of a request that a service worker may send to convey they will listen to push
 * notifications.
 */
@AllArgsConstructor
public class RegisterNotificationInput implements Input {

  @Getter
  private String endpoint;
  @Getter
  private String key;
  @Getter
  private String auth;

  public RegisterNotificationInput() {
  }

  /**
   * Initializes a new RegisterNotificationInput from a &lt;String,Object&gt; map map.
   *
   * @param map contains the graphql input property fields
   *
   * @return a RegisterNotificationInput type
   */
  @SuppressWarnings("unchecked")
  public RegisterNotificationInput fromMap(Map<String, Object> map) {

    System.out.println(map);

    // Add BouncyCastle as an algorithm provider
    if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
      Security.addProvider(new BouncyCastleProvider());
    }

    String endpoint = (String) map.get("endpoint");
    String key = (String) map.get("key");
    String auth = (String) map.get("auth");

    return new RegisterNotificationInput(endpoint, key, auth);
  }
}
