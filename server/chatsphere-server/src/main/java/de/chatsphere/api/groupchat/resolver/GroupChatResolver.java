package de.chatsphere.api.groupchat.resolver;

import de.chatsphere.api.account.model.AccountRepository;
import de.chatsphere.api.chat.transfer.ChatAddedEventDto;
import de.chatsphere.api.chatstamp.transfer.ChatStampDto;
import de.chatsphere.api.groupchat.model.GroupChatRepository;
import de.chatsphere.api.groupchat.transfer.AddParticipantInput;
import de.chatsphere.api.groupchat.transfer.CreateGroupChatInput;
import de.chatsphere.api.groupchat.transfer.GrantPrivilegesInput;
import de.chatsphere.api.groupchat.transfer.GroupChatDto;
import de.chatsphere.api.groupchat.transfer.RemoveParticipantInput;
import de.chatsphere.api.groupchat.transfer.RevokePrivilegesInput;
import de.chatsphere.api.groupchat.transfer.UpdateNameInput;
import de.chatsphere.api.groupchat.transfer.UpdatePictureInput;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The execution of the specified queries/mutations/subscriptions start in their homonymous function
 * in this class.
 */
@NoArgsConstructor
public final class GroupChatResolver {

  private static final Logger log = LoggerFactory.getLogger(GroupChatResolver.class);

  /**
   * Adds a group chat to the chat list.
   *
   * @return TODO: Which user is returned?
   */
  @DataFetcherWiring(
    type = "Mutation",
    name = "createGroupChat"
  )
  public DataFetcher<GroupChatDto> createGroupChat() {
    return environment -> {
      Context context = environment.getContext();
      // Convert inputted parameter of query
      CreateGroupChatInput converted =
        Util.convertMap(environment.getArguments(), CreateGroupChatInput.class);

      GroupChatDto groupChatDto =
        GroupChatRepository.createGroupChat(context, converted);

      List<String> recipients = new LinkedList<>();
      converted.getParticipantUsernames().forEach(participantUsername -> {
        User participant = AccountRepository.getUser(context, participantUsername);
        recipients.add(participant.getProfile().getName());
      });
      ChatAddedEventDto chatAddedEventDto = new ChatAddedEventDto(
        context.getAuthenticator().getUsername(),
        recipients,
        groupChatDto
      );
      ChannelKey channelKey = Bus.getInstance().createKey("chat",
        groupChatDto.getId());

      Bus.getInstance().registerChannel(channelKey);
      Bus.getInstance().postMainChannel(chatAddedEventDto);
      return groupChatDto;
    };
  }

  /**
   * Adds a particpant to the group chat.
   *
   * @return groupchat with one more participant
   */
  @DataFetcherWiring(
    type = "Mutation",
    name = "addParticipant"
  )
  public DataFetcher<Boolean> addParticipant() {
    return environment -> {

      // Convert inputted parameter of query
      AddParticipantInput converted =
        Util.convertMap(environment.getArguments(), AddParticipantInput.class);

      return GroupChatRepository.addParticipant(environment.getContext(), converted);
    };
  }

  /**
   * Removes a particpant from the group chat.
   *
   * @return the updated group chat
   */
  @DataFetcherWiring(
    type = "Mutation",
    name = "removeParticipant"
  )
  public DataFetcher<Boolean> removeParticipant() {
    return environment -> {

      RemoveParticipantInput converted
        = Util.convertMap(environment.getArguments(), RemoveParticipantInput.class);

      return GroupChatRepository.removeParticipant(environment.getContext(), converted);
    };
  }

  /**
   * Changes the name of the group chat.
   *
   * @return the updated stamp object
   */
  @DataFetcherWiring(
    type = "Mutation",
    name = "updateName"
  )
  public DataFetcher<Boolean> updateName() {
    return environment -> {
      UpdateNameInput convert = Util.convertMap(environment.getArguments(), UpdateNameInput.class);
      return GroupChatRepository.updateName(environment.getContext(), convert);
    };
  }

  /**
   * Changes the picture of the group chat.
   *
   * @return the updated stamp object
   */
  @DataFetcherWiring(
    type = "Mutation",
    name = "updatePicture"
  )
  public DataFetcher<Boolean> updatePicture() {
    return environment -> {

      UpdatePictureInput converted
        = Util.convertMap(environment.getArguments(), UpdatePictureInput.class);

      return GroupChatRepository.updatePicture(environment.getContext(), converted);
    };
  }

  /**
   * Adds a new administrator to the group chat.
   *
   * @return updated chat with recently set privileges
   */
  @DataFetcherWiring(
    type = "Mutation",
    name = "grantPrivileges"
  )
  public DataFetcher<Boolean> grantPrivileges() {
    return environment -> {

      GrantPrivilegesInput converted
        = Util.convertMap(environment.getArguments(), GrantPrivilegesInput.class);

      return GroupChatRepository.grantPrivileges(environment.getContext(), converted);
    };
  }

  /**
   * Removes an administrator from the group chat.
   *
   * @return updated chat with recently set privileges
   */
  @DataFetcherWiring(
    type = "Mutation",
    name = "revokePrivileges"
  )
  public DataFetcher<Boolean> revokePrivileges() {
    return environment -> {

      RevokePrivilegesInput converted
        = Util.convertMap(environment.getArguments(), RevokePrivilegesInput.class);

      return GroupChatRepository.revokePrivileges(environment.getContext(), converted);
    };
  }
}
