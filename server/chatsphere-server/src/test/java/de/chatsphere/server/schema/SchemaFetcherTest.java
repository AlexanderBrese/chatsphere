package de.chatsphere.server.schema;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import de.chatsphere.server.graphql.schema.SchemaFetcher;
import de.chatsphere.server.graphql.schema.SchemaFetcher.SchemaConfig;
import org.junit.Test;

public class SchemaFetcherTest {

  private SchemaConfig config = new SchemaConfig("test-sdl", "graphqls");

  @Test(expected = NullPointerException.class)
  public void testEmptyPathException() {
    SchemaConfig config = new SchemaConfig("path-does-not-exist", "graphqls");
    SchemaFetcher fetcher = new SchemaFetcher(config);
  }


  @Test
  public void testSchemaFetching() {
    SchemaFetcher fetcher = new SchemaFetcher(config);
    assertThat(fetcher.getTypeRegistry().getType("Link").isPresent(), is(true));
    assertThat(fetcher.getTypeRegistry().getType("trap").isPresent(), is(false));
  }

}
