package de.chatsphere.api.chat.resolver;

import de.chatsphere.api.account.model.AccountRepository;
import de.chatsphere.api.chat.model.ChatRepository;
import de.chatsphere.api.chat.transfer.AbstractChat;
import de.chatsphere.api.chat.transfer.ChatAddedEventDto;
import de.chatsphere.api.chat.transfer.ChatLeftEventDto;
import de.chatsphere.api.chat.transfer.ChatModifiedEventDto;
import de.chatsphere.api.chat.transfer.LeaveChatsInput;
import de.chatsphere.api.chat.transfer.UpdateChatNotificationSettingsInput;
import de.chatsphere.io.database.schema.user.User;
import de.chatsphere.server.graphql.Context;
import de.chatsphere.server.graphql.schema.annotation.DataFetcherWiring;
import de.chatsphere.server.rxbus.Bus;
import de.chatsphere.server.rxbus.ChannelKey;
import de.chatsphere.util.Util;
import graphql.schema.DataFetcher;
import java.util.LinkedList;
import java.util.List;
import lombok.NoArgsConstructor;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The execution of the specified queries/mutations/subscriptions start in their homonymous function
 * in this class.
 */
@NoArgsConstructor
public final class ChatResolver {

  private static final Logger log = LoggerFactory.getLogger(ChatResolver.class);

  @DataFetcherWiring(
    type = "Query",
    name = "chat"
  )
  public DataFetcher<AbstractChat> chat() {
    return environment ->
      ChatRepository.chat(environment.getContext(), (Integer) environment.getArgument("id"));
  }

  @DataFetcherWiring(
    type = "Query",
    name = "searchChat"
  )
  public DataFetcher<List<AbstractChat>> searchChat() {
    return environment ->
      ChatRepository.searchChat(environment.getContext(), (String) environment.getArgument("name"));
  }

  /**
   * Changes the notification settings of a chat.
   *
   * @return chat that has been changed
   */
  @DataFetcherWiring(
    type = "Mutation",
    name = "updateChatNotificationSettings"
  )
  public DataFetcher<Boolean> updateChatNotificationSettings() {
    return environment -> {

      // Convert inputted parameter of query
      UpdateChatNotificationSettingsInput converted =
        Util.convertMap(environment.getArguments(), UpdateChatNotificationSettingsInput.class);

      return ChatRepository.updateChatNotificationSettings(environment.getContext(), converted);
    };
  }

  /**
   * Leaves a group chat, removes a private chat from the chat list.
   *
   * @return TODO: which user is given back?
   */
  @DataFetcherWiring(
    type = "Mutation",
    name = "leaveChats"
  )
  public DataFetcher<Boolean> leaveChats() {
    return environment -> {
      Context context = environment.getContext();
      // Convert inputted parameter of query
      LeaveChatsInput converted = Util
        .convertMap(environment.getArguments(), LeaveChatsInput.class);

      List<AbstractChat> left = ChatRepository.leaveChats(environment.getContext(), converted);
      left.forEach(chat -> {
        if (chat.getParticipants().size() != 0) {
          List<String> recipients = new LinkedList<>();
          chat.getParticipants().forEach(participantDto -> {
            User participant =
              AccountRepository.getUser(context, participantDto.getUser().getUsername());
            recipients.add(participant.getProfile().getName());
          });

          ChatLeftEventDto chatLeftEventDto = new ChatLeftEventDto(
            context.getAuthenticator().getUsername(),
            recipients,
            chat
          );
          Bus.getInstance().postMainChannel(chatLeftEventDto);
        }

        converted.getChatIds().forEach(chatId -> {
          if (!left.contains(chatId)) {
            ChannelKey key = Bus.getInstance().createKey("chat", chatId);
            Bus.getInstance().unsubscribeChannel(key);
          }
        });
      });

      return true;
    };
  }

  /**
   * TODO.
   */
  @DataFetcherWiring(
    type = "Subscription",
    name = "chatLeft"
  )
  public DataFetcher<Publisher<ChatLeftEventDto>> chatLeft() {
    return environment -> Bus.getInstance().getMainChannel().asFlowable(ChatLeftEventDto.class);
  }

  /**
   * TODO.
   */
  @DataFetcherWiring(
    type = "Subscription",
    name = "chatAdded"
  )
  public DataFetcher<Publisher<ChatAddedEventDto>> chatAdded() {
    return environment -> Bus.getInstance().getMainChannel().asFlowable(ChatAddedEventDto.class);
  }

  /**
   * TODO.
   */
  @DataFetcherWiring(
    type = "Subscription",
    name = "chatModified"
  )
  public DataFetcher<Publisher<ChatModifiedEventDto>> chatModified() {
    return environment -> Bus.getInstance().getMainChannel().asFlowable(ChatModifiedEventDto.class);
  }
}
