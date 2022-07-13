package de.chatsphere.api.shared.scalar;

import de.chatsphere.server.graphql.schema.annotation.ScalarWiring;
import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;
import java.util.regex.Pattern;

public class EmailScalar {

  // RFC822
  private static final Pattern EMAIL_REGEX = Pattern.compile(
    "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
    Pattern.CASE_INSENSITIVE);

  @ScalarWiring
  public static final GraphQLScalarType EMAIL = new GraphQLScalarType("Email",
    "A custom scalar that handles emails",
    new Coercing<Object, Object>() {
      @Override
      public Object serialize(Object dataFetcherResult) {
        return serializeEmail(dataFetcherResult);
      }

      @Override
      public Object parseValue(Object input) {
        return parseEmailFromVariable(input);
      }

      @Override
      public Object parseLiteral(Object input) {
        return parseEmailFromAstLiteral(input);
      }
    }
  );

  private static boolean looksLikeAnEmailAddress(String possibleEmailValue) {
    return !possibleEmailValue.isEmpty()
      && EMAIL_REGEX.matcher(possibleEmailValue).find();
  }

  private static Object serializeEmail(Object dataFetcherResult) {
    String possibleEmailValue = String.valueOf(dataFetcherResult);
    if (looksLikeAnEmailAddress(possibleEmailValue)) {
      return possibleEmailValue;
    } else {
      throw new CoercingSerializeException("Unable to serialize "
        + possibleEmailValue + " as an email address");
    }
  }

  private static Object parseEmailFromVariable(Object input) {
    if (input instanceof String) {
      String possibleEmailValue = input.toString();
      if (looksLikeAnEmailAddress(possibleEmailValue)) {
        return possibleEmailValue;
      }
    }
    throw new CoercingParseValueException("Unable to parse variable value "
      + input + " as an email address");
  }

  private static Object parseEmailFromAstLiteral(Object input) {
    if (input instanceof StringValue) {
      String possibleEmailValue = ((StringValue) input).getValue();
      if (looksLikeAnEmailAddress(possibleEmailValue)) {
        return possibleEmailValue;
      }
    }
    throw new CoercingParseLiteralException(
      "Value is not any email address : '" + String.valueOf(input) + "'"
    );
  }
}
