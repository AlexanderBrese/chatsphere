package de.chatsphere.api.contact.model;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import de.chatsphere.api.account.model.AccountRepository;
import de.chatsphere.api.contact.transfer.BlockContactInput;
import de.chatsphere.api.contact.transfer.ContactDto;
import de.chatsphere.api.contact.transfer.CreateContactInput;
import de.chatsphere.api.contact.transfer.RemoveContactInput;
import de.chatsphere.api.contact.transfer.UnblockContactInput;
import de.chatsphere.api.shared.model.Reportable;
import de.chatsphere.io.database.Database;
import de.chatsphere.io.database.schema.user.User;
import de.chatsphere.io.database.schema.user.UserRelationship;
import de.chatsphere.server.graphql.Context;
import graphql.GraphQLException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ContactRepository implements Reportable {

  private static final Logger log = LoggerFactory.getLogger(ContactRepository.class);

  /**
   * Creates a new user relationship in the database and returns the resulted converted into a
   * contact DTO.
   *
   * @param context the GraphQL context
   * @param createContactInput the contact creation input holding the contact's username
   * @return the resulting contact DTO
   */
  public static ContactDto createContact(Context context, CreateContactInput createContactInput) {
    Database database = context.getDatabase();
    User currentUser = AccountRepository.getCurrentUser(context);
    User contactUser = AccountRepository.getUser(context, createContactInput.getContactUsername());

    Dao<UserRelationship, Integer> contactDao = database.getDao(UserRelationship.class);
    UserRelationship relationship = UserRelationship.builder()
      .owner(currentUser)
      .refers(contactUser)
      .blocked(null)
      .build();

    try {
      contactDao.create(relationship);
    } catch (SQLException e) {
      log.error(e.getMessage());
      throw new GraphQLException(INTERNAL_ERROR);
    }

    return ContactDto.from(contactUser);
  }

  /**
   * Removes a user relationship from the database and returns the removed result converted into a
   * contact DTO.
   *
   * @param context the GraphQL context
   * @param removeContactInput the contact removal input holding the contact's username
   * @return the resulting contact DTO
   */
  public static Boolean removeContact(Context context, RemoveContactInput removeContactInput) {
    Database database = context.getDatabase();
    User currentUser = AccountRepository.getCurrentUser(context);
    User contactUser = AccountRepository.getUser(context, removeContactInput.getContactUsername());

    Dao<UserRelationship, Integer> contactDao = database.getDao(UserRelationship.class);
    UserRelationship contact;
    try {
      List<UserRelationship> relationShips = contactDao.queryBuilder()
        .where()
        .eq("owner", currentUser)
        .and()
        .eq("refers", contactUser)
        .query();

      contact = Objects.requireNonNull(relationShips.get(0));
    } catch (SQLException | NullPointerException e) {
      log.error(e.getMessage());
      throw new GraphQLException(INTERNAL_ERROR);
    }

    try {
      contactDao.delete(contact);
    } catch (SQLException e) {
      log.error(e.getMessage());
      throw new GraphQLException(INTERNAL_ERROR);
    }

    return true;
  }

  /**
   * Unblocks a contact by removing this relationship.
   *
   * @param context to gain read/write access to the database
   * @param input contains the blocked username
   * @return true if report has been received and saved
   */
  public static Boolean unblockContact(Context context, UnblockContactInput input) {

    String contactUsername = input.getContactUsername();
    // Get blocker
    User user = AccountRepository.getCurrentUser(context);
    // Get blocked user
    User blocked = AccountRepository.getUser(context, contactUsername);

    // Look for existing relationship
    try {
      Dao<UserRelationship, ?> relationshipDao = context.getDatabase()
        .getDao(UserRelationship.class);
      QueryBuilder<UserRelationship, ?> queryBuilder = relationshipDao.queryBuilder();

      queryBuilder
        .where()
        .eq("owner", user.getId())
        .and()
        .eq("refers", blocked.getId());

      List<UserRelationship> list = relationshipDao.query(queryBuilder.prepare());

      if (list.size() == 1) {
        UserRelationship relationship = list.get(0);
        context.getDatabase().getDao(UserRelationship.class).delete(relationship);
        return true;

      } else if (list.size() == 0) {
        throw new GraphQLException("User has not been blocked: " + contactUsername);
      } else {
        throw new GraphQLException(INTERNAL_ERROR);
      }

    } catch (SQLException e) {
      logger.error(e.getMessage());
      throw new GraphQLException(INTERNAL_ERROR);
    }
  }

  /**
   * Blocks a contact by adding/changing the relationship.
   *
   * @param context to gain read/write access to the database
   * @param input contains the blocked username
   * @return true if block was successful
   */
  public static Boolean blockContact(Context context, BlockContactInput input) {

    // Get blocker
    User user = AccountRepository.getCurrentUser(context);
    // Get blocked user
    User blocked = AccountRepository.getUser(context, input.getContactUsername());

    // Look for previous existing relationship if it exists
    try {
      Dao<UserRelationship, ?> relationshipDao = context.getDatabase()
        .getDao(UserRelationship.class);
      QueryBuilder<UserRelationship, ?> queryBuilder = relationshipDao.queryBuilder();

      queryBuilder
        .where()
        .eq("owner", user.getId())
        .and()
        .eq("refers", blocked.getId());

      List<UserRelationship> list = relationshipDao.query(queryBuilder.prepare());

      // If entry not yet existing
      if (list.size() == 0) {

        UserRelationship block = UserRelationship.builder()
          .owner(user)
          .refers(blocked)
          .blocked(new Date())
          .build();

        relationshipDao.create(block);

        return true;

        // Change from friend -> foe
      } else if (list.size() == 1) {

        UpdateBuilder<UserRelationship, ?> updateRelation = context.getDatabase()
          .getDao(UserRelationship.class).updateBuilder();
        updateRelation.updateColumnValue("blocked", new Date());
        updateRelation.where().eq("owner", user.getId());
        updateRelation.where().eq("refers", blocked.getId());
        updateRelation.update();

        return true;

      } else {
        throw new GraphQLException(INTERNAL_ERROR);
      }

    } catch (SQLException e) {
      logger.error(e.getMessage());
      throw new GraphQLException(INTERNAL_ERROR);
    }
  }

  /**
   * Used by AccountDto to get the blocked users
   *
   * @param context   need to do queries
   * @param user      get all blocked users of this user
   * @return list of contacts that are blocked
   */
  public static List<ContactDto> getBlockedUsers(Context context, User user) {

    Dao<UserRelationship, ?> relationshipDao = context.getDatabase().getDao(UserRelationship.class);

    try {
      QueryBuilder<UserRelationship, ?> queryBuilder = relationshipDao.queryBuilder();
      queryBuilder.where().eq("owner", user.getId()).and().isNotNull("blocked");

      List<UserRelationship> blocked = relationshipDao.query(queryBuilder.prepare());

      return ContactDto.from(blocked);

    } catch (SQLException e) {
      e.printStackTrace();
      logger.error(e.getMessage());
      return null;
    }
  }
}
