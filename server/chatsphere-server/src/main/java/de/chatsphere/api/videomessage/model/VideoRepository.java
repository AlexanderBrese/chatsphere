package de.chatsphere.api.videomessage.model;

import de.chatsphere.api.chat.transfer.AbstractChat;
import de.chatsphere.api.message.transfer.CreateMessageInput;
import de.chatsphere.api.message.transfer.UpdateMessageInput;
import de.chatsphere.api.videomessage.transfer.VideoManager;
import de.chatsphere.api.videomessage.transfer.VideoMessageDto;
import de.chatsphere.server.graphql.Context;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;


/**
 * Handle access to database. All the videomessage-specific data needs and queries are resolved
 * here.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class VideoRepository {

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
  public static VideoMessageDto getVideoMessage(Context context, int id) {
    VideoManager factory = new VideoManager(context);
    return (VideoMessageDto) factory.getMessage(id);
  }

  /**
   * Append message to chat.
   *
   * @param context {@link Context}
   *
   * @return chat with new message
   */
  public static AbstractChat createVideoMessage(Context context, CreateMessageInput input) {
    VideoManager factory = new VideoManager(context);
    return factory.createMessage(input);
  }

  /**
   * Modifies an existing message.
   *
   * @param context {@link Context}
   *
   * @return chat with new message
   */
  public static VideoMessageDto updateVideoMessage(Context context, UpdateMessageInput input) {
    VideoManager factory = new VideoManager(context);
    return (VideoMessageDto) factory.updateMessage(input);
  }
}
