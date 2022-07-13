<!--
  Presents a round icon of the profile avatar,
  the usernname and email.
-->
<template>
  <!--  Background image/ color -->
  <v-layout
    column
    class="gradient-bg pa-4">
    <v-flex>
      <v-avatar
        size = "80"
        @click="$router.push({ name: 'ProfileSettings' })">
        <img
          :src="account.user.avatarUrl !== ''
            ? account.user.avatarUrl
          : require('@/assets/avatar.png')">
      </v-avatar>
    </v-flex>
    <v-flex class="mt-2">
      <span class="white--text body-2">{{ account.user.displayName }}</span>
    </v-flex>
    <v-flex>
      <span class="white--text caption">{{ account.email }}</span>
    </v-flex>
  </v-layout>
</template>

<script>
import ACCOUNT from '../../../shared/queries/Account.graphql';

/**
 * @vue-data {Object} account - Holds account data.
 */
export default {
  name: 'NavigationAvatar',

  data() {
    return {
      account: {
        user: {
          avatarUrl: undefined,
          displayName: undefined,
        },
        email: undefined,
      },
    };
  },

  apollo: {
    account: {
      query: ACCOUNT,
    },
  },
};
</script>

<style scoped>
</style>

