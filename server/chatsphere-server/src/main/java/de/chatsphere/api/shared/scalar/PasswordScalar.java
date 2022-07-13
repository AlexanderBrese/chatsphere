package de.chatsphere.api.shared.scalar;

import de.chatsphere.server.graphql.schema.annotation.ScalarWiring;
import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PasswordScalar {
  private static final Logger logger = LoggerFactory.getLogger(PasswordScalar.class);

  private static final Pattern PASSWORD_REGEX =
    Pattern.compile("^(?=.*[A-ZÖÜÄẞ])(?=.*([!@#$%^&+=]|[0-9])).{8,}$", Pattern.CASE_INSENSITIVE);

  @ScalarWiring
  public static final GraphQLScalarType PASSWORD = new GraphQLScalarType("Password",
    "A custom scalar that handles passwords",
    new Coercing<Object, Object>() {
      @Override
      public Object serialize(Object dataFetcherResult) {
        return serializePassword(dataFetcherResult);
      }

      @Override
      public Object parseValue(Object input) {
        return parsePasswordFromVariable(input);
      }

      @Override
      public Object parseLiteral(Object input) {
        return parsePasswordFromAstLiteral(input);
      }
    }
  );

  private static boolean looksLikeAPassword(String possiblePasswordValue) {
    logger.info(PASSWORD_REGEX.matcher(possiblePasswordValue).find() + "");
    return !possiblePasswordValue.isEmpty()
      && possiblePasswordValue.length() <= 32
      && PASSWORD_REGEX.matcher(possiblePasswordValue).find();
  }

  private static Object serializePassword(Object dataFetcherResult) {
    String possiblePasswordValue = String.valueOf(dataFetcherResult);
    if (looksLikeAPassword(possiblePasswordValue)) {
      return possiblePasswordValue;
    } else {
      throw new CoercingSerializeException("Unable to serialize "
        + possiblePasswordValue + " as a password");
    }
  }

  private static Object parsePasswordFromVariable(Object input) {
    if (input instanceof String) {
      String possiblePasswordValue = input.toString();
      if (looksLikeAPassword(possiblePasswordValue)) {
        return possiblePasswordValue;
      }
    }
    throw new CoercingParseValueException("Unable to parse variable value "
      + input + " as a password");
  }

  private static Object parsePasswordFromAstLiteral(Object input) {
    if (input instanceof StringValue) {
      String possiblePasswordValue = ((StringValue) input).getValue();
      if (looksLikeAPassword(possiblePasswordValue)) {
        return possiblePasswordValue;
      }
    }
    throw new CoercingParseLiteralException(
      "Value is not a valid password : '" + String.valueOf(input) + "'"
    );
  }
}
