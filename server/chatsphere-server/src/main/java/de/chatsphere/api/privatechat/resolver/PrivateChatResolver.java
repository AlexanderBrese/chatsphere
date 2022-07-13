package de.chatsphere.api.privatechat.resolver;

import de.chatsphere.api.account.model.AccountRepository;
import de.chatsphere.api.chat.model.ChatRepository;
import de.chatsphere.api.chat.transfer.ChatAddedEventDto;
import de.chatsphere.api.privatechat.model.PrivateChatRepository;
import de.chatsphere.api.privatechat.transfer.CreatePrivateChatInput;
import de.chatsphere.api.privatechat.transfer.PrivateChatDto;
import de.chatsphere.io.database.schema.chat.ChatParticipant;
import de.chatsphere.io.database.schema.preference.enumeration.Visibility.Level;
import de.chatsphere.io.database.schema.user.User;
import de.chatsphere.server.graphql.Context;
import de.chatsphere.server.graphql.schema.annotation.DataFetcherWiring;
import de.chatsphere.server.rxbus.Bus;
import de.chatsphere.server.rxbus.ChannelKey;
import de.chatsphere.util.Util;
import graphql.schema.DataFetcher;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrivateChatResolver {

  private static final Logger log = LoggerFactory.getLogger(PrivateChatResolver.class);

  /**
   * Changes the notification settings of a chat.
   *
   * @return chat that has been changed
   */
  @DataFetcherWiring(
    type = "Mutation",
    name = "createPrivateChat"
  )
  public DataFetcher<PrivateChatDto> createPrivateChat() {
    return environment -> {
      Context context = environment.getContext();
      // Convert inputted parameter of query
      CreatePrivateChatInput converted =
        Util.convertMap(environment.getArguments(), CreatePrivateChatInput.class);

      User participantUser =
        AccountRepository.getUser(context, converted.getParticipantUsername());
      ChatParticipant intersection = PrivateChatRepository
        .findChatIntersection(context, participantUser);
      if (intersection != null) {
        ChatRepository.updateVisibility(context, intersection, true);
        return PrivateChatDto.from(intersection.getChat(), context);
      }

      PrivateChatDto privateChatDto = PrivateChatRepository
        .createPrivateChat(context, converted);
      String currentUserName = context.getAuthenticator().getUsername();
      PrivateChatDto copy = privateChatDto;
      copy.getStamp().setDisplayName(currentUserName);

      List<String> recipients = new LinkedList<>();
      User participant = AccountRepository.getUser(context, converted.getParticipantUsername());
      recipients.add(participant.getProfile().getName());

      ChatAddedEventDto chatAddedEventDto = new ChatAddedEventDto(
        currentUserName,
        recipients,
        copy
      );
      ChannelKey channelKey = Bus.getInstance().createKey("chat",
        privateChatDto.getId());

      Bus.getInstance().registerChannel(channelKey);
      Bus.getInstance().postMainChannel(chatAddedEventDto);
      return privateChatDto;
    };
  }
}
