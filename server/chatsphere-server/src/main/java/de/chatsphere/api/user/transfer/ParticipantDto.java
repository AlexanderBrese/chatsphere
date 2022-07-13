package de.chatsphere.api.user.transfer;

import de.chatsphere.io.database.schema.chat.ChatParticipant;
import java.util.LinkedList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * A user that is part of a chat.
 */
@AllArgsConstructor
@Builder
public class ParticipantDto {

  @Getter
  private final UserDto user;
  @Getter
  private final Boolean hasPrivileges;

  public static ParticipantDto from(ChatParticipant chatParticipant) {
    return ParticipantDto.builder()
      .user(UserDto.from(chatParticipant.getParticipant()))
      .hasPrivileges(chatParticipant.getAdmin())
      .build();
  }

  public static List<ParticipantDto> from(List<ChatParticipant> participants) {
    List<ParticipantDto> result = new LinkedList<>();
    for (ChatParticipant participant : participants) {
      result.add(from(participant));
    }
    return result;
  }
}
