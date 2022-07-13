package de.chatsphere.api.link.resolver;

import de.chatsphere.api.link.model.LinkRepository;
import de.chatsphere.api.link.transfer.CreateLinkInput;
import de.chatsphere.api.link.transfer.LinkAddedEventDto;
import de.chatsphere.api.link.transfer.LinkDto;
import de.chatsphere.server.graphql.Context;
import de.chatsphere.server.graphql.schema.annotation.DataFetcherWiring;
import de.chatsphere.server.rxbus.Bus;
import de.chatsphere.server.rxbus.Event;
import de.chatsphere.util.Util;
import graphql.schema.DataFetcher;
import java.util.List;
import lombok.NoArgsConstructor;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Defines resolvers for links. This is were business logic happens (authorization, database access,
 * ...)
 */
@NoArgsConstructor
public final class LinkResolver {

  private static final Logger log = LoggerFactory.getLogger(LinkResolver.class);

  /**
   * Fetches all available links through the link repository.
   *
   * @return a data fetcher with a list of link data transmitter objects
   */
  @DataFetcherWiring(
      type = "Query",
      name = "allLinks"
  )
  public DataFetcher<List<LinkDto>> allLinks() {
    return environment -> LinkRepository.getLinks(environment.getContext());
  }

  /**
   * Creates a new link through the link repository.
   *
   * @return the resulting data fetcher for the mutation
   */
  @DataFetcherWiring(
      type = "Mutation",
      name = "createLink"
  )
  public DataFetcher<LinkDto> createLink() {
    return environment -> {
      Context context = environment.getContext();

      CreateLinkInput createLinkInput =
          Util.convertMap(environment.getArguments(), CreateLinkInput.class);

      LinkDto linkDto = LinkRepository
          .createLink(environment.getContext(), createLinkInput);
      LinkAddedEventDto event =
          new LinkAddedEventDto(context.getAuthenticator().getUsername(), Event.EVERYBODY, linkDto);
      Bus.getInstance().postMainChannel(event);

      return linkDto;
    };
  }

  /**
   * Provides a Flowable object from the newly created links in the main channel.
   * Handlers can then subscribe to this Flowable in their response message
   * via the internal subscription mechanism from graphql java.
   *
   * @return the resulting data fetcher for the Flowable object
   */
  @DataFetcherWiring(
      type = "Subscription",
      name = "linkAdded"
  )
  public DataFetcher<Publisher<LinkAddedEventDto>> linkAdded() {
    return environment -> Bus.getInstance().getMainChannel().asFlowable(LinkAddedEventDto.class);
  }
}
