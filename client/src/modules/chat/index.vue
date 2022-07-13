<!--
  Where actual conversations occur. Recent messages
  are displayed and can be sent.
-->
<template>
  <div>
    <!-- Toolbar -->
    <v-toolbar
      :fixed="$vuetify.breakpoint.xsOnly"
      :absolute="$vuetify.breakpoint.smAndUp"
      class="white--text"
      color="primary"
      app>
      <v-btn
        class="white--text"
        icon
        @click.native="back()">
        <v-icon>arrow_back</v-icon>
      </v-btn>
      <v-toolbar-title>{{ title }}</v-toolbar-title>
      <v-spacer/>
      <v-menu
        v-if="selectedMessages.length > 0"
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
            <v-list-tile-title>Delete message</v-list-tile-title>
          </v-list-tile>
        </v-list>
      </v-menu>
    </v-toolbar>
    <v-content app>
      <v-bottom-sheet
        :value="sheet"
        hide-overlay
        @input="hideOptions($event)">
        <v-list>
          <v-subheader>Message Options</v-subheader>
          <v-list-tile
            @click="edit()"
          >
            <v-list-tile-action>
              <v-icon color="primary">edit</v-icon>
            </v-list-tile-action>
            <v-list-tile-title>Edit</v-list-tile-title>
          </v-list-tile>
        </v-list>
      </v-bottom-sheet>
      <ChatMessageList
        ref="messageList"
        :username="user.username"
        :messages="chat.log"
        @options="showOptions($event)"
        @select="onSelect"/>
    </v-content>
    <v-footer
      height="auto"
      app
      absolute>
      <ChatInputField
        ref="inputField"
        :user="user"/>
    </v-footer>
    <div id="last"/>
  </div>
</template>

<style scoped>

</style>

<script>
/* eslint-disable no-console */

import Navigation from '@/modules/navigation/';
import ChatMessageList from './_components/ChatMessageList';
import ChatInputField from './_components/ChatInputField';
import CHAT from './_queries/Chat.graphql';
import MESSAGE_ADDED from './_queries/MessageAdded.graphql';
import REMOVE_MESSAGES from './_queries/RemoveMessages.graphql';
import MESSAGE_UPDATED from './_queries/MessageUpdated.graphql';
import MESSAGE_REMOVED from './_queries/MessageRemoved.graphql';
import ACCOUNT from '../../shared/queries/Account.graphql';

