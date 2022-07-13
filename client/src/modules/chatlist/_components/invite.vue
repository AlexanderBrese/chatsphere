<template>
  <div class="contacts-invite-view">
    <v-toolbar
      :fixed="$vuetify.breakpoint.xsOnly"
      :absolute="$vuetify.breakpoint.smAndUp"
      class="pl-0"
      dark
      color="primary"
      app>
      <v-btn
        icon
        @click.native="close()">
        <v-icon>arrow_back</v-icon>
      </v-btn>
      <v-toolbar-title>{{ title | uppercase }}</v-toolbar-title>
      <v-spacer/>
      <v-btn
        :disabled="selected.length === 0"
        icon
        @click.native="selected.length > 1 ? dialog = true : createChat()"
      >
        <v-icon>done</v-icon>
      </v-btn>
    </v-toolbar>
    <v-dialog v-model="dialog">
      <v-card>
        <v-card-title>
          <span class="headline">Chat Name</span>
        </v-card-title>
        <v-card-text>
          <v-layout>
            <v-flex xs12>
              <v-text-field
                v-model="name"
                :error="error !== ''"
                :error-messages="error"
                label="Name"
                required/>
            </v-flex>
          </v-layout>
        </v-card-text>
        <v-card-actions>
          <v-spacer/>
          <v-btn
            color="primary"
            flat
            @click.native="dialog = false">Close</v-btn>
          <v-btn
            color="primary"
            flat
            @click.native="createChat()">Save</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
    <v-content
      class="pl-0"
      app>
      <v-layout
        v-if="selected.length > 1"
        align-center
      >
        <v-flex
          text-xs-center
          class="gradient-bg pa-3"
          @click="pickFile">
          <v-avatar
            v-if="iconUrl"
            class = "center"
            size = "150">
            <img :src="iconUrl">
          </v-avatar>
          <div v-else>
            <v-icon
              color="blank"
              size="128px">photo_camera</v-icon>
            <h1 class="headline white--text">{{ 'Upload Image' | uppercase }}</h1>
          </div>
        </v-flex>
        <!-- Upload button -->
        <input
          id="file"
          ref="myFiles"
          style="display: none"
          type="file"
          @change="upload(this)">
      </v-layout>
      <v-list
        two-line
        class="py-0">
        <template v-for="(contact, index) in contacts">
          <v-list-tile
            :key="contact.user.username"
            avatar
            @click="select(contact)">
            <v-list-tile-action @click="select(contact)">
              <v-checkbox
                v-model="selected"
                :value="contact.user.username"
              />
            </v-list-tile-action>
            <v-list-tile-avatar
              @click="navigate(contact)">
              <img
                :src="contact.user.avatarUrl !== ''
                  ? contact.user.avatarUrl
                : require('@/assets/avatar.png')">
            </v-list-tile-avatar>

            <v-list-tile-content>
              <v-list-tile-title v-html="contact.user.displayName"/>
              <v-list-tile-sub-title v-html="'Happy'"/>
            </v-list-tile-content>
          </v-list-tile>
          <v-divider
            v-if="index != contacts.length - 1"
            :key="index"
            inset/>
        </template>
      </v-list>
    </v-content>
  </div>
</template>

<script>
import CONTACTS from '@/modules/contactlist/_queries/Contacts.graphql';
import CHATS from '../_queries/Chats.graphql';
import CREATE_PRIVATE_CHAT from '../_queries/CreatePrivateChat.graphql';
import CREATE_GROUP_CHAT from '../_queries/CreateGroupChat.graphql';

export default {
  name: 'ContactsInviteView',
  data() {
    return {
      title: 'Invite Contacts',
      selected: [],
      dialog: false,
      name: '',
      error: '',
      image: '',
      fileName: '',
      iconUrl: undefined,
    };
  },
  methods: {
    pickFile() {
      this.$refs.myFiles.click();
    },
    upload() {
      const file = this.$refs.myFiles.files[0];
      const reader = new FileReader();
      reader.addEventListener('load', () => {
        // sample result: "data:image/png;base64,iVBORw0..."
        let encodedImage = reader.result;
        this.iconUrl = encodedImage;
        // cut meta information before comma
        encodedImage = encodedImage.slice(encodedImage.indexOf(',') + 1);
        this.image = encodedImage;
        this.fileName = file.name;
      }, false);
      reader.readAsDataURL(file);
    },
    close() {
      this.dialog = false;
      this.$emit('closeDialog');
    },
    reset() {
      this.selected = [];
    },
    select(contact) {
      const index = this.selected.indexOf(contact.user.username);
      if (index === -1) {
        this.selected.push(contact.user.username);
      } else {
        this.selected.splice(index, 1);
      }
    },
    navigate(contact) {
      this.$router.push({ name: 'Profile', params: { username: contact.user.username } });
    },
    createChat() {
      if (this.selected.length === 1) {
        this.createPrivateChat();
      } else {
        this.createGroupChat();
      }
    },
    createPrivateChat() {
      this.$apollo.mutate({
        mutation: CREATE_PRIVATE_CHAT,
        variables: {
          createPrivateChatInput: {
            participantUsername: this.selected[0],
          },
        },
        update: (store, { data: { createPrivateChat } }) => {
          // Add to all contacts list
          const data = store.readQuery({ query: CHATS });
          const index = data.account.chats.findIndex(chat => chat.id === createPrivateChat.id);
          if (index === -1) {
            data.account.chats.push(createPrivateChat);
          }
          store.writeQuery({
            query: CHATS,
            data,
          });
        },
        optimisticResponse: {
          __typename: 'Mutation',
          createPrivateChat: {
            __typename: 'PrivateChat',
            id: null,
            stamp: {
              __typename: 'ChatStamp',
              displayName: '',
              iconUrl: '',
            },
          },
        },
      }).then(({ data }) => {
        this.reset();
        this.close();
        this.$router.push({ path: `/chat/${data.createPrivateChat.id}` });
      })
        .catch(error => {
        // eslint-disable-next-line no-console
          console.error(error);
        });
    },
    createGroupChat() {
      this.$apollo.mutate({
        mutation: CREATE_GROUP_CHAT,
        variables: {
          createGroupChatInput: {
            participantUsernames: this.selected,
            icon: this.image,
            fileName: this.fileName,
            displayName: this.name,
          },
        },
        update: (store, { data: { createGroupChat } }) => {
          // Add to all contacts list
          const data = store.readQuery({ query: CHATS });
          const index = data.account.chats.findIndex(chat => chat.id === createGroupChat.id);
          if (index === -1) {
            data.account.chats.push(createGroupChat);
          }
          store.writeQuery({
            query: CHATS,
            data,
          });
        },
        optimisticResponse: {
          __typename: 'Mutation',
          createGroupChat: {
            __typename: 'GroupChat',
            id: null,
            stamp: {
              __typename: 'ChatStamp',
              displayName: this.name,
              iconUrl: '',
              lastMessage: null,
            },
            notify: {
              __typename: 'Notification',
              push: 'INHERIT',
            },
          },
        },
      }).then(({ data }) => {
        this.reset();
        this.close();
        this.$router.push({ path: `/chat/${data.createGroupChat.id}` });
      })
        .catch(error => {
        // eslint-disable-next-line no-console
          this.error = error.message;
        });
    },
  },
  apollo: {
    contacts: {
      query: CONTACTS,
      update(data) {
        return data.account.contacts;
      },
    },
  },
};
</script>

<style scoped>
    .contacts-invite-view{
        background: #fafafa;
      position: relative;
        height: 500px;
        width: 100%;
    }
</style>
