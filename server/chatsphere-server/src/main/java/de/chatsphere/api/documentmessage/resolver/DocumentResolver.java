package de.chatsphere.api.documentmessage.resolver;

import de.chatsphere.api.documentmessage.model.DocumentRepository;
import de.chatsphere.api.documentmessage.transfer.DocumentMessageDto;
import de.chatsphere.api.message.transfer.CreateMessageInput;
import de.chatsphere.api.message.transfer.UpdateMessageInput;
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
public final class DocumentResolver {

  private static final Logger log = LoggerFactory.getLogger(DocumentResolver.class);

  /**
   * Return a document message by id.
   *
   * @return message with specific id
   */
  @DataFetcherWiring(
    type = "Query",
    name = "documentMessage"
  )
  public DataFetcher<DocumentMessageDto> documentMessage() {
    return environment ->
      DocumentRepository.getDocumentMessage(
        environment.getContext(),
        environment.getArgument("id")
      );
  }


  /**
   * Adds a document message to the chat.
   *
   * @return chat to which it has been sent
   */
  @DataFetcherWiring(
    type = "Mutation",
    name = "createDocumentMessage"
  )
  public DataFetcher<DocumentMessageDto> createDocumentMessage() {
    return environment -> {
      CreateMessageInput converted =
        Util.convertMap(environment.getArguments(), CreateMessageInput.class);
      //return DocumentRepository.createDocumentMessage(environment.getContext(), converted);
      return null;
    };
  }

  /**
   * Updates a document message.
   *
   * @return the updated document
   */
  @DataFetcherWiring(
    type = "Mutation",
    name = "updateDocumentMessage"
  )
  public DataFetcher<DocumentMessageDto> updateDocumentMessage() {
    return environment -> {
      UpdateMessageInput converted =
        Util.convertMap(environment.getArguments(), UpdateMessageInput.class);
      return DocumentRepository.updateDocumentMessage(environment.getContext(), converted);
    };
  }
}
