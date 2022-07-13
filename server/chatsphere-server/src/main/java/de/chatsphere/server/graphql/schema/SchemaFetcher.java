package de.chatsphere.server.graphql.schema;

import graphql.GraphQLError;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import graphql.schema.idl.errors.SchemaProblem;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The schema fetcher takes all schemas from the api folder matching a certain type, parses them
 * into type definitions and merges those type definitions all together.
 */
public class SchemaFetcher {

  private static final Logger log = LoggerFactory.getLogger(SchemaFetcher.class);
  private final SchemaParser schemaParser;
  private final SchemaConfig schemaConfig;
  /**
   * Gets the built type definition registry.
   *
   * @return the type definition registry
   */
  @Getter
  private final TypeDefinitionRegistry typeRegistry;

  /**
   * Initialize a new schema fetcher.
   *
   * @param schemaConfig the schemaConfig Object
   */
  public SchemaFetcher(SchemaConfig schemaConfig) {
    this(schemaConfig, new SchemaParser(), new TypeDefinitionRegistry());
  }

  /**
   * Initialize a new schema fetcher. Enables complete Dependency Injection
   *
   * @param schemaConfig - the schema-config object
   * @param schemaParser - the GraphQl-Java SchemaParser
   * @param typeRegistry - the GraphQL-Java TypeDefinitionRegistry
   */
  public SchemaFetcher(SchemaConfig schemaConfig, SchemaParser schemaParser,
    TypeDefinitionRegistry typeRegistry) {
    this.schemaConfig = schemaConfig;
    this.schemaParser = schemaParser;
    this.typeRegistry = typeRegistry;
    this.build();
  }

  /**
   * Helper method that looks up every file matching a certain file type.
   *
   * @param root the root path to look from
   * @param type the file type
   *
   * @return found files
   */
  private static List<File> getFilesByType(File root, String type) {
    List<File> found = new ArrayList<>();

    if (isEligibleFile(root, type)) {
      found.add(root);
    } else if (root.isDirectory()) {
      for (File file : Objects.requireNonNull(root.listFiles())) {
        found.addAll(getFilesByType(file, type));
      }
    }

    return found;
  }

  /**
   * Checks if the file matches file type.
   *
   * @param file the file
   * @param type the file type
   *
   * @return true/false
   */
  private static boolean isEligibleFile(File file, String type) {
    return file.isFile() && file.getName().endsWith("." + type);
  }

  /**
   * Gets all schema files in a certain path.
   *
   * @return found schema files
   * @throws NullPointerException on invalid schema root path
   */
  private List<File> getSchemas() throws NullPointerException {
    File schemaRoot =
      new File(Objects
        .requireNonNull(
          SchemaFetcher.class.getClassLoader().getResource(schemaConfig.getSchemaPath()))
        .getFile());
    return getFilesByType(schemaRoot, schemaConfig.getEnding());
  }

  /**
   * Builds the type definitions registry.
   */
  private void build() {
    mergeTypeDefinitionRegistry(getSchemas());
  }

  /**
   * Merges schema files into type definition registry.
   *
   * @param schemas the schema files
   */
  private void mergeTypeDefinitionRegistry(List<File> schemas) {
    schemas.forEach(schema -> {
      try {
        TypeDefinitionRegistry registry = schemaParser.parse(schema);
        typeRegistry.merge(registry);
      } catch (SchemaProblem schemaProblem) {
        for (GraphQLError error : schemaProblem.getErrors()) {
          log.error(error.getMessage());
        }
      }
    });
  }

  /**
   * Configuration for the SchemaFetcher.
   */
  @NoArgsConstructor
  @AllArgsConstructor
  @Data
  public static class SchemaConfig {

    /**
     * Path to the modular GraphQL Specification resources root.
     */
    @Getter
    private String schemaPath = "api";

    /**
     * File extension for GraphQL Specification files.
     */
    @Getter
    private String ending = "graphqls";
  }
}
