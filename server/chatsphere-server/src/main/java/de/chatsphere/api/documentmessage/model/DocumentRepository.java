package de.chatsphere.api.documentmessage.model;

import de.chatsphere.api.chat.transfer.AbstractChat;
import de.chatsphere.api.documentmessage.transfer.DocumentManager;
import de.chatsphere.api.documentmessage.transfer.DocumentMessageDto;
import de.chatsphere.api.message.transfer.CreateMessageInput;
import de.chatsphere.api.message.transfer.UpdateMessageInput;
import de.chatsphere.server.graphql.Context;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Handle access to database. All the documentmessage-specific data needs and queries are resolved
 * here.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DocumentRepository {

  /**
   * <p>Fetch the data from the database to fill in the following fields.</p>
   *
   * <p>
   * id: Int!, author: User!, date: Date, url: String!, to: Chat!
   * </p>
   *
   * @param context {@link Context}
   * @param id      of message
   *
   * @return single document with given id
   */
  public static DocumentMessageDto getDocumentMessage(Context context, int id) {
    DocumentManager factory = new DocumentManager(context);
    return (DocumentMessageDto) factory.getMessage(id);
  }

  /**
   * Append message to chat.
   *
   * @param context {@link Context}
   *
   * @return chat with new message
   */
  public static AbstractChat createDocumentMessage(Context context, CreateMessageInput input) {
    DocumentManager factory = new DocumentManager(context);
    return factory.createMessage(input);
  }

  /**
   * Modifies an existing message.
   *
   * @param context {@link Context}
   *
   * @return modified message
   */
  public static DocumentMessageDto updateDocumentMessage(
    Context context,
    UpdateMessageInput input
  ) {
    DocumentManager factory = new DocumentManager(context);
    return (DocumentMessageDto) factory.updateMessage(input);
  }
}
