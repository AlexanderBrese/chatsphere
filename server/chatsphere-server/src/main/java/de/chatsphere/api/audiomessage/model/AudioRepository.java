package de.chatsphere.api.audiomessage.model;

import de.chatsphere.api.audiomessage.transfer.AudioManager;
import de.chatsphere.api.audiomessage.transfer.AudioMessageDto;
import de.chatsphere.api.chat.transfer.AbstractChat;
import de.chatsphere.api.message.transfer.CreateMessageInput;
import de.chatsphere.api.message.transfer.UpdateMessageInput;
import de.chatsphere.server.graphql.Context;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Handle access to database. All the audio-specific data needs and queries are resolved here.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AudioRepository {

  /**
   * <p>Fetch the data from the database to fill in the following fields.
   * TODO: Sort ASC by date, accept argument `last: Int`</p>
   *
   * <p>
   * id: Int!, author: User!, date: Date, audio: String!, to: Chat!
   * </p>
   *
   * @param context {@link Context}
   * @param id      of message
   *
   * @return single chat with given id
   */
  public static AudioMessageDto getAudioMessage(Context context, int id) {
    AudioManager factory = new AudioManager(context);
    return (AudioMessageDto) factory.getMessage(id);
  }

  /**
   * Append message to chat.
   *
   * @param context {@link Context}
   *
   * @return chat with new message
   */
  public static AbstractChat createAudioMessage(Context context, CreateMessageInput input) {
    AudioManager factory = new AudioManager(context);
    return factory.createMessage(input);
  }

  /**
   * Modifies an existing message.
   *
   * @param context {@link Context}
   *
   * @return chat with new message
   */
  public static AudioMessageDto updateAudioMessage(Context context, UpdateMessageInput input) {
    AudioManager factory = new AudioManager(context);
    return (AudioMessageDto) factory.updateMessage(input);
  }
}
