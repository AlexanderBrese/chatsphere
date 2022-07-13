package de.chatsphere.api.contact.transfer;

import de.chatsphere.api.user.transfer.UserDto;
import de.chatsphere.io.database.schema.user.User;
import de.chatsphere.io.database.schema.user.UserRelationship;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
public class ContactDto {

  @Getter
  private final UserDto user;
  @Getter
  private final String nickname;
  @Getter
  private final Date createdAt;

  /**
   * Converts a user database object to a contact DTO.
   *
   * @param user the user database object
   *
   * @return the contact DTO
   */
  public static ContactDto from(User user) {
    return ContactDto.builder()
      .user(UserDto.from(user))
      .build();
  }

  /**
   * Converts a list of UserRelationships to a list of ContactDto.
   *
   * @param relationships the list of relationships this user has which have the field `blocked` set
   *
   * @return the contact DTO
   */
  public static List<ContactDto> from(List<UserRelationship> relationships) {
    List<ContactDto> result = new LinkedList<>();
    relationships.forEach(relationship ->
      result.add(from(relationship.getRefers()))
    );

    return result;
  }
}
