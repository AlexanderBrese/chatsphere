<!--
  ChatList Container which contains all chatlist entries. A Chat can be added.
-->
<template>

  <v-list
    class="py-0 my-2"
    three-line>
    <!-- An chatlist entry -->
    <template v-for="(chatSelection, index) in chatSelections">
      <ChatListElement
        v-longpress="{method: select, arg: {index, chatId: chatSelection.chat.id}}"
        :key="chatSelection.chat.id"
        :selected="chatSelection.isSelected"
        :chat="chatSelection.chat"/>
      <!-- Divider to divide chat entrys-->
      <v-divider
        v-if="index != chatSelection.length - 1"
        :key="-index-1"/>
    </template>
  </v-list>

</template>

<script>
import ChatListElement from './ChatListElement';
import LONGPRESS from '../../../shared/directives/LongpressDirective';


/**
 * @vue-prop {Array} chats - all chat enties. Contains:
 *    - {Id} id - chat id
 *    - {Object} log - Chat Log (Messages of Chat)
 *        - {String} __typename - type of message
 *        - {Object} author - author of message
 *            - {String} displayName - display name of author
 *        - {String} date - date of message
 *        - {String} text - message text
 *    - {Object} stamp - Chat Stamp
 *        - {String} icon - Chat icon
 *        - {String} name - Chat name
 */
export default {
  name: 'ChatList',
  directives: {
    LONGPRESS,
  },
  components: {
    ChatListElement,
  },
  props: {
    chats: {
      type: Array,
      default() {
        return [];
      },
    },
  },
  data() {
    return {
      chatSelections: this.getChatSelections(this.chats),
    };
  },
  watch: {
    chats(newChats) {
      this.chatSelections = this.getChatSelections(newChats);
    },
  },
  methods: {
    getChatSelections(chats) {
      const chatSelections = [];
      chats.forEach((chat) => {
        chatSelections.push({
          chat,
          isSelected: false,
        });
      });
      return chatSelections;
    },
    select(arg, isPressed) {
      if (isPressed || this.selectedChats().length > 0) {
        this.toggleSelected(arg.index);
        this.$emit('select', this.selectedChats());
      } else {
        this.next(arg.chatId);
      }
    },
    toggleSelected(index) {
      const chatSelection = Object.assign({}, this.chatSelections[index]);
      chatSelection.isSelected = !chatSelection.isSelected;
      this.chatSelections.splice(index, 1, chatSelection);
    },
    /**
     * Route to selected chat page.
     */
    next(id) {
      this.$router.push({ path: `/chat/${id}` });
    },
    selectedChats() {
      const selectedChats = [];
      this.chatSelections.forEach(chatSelection => {
        if (chatSelection.isSelected) {
          selectedChats.push(chatSelection.chat.id);
        }
      });
      return selectedChats;
    },
  },
};
</script>
