package de.chatsphere.api.user.transfer;

import de.chatsphere.io.database.schema.Profile;
import de.chatsphere.io.database.schema.chat.Message;
import de.chatsphere.io.database.schema.preference.Phone;
import de.chatsphere.io.database.schema.user.User;
import de.chatsphere.server.graphql.Context;
import java.util.Date;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * The equivalent of the user type defined in the API schema.
 */

@Builder
@AllArgsConstructor
public class UserDto {
  @Getter
  private final String phone;
  @Getter
  private final Date createdAt;
  @Getter
  private final String avatarUrl;
  @Getter
  private final String status;
  @Getter
  private final String displayName;
  @Getter
  private final String username;

  /**
   * Creates a new user DTO from a user database object.
   *
   * @param user the user database object
   * @return the user DTO
   */
  public static UserDto from(User user) {
    Profile profile = user.getProfile();
    String number = Optional.ofNullable(user)
      .map(User::getPhone)
      .map(Phone::getNumber)
      .orElse("");
    String avatarUrl = Optional.ofNullable(profile)
      .map(Profile::getIcon)
      .map(file -> {
        if (file.getType() == null || file.getHash() == null) {
          return null;
        }
        return Context.getFileStorage().getDownloadUri(file);
      })
      .orElse("");
    String status = Optional.ofNullable(user)
      .map(User::getStatus)
      .map(Message::getContent)
      .orElse("");

    return UserDto.builder()
      .phone(number)
      .createdAt(user.getCreatedAt())
      .avatarUrl(avatarUrl)
      .status(status)
      .displayName(profile.getDisplayName())
      .username(profile.getName())
      .build();
  }
}
