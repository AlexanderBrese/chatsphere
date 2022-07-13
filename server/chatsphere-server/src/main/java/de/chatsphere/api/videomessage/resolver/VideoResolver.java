package de.chatsphere.api.videomessage.resolver;

import de.chatsphere.api.message.transfer.CreateMessageInput;
import de.chatsphere.api.message.transfer.UpdateMessageInput;
import de.chatsphere.api.videomessage.model.VideoRepository;
import de.chatsphere.api.videomessage.transfer.VideoMessageDto;
import de.chatsphere.server.graphql.schema.annotation.DataFetcherWiring;
import de.chatsphere.util.Util;
import graphql.schema.DataFetcher;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The execution of the specified queries/mutations/subscriptions start in their homonymous function
 * in this class.
 */
@NoArgsConstructor
public final class VideoResolver {

  private static final Logger log = LoggerFactory.getLogger(VideoResolver.class);

  /**
   * Return a video message by id.
   *
   * @return message with specific id
   */
  @DataFetcherWiring(
    type = "Query",
    name = "videoMessage"
  )
  public DataFetcher<VideoMessageDto> videoMessage() {
    return environment -> VideoRepository
      .getVideoMessage(environment.getContext(), environment.getArgument("id"));
  }


  /**
   * Adds a video message to the chat.
   *
   * @return chat to which it has been sent
   */
  @DataFetcherWiring(
    type = "Mutation",
    name = "createVideoMessage"
  )
  public DataFetcher<VideoMessageDto> createVideoMessage() {
    return environment -> {
      CreateMessageInput converted = Util
        .convertMap(environment.getArguments(), CreateMessageInput.class);
      //return VideoRepository.createVideoMessage(environment.getContext(), converted);
      return null;
    };
  }

  /**
   * Updates a video message.
   *
   * @return groupchat with one more participant
   */
  @DataFetcherWiring(
    type = "Mutation",
    name = "updateVideoMessage"
  )
  public DataFetcher<VideoMessageDto> updateVideoMessage() {
    return environment -> {
      UpdateMessageInput converted = Util
        .convertMap(environment.getArguments(), UpdateMessageInput.class);
      return VideoRepository.updateVideoMessage(environment.getContext(), converted);
    };
  }
}
