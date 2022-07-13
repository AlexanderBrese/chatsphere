package de.chatsphere.api.shared.scalar;

import de.chatsphere.server.graphql.schema.annotation.ScalarWiring;
import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;
import java.util.regex.Pattern;

public class UsernameScalar {

  private static final Pattern USERNAME_REGEX = Pattern.compile(
    "\\s",
    Pattern.UNICODE_CHARACTER_CLASS);

  @ScalarWiring
  public static final GraphQLScalarType USERNAME = new GraphQLScalarType("Username",
    "A custom scalar that handles usernames",
    new Coercing<Object, Object>() {
      @Override
      public Object serialize(Object dataFetcherResult) {
        return serializeUsername(dataFetcherResult);
      }

      @Override
      public Object parseValue(Object input) {
        return parseUsernameFromVariable(input);
      }

      @Override
      public Object parseLiteral(Object input) {
        return parseUsernameFromAstLiteral(input);
      }
    }
  );

  private static boolean lookslikeAUsername(String possibleUsernameValue) {
    return !possibleUsernameValue.isEmpty()
      && possibleUsernameValue.length() >= 4
      && possibleUsernameValue.length() <= 20
      && !USERNAME_REGEX.matcher(possibleUsernameValue).find();
  }

  private static Object serializeUsername(Object dataFetcherResult) {
    String possibleUsernameValue = String.valueOf(dataFetcherResult);
    if (lookslikeAUsername(possibleUsernameValue)) {
      return possibleUsernameValue;
    } else {
      throw new CoercingSerializeException("Unable to serialize "
        + possibleUsernameValue + " as an username");
    }
  }

  private static Object parseUsernameFromVariable(Object input) {
    if (input instanceof String) {
      String possibleUsernameValue = input.toString();
      if (lookslikeAUsername(possibleUsernameValue)) {
        return possibleUsernameValue;
      }
    }
    throw new CoercingParseValueException("Unable to parse variable value "
      + input + " as an username");
  }

  private static Object parseUsernameFromAstLiteral(Object input) {
    if (input instanceof StringValue) {
      String possibleUsernameValue = ((StringValue) input).getValue();
      if (lookslikeAUsername(possibleUsernameValue)) {
        return possibleUsernameValue;
      }
    }
    throw new CoercingParseLiteralException(
      "Value is not a valid username : '" + String.valueOf(input) + "'"
    );
  }
}
