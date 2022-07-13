package de.chatsphere.feature.config;

import java.lang.reflect.Type;
import java.net.URL;
import org.seedstack.coffig.Coffig;
import org.seedstack.coffig.provider.JacksonProvider;

/**
 * Configuration Object which encapsulates the loading of Config objects.
 */
public class Config {

  private static URL url = Config.class.getClassLoader().getResource("properties.yaml");

  private static Coffig coffig = Coffig.builder().withProviders(
    new JacksonProvider().addSource(url)
  ).build();

  /**
   * Loads a specific configuration
   *
   * <p>May be obsolete in the future by replacing it with an annotation.
   *
   * @param configurable the {@link org.seedstack.coffig} annotated class
   * @param <T>          class instance object
   *
   * @return the initialized and configured class instance
   * @see org.seedstack.coffig.Coffig#get(Type, String...)
   */
  public static <T> T getConfig(Class<T> configurable, String... path) {
    return coffig.get(configurable, path);
  }
}
