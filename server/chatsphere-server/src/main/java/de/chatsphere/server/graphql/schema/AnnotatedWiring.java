package de.chatsphere.server.graphql.schema;

import de.chatsphere.server.graphql.schema.annotation.DataFetcherWiring;
import de.chatsphere.server.graphql.schema.annotation.ScalarWiring;
import de.chatsphere.util.Util;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLScalarType;
import graphql.schema.TypeResolver;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.RuntimeWiring.Builder;
import graphql.schema.idl.TypeRuntimeWiring;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A helper class to make wiring more convenient by using java reflection and annotations.
 */
final class AnnotatedWiring {

  private static final Logger log = LoggerFactory.getLogger(AnnotatedWiring.class);
  private final String apiPath;
  private final String resolverFolderName;
  private final String scalarFolderName;
  private final List<String> interfaceTypes;
  private Builder runtimeWiring;

  /**
   * Creates a new RuntimeWiring and registers annotated wiring.
   */
  AnnotatedWiring(List<String> interfaceTypes) {

    apiPath = "/de/chatsphere/api";
    resolverFolderName = "resolver";
    scalarFolderName = "scalar";
    runtimeWiring = RuntimeWiring.newRuntimeWiring();
    this.interfaceTypes = interfaceTypes;

    try {
      register();
    } catch (NullPointerException e) {
      log.error(e.getMessage());
    }
  }

  /**
   * Builds the runtime wiring.
   *
   * @return built runtime wiring
   */
  RuntimeWiring buildRuntimeWiring() {
    return runtimeWiring.build();
  }

  /**
   * Checks if the file is an eligible scalar directory.
   *
   * @param root the file
   *
   * @return true/false
   */
  private boolean isEligibleScalarDirectory(File root) {
    String name = root.getName();
    return root.isDirectory()
      && !name.equals("model")
      && !name.equals("transfer")
      && !name.equals(resolverFolderName);
  }

  /**
   * Checks if the file is an eligible resolver directory.
   *
   * @param root the file
   *
   * @return true/false
   */
  private boolean isEligibleResolverDirectory(File root) {
    String name = root.getName();
    return root.isDirectory()
      && !name.equals("model")
      && !name.equals("transfer")
      && !name.equals(scalarFolderName);
  }

  /**
   * Checks if the file is eligible.
   *
   * @param file the file
   *
   * @return true/false
   */
  private boolean isEligibleFile(File file) {
    return file.isFile()
      && !file.getName().contains("$")
      && !file.getName().equals("package-info");
  }

  /**
   * Registers annotated wiring by querying the resolver folder for data fetchers and type
   * resolvers.
   *
   * @throws NullPointerException a file does not exist
   */
  private void register() throws NullPointerException {
    String fullApiPath = getClass().getResource(apiPath).getPath();
    File apiFolder = new File(fullApiPath);
    Objects.requireNonNull(apiFolder);
    String packageName = Util.getPackageName(apiPath);

    registerScalarRecursively(apiFolder, packageName);
    registerResolverRecursively(apiFolder, packageName);
    registerTypeResolverWiring();
  }

  /**
   * Registers resolvers recursively.
   *
   * @param root                 the directory
   * @param directoryPackageName the package name for this directory
   *
   * @throws NullPointerException a file does not exist
   */
  private void registerResolverRecursively(File root, String directoryPackageName)
    throws NullPointerException {
    for (final File file : Objects.requireNonNull(root.listFiles())) {
      StringJoiner joiner = new StringJoiner(".");
      joiner
        .add(directoryPackageName)
        .add(FilenameUtils.removeExtension(file.getName()));

      String packageName = joiner.toString();
      if (isEligibleFile(file)) {
        registerResolverWiring(file, packageName);
      } else if (isEligibleResolverDirectory(file)) {
        registerResolverRecursively(file, packageName);
      }
    }
  }

  /**
   * Registers resolver wiring in general for a certain path and file by getting the resolver class
   * and resolver class instance.
   *
   * @param file the resolver file
   */
  private void registerResolverWiring(File file, String packageName) {
    Class<?> resolverClass = Util.getClass(file, packageName);
    Object resolverInstance = Util.getInstance(resolverClass);

    for (Method method : resolverClass.getDeclaredMethods()) {
      if (method.isAnnotationPresent(DataFetcherWiring.class)) {
        registerDataFetcherWiring(method, resolverInstance);
      }
    }
  }

