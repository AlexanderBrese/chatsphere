package de.chatsphere.server.graphql.schema;

import de.chatsphere.server.graphql.schema.SchemaFetcher.SchemaConfig;
import graphql.GraphQLError;
import graphql.language.InterfaceTypeDefinition;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.TypeDefinitionRegistry;
import graphql.schema.idl.errors.SchemaProblem;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Defines an executable GraphQLSchema using annotated wiring.
 */
public final class AnnotatedSchema {

  private static final Logger log = LoggerFactory.getLogger(AnnotatedSchema.class);
  private final GraphQLSchema graphqlSchema;

  /**
   * Initialize the GraphQLSchema by building it.
   */
  public AnnotatedSchema() {
    graphqlSchema = build();
  }

  /**
   * Gets the GraphQLSchema.
   *
   * @return the GraphQLSchema
   */
  public GraphQLSchema getSchema() {
    return graphqlSchema;
  }

  /**
   * Builds an executable GraphQLSchema by reading a predefined schema SDL file, extracting name
   * definitions from it and wiring it using annoated wiring.
   *
   * @return the built GraphQLSchema
   */
  private GraphQLSchema build() {
    // TODO: ChangeMe
    SchemaFetcher schemaFetcher = new SchemaFetcher(new SchemaConfig());

    TypeDefinitionRegistry typeRegistry = schemaFetcher.getTypeRegistry();
    List<String> interfaceTypes = new LinkedList<>();
    typeRegistry.types().values().forEach(typeDefinition -> {
      if (typeDefinition instanceof InterfaceTypeDefinition) {
        interfaceTypes.add(typeDefinition.getName());
      }
    });

    RuntimeWiring wiring = getWiring(interfaceTypes);
    return buildSchema(typeRegistry, wiring);
  }


  /**
   * Gets the runtime wiring for the schema.
   *
   * @return the runtime wiring
   */
  private RuntimeWiring getWiring(List<String> interfaceTypes) {
    AnnotatedWiring annotatedWiring = new AnnotatedWiring(interfaceTypes);
    return annotatedWiring.buildRuntimeWiring();
  }

  /**
   * Builds an executable GraphQLSChema using a schema generator with name definitions and wiring.
   *
   * @param typeRegistry the name definitons
   * @param wiring       the sdl wiring
   *
   * @return the GraphQLSchema
   */
  private GraphQLSchema buildSchema(TypeDefinitionRegistry typeRegistry, RuntimeWiring wiring) {
    SchemaGenerator schemaGenerator = new SchemaGenerator();
    GraphQLSchema schema = null;
    try {
      schema = schemaGenerator.makeExecutableSchema(typeRegistry, wiring);
    } catch (SchemaProblem schemaProblem) {
      for (GraphQLError e : schemaProblem.getErrors()) {
        log.error(e.getMessage());
      }
    }
    return schema;
  }
}
