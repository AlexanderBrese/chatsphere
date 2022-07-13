<!--
  Presents a round icon of the profile avatar,
  the name and some navigation buttons.
-->
<template>
  <!--  Background image/ color -->
  <v-layout
    class="gradient-bg"
    column>
    <!-- Profile avatar -->
    <v-avatar
      class="center my-4"
      size = "150"
      @click="pickFile">
      <img
        :src="account.user.avatarUrl !== ''
          ? account.user.avatarUrl
        : require('@/assets/avatar.png')">
    </v-avatar>

    <!-- Upload button -->
    <input
      id="file"
      ref="myFiles"
      style="display: none"
      type="file"
      @change="upload(this)">
    <TextfieldDisplayName
      :account = "account"
      @submit = "$emit('submitDisplayName', $event)"/>
  </v-layout>
</template>

<script>
/* eslint-disable no-console */

import TextfieldDisplayName from './_ProfileTextfields/TextfieldDisplayName';
import UPDATE_AVATAR from '../_queries/UpdateAvatar.graphql';
import ACCOUNT from '../../../shared/queries/Account.graphql';

/**
 * @vue-prop {Object} account - Holds account data (avatar and displayName).
 */
export default {
  name: 'ProfileAvatar',

  components: {
    TextfieldDisplayName,
  },

  props: {
    account: {
      type: Object,
      default() {
        return {};
      },
    },
  },

  methods: {
    /**
    * Goes back to the last visted route.
    */
    back() {
      this.$router.go(-1);
    },

    pickFile() {
      this.$refs.myFiles.click();
    },

    /**
     * Extracts and parses inputs given via upload button,
     * sends them to the server.
     */
    upload() {
      const file = this.$refs.myFiles.files[0];
      const reader = new FileReader();
      reader.addEventListener('load', () => {
        // sample result: "data:image/png;base64,iVBORw0..."
        let encodedImage = reader.result;

        // cut meta information before comma
        encodedImage = encodedImage.slice(encodedImage.indexOf(',') + 1);

        // send
        try {
          this.$apollo.mutate({
            mutation: UPDATE_AVATAR,
            variables: {
              input: {
                avatar: encodedImage,
                fileName: file.name,
              },
            },
            refetchQueries: [{
              query: ACCOUNT,
            }],

          // Let index.vue know that a new url has to be used
          });
        } catch (e) {
          console.error(e);
        }
      }, false);
      reader.readAsDataURL(file);
    },
  },
};
</script>

<style scoped>
  .avatar {
    margin-top: 75px;
  }

  div#textfield {
    width: 50%;
    margin-top: 0px;
    margin: auto;
  }

  .center{
    margin: 0 auto;
  }

  #title {
    color: white;
  }

  @media only screen and (max-width: 600px) {
  div#textfield {
    width: 90%;
  }

}
</style>

