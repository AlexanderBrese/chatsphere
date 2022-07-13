<!--
  A Chatlist entry. A Chat can be opened.
-->
<template>
  <v-list-tile
    :class="activeClass"
    avatar
    @click="''">
    <!-- Avatar/Icon of a Chat entry-->
    <v-list-tile-avatar
      class="mt-0">
      <img
        v-if="!selected"
        :src="chat.stamp.iconUrl !== '' ? chat.stamp.iconUrl : require('@/assets/avatar.png')">
      <v-icon
        v-else
        class="primary white--text">done</v-icon>
    </v-list-tile-avatar>

    <v-list-tile-content>
      <v-list-tile-sub-title class="caption">
        {{ previewStatus | uppercase }}
      </v-list-tile-sub-title>
      <!-- Title of a chat.-->
      <v-list-tile-title class="text--primary">{{ chat.stamp.displayName }}</v-list-tile-title>

      <!-- Display last Message-->
      <v-list-tile-sub-title class="text--primary body-1">
        {{ previewAuthorName }} {{ previewText }}
      </v-list-tile-sub-title>

    </v-list-tile-content>

    <!-- Time of last Message of Chat-->
    <v-list-tile-action>
      <v-list-tile-sub-title>
        {{ previewDate | moment("from") }}
      </v-list-tile-sub-title>
    </v-list-tile-action>

  </v-list-tile>

</template>

<script>
/**
 * @vue-prop {Object} chat - a single chat entity. Contains:
 * @vue-prop {Object} account - account of loggedin user. Contains:
 */
export default {
  name: 'ChatListElement',

  props: {
    chat: {
      type: Object,
      default() {
        return {};
      },
    },
    selected: {
      type: Boolean,
      default() {
        return false;
      },
    },
  },

  computed: {
    activeClass() {
      return {
        active: this.selected,
      };
    },
    previewText() {
      if (!this.chat.stamp.lastMessage) {
        return '';
      }
      // eslint-disable-next-line no-underscore-dangle
      if (this.chat.stamp.lastMessage.__typename === 'AudioMessage') {
        return 'Audio';

        // eslint-disable-next-line no-underscore-dangle
      } else if (this.chat.stamp.lastMessage.__typename === 'DocumentMessage') {
        return 'Document';

        // eslint-disable-next-line no-underscore-dangle
      } else if (this.chat.stamp.lastMessage.__typename === 'PictureMessage') {
        return 'Picture';

        // eslint-disable-next-line no-underscore-dangle
      } else if (this.chat.stamp.lastMessage.__typename === 'VideoMessage') {
        return 'Video';
      }

      return `- ${this.chat.stamp.lastMessage.text}`;
    },
    previewAuthorName() {
      // eslint-disable-next-line no-console
      if (!this.chat.stamp.lastMessage) {
        return '';
      }
      return this.chat.stamp.lastMessage.author.displayName;
    },
    previewStatus() {
      if (!this.chat.stamp.lastMessage) {
        return '';
      }
      return this.chat.stamp.lastMessage.author.status;
    },
    previewDate() {
      if (!this.chat.stamp.lastMessage) {
        return '';
      }
      return this.chat.stamp.lastMessage.updatedAt;
    },
  },
};
</script>

<style scoped>
  .avatar {
    min-width: 64px;
  }
  .active {
    background: rgba(0,0,0,0.08) !important;
  }
</style>

