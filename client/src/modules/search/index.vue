<!--
  Offers user interfaces to seek out new chats
  and display search results.
-->
<template>
  <div>
    <!-- Navigationbar -->
    <v-navigation-drawer
      v-model="drawer"
      app
      absolute
      floating>
      <navigation/>
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
      <v-text-field
        v-model="searchText"
        color="blank"
        label="Search"
        hide-details
        autofocus
        single-line
      />
    </v-toolbar>

    <!-- Main content -->
    <v-content app>
      <v-expansion-panel
        v-model="isExpanded"
        expand>

        <!-- Results for Private Chats -->
        <ExpansionPansel
          :type="true"
          :title="'Private Chats'"
          :list="privateChatMatches"
        />

        <!-- Results for Group Chats -->
        <ExpansionPansel
          :type="true"
          :title="'Group Chats'"
          :list="groupChatMatches"
        />

        <!-- Results for Contacts -->
        <ExpansionPansel
          :type="false"
          :title="'Contacts'"
          :list="contactMatches"
          :path="'/profile/'"
        />

      </v-expansion-panel>
    </v-content>

  </div>
</template>

<style scoped>

</style>
<script>
import Navigation from '@/modules/navigation/';
import ExpansionPansel from './_components/ResultExpansionPanel';
import SEARCH from './_queries/Search.graphql';

/**
 * @vue-data {Object} chatMatches - Container of all types results
 *                                [0] - Results of one's own open private chats
 *                                [1] - Results of one's own group chat matches
 * @vue-data {Object} contactMatches - Container of contacts result. Not part of chatMatches because
 *                                     the entry's responses vary in structure to chats.
 * @vue-data {Array} isExpanded - whether privateChat (index 0), groupchat (index 1),
 *                                   or contacts (index 2) is expanded
 * @vue-data {String} searchText - current user input
 */
export default {
  name: 'Search',

  components: {
    Navigation,
    ExpansionPansel,
  },

  props: { },

  data() {
    return {
      drawer: this.$vuetify.breakpoint.name !== 'xs',
      searchText: '',
      // no choice but to put it in an array due to vuetify demanding it
      isExpanded: [true, true, true],
      privateChatMatches: [],
      groupChatMatches: [],
      contactMatches: [],
      /* chatMatches: [
        {
          name: 'Private Chats',
          results: [],
        },
        {
          name: 'Group Chats',
          results: [],
        },
      ],
      contactMatches: {
        name: 'Contacts',
        results: [],
      }, */
    };
  },

  apollo: {

    /**
     * Query for Chats + Contacts
     *
     * `search` is not used. Responses are processed and distributed to appropriate arrays.
     *
     */
    search: {
      query: SEARCH,

      /**
       * Send the user's input
       */
      variables() {
        return {
          input: this.searchText,
        };
      },

      /**
       * Extract relevant information of response
       */
      update(result) {
        /* eslint-disable no-underscore-dangle */
        // discard previous results
        this.privateChatMatches = [];
        this.groupChatMatches = [];
        this.contactMatches = [];

        // Process chats and put into appropriate arrays
        result.searchChat.forEach(chat => {
          if (chat.__typename === 'PrivateChat') {
            this.privateChatMatches.push(chat);
          } else if (chat.__typename === 'GroupChat') {
            this.groupChatMatches.push(chat);
          }
        });

        // Process contacts and put into appropriate array
        this.contactMatches = result.account.contacts.filter(contact => {
          // show results whose prefix match either nickname, username or displayname
          let [nickname, username, displayName] = [
            contact.nickname || '',
            contact.user.username,
            contact.user.displayName,
          ];
          [nickname, username, displayName] = [
            nickname.toLowerCase(),
            username.toLowerCase(),
            displayName.toLowerCase(),
          ];

          return nickname.startsWith(this.searchText.toLowerCase())
          || username.startsWith(this.searchText.toLowerCase())
          || displayName.startsWith(this.searchText.toLowerCase());
        });

        // Expand panel if there are results inside
        this.isExpanded[0] = this.privateChatMatches.length > 0;
        this.isExpanded[1] = this.groupChatMatches.length > 0;
        this.isExpanded[2] = this.contactMatches.length > 0;

        return undefined;
      },

      debounce: 200,
    },
  },
};
</script>
