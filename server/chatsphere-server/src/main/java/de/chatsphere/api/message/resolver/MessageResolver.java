package de.chatsphere.api.message.resolver;

import de.chatsphere.api.account.model.AccountRepository;
import de.chatsphere.api.chat.model.ChatRepository;
import de.chatsphere.api.chat.transfer.ChatModifiedEventDto;
import de.chatsphere.api.message.model.MessageRepository;
import de.chatsphere.api.message.transfer.AbstractMessage;
import de.chatsphere.api.message.transfer.MessageAddedEventDto;
import de.chatsphere.api.message.transfer.MessageRemovedEventDto;
import de.chatsphere.api.message.transfer.MessageUpdatedEventDto;
import de.chatsphere.api.message.transfer.RemoveMessagesInput;
import de.chatsphere.api.shared.transfer.SubscriptionInput;
import de.chatsphere.io.database.schema.chat.Chat;
import de.chatsphere.io.database.schema.chat.ChatLine;
import de.chatsphere.io.database.schema.chat.ChatParticipant;
import de.chatsphere.server.graphql.Context;
import de.chatsphere.server.graphql.schema.annotation.DataFetcherWiring;
import de.chatsphere.server.rxbus.Bus;
import de.chatsphere.server.rxbus.ChannelKey;
import de.chatsphere.util.Util;
import graphql.schema.DataFetcher;
import java.util.LinkedList;
import java.util.List;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageResolver {
  private static final Logger log = LoggerFactory.getLogger(MessageResolver.class);

  /**
   * Creates a new link through the link repository.
   *
   * @return the resulting data fetcher for the mutation
   */
  @DataFetcherWiring(
    type = "Mutation",
    name = "removeMessages"
  )
  public DataFetcher<Boolean> removeMessages() {
    return environment -> {
      Context context = environment.getContext();

      RemoveMessagesInput removeMessagesInput =
        Util.convertMap(environment.getArguments(), RemoveMessagesInput.class);

      List<ChatLine> chatLines = MessageRepository
        .removeMessages(context, removeMessagesInput);

      Chat chat = chatLines.get(0).getChat();
      Integer chatId = chat.getId();
      ChannelKey channelKey = Bus.getInstance().createKey("chat", chatId);

      List<ChatParticipant> chatParticipants =
        ChatRepository.getChatParticipants(context, chatId);
      List<String> recipients = new LinkedList<>();
      chatParticipants.forEach(chatParticipant -> {
        recipients.add(chatParticipant.getParticipant().getProfile().getName());
      });

      chatLines.forEach(chatLine -> {
        MessageRemovedEventDto messageRemovedEventDto = new MessageRemovedEventDto(
          context.getAuthenticator().getUsername(),
          recipients,
          chatLine.getMessage().getId()
        );
        Bus.getInstance().post(channelKey, messageRemovedEventDto);
      });
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

  /**
   * asdasd.
   *
   * @return asdasd.
   */
  @DataFetcherWiring(
    type = "Subscription",
    name = "messageRemoved"
  )
  public DataFetcher<Publisher<MessageRemovedEventDto>> messageRemoved() {
    return environment -> {
      SubscriptionInput subscriptionInput =
        Util.convertMap(environment.getArguments(), SubscriptionInput.class);

      ChannelKey channelKey = Bus.getInstance().createKey(subscriptionInput.getChannelName(),
        subscriptionInput.getChannelId());

      return Bus.getInstance()
        .getChannel(channelKey)
        .asFlowable(MessageRemovedEventDto.class);
    };
  }

  /**
   * asdasd.
   *
   * @return asdasd.
   */
  @DataFetcherWiring(
    type = "Subscription",
    name = "messageUpdated"
  )
  public DataFetcher<Publisher<MessageUpdatedEventDto>> messageUpdated() {
    return environment -> {
      SubscriptionInput subscriptionInput =
        Util.convertMap(environment.getArguments(), SubscriptionInput.class);

      ChannelKey channelKey = Bus.getInstance().createKey(subscriptionInput.getChannelName(),
        subscriptionInput.getChannelId());

      return Bus.getInstance()
        .getChannel(channelKey)
        .asFlowable(MessageUpdatedEventDto.class);
    };
  }

  /**
   * asdasd.
   *
   * @return asdasd.
   */
  @DataFetcherWiring(
    type = "Subscription",
    name = "messageAdded"
  )
  public DataFetcher<Publisher<MessageAddedEventDto>> messageAdded() {
    return environment -> {
      SubscriptionInput subscriptionInput =
        Util.convertMap(environment.getArguments(), SubscriptionInput.class);

      ChannelKey channelKey = Bus.getInstance().createKey(subscriptionInput.getChannelName(),
        subscriptionInput.getChannelId());

      return Bus.getInstance()
        .getChannel(channelKey)
        .asFlowable(MessageAddedEventDto.class);
    };
  }
}
