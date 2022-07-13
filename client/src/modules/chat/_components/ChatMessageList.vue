<!--
  Container that gathers all messages and displays them
-->
<template>
  <v-list class="chat-list">
    <!-- Each list entry a ChatLine -->
    <v-list-tile
      v-for="(messageSelection) in messageSelections"
      :class="{'active': messageSelection.isSelected,
               'right': !isAuthor(messageSelection)}"
      :key="messageSelection.message.id"
      class="pb-2 message"
    >
      <div>
        <span>
          {{ messageSelection.message.author.username }}
        </span>
        <span class="caption grey--text text--darken--4">
          {{ messageSelection.message.updatedAt | moment("from") }}
        </span>
      </div>
      <ChatLine
        v-longpress="{method: select, arg: {messageId: messageSelection.message.id}}"
        :align-right="!isAuthor(messageSelection)"
        :value="messageSelection.message.text"
        :is-read="true"
        :time="new Date()"
        :avatar-url="messageSelection.message.author.avatarUrl"
      />
    </v-list-tile>
  </v-list>
</template>

<style scoped>
  .active {
    background: rgba(0,0,0,0.08) !important;
  }
  .chat-list{
    display: flex;
    justify-content: flex-end;
    flex-direction: column;
  }
  .message >>> .v-list__tile {
    align-items: flex-start !important;
    flex-direction: column;
    height: inherit !important;
  }

  .right >>> .v-list__tile {
    justify-content: flex-end;
    align-items: flex-end !important;
  }
</style>

<script>
import ChatLine from './ChatLine';
import LONGPRESS from '../../../shared/directives/LongpressDirective';

/**
   * @vue-data {Array} messages - Contains title and subtitle of each menu item
   */
export default {
  components: {
    ChatLine,
  },
  directives: {
    LONGPRESS,
  },
  props: {
    messages: {
      type: Array,
      default() {
        return [];
      },
    },
    username: {
      type: String,
      required: true,
    },
  },

  data() {
    return {
      messageSelections: this.getMessageSelections(this.messages),
    };
  },
  watch: {
    messages(newMessages) {
      this.messageSelections = this.getMessageSelections(newMessages);
    },
  },

  methods: {
    isAuthor(messageSelection) {
      return messageSelection.message.author.username === this.username;
    },
    changeText(index, text) {
      const messageSelection = Object.assign({}, this.messageSelections[index]);
      messageSelection.text = text;
      this.messageSelections.splice(index, 1, messageSelection);
    },
    getMessageSelections(messages) {
      const messageSelections = [];
      messages.forEach((message) => {
        messageSelections.push({
          message,
          isSelected: false,
        });
      });
      return messageSelections;
    },
    select(arg, isPressed) {
      const index =
        this.messageSelections.findIndex(selection => selection.message.id === arg.messageId);
      if (this.messageSelections[index].message.author.username !== this.username) {
        return;
      }
      if (isPressed || this.selectedMessages().length > 0) {
        this.toggleSelected(index);
        this.$emit('select', this.selectedMessages());
      } else {
        this.showOptions(index);
      }
    },
    showOptions(index) {
      this.$emit('options', index);
    },
    toggleSelected(index) {
      const messageSelection = Object.assign({}, this.messageSelections[index]);
      messageSelection.isSelected = !messageSelection.isSelected;
      this.messageSelections.splice(index, 1, messageSelection);
    },
    selectedMessages() {
      const selectedMessages = [];
      this.messageSelections.forEach(messageSelection => {
        if (messageSelection.isSelected) {
          selectedMessages.push(messageSelection.message.id);
        }
      });
      return selectedMessages;
    },
  },
};
</script>
