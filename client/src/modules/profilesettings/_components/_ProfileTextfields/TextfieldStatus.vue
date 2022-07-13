<template>
  <v-layout>
    <v-flex>
      <!-- success alert shown under textfield -->
      <v-alert
        :value="success"
        type="success"
        transition="scale-transition"
      >
        {{ this.$t('profileSettings.status.success') }}
      </v-alert>

      <!-- error alert shown under textfield -->
      <v-alert
        :value="error"
        type="error"
        transition="scale-transition"
      >
        {{ this.$t('profileSettings.status.error') }}
      </v-alert>

      <v-text-field
        ref= "statusField"
        v-model= "status"
        :rules= "rules"
        :placeholder= "placeholder"
        :label = "label"
        :hint = "hint"
        type="text"
        @keyup.enter= "loseFocus()"
        @blur = "submit"
      />
    </v-flex>
  </v-layout>
</template>

<script>

/**
 * @vue-data {String} status - Status Message of User/Input value of textfield.
 * @vue-data {String} statusSaved - Saves input (status) before user changes input.
 * @vue-data {String} placeholder - Sets the input’s placeholder text.
 * @vue-data {String} label - Sets input label.
 * @vue-data {String} hint - Sets hint text shown under textfield.
 * @vue-data {Boolean} success - Returns true if submit of data succeed.
 * @vue-data {Boolean} error - Returns true if submit of data failed.
 * @vue-data {Array} rules - Array of functions that return either True or a String with
   an error message. Used for validation of textfield.
  */
export default {
  name: 'TextfieldStatus',

  props: {
    account: {
      type: Object,
      default() {
        return {};
      },
    },
  },

  data() {
    return {
      status: this.account.user.status,
      statusSaved: this.account.user.status,
      placeholder: this.$t('profileSettings.status.none'),
      label: this.$t('profileSettings.status.status'),
      hint: this.$t('profileSettings.status.hint'),
      success: false,
      error: false,
      rules: [
        input => (input === undefined || input.length <= 64) || this.$t('profileSettings.status.rules.length'),
      ],
    };
  },

  watch: {
    'account.user.status': function status() {
      if (this.status !== this.account.user.status) {
        this.status = this.account.user.status;
        this.statusSaved = this.account.user.status;
      }
    },
  },

  methods: {

    /**
     * Triggers blur event on v-text-field
     * ToDo: Refactoring
     */
    loseFocus() {
      this.$refs.statusField.$refs.input.blur();
    },

    /**
     * Checks data for validation and submit data.
     */
    submit() {
      const val = this.rules.every(rule => typeof rule(this.status) !== 'string');
      if (val) {
        if (this.status !== this.statusSaved) {
          this.success = true;
          setTimeout(() => {
            this.success = false;
          }, 1500);

          this.$emit('submit', this.status);

          if (this.status === '') {
            this.placeholder = this.$t('profileSettings.status.none');
          }
          this.statusSaved = this.status;
        }
      } else {
        if (this.status !== '') {
          this.error = true;
          setTimeout(() => {
            this.error = false;
          }, 1500);
        }

        this.status = this.statusSaved;
      }
      this.label = this.$t('profileSettings.status.status');
    },
  },
};
</script>
