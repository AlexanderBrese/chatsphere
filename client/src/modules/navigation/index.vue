<!--
  Can be swiped to the right to display a sidebar with one's own
  profile avatar ,name, email.
  Can navigate to Contacts/Settings. Can invite friends and logout.
-->
<template>
  <v-layout
    column
    fill-height>
    <v-flex>
      <NavigationAvatar/>
    </v-flex>
    <v-flex fill-height>
      <NavigationData
        :route-list="routeList"/>
    </v-flex>
    <v-flex>
      <v-btn
        block
        depressed
        large
        color="primary"
        class="white--text no-border ma-0"
        @click.native="logout()"
      >
        Logout
      </v-btn>
    </v-flex>
  </v-layout>
</template>

<style scoped>

</style>
<script>
import { APP_ROUTES } from '@/router/routes';
import localforage from 'localforage';
import NavigationAvatar from './_components/NavigationAvatar';
import NavigationData from './_components/NavigationData';
import LOGOUT from './_queries/Logout.graphql';

export default {
  name: 'Navigation',

  components: {
    NavigationAvatar,
    NavigationData,
  },

  data() {
    return {
      routeList: APP_ROUTES,
    };
  },

  methods: {
    logout() {
      this.$apollo.query({
        query: LOGOUT,
      }).then(() => {
        localforage.setItem('token', null).then(() => {
          this.$router.push({ path: '/' }, () => {
            this.$apolloProvider.defaultClient.cache.reset();
          });
        });
      });
    },
  },
};
</script>
