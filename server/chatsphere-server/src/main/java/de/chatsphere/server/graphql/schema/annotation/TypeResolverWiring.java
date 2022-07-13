package de.chatsphere.server.graphql.schema.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation used to define name resolver wiring.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TypeResolverWiring {

  /**
   * Defines the type resolver name (interface name).
   *
   * @return the type resolver name.
   */
  String name();
}
