package de.chatsphere.api.picturemessage.resolver;

import de.chatsphere.api.message.transfer.CreateMessageInput;
import de.chatsphere.api.message.transfer.UpdateMessageInput;
import de.chatsphere.api.picturemessage.model.PictureRepository;
import de.chatsphere.api.picturemessage.transfer.PictureMessageDto;
import de.chatsphere.server.graphql.schema.annotation.DataFetcherWiring;
import de.chatsphere.util.Util;
import graphql.schema.DataFetcher;
import lombok.NoArgsConstructor;

/**
 * The execution of the specified queries/mutations/subscriptions start in their homonymous function
 * in this class.
 */
@NoArgsConstructor
public final class PictureResolver {

  /**
   * Return a picture message by id.
   *
   * @return message with specific id
   */
  @DataFetcherWiring(
    type = "Query",
    name = "pictureMessage"
  )
  public DataFetcher<PictureMessageDto> pictureMessage() {
    return environment -> PictureRepository
      .getPictureMessage(environment.getContext(), environment.getArgument("id"));
  }


  /**
   * adds a picture message to the chat.
   *
   * @return chat to which it has been sent
   */
  @DataFetcherWiring(
    type = "Mutation",
    name = "createPictureMessage"
  )
  public DataFetcher<PictureMessageDto> createPictureMessage() {
    return environment -> {
      CreateMessageInput converted = Util
        .convertMap(environment.getArguments(), CreateMessageInput.class);
      //return PictureRepository.createPictureMessage(environment.getContext(), converted);
      return null;
    };
  }

  /**
   * Updates a picture message.
   *
   * @return groupchat with one more participant
   */
  @DataFetcherWiring(
    type = "Mutation",
    name = "updatePictureMessage"
  )
  public DataFetcher<PictureMessageDto> updatePictureMessage() {
    return environment -> {
      UpdateMessageInput converted = Util
        .convertMap(environment.getArguments(), UpdateMessageInput.class);
      return PictureRepository.updatePictureMessage(environment.getContext(), converted);
    };
  }
}
