<!--
  List of all navigation links.
-->
<template>

  <v-list>

    <template v-for="route in routeList">
      <v-list-tile
        v-if="route.icon !== undefined"
        :key="route.path"
        :to="route.path"
      >
        <v-list-tile-action>
          <v-icon>{{ route.icon }}</v-icon>
        </v-list-tile-action>
        <v-list-tile-content>
          <v-list-tile-title class="subheading">{{ route.name }}</v-list-tile-title>
        </v-list-tile-content>
      </v-list-tile>
    </template>
  </v-list>
</template>

<script>
/**
 * @vue-props {Array} routeList - Holds data for navigation routes .
 */
export default {
  name: 'NavigationData',

  props: {
    routeList: {
      type: Array,
      default: undefined,
    },
  },
};

</script>

<style>
</style>
