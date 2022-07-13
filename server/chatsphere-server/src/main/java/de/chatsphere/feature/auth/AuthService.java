package de.chatsphere.feature.auth;

import io.jsonwebtoken.JwtException;
import lombok.Getter;
import lombok.Setter;

public final class AuthService {

  private TokenService tokenService = new TokenService();
  @Getter
  @Setter
  private String username;

  /**
   * Decrypts a secured JWT.
   *
   * @param jws the secured JWT
   *
   * @return the username
   * @throws IllegalArgumentException a wrong token was supplied
   */
  public String decrypt(String jws) throws IllegalArgumentException {
    if (jws == null || jws.isEmpty()) {
      throw new IllegalArgumentException("Wrong token supplied.");
    }
    try {
      return tokenService.readToken(jws);
    } catch (JwtException e) {
      throw new IllegalArgumentException("Wrong token supplied.");
    }
  }

  /**
   * Encrypts a username into a secured JWT.
   *
   * @param username the username
   *
   * @return the secured JWT
   */
  public String encrypt(String username) {
    return tokenService.createToken(username);
  }

  /**
   * Checks if a username exists and therefore is authenticated.
   *
   * @return true/false
   */
  public boolean isAuthenticated() {
    return !username.isEmpty();
  }
}
