<!--
  A textbox to type a message and send it.
-->
<template>
  <v-text-field
    ref="textField"
    v-model="text"
    box
    hide-details
    label="Message"
    type="text"
    class="send"
    @keyup.enter.native="send"
  >
    <v-icon
      slot="append-outer"
      @click.native="send">send</v-icon>
  </v-text-field>
</template>

<style scoped>
  .send >>> .v-input__append-outer {
    margin-right: 16px !important;
    margin-left: 16px;
  }
</style>

<script>
import CREATE_PLAIN_MESSAGE from '../_queries/CreatePlainMessage.graphql';
import UPDATE_PLAIN_MESSAGE from '../_queries/UpdatePlainMessage.graphql';
import CHAT from '../_queries/Chat.graphql';
import CHATS from '../../chatlist/_queries/Chats.graphql';
/**
 * @vue-data {String} message - inputted message to be sent
 */
export default {
  props: {
    user: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      message: undefined,
      text: '',
      selectedIndex: -1,
    };
  },

  methods: {
    select(index, message) {
      this.text = message.text;
      this.message = message;
      this.$refs.textField.focus();
      this.selectedIndex = index;
    },
    send() {
      if (this.text.length === 0) return;
      const [text] = [this.text];
      this.text = '';

      if (this.selectedIndex > -1) {
        this.updateMessage(text, this.message.id);
      } else {
        this.createMessage(text);
      }
    },
    createMessage(text) {
      this.$apollo.mutate({
        mutation: CREATE_PLAIN_MESSAGE,
        variables: {
          createMessageInput: {
            text,
            chatId: this.$route.params.id,
          },
        },
        refetchQueries: [{
          query: CHATS,
        }],
        update: (store, { data: { createPlainMessage } }) => {
          // Add to all contacts list
          const data = store.readQuery({
            query: CHAT,
            variables: {
              id: this.$route.params.id,
            },
          });
          data.chat.log.push(createPlainMessage);
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
          createPlainMessage: {
            __typename: 'PlainMessage',
            id: null,
            author: this.user,
            text,
            updatedAt: new Date(),
          },
        },
      });
    },
    updateMessage(text, messageId) {
      this.$apollo.mutate({
        mutation: UPDATE_PLAIN_MESSAGE,
        variables: {
          updateMessageInput: {
            messageId,
            text,
          },
        },
        refetchQueries: [{
          query: CHATS,
        }],
        update: (store) => {
          // Add to all contacts list
          const data = store.readQuery({
            query: CHAT,
            variables: {
              id: this.$route.params.id,
            },
          });
          const updatedMessage = Object.assign({}, data.chat.log[this.selectedIndex]);
          updatedMessage.text = text;
          data.chat.log.splice(this.selectedIndex, 1, updatedMessage);
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
          updatePlainMessage: true,
        },
      }).then(() => { this.selectedIndex = -1; });
    },
  },
};
</script>
