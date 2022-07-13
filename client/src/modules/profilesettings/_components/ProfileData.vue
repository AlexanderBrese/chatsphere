<!--
  List of all profile data that the user can change
-->
<template>

  <!-- ProfileList -->
  <v-container class="container">

    <!-- ProfileListEntry: Status -->
    <TextfieldStatus
      :account = "account"
      @submit = "$emit('submitStatus', $event)"
    />

    <!-- ProfileListEntry: Phone Number -->
    <TextfieldPhone
      :account = "account"
      @submit = "$emit('submitPhone', $event)"
    />

    <!-- ProfileListEntry: E-Mail -->
    <TextfieldEmail
      :account = "account"
      @submit = "$emit('submitEmail', $event)"
    />

    <!-- ProfileListEntry: Password -->
    <TextfieldPassword
      @submit = "$emit('submitPassword', $event)"
    />

  </v-container>
</template>

<script>
import TextfieldStatus from './_ProfileTextfields/TextfieldStatus';
import TextfieldPhone from './_ProfileTextfields/TextfieldPhone';
import TextfieldEmail from './_ProfileTextfields/TextfieldEmail';
import TextfieldPassword from './_ProfileTextfields/TextfieldPassword';

/**
 * @vue-prop {Object} account - Holds account data.
 */
export default {
  name: 'ProfileData',

  components: {
    TextfieldStatus,
    TextfieldPhone,
    TextfieldEmail,
    TextfieldPassword,
  },

  props: {
    account: {
      type: Object,
      default() {
        return {};
      },
    },
  },
};
</script>

<style>
  .container {
    margin: 0;
    max-width: 100%;
  }
</style>
