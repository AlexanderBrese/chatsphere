package de.chatsphere.feature.password;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.core.Is.is;

import org.junit.Test;

public class HasherTest {

  private final Hasher hasher = new Hasher(new HasherConfig());
  private final String testString = "٩(-̮̮̃-̃)۶ ٩(●̮̮̃•̃)۶ ٩(͡๏̯͡๏)۶ ٩(-̮̮̃•̃)";
  private final String testStringHashed = "$argon2i$v=19$m=65536,t=11,p=1$ZM4gr8iHTFHrS6KpWQUQ5Q$"
    + "tcK/oCg82RnAJUBqS9lLOZQx/9XAyfZbmiLtxCoexAU";

  /**
   * Ensures that the resulting hash is within the limit of the database schema.
   */
  @Test
  public void testUnicodeHashLength() {

    System.out.println(hasher.hash("Password123"));
    assertThat(hasher.hash(testString).length(), is(lessThan(256)));
  }

  /**
   * Ensures decoding functionality.
   */
  @Test
  public void testUnicodeVerify() {
    assertThat(hasher.verify(testStringHashed, testString), is(true));
  }

  /**
   * Ensures that hash values created can be verified correctly.
   */
  @Test
  public void selfHashVerificationTest() {
    assertThat(hasher.verify(hasher.hash(testString), testString), is(true));
  }
}
