package de.chatsphere.api.notificationmessage.resolver;

import de.chatsphere.api.notificationmessage.model.NotificationRepository;
import de.chatsphere.api.notificationmessage.transfer.CreateNotificationMessageInput;
import de.chatsphere.api.notificationmessage.transfer.NotificationEventDto;
import de.chatsphere.api.notificationmessage.transfer.NotificationMessageDto;
import de.chatsphere.api.notificationmessage.transfer.UpdateNotificationMessageInput;
import de.chatsphere.server.graphql.schema.annotation.DataFetcherWiring;
import de.chatsphere.server.rxbus.Bus;
import de.chatsphere.util.Util;
import graphql.schema.DataFetcher;
import lombok.NoArgsConstructor;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The execution of the specified queries/mutations/subscriptions start in their homonymous function
 * in this class.
 */
@NoArgsConstructor
public final class NotificationResolver {

  private static final Logger log = LoggerFactory.getLogger(NotificationResolver.class);

  /**
   * Return a notification message by id.
   *
   * @return message with specific id
   */
  @DataFetcherWiring(
    type = "Query",
    name = "notificationMessage"
  )
  public DataFetcher<NotificationMessageDto> notificationMessage() {
    return environment ->
      NotificationRepository.getNotificationMessage(
        environment.getContext(), (int) environment.getArgument("id"));
  }

  /**
   * User provides information to how to contact his browser vendor's push server Subsequently
   * confirms he wants to listen to push notifications.
   *
   * @return true if everything was gucci
   */
  @DataFetcherWiring(
    type = "Subscription",
    name = "notificationReceived"
  )
  public DataFetcher<Publisher<NotificationEventDto>> notificationReceived() {
    return environment ->
      Bus.getInstance().getMainChannel().asFlowable(NotificationEventDto.class);
  }

  /**
   * Adds a notification message to the chat.
   *
   * @return chat to which it has been sent
   */
  @DataFetcherWiring(
    type = "Mutation",
    name = "createNotificationMessage"
  )
  public DataFetcher<Boolean> createNotificationMessage() {
    return environment -> {

      CreateNotificationMessageInput converted
        = Util.convertMap(environment.getArguments(), CreateNotificationMessageInput.class);

      return NotificationRepository.createNotificationMessage(environment.getContext(), converted);
    };
  }

  /**
   * Updates a notification message.
   *
   * @return notifaction message that has been updated
   */
  @DataFetcherWiring(
    type = "Mutation",
    name = "updateNotificationMessage"
  )
  public DataFetcher<NotificationMessageDto> updateNotificationMessage() {
    return environment -> {

      UpdateNotificationMessageInput converted
        = Util.convertMap(environment.getArguments(), UpdateNotificationMessageInput.class);

      return NotificationRepository.updateNotificationMessage(environment.getContext(), converted);
    };
  }
}
