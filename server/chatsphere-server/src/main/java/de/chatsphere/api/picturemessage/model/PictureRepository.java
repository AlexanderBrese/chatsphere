package de.chatsphere.api.picturemessage.model;

import de.chatsphere.api.chat.transfer.AbstractChat;
import de.chatsphere.api.message.transfer.CreateMessageInput;
import de.chatsphere.api.message.transfer.UpdateMessageInput;
import de.chatsphere.api.picturemessage.transfer.PictureManager;
import de.chatsphere.api.picturemessage.transfer.PictureMessageDto;
import de.chatsphere.server.graphql.Context;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Handle access to database. All the groupchat-specific data needs and queries are resolved here.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PictureRepository {

  /**
   * <p>Fetch the data from the database to fill in the following fields.</p>
   *
   * <p>
   * id: Int!, author: User!, date: Date, picture(size: Int = 64): String!, to: Chat!
   * </p>
   *
   * @param context {@link Context}
   * @param id      of message
   *
   * @return single chat with given id
   */
  public static PictureMessageDto getPictureMessage(Context context, int id) {
    PictureManager factory = new PictureManager(context);
    return (PictureMessageDto) factory.getMessage(id);
  }

  /**
   * Append message to chat.
   *
   * @param context {@link Context}
   *
   * @return chat with new message
   */
  public static AbstractChat createPictureMessage(Context context, CreateMessageInput input) {
    PictureManager factory = new PictureManager(context);
    return factory.createMessage(input);
  }

  /**
   * Modifies an existing message.
   *
   * @param context {@link Context}
   *
   * @return chat with new message
   */
  public static PictureMessageDto updatePictureMessage(Context context, UpdateMessageInput input) {
    PictureManager factory = new PictureManager(context);
    return (PictureMessageDto) factory.updateMessage(input);
  }
}
