package de.chatsphere.api.contact.resolver;

import de.chatsphere.api.contact.model.ContactRepository;
import de.chatsphere.api.contact.transfer.BlockContactInput;
import de.chatsphere.api.contact.transfer.ContactDto;
import de.chatsphere.api.contact.transfer.CreateContactInput;
import de.chatsphere.api.contact.transfer.RemoveContactInput;
import de.chatsphere.api.contact.transfer.UnblockContactInput;
import de.chatsphere.server.graphql.Context;
import de.chatsphere.server.graphql.schema.annotation.DataFetcherWiring;
import de.chatsphere.util.Util;
import graphql.schema.DataFetcher;

public final class ContactResolver {

  /**
   * Adds a contact to the account's contact list.
   *
   * @return the contact
   */
  @DataFetcherWiring(
    type = "Mutation",
    name = "createContact"
  )
  public DataFetcher<ContactDto> createContact() {
    return environment -> {
      Context context = environment.getContext();
      CreateContactInput createContactInput =
        Util.convertMap(environment.getArguments(), CreateContactInput.class);
      return ContactRepository.createContact(context, createContactInput);
    };
  }

  /**
   * TODO: Implement
   * Changes the nickname of a contact.
   *
   * @return true/false
   */
  @DataFetcherWiring(
    type = "Mutation",
    name = "updateNickname"
  )
  public DataFetcher<Boolean> updateNickname() {
    return environment -> false;
  }

  /**
   * Removes a contact from the account's contact list.
   *
   * @return true/false
   */
  @DataFetcherWiring(
    type = "Mutation",
    name = "removeContact"
  )
  public DataFetcher<Boolean> removeContact() {
    return environment -> {
      Context context = environment.getContext();
      RemoveContactInput removeContactInput =
        Util.convertMap(environment.getArguments(), RemoveContactInput.class);
      return ContactRepository.removeContact(context, removeContactInput);
    };
  }

  /**
   * Adds a contact to the account's block list.
   *
   * @return true/false
   */
  @DataFetcherWiring(
    type = "Mutation",
    name = "blockContact"
  )
  public DataFetcher<Boolean> blockContact() {
    return environment -> {
      Context context = environment.getContext();
      BlockContactInput input =
        Util.convertMap(environment.getArguments(), BlockContactInput.class);

      return ContactRepository.blockContact(context, input);
    };
  }

  /**
   * Removes a contact from the account's block list.
   *
   * @return true/false
   */
  @DataFetcherWiring(
    type = "Mutation",
    name = "unblockContact"
  )
  public DataFetcher<Boolean> unblockContact() {
    return environment -> {
      Context context = environment.getContext();
      UnblockContactInput input =
        Util.convertMap(environment.getArguments(), UnblockContactInput.class);

      return ContactRepository.unblockContact(context, input);
    };
  }
}
