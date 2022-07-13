<!--
  Present buttons to send multimedia type messages
-->
<template>
  <v-layout row>

    <v-menu offset-y>

      <!-- Should be an "arrow down" button -->
      <v-btn
        slot="activator"
        color="primary"
        dark
      >
        Dropdown
      </v-btn>

      <!-- Three buttons to upload picture, audio, document -->
      <v-btn
        fab
        dark
        color="teal"
      >
        <v-icon dark>list</v-icon>
      </v-btn>

      <v-btn
        fab
        dark
        large
        color="cyan"
      >
        <v-icon dark>edit</v-icon>
      </v-btn>

      <v-btn
        fab
        dark
        large
        color="purple"
      >
        <v-icon dark>android</v-icon>
      </v-btn>

    </v-menu>

  </v-layout>
</template>

<style scoped>

</style>

<script>
/**
 * @vue-data {Array} entries - Contains all entries fetched from the server.
 */
export default {
  props: { },

  data() {
    return {

    };
  },

  computed: {

  },

  methods: {

    /**
     * Upload a picture
     */
    uploadPicture() {

    },

    /**
     * Upload an audio file
     */
    uploadAudio() {

    },

    /**
     * Upload a document
     */
    uploadDocument() {

    },
  },
};
</script>
