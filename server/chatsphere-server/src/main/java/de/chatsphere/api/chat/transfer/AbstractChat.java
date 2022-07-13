package de.chatsphere.api.chat.transfer;

import de.chatsphere.api.chatstamp.transfer.ChatStampDto;
import de.chatsphere.api.message.transfer.AbstractMessage;
import de.chatsphere.api.shared.transfer.NotificationDto;
import de.chatsphere.api.shared.transfer.NotificationPreference;
import de.chatsphere.api.user.transfer.ParticipantDto;
import java.util.LinkedList;
import java.util.List;

public interface AbstractChat {

  Integer id = 0;
  List<ParticipantDto> participants = new LinkedList<>();
  List<AbstractMessage> log = new LinkedList<>();
  NotificationDto notify = new NotificationDto(NotificationPreference.INHERIT);
  ChatStampDto stamp = ChatStampDto.builder().build();

  Integer getId();

  List<ParticipantDto> getParticipants();

  List<AbstractMessage> getLog();

  NotificationDto getNotify();

  ChatStampDto getStamp();

  Boolean equals(AbstractChat other);
}
