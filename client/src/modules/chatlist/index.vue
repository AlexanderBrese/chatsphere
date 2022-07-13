<!--
  To view all open chats. New chats can be added or opened to start a conversation.
  The Sidebar and Search can be displayed.
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
      class="white--text"
      color="primary"
      app>
      <v-toolbar-side-icon
        v-if="$vuetify.breakpoint.name === 'xs'"
        class="white--text"
        @click.native="drawer = !drawer"/>
      <v-toolbar-title>{{ title | uppercase }}</v-toolbar-title>
      <v-spacer/>
      <v-btn
        v-if="selectedChats.length <= 0"
        icon
        class="white--text"
        @click.native="add()">
        <v-icon>add</v-icon>
      </v-btn>
      <v-menu
        v-else
        :close-on-content-click="false"
        bottom
        left>
        <v-btn
          slot="activator"
          dark
          icon
        >
          <v-icon>more_vert</v-icon>
        </v-btn>

        <v-list>
          <v-list-tile
            @click="remove()"
          >
            <v-list-tile-action>
              <v-icon medium>delete</v-icon>
            </v-list-tile-action>
            <v-list-tile-title>{{ $t('chatlist.deleteChat') }}</v-list-tile-title>
          </v-list-tile>
          <v-list-tile
            @click="notify()"
          >
            <v-list-tile-action>
              <v-switch
                v-model="notification"
                color="primary"
              />
            </v-list-tile-action>
            <v-list-tile-title>{{ $t('chatlist.notification') }}</v-list-tile-title>
          </v-list-tile>
        </v-list>
      </v-menu>
    </v-toolbar>
    <v-content>
      <ChatList
        :chats="chats"
        @select="onSelect"/>
    </v-content>

    <v-dialog
      v-model="dialog"
      :fullscreen="$vuetify.breakpoint.xsOnly"
      scrollable
      hide-overlay
      max-width="500px"
      transition="dialog-bottom-transition">
      <ContactsInviteView
        @closeDialog="dialog = false"/>
    </v-dialog>
  </div>
</template>

<style scoped>

</style>

<script>
import ChatList from '@/modules/chatlist/_components/ChatList';
import Navigation from '@/modules/navigation/';
import ContactsInviteView from './_components/invite';
import CHATS from './_queries/Chats.graphql';
import CHAT_ADDED from './_queries/ChatAdded.graphql';
import CHAT_LEFT from './_queries/ChatLeft.graphql';
import CHAT_MODIFIED from './_queries/ChatModified.graphql';
import LEAVE_CHATS from './_queries/LeaveChats.graphql';

export default {
  name: 'ChatModule',
  components: {
    ChatList,
    Navigation,
    ContactsInviteView,
  },
  data() {
    return {
      drawer: this.$vuetify.breakpoint.name !== 'xs',
      title: 'Chat',
      dialog: false,
      selectedChats: [],
      notification: false,
    };
  },
  methods: {
    add() {
      this.dialog = true;
    },
    onSelect(selectedChats) {
      this.selectedChats = selectedChats;
    },
    notify() {
      this.notification = !this.notification;
    },
    remove() {
      // eslint-disable-next-line no-console
      this.$apollo.mutate({
        mutation: LEAVE_CHATS,
        variables: {
          leaveChatsInput: {
            chatIds: this.selectedChats,
          },
        },
        update: (store) => {
          // Add to all contacts list
          const data = store.readQuery({ query: CHATS });
          this.selectedChats.forEach(leftChatId => {
            const index = data.account.chats.findIndex(chat => chat.id === leftChatId);
            if (index === -1) {
              return;
            }
            data.account.chats.splice(index, 1);
          });
          store.writeQuery({
            query: CHATS,
            data,
          });
        },
        optimisticResponse: {
          __typename: 'Mutation',
          leaveChats: true,
        },
      }).then(() => {
        this.selectedChats = [];
        // eslint-disable-next-line no-console
      }).catch((e) => console.error(e));
    },
  },
  apollo: {
    chats: {
      query: CHATS,
      update(data) {
        return data.account.chats;
      },
      subscribeToMore: [
        {
          document: CHAT_ADDED,
          updateQuery: (previousResult, { subscriptionData }) => {
            // Here, return the new result from the previous with the new data
            const newChat = subscriptionData.data.chatAdded.chat;
            return {
              account: {
                // eslint-disable-next-line no-underscore-dangle
                __typename: 'Account',
                chats: [
                  ...previousResult.account.chats,
                  newChat,
                ],
              },
            };
          },
        },
        {
          document: CHAT_MODIFIED,
          updateQuery: (previousResult, { subscriptionData }) => {
            // Here, return the new result from the previous with the new data
            const modifiedChat = subscriptionData.data.chatModified.chat;
            const modifiedIndex =
              previousResult.account.chats.findIndex((chat) => chat.id === modifiedChat.id);
            const updatedAccount = {
              chats: [...previousResult.account.chats],
            };
            updatedAccount.chats.splice(modifiedIndex, 1, modifiedChat);
            return {
              account: {
                // eslint-disable-next-line no-underscore-dangle
                __typename: 'Account',
                chats: updatedAccount.chats,
              },
            };
          },
        },
        {
          document: CHAT_LEFT,
          updateQuery: (previousResult, { subscriptionData }) => {
            // eslint-disable-next-line no-console
            // Here, return the new result from the previous with the new data
            const leftChat = subscriptionData.data.chatLeft.chat;
            const leftIndex =
              previousResult.account.chats.findIndex((chat) => chat.id === leftChat.id);
            const updatedAccount = {
              chats: [...previousResult.account.chats],
            };
            updatedAccount.chats.splice(leftIndex, 1, leftChat);
            return {
              account: {
                // eslint-disable-next-line no-underscore-dangle
                __typename: previousResult.account.__typename,
                chats: updatedAccount.chats,
              },
            };
          },
        },
      ],
    },
  },
};
</script>
