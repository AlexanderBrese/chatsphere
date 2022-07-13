package de.chatsphere.api.notificationmessage.model;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.UpdateBuilder;
import de.chatsphere.api.account.model.AccountRepository;
import de.chatsphere.api.chat.transfer.AbstractChat;
import de.chatsphere.api.notificationmessage.transfer.CreateNotificationMessageInput;
import de.chatsphere.api.notificationmessage.transfer.NotificationEventDto;
import de.chatsphere.api.notificationmessage.transfer.NotificationMessageDto;
import de.chatsphere.api.notificationmessage.transfer.RegisterNotificationInput;
import de.chatsphere.api.notificationmessage.transfer.UpdateNotificationMessageInput;
import de.chatsphere.api.shared.model.Reportable;
import de.chatsphere.api.shared.transfer.SubscriptionInput;
import de.chatsphere.io.database.schema.user.Receiver;
import de.chatsphere.io.database.schema.user.User;
import de.chatsphere.server.graphql.Context;
import de.chatsphere.server.rxbus.Bus;
import de.chatsphere.server.rxbus.ChannelKey;
import graphql.GraphQLException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import nl.martijndwars.webpush.Utils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jose4j.lang.JoseException;
import org.seedstack.coffig.Coffig;
import org.seedstack.coffig.provider.JacksonProvider;

/**
 * Handle access to database. All the notification-specific data needs and queries are resolved
 * here.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NotificationRepository implements Reportable {

  /**
   * The Time to live of GCM notifications.
   */
  private static final int TTL = 255;

  /**
   * Need access to properties of config.
   */
  private static final Coffig coffig = Coffig
    .builder()
    .withProviders(new JacksonProvider()
      .addSource(NotificationRepository.class.getClassLoader().getResource("properties.yaml")))
    .build();

  /**
   * <p>Fetch the data from the database to fill in the following fields.</p>
   *
   * <p>
   * id: Int!,
   * author: User!,
   * text: String!,
   * #notify: Notification!,
   * recipient: User!,
   * to: Chat!
   * </p>
   *
   * @param context {@link Context}
   * @param id      of message
   *
   * @return the message with the given id or null
   */
  public static NotificationMessageDto getNotificationMessage(Context context, int id) {
    System.out.println(id);

    return null;
  }

  /**
   * Append message to chat.
   *
   * @param context {@link Context}
   *
   * @return chat with new message
   */
  public static Boolean createNotificationMessage(Context context,
    CreateNotificationMessageInput input) {

    // prepare recipients
    List<String> recipients = new LinkedList<>();
    recipients.add(input.getRecipientName());

    NotificationEventDto notificationEventDto = new NotificationEventDto(
      context.getAuthenticator().getUsername(),
      recipients,
      input.getText(),
      input.getChatId()
    );

    // Send
    Bus.getInstance().postMainChannel(notificationEventDto);
    return true;
  }

  /**
   * Modifies an existing message.
   *
   * @param context {@link Context}
   *
   * @return the modified notification message
   */
  public static NotificationMessageDto updateNotificationMessage(
    Context context, UpdateNotificationMessageInput updateNotificationMessageInput) {
    System.out.println(updateNotificationMessageInput);

    // TODO: Modify here

    return null;
  }


  /**
   * deprecated.
   *
   * @param receiver n/a
   * @param payload n/a
   */
  private static void sendPushMessage(Receiver receiver, byte[] payload) {

    logger.info("Sending push notification to " + receiver.getEndpoint());

    // Create a notification with the endpoint, userPublicKey from the subscription and a custom payload
    Notification notification = null;
    try {
      notification = new Notification(
        receiver.getEndpoint(),
        receiver.getUserPublicKey(),
        receiver.getAuthAsBytes(),
        payload
      );
    } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeySpecException e) {
      e.printStackTrace();
      logger.error(e.getMessage());
    }

    // Instantiate the push service, no need to use an API key for Push API
    PushService pushService = new PushService();

    // Send the notification
    try {
      pushService
        .setPublicKey(Utils.loadPublicKey(coffig.get(String.class, "pushnotifications.publickey")));
      pushService.setPrivateKey(
        Utils.loadPrivateKey(coffig.get(String.class, "pushnotifications.privatekey")));
      pushService.send(notification);
    } catch (JoseException | InterruptedException | GeneralSecurityException | IOException | ExecutionException e) {
      e.printStackTrace();
      logger.error(e.getMessage());
    }
  }
}
