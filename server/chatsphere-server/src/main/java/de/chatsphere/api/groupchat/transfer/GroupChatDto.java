package de.chatsphere.api.groupchat.transfer;

import de.chatsphere.api.chat.model.ChatRepository;
import de.chatsphere.api.chat.transfer.AbstractChat;
import de.chatsphere.api.chatstamp.transfer.ChatStampDto;
import de.chatsphere.api.message.transfer.AbstractMessage;
import de.chatsphere.api.shared.transfer.NotificationDto;
import de.chatsphere.api.user.transfer.ParticipantDto;
import de.chatsphere.io.database.schema.chat.Chat;
import de.chatsphere.io.database.schema.chat.ChatLine;
import de.chatsphere.io.database.schema.chat.ChatParticipant;
import de.chatsphere.io.database.schema.preference.enumeration.Notifiable.Level;
import de.chatsphere.server.graphql.Context;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * asdasd.
 */
@Builder
@AllArgsConstructor
public class GroupChatDto implements AbstractChat {

  private static final Logger logger = LoggerFactory.getLogger(GroupChatDto.class);

  @Getter
  private final Integer id;
  @Getter
  private final List<ParticipantDto> participants;
  @Getter
  private final List<AbstractMessage> log;
  @Getter
  private final NotificationDto notify;
  @Getter
  private final ChatStampDto stamp;
  @Getter
  private final Boolean isPublic;

  /**
   * <p>TODO: Possible bottleneck: Each creation of a GroupChat requires
   * generating the list of chatparticipants (i.e. a search for each ChatParticipant in
   * corresponding table)</p>
   *
   * <p>Fields:</p>
   *
   * <p>
   * id: Int!, participants: [Participant!]!, log(last: Int): [Message], notify: Notification!,
   * stamp: ChatStamp, isPublic: Boolean!
   * </p>
   *
   * @param chat - the corresponding database object returned when using ormlite
   * @param context - requires username field
   * @return a converted object ready to be sent
   */
  public static GroupChatDto from(Chat chat, Context context) {
    // `participants`
    List<ChatParticipant> chatParticipants =
      ChatRepository.getChatParticipants(context, chat.getId());
    List<ParticipantDto> participants = ParticipantDto.from(chatParticipants);

    // `log`
    List<ChatLine> chatLines = ChatRepository.getChatLines(context, chat);
    List<AbstractMessage> log = AbstractMessage.from(chatLines);

    // `notify`
    ChatParticipant recipient = null;
    for (ChatParticipant chatParticipant : chatParticipants) {
      if (chatParticipant.getParticipant().getProfile().getName()
        .equals(context.getAuthenticator().getUsername())) {
        recipient = chatParticipant;
        break;
      }
    }

    NotificationDto notify = NotificationDto.from(Level.INHERIT);
    if (recipient != null) {
      notify = NotificationDto.from(recipient.getSubscription().getDescription());
    }

    // stamp
    AbstractMessage lastMessage = log.size() > 0 ? log.get(log.size() - 1) : null;
    ChatStampDto stamp = ChatStampDto.from(chat, lastMessage);

    return GroupChatDto.builder()
      .id(chat.getId())
      .participants(participants)
      .log(log)
      .stamp(stamp)
      .isPublic(chat.isPublic())
      .notify(notify)
      .build();
  }

  @Override
  public Boolean equals(AbstractChat other) {
    return getId().equals(other.getId());
  }
}
