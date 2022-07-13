<template>
  <v-app>
    <div
      v-if="$vuetify.breakpoint.name !== 'xs'"
      class="chatsphere">
      <div class="upper-bg primary"/>
      <v-container
        class="chatsphere-container">
        <v-card class="chatsphere-card">
          <router-view @authenticated="listenToPush"/>
        </v-card>
      </v-container>
    </div>

    <!-- Either Login, Registration or MainView -->
    <router-view
      v-else
      @authenticated="listenToPush"/>
  </v-app>
</template>
<script>
import runSW from '@/service-worker';
import localforage from 'localforage';
import REGISTER_NOTIFICATION from './_queries/NotificationReceived.graphql';

/**
 * @vue-data {Boolean} drawer - whether toolbar should be shown or not
 * @vue-data {String} title - Toolbar title
 */
export default {
  name: 'App',

  // Check if token exists
  beforeMount() {
    localforage.getItem('token').then(token => {
      if (token !== null) {
        this.listenToPush(token);
      }
    });
  },

  methods: {
    // Listen to notifications if authorized
    listenToPush(token) {
      if (token !== null) {
        runSW();
        const observer = this.$apollo.subscribe({
          query: REGISTER_NOTIFICATION,
        });

        observer.subscribe({
          next: (received) => {
            // Only send notification if not chatting
            const pattern = /chat\/\d+/gi;
            if (!pattern.test(this.$router.currentRoute.path)) {
              /* eslint-disable no-new */
              const notification = new Notification('New Message', {
                body: received.data.notificationReceived.text,
                icon: '/static/img/icons/android-chrome-192x192.png',
                tag: 'chatsphere-notification',
              });

              notification.onclick = event => {
                event.preventDefault(); // prevent focusing the Notification's tab
                window.open(`${window.origin}/chat/${received.data.notificationReceived.chatId}`);
              };
            }
          },
        });
      }
    },
  },
};
</script>
<style>
  .chatsphere {
    height: 100%;
  }
  .chatsphere-card {
    height: 100%;
  }
  .upper-bg {
    height: 20%;
    width: 100%;
    position: absolute;
  }
  .chatsphere-container {
    height: 100%;
    margin: auto !important;
    max-width: 980px !important;
  }
  .v-toolbar__content .v-btn {
    z-index: 2;
  }
  a.primary--text {
    color: #595C84 !important;
  }
  .no-border{
    border-radius: 0 !important;
  }
  .gradient-bg{
    background-image: linear-gradient( to right, #0F1626, #2A3A5E);
  }
</style>
