<template>
  <v-layout>
    <!-- Navigationbar -->
    <v-navigation-drawer
      v-model="drawer"
      app
      absolute
      floating>
      <navigation :disable-resize-watcher="true"/>
    </v-navigation-drawer>
    <!-- Toolbar -->
    <v-toolbar
      :fixed="$vuetify.breakpoint.xsOnly"
      :absolute="$vuetify.breakpoint.smAndUp"
      dark
      color="primary"
      app>
      <v-toolbar-side-icon
        v-if="$vuetify.breakpoint.name === 'xs'"
        class="white--text"
        @click.native="drawer = !drawer"/>
      <template v-if="searchEnabled">
        <v-text-field
          v-model="searchText"
          color="blank"
          label="Search"
          hide-details
          autofocus
          single-line
        />
        <v-btn
          icon
          @click.stop="searchEnabled = !searchEnabled">
          <v-icon>close</v-icon>
        </v-btn>
      </template>

      <template v-else>
        <v-toolbar-title>{{ title | uppercase }}</v-toolbar-title>
        <v-spacer/>
        <v-btn
          icon
          @click.stop="searchEnabled = !searchEnabled">
          <v-icon>add</v-icon>
        </v-btn>
      </template>
    </v-toolbar>

    <v-content>
      <template v-if="!searchEnabled">
        <!-- <InviteFriends/> -->
        <ContactList :contacts="contacts"/>
      </template>
      <template v-else>
        <SearchContacts
          :users="queriedUsers"
          :contacts="contacts"/>
      </template>
    </v-content>

  </v-layout>
</template>

<script>
import Navigation from '@/modules/navigation/';
import InviteFriends from './_components/InviteFriends';
import ContactList from './_components/ContactList';
import SearchContacts from './_components/SearchContacts';
import SEARCH_USERS from './_queries/SearchUsers.graphql';
import USER_ADDED from './_queries/UserAdded.graphql';
import CONTACTS from './_queries/Contacts.graphql';

export default {
  name: 'ContactModule',
  components: {
    Navigation,
    InviteFriends,
    ContactList,
    SearchContacts,
  },
  data() {
    return {
      drawer: this.$vuetify.breakpoint.name !== 'xs',
      title: 'Contacts',
      searchEnabled: false,
      searchText: '',
    };
  },
  apollo: {
    // Query with parameters
    queriedUsers: {
      query: SEARCH_USERS,
      // Reactive parameters
      variables() {
        // Use vue reactive properties here
        return {
          username: this.searchText,
        };
      },
      update(data) {
        return data.searchUsers;
      },
      debounce: 200,
    },
    $subscribe: {
      // When a tag is added
      userAdded: {
        query: USER_ADDED,
        // Result hook
        result({ data }) {
          this.queriedUsers.push(data.userAdded.user);
        },
      },
    },
    contacts: {
      query: CONTACTS,
      update(data) {
        return data.account.contacts;
      },
    },
  },
};
</script>

<style scoped></style>
