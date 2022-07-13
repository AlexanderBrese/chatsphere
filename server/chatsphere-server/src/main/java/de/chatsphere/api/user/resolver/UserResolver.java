package de.chatsphere.api.user.resolver;

import de.chatsphere.api.user.model.UserRepository;
import de.chatsphere.api.user.transfer.ReportUserInput;
import de.chatsphere.api.user.transfer.UpdateAvatarInput;
import de.chatsphere.api.user.transfer.UpdateDisplayNameInput;
import de.chatsphere.api.user.transfer.UpdatePhoneInput;
import de.chatsphere.api.user.transfer.UpdateStatusInput;
import de.chatsphere.api.user.transfer.UserAddedEventDto;
import de.chatsphere.api.user.transfer.UserDto;
import de.chatsphere.server.graphql.Context;
import de.chatsphere.server.graphql.schema.annotation.DataFetcherWiring;
import de.chatsphere.server.rxbus.Bus;
import de.chatsphere.util.Util;
import graphql.schema.DataFetcher;
import java.util.List;
import org.reactivestreams.Publisher;

/**
 * Defines resolvers for user related information.
 */
public class UserResolver {

  /**
   * Gets a list of users by username.
   *
   * @return the list of users
   */
  @DataFetcherWiring(
    type = "Query",
    name = "searchUsers"
  )
  public DataFetcher<List<UserDto>> searchUsers() {
    return environment -> {
      Context context = environment.getContext();
      @SuppressWarnings("unchecked")
      String username = (String) environment.getArguments().get("username");
      return UserRepository.searchUsers(context, username);
    };
  }

  /**
   * Gets a list of users by username.
   *
   * @return the list of users
   */
  @DataFetcherWiring(
    type = "Query",
    name = "user"
  )
  public DataFetcher<UserDto> user() {
    return environment -> {
      Context context = environment.getContext();
      @SuppressWarnings("unchecked")
      String username = (String) environment.getArguments().get("username");
      return UserRepository.user(context, username);
    };
  }

  /**
   * Updates a user avatar.
   *
   * @return the changed user
   */
  @DataFetcherWiring(
    type = "Mutation",
    name = "updateAvatar"
  )
  public DataFetcher<String> updateAvatar() {

    return environment -> {
      Context context = environment.getContext();
      UpdateAvatarInput input = Util
        .convertMap(environment.getArguments(), UpdateAvatarInput.class);
      return UserRepository.updateAvatar(context, input);
    };
  }

  /**
   * Updates a user phone.
   *
   * @return the changed user
   */
  @DataFetcherWiring(
    type = "Mutation",
    name = "updatePhone"
  )
  public DataFetcher<Boolean> updatePhone() {
    return environment -> {
      Context context = environment.getContext();
      UpdatePhoneInput input = Util.convertMap(environment.getArguments(), UpdatePhoneInput.class);
      return UserRepository.updatePhone(context, input);
    };
  }

  /**
   * Updates a user display name.
   *
   * @return the changed user
   */
  @DataFetcherWiring(
    type = "Mutation",
    name = "updateDisplayName"
  )
  public DataFetcher<Boolean> updateDisplayName() {
    return environment -> {
      Context context = environment.getContext();
      UpdateDisplayNameInput input = Util
        .convertMap(environment.getArguments(), UpdateDisplayNameInput.class);
      return UserRepository.updateDisplayName(context, input);
    };
  }

  /**
   * Updates a user status.
   *
   * @return the changed user
   */
  @DataFetcherWiring(
    type = "Mutation",
    name = "updateStatus"
  )
  public DataFetcher<Boolean> updateStatus() {
    return environment -> {
      Context context = environment.getContext();
      UpdateStatusInput input = Util
        .convertMap(environment.getArguments(), UpdateStatusInput.class);
      return UserRepository.updateStatus(context, input);
    };
  }

  /**
   * Reports a user.
   *
   * @return the new updated account
   */
  @DataFetcherWiring(
    type = "Mutation",
    name = "reportUser"
  )
  public DataFetcher<Boolean> reportUser() {
    return environment -> {
      Context context = environment.getContext();
      ReportUserInput input =
        Util.convertMap(environment.getArguments(), ReportUserInput.class);

      return UserRepository.reportUser(context, input);
    };
  }

  /**
   * TODO.
   */
  @DataFetcherWiring(
    type = "Subscription",
    name = "userAdded"
  )
  public DataFetcher<Publisher<UserAddedEventDto>> userAdded() {
    return environment -> Bus.getInstance().getMainChannel().asFlowable(UserAddedEventDto.class);
  }
}
