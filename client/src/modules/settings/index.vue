<!--
  Toggle specific settings about one's account.
  View blocked contacts or stop receiving push notifications.
-->
<template>
  <div>
    <!-- Navigationbar -->
    <v-navigation-drawer
      v-model="drawer"
      app
      absolute
      floating>
      <navigation/>
    </v-navigation-drawer>
    <!-- Toolbar -->
    <v-toolbar
      :fixed="$vuetify.breakpoint.xsOnly"
      :absolute="$vuetify.breakpoint.smAndUp"
      class="white--text"
      color="primary"
      app>
      <v-toolbar-side-icon
        v-if="$vuetify.breakpoint.name === 'xs'"
        class="white--text"
        @click.native="drawer = !drawer"/>
      <v-toolbar-title>{{ title | uppercase }}</v-toolbar-title>
    </v-toolbar>
    <v-content app>
      <v-layout row>
        <v-flex>
          <v-card>
            <SettingsList/>
          </v-card>
        </v-flex>
      </v-layout>
    </v-content>
  </div>
</template>

<script>
import Navigation from '@/modules/navigation/';
import SettingsList from './_components/SettingsList';

export default {
  components: {
    SettingsList,
    Navigation,
  },
  data() {
    return {
      title: this.$t('settings.settings'),
      drawer: this.$vuetify.breakpoint.name !== 'xs',
    };
  },
};

</script>
