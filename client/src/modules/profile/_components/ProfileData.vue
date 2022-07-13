<!--
  List of all profile data that the user consented to reveal
-->
<template>

  <!-- ProfileList -->
  <v-list two-line>

    <!-- ProfileListEntry: Username -->
    <v-list-tile>
      <v-list-tile-action>
        <v-icon color="primary">account_circle</v-icon>
      </v-list-tile-action>

      <v-list-tile-content>
        <v-list-tile-title>{{ user.username }}</v-list-tile-title>
        <v-list-tile-sub-title>Username</v-list-tile-sub-title>
      </v-list-tile-content>
    </v-list-tile>

    <v-divider inset/>

    <!-- ProfileListEntry: Phone Number -->
    <v-list-tile>
      <v-list-tile-action>
        <v-icon color="primary">phone</v-icon>
      </v-list-tile-action>

      <v-list-tile-content>
        <v-list-tile-title>{{ user.phone }}</v-list-tile-title>
        <v-list-tile-sub-title>{{ $t('profileOverview.phone') }}</v-list-tile-sub-title>
      </v-list-tile-content>
    </v-list-tile>

    <v-divider inset/>

    <!-- ProfileListEntry: Status -->
    <v-list-tile>
      <v-list-tile-action>
        <v-icon color="primary">info</v-icon>
      </v-list-tile-action>

      <v-list-tile-content>
        <v-list-tile-title>{{ user.status }}</v-list-tile-title>
        <v-list-tile-sub-title>{{ $t('profileOverview.status') }}</v-list-tile-sub-title>
      </v-list-tile-content>
    </v-list-tile>

    <v-divider inset/>

    <!-- ProfileListEntry: Block Contact -->
    <v-list-tile
      v-if="contact && !blocked"
      @click="$emit('blockAccount')">
      <v-list-tile-action>
        <v-icon color="red">block</v-icon>
      </v-list-tile-action>

      <v-list-tile-content>
        <v-list-tile-title class="textRed">
          {{ $t('profileOverview.blockContact') }}
        </v-list-tile-title>
      </v-list-tile-content>
    </v-list-tile>

    <v-divider
      v-if="contact && !blocked"
      inset/>

    <!-- ProfileListEntry: Unblock Contact -->
    <v-list-tile
      v-if="contact && blocked"
      @click="$emit('unblockAccount')">
      <v-list-tile-action>
        <v-icon color="green">block</v-icon>
      </v-list-tile-action>

      <v-list-tile-content>
        <v-list-tile-title class="textGreen">
          {{ $t('profileOverview.unblockContact') }}
        </v-list-tile-title>
      </v-list-tile-content>
    </v-list-tile>

    <v-divider
      v-if="contact && blocked"
      inset/>

    <!-- ProfileListEntry: Delete Contact -->
    <v-list-tile
      v-if="contact"
      @click="$emit('deleteContact')">
      <v-list-tile-action>
        <v-icon color="red">delete</v-icon>
      </v-list-tile-action>

      <v-list-tile-content>
        <v-list-tile-title class="textRed">
          {{ $t('profileOverview.deleteContact') }}
        </v-list-tile-title>
      </v-list-tile-content>
    </v-list-tile>

    <v-divider
      v-if="contact"
      inset/>

    <!-- ProfileListEntry: Report Contact -->
    <v-list-tile
      @click="dialog = true">
      <v-list-tile-action>
        <v-icon
          style="position: absolute;"
          color="red">thumb_down_alt</v-icon>
      </v-list-tile-action>

      <v-list-tile-content>
        <v-list-tile-title>
          {{ $t('profileOverview.reportUser') }}
        </v-list-tile-title>
      </v-list-tile-content>
    </v-list-tile>

    <!-- Dialog shown when reporting a user -->
    <v-dialog
      v-model="dialog"
      hide-overlay
      max-width="290"
    >
      <v-card>
        <v-card-title>{{ $t('profileOverview.report.title') }}</v-card-title>

        <v-divider/>

        <!-- textarea to input report message -->
        <v-card-text>
          <v-textarea
            v-model="reportMessage"
            auto-grow
            autofocus
            clearable
          />
        </v-card-text>

        <v-divider/>

        <v-card-actions>
          <v-btn
            color="primary"
            flat
            @click="sendReport()"
          >
            {{ $t('profileOverview.report.buttonSend') }}
          </v-btn>
          <v-btn
            color="error"
            flat
            @click="cancelReport()"
          >
            {{ $t('profileOverview.report.buttonCancel') }}
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-list>
</template>

<script>
/**
 * @vue-data {Object} user - User Object
 */
export default {

  props: {
    user: {
      type: Object,
      default() {
        return {
          phone: '+49 12345 6789',
          status: 'Default',
        };
      },
    },
    contact: {
      type: Boolean,
      default() {
        return false;
      },
    },
    blocked: {
      type: Boolean,
      default() {
        return false;
      },
    },
  },

  data() {
    return {
      dialog: false,
      reportMessage: '',
    };
  },

  methods: {
    sendReport() {
      this.dialog = false;
      this.$emit('reportAccount', this.reportMessage);
      this.reportMessage = '';
    },

    cancelReport() {
      this.dialog = false;
      this.reportMessage = '';
    },
  },
};
</script>
