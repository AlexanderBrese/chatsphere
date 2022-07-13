<template>
  <div id="textfield">
    <!-- success alert shown above textfield -->
    <v-alert
      :value= "success"
      type= "success"
      transition= "scale-transition"
    >
      {{ this.$t('profileSettings.displayName.success') }}
    </v-alert>

    <!-- error alert shown above textfield -->
    <v-alert
      :value= "error"
      type= "error"
      transition= "scale-transition"
    >
      {{ this.$t('profileSettings.displayName.error') }}
    </v-alert>

    <v-text-field
      ref= "displayNameField"
      v-model= "displayName"
      :rules= "rules"
      :placeholder= "placeholder"
      :label= "label"
      :hint= "hint"
      type= "text"
      dark
      @keyup.enter= "loseFocus()"
      @click= "clearInput"
      @blur= "submit"
    />
  </div>
</template>

<script>

/**
 * @vue-data {String} displayName - displayName of User/Input value of textfield .
 * @vue-data {String} displayNameSaved - Saves input (displayName) before user changes input.
 * @vue-data {String} placeholder - Sets the input’s placeholder text.
 * @vue-data {String} label - Sets input label.
 * @vue-data {String} hint - Sets hint text shown under textfield.
 * @vue-data {Boolean} success - Returns true if submit of data succeed.
 * @vue-data {Boolean} error - Returns true if submit of data failed.
 * @vue-data {Boolean} focused - Returns true if textfield is focused.
 * @vue-data {Array} rules - Array of functions that return either True or a String with
    an error message. Used for validation of textfield.
  */
export default {
  name: 'TextfieldDisplayName',

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
      displayName: this.account.user.displayName,
      displayNameSaved: '',
      placeholder: '',
      label: this.$t('profileSettings.displayName.displayName'),
      hint: this.$t('profileSettings.displayName.hint'),
      success: false,
      error: false,
      focused: false,
      rules: [
        input => !!input || this.$t('profileSettings.displayName.rules.required'),
        input => (input && input.length <= 20) || this.$t('profileSettings.displayName.rules.length'),
      ],
    };
  },

  watch: {
    'account.user.displayName': function displayName() {
      if (this.displayName !== this.account.user.displayName) {
        this.displayName = this.account.user.displayName;
      }
    },
  },

  methods: {
    /**
     * Delete input and set old input as placeholder, if textfield is not focused before.
     */
    clearInput() {
      if (!this.focused) {
        this.label = this.$t('profileSettings.displayName.newDisplayName');
        this.displayNameSaved = this.displayName;
      }

      this.focused = true;
    },

    /**
     * Triggers blur event on v-text-field
     * ToDo: Refactoring
     */
    loseFocus() {
      this.$refs.displayNameField.$refs.input.blur();
    },

    /**
     * Checks data for validation and submit data.
     */
    submit() {
      this.focused = false;
      const val = this.rules.every(rule => typeof rule(this.displayName) !== 'string');
      if (val) {
        if (this.displayName !== this.displayNameSaved) {
          this.success = true;
          setTimeout(() => {
            this.success = false;
          }, 1500);

          this.$emit('submit', this.displayName);
        }
      } else {
        if (this.displayName !== '') {
          this.error = true;
          setTimeout(() => {
            this.error = false;
          }, 1500);
        }

        this.displayName = this.displayNameSaved;
      }
      this.label = this.$t('profileSettings.displayName.displayName');
    },
  },
};
</script>
