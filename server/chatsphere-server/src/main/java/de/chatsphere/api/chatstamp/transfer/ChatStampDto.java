package de.chatsphere.api.chatstamp.transfer;

import de.chatsphere.api.message.transfer.AbstractMessage;
import de.chatsphere.io.database.schema.Profile;
import de.chatsphere.io.database.schema.chat.Chat;
import de.chatsphere.server.graphql.Context;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Visual representation of a chat entry. Depicts an avatar and the name of that chat depending on
 * its type.
 */
@AllArgsConstructor
@Builder
public class ChatStampDto {

  @Getter
  private final String iconUrl;
  @Getter
  private final AbstractMessage lastMessage;
  @Getter
  @Setter
  private String displayName;

  /**
   * PrivateChat: Display stamp of recipient's profile.
   *
   * @param recipient - partner of this conversation
   * @return stamp
   */
  public static ChatStampDto from(Profile recipient, AbstractMessage lastMessage) {

    String icon = Optional.ofNullable(recipient)
      .map(Profile::getIcon)
      .map(file -> {
        if (file.getType() == null || file.getHash() == null) {
          return null;
        }
        return Context.getFileStorage().getDownloadUri(file);
      })
      .orElse("");
    return ChatStampDto.builder()
      .iconUrl(icon)
      .displayName(recipient.getDisplayName())
      .lastMessage(lastMessage)
      .build();
  }

  /**
   * GroupChat: Display stamp that is unique to the chat. TODO: Implement icon
   *
   * @param groupChat - n/a
   * @return stamp
   */
  public static ChatStampDto from(Chat groupChat, AbstractMessage lastMessage) {
    String iconUrl = Optional.ofNullable(groupChat.getProfile())
      .map(Profile::getIcon)
      .map(file -> {
        if (file.getType() == null || file.getHash() == null) {
          return null;
        }
        return Context.getFileStorage().getDownloadUri(file);
      })
      .orElse("");
    return ChatStampDto.builder()
      .iconUrl(iconUrl)
      .displayName(groupChat.getProfile().getDisplayName())
      .lastMessage(lastMessage)
      .build();
  }

}
