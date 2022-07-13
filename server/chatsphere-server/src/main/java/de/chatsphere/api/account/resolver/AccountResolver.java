package de.chatsphere.api.account.resolver;

import de.chatsphere.api.account.model.AccountRepository;
import de.chatsphere.api.account.transfer.AccountDto;
import de.chatsphere.api.account.transfer.AuthenticationPayload;
import de.chatsphere.api.account.transfer.CreateAccountInput;
import de.chatsphere.api.account.transfer.LoginInput;
import de.chatsphere.api.account.transfer.UpdateEmailInput;
import de.chatsphere.api.account.transfer.UpdateNotificationInput;
import de.chatsphere.api.account.transfer.UpdatePasswordInput;
import de.chatsphere.api.user.transfer.UserAddedEventDto;
import de.chatsphere.api.user.transfer.UserDto;
import de.chatsphere.server.graphql.Context;
import de.chatsphere.server.graphql.schema.annotation.DataFetcherWiring;
import de.chatsphere.server.rxbus.Bus;
import de.chatsphere.server.rxbus.Event;
import de.chatsphere.util.Util;
import graphql.schema.DataFetcher;
import java.util.LinkedList;
import java.util.List;

/**
 * Defines resolvers for account related information.
 */
public final class AccountResolver {

  /**
   * Logs out the current account.
   *
   * @return true/false
   */
  @DataFetcherWiring(
    type = "Query",
    name = "logout"
  )
  public DataFetcher<Boolean> logout() {
    return environment -> {
      Context context = environment.getContext();
      Bus.getInstance().unsubscribe(context.getAuthenticator().getUsername());
      context.getAuthenticator().setUsername("");
      return true;
    };
  }

  /**
   * Gets the current account.
   *
   * @return the account
   */
  @DataFetcherWiring(
    type = "Query",
    name = "account"
  )
  public DataFetcher<AccountDto> account() {
    return environment -> {
      Context context = environment.getContext();
      return AccountRepository.account(context);
    };
  }

  /**
   * Checks if a username is taken.
   *
   * @return true/false
   */
  @DataFetcherWiring(
    type = "Query",
    name = "usernameTaken"
  )
  public DataFetcher<Boolean> usernameTaken() {
    return environment -> {
      Context context = environment.getContext();
      @SuppressWarnings("unchecked")
      String username = (String) environment.getArguments().get("username");
      return AccountRepository.usernameTaken(context, username);
    };
  }

  /**
   * Creates a new account with the provided username, email and password.
   *
   * @return the account and a token
   */
  @DataFetcherWiring(
    type = "Mutation",
    name = "createAccount"
  )
  public DataFetcher<AuthenticationPayload> createAccount() {
    return environment -> {
      Context context = environment.getContext();
      CreateAccountInput createAccountInput
        = Util.convertMap(environment.getArguments(), CreateAccountInput.class);

      AuthenticationPayload authenticationPayload =
        AccountRepository.createAccount(context, createAccountInput);

      UserDto userDto = UserDto.from(AccountRepository.getCurrentUser(context));
      List<String> recipients = new LinkedList<>();
      recipients.add(Event.EVERYBODY);

      UserAddedEventDto userAddedEventDto = new UserAddedEventDto(
        context.getAuthenticator().getUsername(),
        recipients,
        userDto
      );

      Bus.getInstance().postMainChannel(userAddedEventDto);

      return authenticationPayload;
    };
  }


  /**
   * Does a login with the provided email and password.
   *
   * @return the account and a token
   */
  @DataFetcherWiring(
    type = "Query",
    name = "login"
  )
  public DataFetcher<AuthenticationPayload> login() {
    return environment -> {
      Context context = environment.getContext();
      LoginInput loginInput = Util.convertMap(environment.getArguments(), LoginInput.class);

      return AccountRepository.login(context, loginInput);
    };
  }

  /**
   * changes the password of an account.
   *
   * @return the new updated account
   */
  @DataFetcherWiring(
    type = "Mutation",
    name = "updatePassword"
  )
  public DataFetcher<Boolean> updatePassword() {
    return environment -> {
      Context context = environment.getContext();
      UpdatePasswordInput updatePasswordInput
        = Util.convertMap(environment.getArguments(), UpdatePasswordInput.class);

      return AccountRepository.updatePassword(context, updatePasswordInput);
    };
  }

  /**
   * changes the email address of an account.
   *
   * @return the new updated account
   */
  @DataFetcherWiring(
    type = "Mutation",
    name = "updateEmail"
  )
  public DataFetcher<Boolean> updateEmail() {
    return environment -> {
      Context context = environment.getContext();
      UpdateEmailInput input = Util.convertMap(environment.getArguments(), UpdateEmailInput.class);

      return AccountRepository.updateEmail(context, input);
    };
  }

  /**
   * changes the global notification settings of a user.
   *
   * @return the new updated account
   */
  @DataFetcherWiring(
    type = "Mutation",
    name = "updateNotificationSettings"
  )
  public DataFetcher<Boolean> updateNotificationSettings() {
    return environment -> {
      Context context = environment.getContext();
      UpdateNotificationInput input =
        Util.convertMap(environment.getArguments(), UpdateNotificationInput.class);

      return AccountRepository.updateNotificationSettings(context, input);
    };
  }

}
