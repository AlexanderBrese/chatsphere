<template>
  <v-list
    two-line
    class="py-0 my-2">
    <template v-for="(contact, index) in contacts">
      <v-list-tile
        :key="contact.user.username"
        ripple
        avatar
        @click="select(contact)">
        <v-list-tile-avatar>
          <img
            :src="contact.user.avatarUrl !== ''
              ? contact.user.avatarUrl
            : require('@/assets/avatar.png')">
        </v-list-tile-avatar>

        <v-list-tile-content>
          <v-list-tile-title>{{ contact.user.displayName }}</v-list-tile-title>
          <v-list-tile-sub-title v-html="contact.user.status"/>
        </v-list-tile-content>
      </v-list-tile>
      <v-divider
        v-if="index + 1 < contacts.length"
        :key="index"
        inset/>
    </template>
  </v-list>
</template>

<script>
export default {
  name: 'ContactList',
  props: {
    contacts: {
      type: Array,
      required: true,
      default: () => ([]),
    },
  },
  methods: {
    select(contact) {
      this.$router.push({ name: 'Profile', params: { username: contact.user.username } });
    },
  },
};
</script>

<style scoped>

</style>
