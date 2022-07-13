package de.chatsphere.server.graphql.schema.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation used to define data fetcher wiring.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DataFetcherWiring {

  /**
   * Defines the data fetcher name (Query, Mutation, Subscription).
   *
   * @return the data fetcher name
   */
  String type();

  /**
   * Defines the data fetcher name.
   *
   * @return the data fetcher name.
   */
  String name();
}
