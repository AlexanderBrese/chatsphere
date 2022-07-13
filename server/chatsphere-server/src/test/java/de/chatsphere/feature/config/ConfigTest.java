package de.chatsphere.feature.config;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import de.chatsphere.io.database.DatabaseConfig;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.seedstack.coffig.Coffig;
import org.seedstack.coffig.provider.JacksonProvider;

@FixMethodOrder(MethodSorters.JVM)
public class ConfigTest {

  @Test
  public void testPropertiesExistence() {
    assertThat(getClass().getClassLoader().getResource("test.yaml").toString().isEmpty(),
      is(false));
  }

  @Test
  public void testPropertiesExistenceStatic() {
    assertThat(Config.class.getClassLoader().getResource("test.yaml").toString().isEmpty(),
      is(false));
  }

  @Test
  public void testCoffigConfigAvailability() {
    Coffig coffig = Coffig.builder().withProviders(
      new JacksonProvider().addSource(
        getClass().getClassLoader().getResource("test.yaml")
      )
    ).build();
    assertThat(coffig.toString().trim(), is(equalTo("---")));

    assertThat(coffig.get(String.class, "some.string"), is(equalTo("value1")));
    assertThat(coffig.get(int[].class, "some.array"), is(equalTo(new int[]{1, 2, 3})));
    assertThat(coffig.getOptional(String.class, "unknown.node").orElse("default"),
      is(equalTo("default")));

    assertThat(coffig.toString().trim(), is(not(equalTo("---"))));
    System.out.println(coffig.toString());
  }

  @Test
  public void testCoffigIntegration() {
    Coffig coffig = Coffig.builder().withProviders(
      new JacksonProvider().addSource(
        getClass().getClassLoader().getResource("properties.yaml")
      )
    ).build();
    DatabaseConfig config = coffig.get(DatabaseConfig.class, "database");
    assertThat(config.getPassword().trim().isEmpty(), is(false));
  }
}