  /**
   * Registers scalars recursively.
   *
   * @param root                 the directory
   * @param directoryPackageName the package name of the directory
   *
   * @throws NullPointerException a file does not exist
   */
  private void registerScalarRecursively(File root, String directoryPackageName)
    throws NullPointerException {
    for (final File file : Objects.requireNonNull(root.listFiles())) {
      StringJoiner joiner = new StringJoiner(".");
      joiner
        .add(directoryPackageName)
        .add(FilenameUtils.removeExtension(file.getName()));

      String packageName = joiner.toString();
      if (isEligibleFile(file)) {
        registerScalarWiring(file, packageName);
      } else if (isEligibleScalarDirectory(file)) {
        registerScalarRecursively(file, packageName);
      }
    }
  }

  /**
   * Does the scalar wiring.
   *
   * @param file        the scalar
   * @param packageName the package name of the scalar
   */
  private void registerScalarWiring(File file, String packageName) {
    Class<?> resolverClass = Util.getClass(file, packageName);

    for (Field field : resolverClass.getDeclaredFields()) {
      if (field.isAnnotationPresent(ScalarWiring.class)) {
        addScalarWiring(field);
      }
    }
  }

  /**
   * Registers a data fetcher wiring by getting name and type from the DataFetcherWiring annotation
   * and by invoking the data fetcher method on the data fetcher class instance to get the data
   * fetcher object.
   *
   * @param dataFetcherMethod   the data fetcher method to invoke
   * @param dataFetcherInstance the data fetcher class instance used to invoke the method
   */
  private void registerDataFetcherWiring(Method dataFetcherMethod, Object dataFetcherInstance) {
    DataFetcherWiring annotation = dataFetcherMethod.getDeclaredAnnotation(DataFetcherWiring.class);
    String name = annotation.name();
    String type = annotation.type();
    DataFetcher<?> dataFetcher = null;
    try {
      dataFetcher = (DataFetcher<?>) dataFetcherMethod.invoke(dataFetcherInstance);
    } catch (IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }
    addDataFetcherWiring(type, name, dataFetcher);
  }


  /**
   * Registers type resolver wiring generically.
   */
  private void registerTypeResolverWiring() {
    interfaceTypes.forEach(interfaceType -> {
      TypeRuntimeWiring.Builder builder = TypeRuntimeWiring.newTypeWiring(interfaceType);
      runtimeWiring = runtimeWiring.type(builder.typeResolver(genericTypeResolver()));
    });
  }

  /**
   * Takes the class name of the environment object to return a schema object type. It is assumed
   * that the class name reflects the schema object name and ends with 'dto'.
   *
   * @return the type resolver
   */
  private TypeResolver genericTypeResolver() {
    return env -> {
      Object javaObject = env.getObject();
      String schemaType = javaObject.getClass().getSimpleName();
      // Remove 'Dto'
      schemaType = schemaType.substring(0, schemaType.length() - 3);
      return env.getSchema().getObjectType(schemaType);
    };
  }

  /**
   * Adds a scalar to the runtime wiring.
   *
   * @param scalarField the annotated field of the scalar
   */
  private void addScalarWiring(Field scalarField) {
    GraphQLScalarType scalarType;
    try {
      scalarType = (GraphQLScalarType) scalarField.get(null);
      runtimeWiring = runtimeWiring.scalar(scalarType);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
  }


  /**
   * Adds a data fetcher to the runtime wiring.
   *
   * @param type        the data fetcher name (query, mutation, subscription)
   * @param name        the data fetcher name
   * @param dataFetcher the data fetcher
   */
  private void addDataFetcherWiring(String type, String name, DataFetcher<?> dataFetcher) {
    TypeRuntimeWiring.Builder builder = TypeRuntimeWiring.newTypeWiring(type);
    runtimeWiring = runtimeWiring.type(builder.dataFetcher(name, dataFetcher));
  }
}
