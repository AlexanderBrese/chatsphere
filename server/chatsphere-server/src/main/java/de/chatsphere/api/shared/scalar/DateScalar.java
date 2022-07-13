package de.chatsphere.api.shared.scalar;

import de.chatsphere.server.graphql.schema.annotation.ScalarWiring;
import graphql.schema.Coercing;
import graphql.schema.GraphQLScalarType;

public class DateScalar {

  @ScalarWiring
  public static final GraphQLScalarType DATE = new GraphQLScalarType("Date",
    "A custom scalar that handles dates",
    new Coercing<Object, Object>() {
      @Override
      public Object serialize(Object dataFetcherResult) {
        return dataFetcherResult;
      }

      @Override
      public Object parseValue(Object input) {
        return input;
      }

      @Override
      public Object parseLiteral(Object input) {
        return input;
      }
    }
  );
}
