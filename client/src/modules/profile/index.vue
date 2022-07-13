<!--
  Presents an overview of a profile.
  Users may navigate to further functionalities like "Add contact"
  or "Block contact" via this component.
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
      <v-toolbar-title>{{ user.displayName | uppercase }}</v-toolbar-title>
    </v-toolbar>
    <v-content app>
      <ProfileAvatar
        :contact= "contact"
        :user= "getUser()"
        @updateNickname= "updateNickname($event)"
      />
      <v-card>
        <ProfileData
          :user= "user"
          :contact= "contact"
          :blocked= "blocked"
          @reportAccount= "reportAccount($event)"
          @unblockAccount= "unblockAccount()"
          @blockAccount= "blockAccount()"
          @deleteContact= "deleteContact()"/>
      </v-card>
    </v-content>
  </div>
</template>

<script>
import gql from 'graphql-tag';

import ProfileAvatar from './_components/ProfileAvatar';
import ProfileData from './_components/ProfileData';

const USER = gql`
  query user($input: String!) {
    user(username: $input) {
      phone
      avatarUrl
      status
      displayName
      username
    }
  }
`;

const ACCOUNT = gql`
  query account {
    account {
      user {
        username
      }
      contacts {
        user {
          phone
          avatarUrl
          status
          username
        }
        nickname
      }
      blocked {
        user {
          username
        }
      }
    }
  }
`;

const UPDATE_NICKNAME = gql`
  mutation UpdateNickname($input: UpdateNicknameInput!) {
    updateNickname(updateNicknameInput: $input)
  }
`;

const REPORT_USER = gql`
  mutation reportUser($input: ReportUserInput!) {
    reportUser(reportUserInput: $input)
  }
`;

const BLOCK_CONTACT = gql`
  mutation blockContact($input: BlockContactInput!) {
    blockContact(blockContactInput: $input)
  }
`;

const UNBLOCK_CONTACT = gql`
  mutation unblockContact($input: UnblockContactInput!) {
    unblockContact(unblockContactInput: $input)
  }
`;

const DELETE_CONTACT = gql`
  mutation removeContact($input: RemoveContactInput!) {
  removeContact(removeContactInput: $input)
}
`;

/**
 * @vue-data {Boolean} contact: Whether User is a contact or not.
 * @vue-data {Boolean} blocked: Whether User is a blocked or not.
 * @vue-data {Object} account: Account Object of logged in User.
 * @vue-data {Object} user: User Object of selected User.
 */
export default {
  name: 'ProfileModule',

  components: {
    ProfileAvatar,
    ProfileData,
  },

  data() {
    return {
      dialog: false,
      blocked: false,
      contact: false,
      title: this.$t('profile.title'),
      account: {
        user: {
          username: '',
        },
        contacts: [],
        blocked: [],
      },
      user: {},
    };
  },

  watch: {
    user: function userCheck() {
      if (this.user === null) {
        this.dialog = true;
      }
    },

    'account.contacts': function contactsCheck() {
      if (this.checkContact()) {
        this.contact = true;
        if (this.checkBlocked()) {
          this.blocked = true;
        } else {
          this.blocked = false;
        }
      } else {
        this.contact = false;
        this.blocked = false;
      }
    },

    'account.blocked': function blockedCheck() {
      if (this.checkBlocked()) {
        this.blocked = true;
      } else {
        this.blocked = false;
      }
    },
  },

  methods: {
    /**
     * Changes the nickname of an contact.
     */
    updateNickname(nicknameChanged) {
      this.$apollo.mutate({
        mutation: UPDATE_NICKNAME,
        variables: {
          input: {
            contactUsername: this.user.username,
            nickname: nicknameChanged,
          },
        },
        update: (store) => {
          const data = store.readQuery({
            query: ACCOUNT,
          });
          data.account.contacts[data.account.contacts
            .findIndex(contact => contact.user.username === this.user.username)]
            .nickname = nicknameChanged;

          store.writeQuery({ query: ACCOUNT, data });
        },
      });
    },

    /**
     * Reports an account.
     */
    reportAccount(reportMessage) {
      this.$apollo.mutate({
        mutation: REPORT_USER,
        variables: {
          input: {
            reportedUsername: this.user.username,
            reason: reportMessage,
          },
        },
      });
    },

    /**
     * Unblocks an account.
     */
    unblockAccount() {
      this.$apollo.mutate({
        mutation: UNBLOCK_CONTACT,
        variables: {
          input: {
            contactUsername: this.user.username,
          },
        },
        update: (store) => {
          const data = store.readQuery({
            query: ACCOUNT,
          });
          data.account.blocked = data.account.blocked
            .filter(contact => contact.user.username !== this.user.username);

          store.writeQuery({ query: ACCOUNT, data });
        },
      });
    },

    /**
     * Blocks an account.
     */
    blockAccount() {
      this.$apollo.mutate({
        mutation: BLOCK_CONTACT,
        variables: {
          input: {
            contactUsername: this.user.username,
          },
        },
        update: (store) => {
          const data = store.readQuery({
            query: ACCOUNT,
          });

          data.account.blocked
            .push(data.account.contacts
              .filter(contact => contact.user.username === this.user.username)[0]);

          store.writeQuery({ query: ACCOUNT, data });
        },
      });
    },

    /**
     * Deletes a contact.
     */
    deleteContact() {
      this.$apollo.mutate({
        mutation: DELETE_CONTACT,
        variables: {
          input: {
            contactUsername: this.user.username,
          },
        },
        update: (store) => {
          const data = store.readQuery({
            query: ACCOUNT,
          });
          data.account.contacts = data.account.contacts
            .filter(con => con.user.username !== this.user.username);
          store.writeQuery({ query: ACCOUNT, data });
        },
      });

      if (this.checkBlocked()) {
        this.unblockAccount();
      }
    },

    /**
     * Checks if a user is blocked
     */
    checkBlocked() {
      if (this.account.blocked
        .filter(contact => this.user.username === contact.user.username).length > 0) {
        return true;
      }
      return false;
    },

    /**
     * Checks if a user is in contact list
     */
    checkContact() {
      if (this.account.contacts
        .filter(contact => this.user.username === contact.user.username).length > 0) {
        return true;
      }
      return false;
    },

    /**
     * If User is contact return contact object, else user Object.
     */
    getUser() {
      if (this.checkContact()) {
        return this.account.contacts
          .filter(contact => this.user.username === contact.user.username)[0];
      }
      return { user: this.user };
    },
  },

  apollo: {
    user: {
      query: USER,
      variables() {
        return {
          input: this.$route.params.username,
        };
      },
    },
    account: {
      query: ACCOUNT,
    },
  },
};
</script>

<style>
</style>
