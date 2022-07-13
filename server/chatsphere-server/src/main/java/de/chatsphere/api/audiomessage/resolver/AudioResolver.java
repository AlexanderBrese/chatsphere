package de.chatsphere.api.audiomessage.resolver;

import de.chatsphere.api.audiomessage.model.AudioRepository;
import de.chatsphere.api.audiomessage.transfer.AudioMessageDto;
import de.chatsphere.api.message.transfer.CreateMessageInput;
import de.chatsphere.api.message.transfer.UpdateMessageInput;
import de.chatsphere.server.graphql.schema.annotation.DataFetcherWiring;
import de.chatsphere.util.Util;
import graphql.schema.DataFetcher;
import lombok.NoArgsConstructor;

/**
 * The execution of the specified queries/mutations/subscriptions start in their homonymous function
 * in this class.
 */
@NoArgsConstructor
public final class AudioResolver {

  /**
   * Return an audio message by id.
   *
   * @return message with specific id
   */
  @DataFetcherWiring(
    type = "Query",
    name = "audioMessage"
  )
  public DataFetcher<AudioMessageDto> audioMessage() {
    return environment ->
      AudioRepository.getAudioMessage(environment.getContext(), environment.getArgument("id"));
  }


  /**
   * Adds an audio message to the chat.
   *
   * @return chat to which it has been sent
   */
  @DataFetcherWiring(
    type = "Mutation",
    name = "createAudioMessage"
  )
  public DataFetcher<AudioMessageDto> createAudioMessage() {
    return environment -> {
      CreateMessageInput converted =
        Util.convertMap(environment.getArguments(), CreateMessageInput.class);
      //return AudioRepository.createAudioMessage(environment.getContext(), converted);
      return null;
    };
  }

  /**
   * Updates an audio message.
   *
   * @return groupchat with one more participant
   */
  @DataFetcherWiring(
    type = "Mutation",
    name = "updateAudioMessage"
  )
  public DataFetcher<AudioMessageDto> updateAudioMessage() {
    return environment -> {
      UpdateMessageInput converted =
        Util.convertMap(environment.getArguments(), UpdateMessageInput.class);
      return AudioRepository.updateAudioMessage(environment.getContext(), converted);
    };
  }
}
