<!--
  Presents a round icon of the profile avatar
  and the nickname.
-->

<template>
  <!--  Background image/ color -->
  <v-layout
    class="gradient-bg"
    column
  >
    <v-avatar
      class="center my-4"
      size = "150">
      <img
        :src="user.user.avatarUrl !== ''
          ? user.user.avatarUrl
        : require('@/assets/avatar.png')">
    </v-avatar>

    <TextfieldNickName
      v-if="contact"
      :user = "user"
      @updateNickname = "$emit('updateNickname', $event)"
    />

    <v-card-title
      v-else
      class="center nickname white--text"
    >
      {{ user.user.displayName }}
    </v-card-title>
  </v-layout>
</template>

<script>
import TextfieldNickName from './TextfieldNickName';

/**
 * @vue-prop {Boolean} contact - Whether User is a contact or not.
 * @vue-prop {Object} user - User or Contact Object.
 */
export default {
  name: 'ProfileAvatar',

  components: {
    TextfieldNickName,
  },

  props: {
    contact: {
      type: Boolean,
      default() {
        return false;
      },
    },
    user: {
      type: Object,
      default() {
        return {};
      },
    },
  },
};
</script>

<style scoped>
  .avatar {
    margin-top: 75px;
  }

  .nickname {
    font-size: 25px;
  }

  div#textfield {
    width: 50%;
    margin-top: 0px;
    margin: auto;
  }

  .center {
    margin-left: auto;
    margin-right: auto;
  }

  .menu {
    right: 10px;
    position: absolute;
  }

  #title {
    color: white;
  }

  @media only screen and (max-width: 600px) {
  div#textfield {
    width: 90%;
  }

  .textRed {
    color: red;
  }

  .textGreen {
    color: green;
  }
}
</style>
