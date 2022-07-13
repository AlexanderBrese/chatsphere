<!--
  Presents an overview of own profile.
-->
<template>

  <div>
    <!-- Toolbar -->
    <v-toolbar
      :fixed="$vuetify.breakpoint.xsOnly"
      :absolute="$vuetify.breakpoint.smAndUp"
      dark
      color="primary"
      app>
      <v-btn
        icon
        @click.native="$router.go(-1)">
        <v-icon>arrow_back</v-icon>
      </v-btn>
      <v-toolbar-title>{{ account.user.displayName | uppercase }}</v-toolbar-title>
    </v-toolbar>

    <v-content app>
      <ProfileAvatar
        :account = "account"
        @submitDisplayName = "submitDisplayName($event)"/>
      <v-card>
        <ProfileData
          :account = "account"
          @submitEmail = "submitEmail($event)"
          @submitStatus = "submitStatus($event)"
          @submitPassword = "submitPassword($event)"
          @submitPhone = "submitPhone($event)"/>
      </v-card>
    </v-content>
  </div>
</template>

<script>
import ACCOUNT from '../../shared/queries/Account.graphql';
import UPDATE_EMAIL from './_queries/UpdateEmail.graphql';
import UPDATE_PASSWORD from './_queries/UpdatePassword.graphql';
import UPDATE_PHONE from './_queries/UpdatePhone.graphql';
import UPDATE_DISPLAY_NAME from './_queries/UpdateDisplayName.graphql';
import UPDATE_STATUS from './_queries/UpdateStatus.graphql';

import ProfileAvatar from './_components/ProfileAvatar';
import ProfileData from './_components/ProfileData';

export default {
  name: 'ProfileSettingsModule',

  components: {
    ProfileAvatar,
    ProfileData,
  },

  data() {
    return {
      account: {
        email: undefined,
        user: {
          avatar: undefined,
          phone: undefined,
          status: undefined,
          displayName: undefined,
        },
      },
    };
  },

  methods: {
    /**
     * Submit DisplayName
     */
    submitDisplayName(displayName) {
      this.$apollo.mutate({
        mutation: UPDATE_DISPLAY_NAME,
        variables: {
          updateDisplayNameInput: {
            displayName,
          },
        },
        update: (store) => {
          const data = store.readQuery({ query: ACCOUNT });
          data.account.user.displayName = displayName;
          store.writeQuery({ query: ACCOUNT, data });
        },
      });
    },

    /**
     * Submit Status
     */
    submitStatus(status) {
      this.$apollo.mutate({
        mutation: UPDATE_STATUS,
        variables: {
          updateStatusInput: {
            status,
          },
        },
        update: (store) => {
          const data = store.readQuery({ query: ACCOUNT });
          data.account.user.status = status;
          store.writeQuery({ query: ACCOUNT, data });
        },
      });
    },

    /**
     * Submit Phone
     */
    submitPhone(phone) {
      this.$apollo.mutate({
        mutation: UPDATE_PHONE,
        variables: {
          updatePhoneInput: {
            phone,
          },
        },
        update: (store) => {
          const data = store.readQuery({ query: ACCOUNT });
          data.account.user.phone = phone;
          store.writeQuery({ query: ACCOUNT, data });
        },
      });
    },

    /**
     * Submit Email
     */
    submitEmail(email) {
      this.$apollo.mutate({
        mutation: UPDATE_EMAIL,
        variables: {
          updateEmailInput: {
            email,
          },
        },
        update: (store) => {
          const data = store.readQuery({ query: ACCOUNT });
          data.account.email = email;
          store.writeQuery({ query: ACCOUNT, data });
        },
      });
    },

    /**
     * Submit Password
     */
    submitPassword(password) {
      this.$apollo.mutate({
        mutation: UPDATE_PASSWORD,
        variables: {
          updatePasswordInput: {
            password,
          },
        },
      });
    },
  },

  apollo: {
    account: {
      query: ACCOUNT,
    },
  },
};
</script>
