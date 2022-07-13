<template>
  <v-list
    two-line
    class="py-0 my-2">
    <template v-for="(user, index) in users">
      <v-list-tile
        :key="user.username"
        avatar>
        <v-list-tile-avatar
          @click="select(user)">
          <img
            :src="user.avatarUrl !== ''
              ? user.avatarUrl
            : require('@/assets/avatar.png')">
        </v-list-tile-avatar>

        <v-list-tile-content>
          <v-list-tile-title>{{ user.displayName }}</v-list-tile-title>
          <v-list-tile-sub-title class="text--primary">{{ user.username }}</v-list-tile-sub-title>
          <v-list-tile-sub-title>{{ user.status }}</v-list-tile-sub-title>
        </v-list-tile-content>
        <v-list-tile-action>
          <v-btn
            v-if="!isExistingContact(user)"
            icon
            @click.native="add(user)">
            <v-icon color="primary">add</v-icon>
          </v-btn>
          <v-btn
            v-else
            icon
            @click.native="remove(user)">
            <v-icon color="primary">remove</v-icon>
          </v-btn>
        </v-list-tile-action>
      </v-list-tile>
      <v-divider
        v-if="index != users.length - 1"
        :key="index"
        inset/>
    </template>
  </v-list>
</template>

<script>
import CREATE_CONTACT from '../_queries/CreateContact.graphql';
import REMOVE_CONTACT from '../_queries/RemoveContact.graphql';
import CONTACTS from '../_queries/Contacts.graphql';

export default {
  name: 'SearchContacts',
  props: {
    users: {
      type: Array,
      required: false,
      default: () => ([]),
    },
    contacts: {
      type: Array,
      required: true,
      default: () => ([]),
    },
  },
  data() {
    return {
      error: '',
    };
  },
  methods: {
    isExistingContact(user) {
      return this.contacts.find((contact) => contact.user.username === user.username) !== undefined;
    },
    select(user) {
      this.$router.push({ name: 'Profile', params: { username: user.username } });
    },
    add(user) {
      try {
        this.$apollo.mutate({
          mutation: CREATE_CONTACT,
          variables: {
            createContactInput: {
              contactUsername: user.username,
            },
          },
          update: (store, { data: { createContact } }) => {
            // Add to all contacts list
            const data = store.readQuery({ query: CONTACTS });
            data.account.contacts.push(createContact);
            store.writeQuery({
              query: CONTACTS,
              data,
            });
          },
          optimisticResponse: {
            __typename: 'Mutation',
            createContact: {
              __typename: 'Contact',
              user: {
                __typename: 'User',
                username: user.username,
                displayName: user.displayName,
                status: user.status,
                avatarUrl: user.avatarUrl,
              },
              nickname: '',
              Date: new Date(),
            },
          },
        });
      } catch (e) {
        // eslint-disable-next-line
        console.error(e);
      }
    },
    remove(user) {
      try {
        this.$apollo.mutate({
          mutation: REMOVE_CONTACT,
          variables: {
            removeContactInput: {
              contactUsername: user.username,
            },
          },
          update: (store) => {
            // Add to all contacts list
            const data = store.readQuery({ query: CONTACTS });
            const index =
              data.account
                .contacts
                .findIndex(contact => contact.user.username === user.username);
            if (index === -1) {
              return;
            }
            data.account.contacts.splice(index, 1);
            store.writeQuery({
              query: CONTACTS,
              data,
            });
          },
          optimisticResponse: {
            __typename: 'Mutation',
            removeContact: true,
          },
        });
      } catch (e) {
        // eslint-disable-next-line
        console.error(e);
      }
    },
  },
};
</script>

<style scoped>

</style>
