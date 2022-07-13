package de.chatsphere.api.plainmessage.resolver;

import com.j256.ormlite.dao.Dao;
import de.chatsphere.api.account.model.AccountRepository;
import de.chatsphere.api.chat.model.ChatRepository;
import de.chatsphere.api.chat.transfer.AbstractChat;
import de.chatsphere.api.chat.transfer.ChatAddedEventDto;
import de.chatsphere.api.chat.transfer.ChatModifiedEventDto;
import de.chatsphere.api.message.transfer.AbstractMessage;
import de.chatsphere.api.message.transfer.CreateMessageInput;
import de.chatsphere.api.message.transfer.MessageAddedEventDto;
import de.chatsphere.api.message.transfer.MessageUpdatedEventDto;
import de.chatsphere.api.message.transfer.UpdateMessageInput;
import de.chatsphere.api.notificationmessage.model.NotificationRepository;
import de.chatsphere.api.notificationmessage.transfer.CreateNotificationMessageInput;
import de.chatsphere.api.plainmessage.model.PlainMessageRepository;
import de.chatsphere.api.plainmessage.transfer.PlainMessageDto;
import de.chatsphere.api.privatechat.transfer.PrivateChatDto;
import de.chatsphere.io.database.schema.chat.Chat;
import de.chatsphere.io.database.schema.chat.ChatLine;
import de.chatsphere.io.database.schema.chat.ChatParticipant;
import de.chatsphere.io.database.schema.preference.enumeration.Notifiable;
import de.chatsphere.io.database.schema.preference.enumeration.Visibility;
import de.chatsphere.io.database.schema.preference.enumeration.Visibility.Level;
import de.chatsphere.server.graphql.Context;
import de.chatsphere.server.graphql.schema.annotation.DataFetcherWiring;
import de.chatsphere.server.rxbus.Bus;
import de.chatsphere.server.rxbus.ChannelKey;
import de.chatsphere.util.Util;
import graphql.GraphQLException;
import graphql.schema.DataFetcher;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlainMessageResolver {

  private static final Logger log = LoggerFactory.getLogger(PlainMessageResolver.class);

  /**
   * asdasd.
   *
   * @return asdasd.
   */
  @DataFetcherWiring(
    type = "Mutation",
    name = "createPlainMessage"
  )
  public DataFetcher<PlainMessageDto> createPlainMessage() {
    return environment -> {
      Context context = environment.getContext();

      CreateMessageInput createMessageInput =
        Util.convertMap(environment.getArguments(), CreateMessageInput.class);

      PlainMessageDto plainMessage = PlainMessageRepository
        .createPlainMessage(context, createMessageInput);

      List<ChatParticipant> chatParticipants =
        ChatRepository.getChatParticipants(context, createMessageInput.getChatId());

      List<String> recipients = new LinkedList<>();
      chatParticipants.forEach(chatParticipant -> {
        String participantName = chatParticipant.getParticipant().getProfile().getName();
        if (!participantName.equals(context.getAuthenticator().getUsername())) {
          recipients.add(participantName);

          // Send out Notifications
          Notifiable notify = AccountRepository.getPreferences(context, chatParticipant.getParticipant()).getNotification();

          // Only send it if not blocked
          if (notify.getDescription() == Notifiable.Level.NOTIFY) {
            String message = context.getAuthenticator().getUsername() + ": " + createMessageInput.getText();
            NotificationRepository.createNotificationMessage(
              context, new CreateNotificationMessageInput(message, participantName, createMessageInput.getChatId()));
          }

          if (!chatParticipant.getVisibility()) {
            chatParticipant.setVisibility(true);
            Dao<ChatParticipant, Integer> chatParticipantDao =
              context.getDatabase().getDao(ChatParticipant.class);
            try {
              chatParticipantDao.update(chatParticipant);
            } catch (SQLException e) {
              log.error(e.getMessage());
              throw new GraphQLException("An Internal error occurred");
            }

            ChatAddedEventDto chatAddedEventDto = new ChatAddedEventDto(
              context.getAuthenticator().getUsername(),
              recipients,
              PrivateChatDto.from(chatParticipant.getChat(), context)
            );

            Bus.getInstance().postMainChannel(chatAddedEventDto);
          }
        }
      });

      MessageAddedEventDto messageAddedEventDto = new MessageAddedEventDto(
        context.getAuthenticator().getUsername(),
        recipients,
        plainMessage
      );

      ChannelKey channelKey = Bus.getInstance().createKey("chat",
        createMessageInput.getChatId());

      Bus.getInstance().post(channelKey, messageAddedEventDto);
      Chat chat = chatParticipants.get(0).getChat();
      String displayName = AccountRepository.getCurrentUser(context).getProfile().getDisplayName();
      ChatModifiedEventDto chatAddedEventDto = new ChatModifiedEventDto(
        context.getAuthenticator().getUsername(),
        recipients,
        ChatRepository.chat(context, chat, displayName)
      );
      Bus.getInstance().postMainChannel(chatAddedEventDto);
      return plainMessage;
    };
  }

  /**
   * asdasd.
   *
   * @return asdasd.
   */
  @DataFetcherWiring(
    type = "Mutation",
    name = "updatePlainMessage"
  )
  public DataFetcher<Boolean> updatePlainMessage() {
    return environment -> {
      Context context = environment.getContext();

      UpdateMessageInput updateMessageInput =
        Util.convertMap(environment.getArguments(), UpdateMessageInput.class);

      ChatLine chatLine = PlainMessageRepository.updatePlainMessage(context, updateMessageInput);
      Chat chat = chatLine.getChat();
      Integer chatId = chat.getId();
      ChannelKey channelKey = Bus.getInstance().createKey("chat", chatId);
      List<ChatParticipant> chatParticipants =
        ChatRepository.getChatParticipants(context, chatId);
      List<String> recipients = new LinkedList<>();
      chatParticipants.forEach(chatParticipant -> {
        recipients.add(chatParticipant.getParticipant().getProfile().getName());
      });
      MessageUpdatedEventDto messageUpdatedEventDto = new MessageUpdatedEventDto(
        context.getAuthenticator().getUsername(),
        recipients,
        AbstractMessage.from(chatLine)
      );

      Bus.getInstance().post(channelKey, messageUpdatedEventDto);
      String displayName = AccountRepository.getCurrentUser(context).getProfile().getDisplayName();
      ChatModifiedEventDto chatAddedEventDto = new ChatModifiedEventDto(
        context.getAuthenticator().getUsername(),
        recipients,
        ChatRepository.chat(context, chat, displayName)
      );
      Bus.getInstance().postMainChannel(chatAddedEventDto);
      return true;
    };
  }
}
