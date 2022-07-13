package de.chatsphere.api.privatechat.transfer;

import de.chatsphere.api.chat.model.ChatRepository;
import de.chatsphere.api.chat.transfer.AbstractChat;
import de.chatsphere.api.chatstamp.transfer.ChatStampDto;
import de.chatsphere.api.message.transfer.AbstractMessage;
import de.chatsphere.api.shared.transfer.NotificationDto;
import de.chatsphere.api.user.transfer.ParticipantDto;
import de.chatsphere.io.database.schema.chat.Chat;
import de.chatsphere.io.database.schema.chat.ChatLine;
import de.chatsphere.io.database.schema.chat.ChatParticipant;
import de.chatsphere.io.database.schema.preference.enumeration.Visibility.Level;
import de.chatsphere.server.graphql.Context;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data transmitter object for links. (@see <a href="https://en.wikipedia.org/wiki/Data_transfer_object">DATA_TRANSFER_OBJECT.md</a>)
 */
@AllArgsConstructor
@Builder
public class PrivateChatDto implements AbstractChat {

  private static final Logger logger = LoggerFactory.getLogger(PrivateChatDto.class);
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

  public static PrivateChatDto from(Chat chat, Context context, String displayName) {
    PrivateChatDto privateChatDto = from(chat, context);
    privateChatDto.getStamp().setDisplayName(displayName);

    return privateChatDto;
  }

  public static PrivateChatDto from(Chat chat, Context context) {
    // `participants`
    List<ChatParticipant> chatParticipants =
      ChatRepository.getChatParticipants(context, chat.getId());

    assert chatParticipants.size() == 2;
    ChatParticipant current;
    ChatParticipant other;
    if (chatParticipants.get(0)
      .getParticipant()
      .getProfile()
      .getName()
      .equals(context.getAuthenticator().getUsername())) {
      current = chatParticipants.get(0);
      other = chatParticipants.get(1);
    } else {
      current = chatParticipants.get(1);
      other = chatParticipants.get(0);
    }

    if (!current.getVisibility()) {
      return null;
    }

    List<ParticipantDto> participants = ParticipantDto.from(chatParticipants);

    // `log`
    List<ChatLine> chatLines = ChatRepository.getChatLines(context, chat);
    List<AbstractMessage> log = AbstractMessage.from(chatLines);

    // `notify`
    NotificationDto notify = NotificationDto.from(current.getSubscription().getDescription());

    // stamp
    AbstractMessage lastMessage = log.size() > 0 ? log.get(log.size() - 1) : null;
    ChatStampDto stamp = ChatStampDto.from(other.getParticipant().getProfile(), lastMessage);
    return PrivateChatDto.builder()
      .id(chat.getId())
      .participants(participants)
      .log(log)
      .stamp(stamp)
      .notify(notify)
      .build();
  }

  @Override
  public Boolean equals(AbstractChat other) {
    return getId().equals(other.getId());
  }
}
