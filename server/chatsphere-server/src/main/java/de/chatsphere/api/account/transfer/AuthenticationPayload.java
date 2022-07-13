package de.chatsphere.api.account.transfer;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class AuthenticationPayload {

  @Getter
  private final String token;
}