export default {
  components: {
    ChatMessageList,
    ChatInputField,
    Navigation,
  },
  data() {
    return {
      sheet: false,
      chat: {
        stamp: '',
      },
      user: {
        username: '',
        status: '',
        avatarUrl: '',
        displayName: '',
      },
      options: {
        duration: 300,
        offset: 0,
        easing: 'easeInOutCubic',
      },
      selectedMessages: [],
      selectedIndex: undefined,
    };
  },
  computed: {
    title() {
      if (this.chat.stamp) {
        return this.chat.stamp.displayName.toUpperCase();
      }
      return '';
    },
  },
  methods: {
    hideOptions() {
      this.sheet = false;
    },
    showOptions(index) {
      setTimeout(() => { this.sheet = true; }, 1);
      this.selectedIndex = index;
    },
    edit() {
      const selectedMessage = this.chat.log[this.selectedIndex];
      this.$refs.inputField.select(this.selectedIndex, selectedMessage);
      this.hideOptions();
    },
    back() {
      this.$router.back();
    },
    onSelect(selectedMessages) {
      this.selectedMessages = selectedMessages;
    },
    remove() {
      // eslint-disable-next-line no-console
      this.$apollo.mutate({
        mutation: REMOVE_MESSAGES,
        variables: {
          removeMessagesInput: {
            messageIds: this.selectedMessages,
          },
        },
        update: (store) => {
          // Add to all contacts list
          const data = store.readQuery({
            query: CHAT,
            variables: {
              id: this.$route.params.id,
            },
          });
          this.selectedMessages.forEach(messageId => {
            const index = data.chat.log.findIndex(message => message.id === messageId);
            if (index === -1) {
              return;
            }
            data.chat.log.splice(index, 1);
          });
          store.writeQuery({
            query: CHAT,
            variables: {
              id: this.$route.params.id,
            },
            data,
          });
        },
        optimisticResponse: {
          __typename: 'Mutation',
          removeMessages: true,
        },
      }).then(() => {
        this.selectedMessages = [];
        // eslint-disable-next-line no-console
      }).catch((e) => console.error(e));
    },
  },

  apollo: {
    user: {
      query: ACCOUNT,
      update(data) {
        return data.account.user;
      },
    },
    chat: {
      query: CHAT,
      fetchPolicy: 'cache-and-network',
      variables() {
        return {
          id: this.$route.params.id,
        };
      },
      manual: true,
      result({ data, loading }) {
        if (loading) {
          return;
        }
        let shallScroll = false;
        if (!this.chat.log || this.chat.log.length < data.chat.log.length) {
          shallScroll = true;
        }
        let shallUpdate = false;
        if (!this.chat.log) {
          shallUpdate = true;
        } else {
          data.chat.log.forEach(newMessage => {
            const index = this.chat.log.findIndex(message => message.id === newMessage.id);
            if (index === -1) {
              shallUpdate = true;
            } else if (this.chat.log[index].text !== newMessage.text) {
              shallUpdate = true;
            }
          });

          if (this.chat.log.length > data.chat.log.length) {
            shallUpdate = true;
          }
        }
        if (shallUpdate) {
          this.chat = data.chat;
        }

        if (shallScroll) {
          this.$nextTick(() => {
            this.$vuetify.goTo('#last', this.options);
          });
        }
      },
      subscribeToMore: [
        {
          document: MESSAGE_ADDED,
          variables() {
            return {
              subscriptionInput: {
                channelName: 'chat',
                channelId: Number(this.$route.params.id),
              },
            };
          },
          updateQuery: (previousResult, { subscriptionData }) => {
            const newMessage = subscriptionData.data.messageAdded.message;
            return {
              chat: {
                // eslint-disable-next-line no-underscore-dangle
                __typename: previousResult.chat.__typename,
                id: previousResult.chat.id,
                participants: previousResult.chat.participants,
                log: [
                  ...previousResult.chat.log,
                  newMessage,
                ],
                stamp: previousResult.chat.stamp,
              },
            };
          },
        },
        {
          document: MESSAGE_UPDATED,
          variables() {
            return {
              subscriptionInput: {
                channelName: 'chat',
                channelId: Number(this.$route.params.id),
              },
            };
          },
          updateQuery: (previousResult, { subscriptionData }) => {
            const updatedMessage = subscriptionData.data.messageUpdated.message;
            const index =
              previousResult.chat.log.findIndex(message => updatedMessage.id === message.id);
            const updatedChat = {
              log: [...previousResult.chat.log],
            };
            updatedChat.log.splice(index, 1, updatedMessage);
            return {
              chat: {
                // eslint-disable-next-line no-underscore-dangle
                __typename: previousResult.chat.__typename,
                id: previousResult.chat.id,
                participants: previousResult.chat.participants,
                log: updatedChat.log,
                stamp: previousResult.chat.stamp,
              },
            };
          },
        },
        {
          document: MESSAGE_REMOVED,
          variables() {
            return {
              subscriptionInput: {
                channelName: 'chat',
                channelId: Number(this.$route.params.id),
              },
            };
          },
          updateQuery: (previousResult, { subscriptionData }) => {
            const removeId = subscriptionData.data.messageRemoved.messageId;
            const log = previousResult.chat.log.filter(message => message.id !== removeId);
            return {
              chat: {
                // eslint-disable-next-line no-underscore-dangle
                __typename: previousResult.chat.__typename,
                id: previousResult.chat.id,
                participants: previousResult.chat.participants,
                log,
                stamp: previousResult.chat.stamp,
              },
            };
          },
        },
      ],
    },
  },
};
</script>
