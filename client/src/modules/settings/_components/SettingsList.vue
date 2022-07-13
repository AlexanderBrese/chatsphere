<template>
  <v-list
    two-line
    subheader
  >
    <!-- Profile Section -->
    <v-subheader>{{ $t('settings.profileSettings') }}</v-subheader>
    <v-divider/>

    <v-list-tile @click="openProfileSettings()">
      <v-list-tile-avatar>
        <img
          :src="account.user.avatarUrl !== ''
            ? account.user.avatarUrl
          : require('@/assets/avatar.png')">

      </v-list-tile-avatar>

      <v-list-tile-content>
        <v-list-tile-title>{{ account.user.displayName }}</v-list-tile-title>
        <v-list-tile-sub-title>{{ account.user.status }}</v-list-tile-sub-title>
      </v-list-tile-content>
    </v-list-tile>

    <!-- General Section -->
    <v-subheader>{{ $t('settings.generalSettings') }}</v-subheader>
    <v-divider/>

    <!-- Blocked Contacts -->
    <v-list-tile
      disabled
      avatar
      @click="openBlockedContacts()"
    >
      <v-list-tile-avatar>
        <v-icon color="red">block</v-icon>
      </v-list-tile-avatar>

      <v-list-tile-content>
        <v-list-tile-title>{{ $t('settings.blockedContacts') }}</v-list-tile-title>
        <v-list-tile-sub-title>{{ $t('settings.blockedContactsSub') }}</v-list-tile-sub-title>
      </v-list-tile-content>
    </v-list-tile>

    <!-- Privacy Settings -->
    <v-list-tile
      disabled
      avatar
      @click="openprivacySettings()"
    >
      <v-list-tile-avatar>
        <v-icon color="primary">visibility</v-icon>
      </v-list-tile-avatar>

      <v-list-tile-content>
        <v-list-tile-title>{{ $t('settings.privacy') }}</v-list-tile-title>
        <v-list-tile-sub-title>{{ $t('settings.privacySub') }}</v-list-tile-sub-title>
      </v-list-tile-content>
    </v-list-tile>

    <!-- Notification Section -->
    <v-subheader>{{ $t('settings.notificationSettings') }}</v-subheader>
    <v-divider/>

    <!-- Push Notification -->
    <v-list-tile avatar>
      <v-list-tile-avatar>
        <v-icon
          v-if="pushNotification === 'NOTIFY'"
          color="success">
          notifications
        </v-icon>
        <v-icon
          v-else
          color="red">
          notifications_off
        </v-icon>
      </v-list-tile-avatar>

      <v-list-tile-content>
        <v-list-tile-title>{{ $t('settings.notification') }}</v-list-tile-title>
        <v-list-tile-sub-title>{{ $t('settings.notificationSub') }}</v-list-tile-sub-title>
      </v-list-tile-content>

      <v-list-tile-action>
        <v-switch
          v-model="pushNotification"
          color="primary"
          true-value="NOTIFY"
          false-value="MUTE"
        />
      </v-list-tile-action>
    </v-list-tile>

  </v-list>
</template>

<script>
import UPDATE_NOTIFICATION_SETTINGS from '../_queries/UpdateNotificationSettings.graphql';
import ACCOUNT from '../../../shared/queries/Account.graphql';

/**
 * @vue-data {String} pushNotification - push notification status
 * @vue-data {Object} account - account object of user
 */
export default {
  data() {
    return {
      pushNotification: 'MUTE',
      account: {
        notify: {
          push: 'MUTE',
        },
        user: {
          avatarUrl: '',
          displayName: 'displayName',
          status: 'status',
        },
      },
    };
  },

  watch: {
    /**
     * Watches for changes of notification settings.
     */
    'account.notify.push': function accountPushWatcher() {
      this.pushNotification = this.account.notify.push;
    },

    /**
     * If user changes notification status, mutate.
     */
    pushNotification() {
      if (this.pushNotification !== this.account.notify.push) {
        this.$apollo.mutate({
          mutation: UPDATE_NOTIFICATION_SETTINGS,
          variables: {
            input: {
              notify: { push: this.pushNotification },
            },
          },
        });
        this.$apollo.queries.account.refetch();
      }
    },
  },

  methods: {
    /**
     * Open profile settings.
     */
    openProfileSettings() {
      this.$router.push({ name: 'ProfileSettings' });
    },

    /**
     * Open blocked contacts list
     */
    openBlockedContacts() {
    },

    /**
     * Open privacy settings
     */
    openPrivacySettings() {
    },
  },

  apollo: {
    account: {
      query: ACCOUNT,
    },
  },
};
</script>

