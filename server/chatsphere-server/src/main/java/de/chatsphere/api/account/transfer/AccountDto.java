package de.chatsphere.api.account.transfer;

import de.chatsphere.api.chat.transfer.AbstractChat;
import de.chatsphere.api.contact.model.ContactRepository;
import de.chatsphere.api.contact.transfer.ContactDto;
import de.chatsphere.api.shared.transfer.NotificationDto;
import de.chatsphere.api.user.transfer.UserDto;
import de.chatsphere.io.database.schema.preference.enumeration.Notifiable;
import de.chatsphere.io.database.schema.user.User;
import de.chatsphere.io.database.schema.user.UserRelationship;
import de.chatsphere.server.graphql.Context;
import java.util.LinkedList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * The equivalent of the account type defined in the API schema.
 */
@AllArgsConstructor
@Builder
public class AccountDto {
  @Getter
  private final String email;
  @Getter
  private final UserDto user;
  @Getter
  private final List<ContactDto> contacts;
  @Getter
  private final List<ContactDto> blocked;
  @Getter
  private final List<AbstractChat> chats;
  @Getter
  private final NotificationDto notify;

  /**
   * Creates a new account DTO from the provide database objects.
   *
   * @param context  to get the blocked list
   * @param user     the user database object
   * @param notifiable the notification settings
   * @param contacts the user relationship database objects
   * @param chats    the chat database objects
   *
   * @return the account DTO
   */
  public static AccountDto from(
    Context context,
    User user,
    Notifiable notifiable,
    List<UserRelationship> contacts,
    List<AbstractChat> chats
  ) {
    UserDto userDto = UserDto.from(user);
    List<ContactDto> contactDtoList = new LinkedList<>();
    contacts.forEach(contact -> contactDtoList.add(ContactDto.from(contact.getRefers())));

    NotificationDto notificationDto = NotificationDto.from(notifiable.getDescription());



    return AccountDto.builder()
      .email(user.getEmail().getMail())
      .user(userDto)
      .contacts(contactDtoList)
      .blocked(ContactRepository.getBlockedUsers(context, user))
      .chats(chats)
      .notify(notificationDto)
      .build();
  }
}
