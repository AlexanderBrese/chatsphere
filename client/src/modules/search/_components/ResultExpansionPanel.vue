<template>
  <v-expansion-panel-content>
    <!-- Category title -->
    <div slot="header">{{ title }}</div>

    <!-- Display results If it is a chat list -->
    <v-card v-if="type">
      <v-list two-line>
        <template v-for="(entry, index) in list">
          <v-list-tile
            :key="index"
            avatar
            @click="$router.push({ path: '/chat/' + entry.id })">

            <!-- Avatar -->
            <v-list-tile-avatar>
              <img
                :src="entry.stamp.iconUrl !== ''
                  ? entry.stamp.iconUrl
                : require('@/assets/avatar.png')">
            </v-list-tile-avatar>

            <!-- Content to the right of avatar -->
            <v-list-tile-content>
              <v-list-tile-title v-html="entry.stamp.displayName"/>
            </v-list-tile-content>
          </v-list-tile>

          <v-divider
            v-if="index != list.length - 1"
            :key="-index-1"
            inset/>
        </template>
      </v-list>
    </v-card>

    <!-- Display results If it is a contact list -->
    <v-card v-else>
      <v-list two-line>
        <template v-for="(entry, index) in list">
          <v-list-tile
            :key="index"
            avatar
            @click="$router.push({ path: '/profile/' + entry.user.username })">
            <v-list-tile-avatar>
              <img
                :src="entry.user.avatarUrl !== ''
                  ? entry.user.avatarUrl
                : require('@/assets/avatar.png')">
            </v-list-tile-avatar>

            <v-list-tile-content>
              <v-list-tile-title v-html="entry.nickname ? entry.nickname : entry.user.displayName"/>
              <v-list-tile-sub-title v-html="'Username: ' + entry.user.username"/>
            </v-list-tile-content>
          </v-list-tile>
          <v-divider
            v-if="index != list.length - 1"
            :key="-index - 1"
            inset/>
        </template>
      </v-list>
    </v-card>

  </v-expansion-panel-content>
</template>

<script>


/**
 * @vue-data
 * @vue-data
 */
export default {

  props: {
    type: {
      type: Boolean,
      required: true,
    },
    title: {
      type: String,
      required: true,
    },
    list: {
      type: Array,
      required: true,
    },
  },

  data() {
    return {
    };
  },
};
</script>

