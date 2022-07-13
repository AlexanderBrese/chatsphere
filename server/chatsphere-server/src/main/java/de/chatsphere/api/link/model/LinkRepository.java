package de.chatsphere.api.link.model;

import de.chatsphere.api.link.transfer.CreateLinkInput;
import de.chatsphere.api.link.transfer.LinkDto;
import de.chatsphere.io.database.schema.Link;
import de.chatsphere.server.graphql.Context;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * A repository is a gateway between business logic and database connection. This repository defines
 * how links get accessed (queried/mutated) via the database.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LinkRepository {

  /**
   * Queries for all links in the database.
   *
   * @param context the context used to connect to the database
   * @return a list of link data transmitter objects
   */
  public static List<LinkDto> getLinks(Context context) {
    List<LinkDto> linkDtos = new LinkedList<>();

    try {
      context.getDatabase().getDao(Link.class).queryForAll()
          .forEach(link -> addLinkDto(link, linkDtos));
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return linkDtos;
  }

  /**
   * Creates a new link in the database and returns it with a mutation response.
   *
   * @param context   the context used to connect to the database.
   * @param linkInput arguments to create a link
   * @return the mutation response of the link creation
   */
  public static LinkDto createLink(Context context, CreateLinkInput linkInput) {
    Link link = new Link();
    link.setDescription(linkInput.getDescription());
    link.setUrl(linkInput.getUrl());

    LinkDto linkDto = null;

    try {
      context.getDatabase().getDao(Link.class).create(link);
      int id = context.getDatabase().getDao(Link.class).queryForSameId(link).getId();
      linkDto = new LinkDto(id, linkInput.getUrl(), linkInput.getDescription());
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return linkDto;
  }

  /**
   * Converts a database link object into a new link dto and adds it to a linkDTO list.
   *
   * @param link     the database link object to convert
   * @param linkDtos the link dto list
   */
  private static void addLinkDto(Link link, List<LinkDto> linkDtos) {
    LinkDto linkDto = new LinkDto(link.getId(), link.getUrl(), link.getDescription());
    linkDtos.add(linkDto);
  }
}
