<template>
  <v-layout>
    <v-flex>
      <!-- success alert shown under textfield -->
      <v-alert
        :value= "success"
        type= "success"
        transition= "scale-transition"
      >
        {{ successMessage }}
      </v-alert>

      <!-- error alert shown under textfield -->
      <v-alert
        :value= "error"
        type= "error"
        transition= "scale-transition"
      >
        {{ errorMessage }}
      </v-alert>

      <!-- error alert shown above textfield, if passwords doesn't match -->
      <v-alert
        :value= "errorRepeat"
        type= "error"
        transition= "scale-transition"
      >
        {{ errorRepeatMessage }}
      </v-alert>

      <v-text-field
        v-model= "password"
        :rules= "rules"
        :label= "label"
        :hint= "hint"
        :placeholder= "placeholder"
        type= "password"
        @keyup.enter= "focusRepeat()"
        @click= "switchLabel"
        @input= "showSecondTextfield"
        @blur= "unfocus"
      />

      <!-- Repeat textfield (repeat password) -->
      <v-text-field
        v-if= "showRepeat"
        ref= "repeatField"
        v-model= "passwordRepeat"
        :rules= "rulesRepeat"
        :placeholder= "placeholder"
        :label= "labelRepeat"
        :hint= "hintRepeat"
        type= "password"
        @keyup.enter= "loseFocus()"
        @blur= "submit"
      />
    </v-flex>
  </v-layout>
</template>

<script>
export default {
  name: 'TextfieldPassword',

  /**
   * @vue-data {String} password - Password input value of textfield.
   * @vue-data {String} passwordRepeat - Password input value of second textfield.
   * @vue-data {Boolean} showRepeat - Returns true if repeat textfield should be shown.
   * @vue-data {String} placeholder - Sets the input’s placeholder text.
   * @vue-data {String} label - Sets input label.
   * @vue-data {String} labelRepeat - Sets input label of repeat textfield.
   * @vue-data {String} hint - Sets hint text shown under textfield.
   * @vue-data {String} hintRepeat - Sets hint text shown under repeat textfield.
   * @vue-data {Boolean} success - Returns true if submit of data succeed.
   * @vue-data {String} successMessage - Sets message shown at success.
   * @vue-data {Boolean} error - Returns true if error occurs at first textfield.
   * @vue-data {String} errorMessage - Sets message shown at error.
   * @vue-data {Boolean} errorRepeat - Returns true if error occurs at repeat textfield.
   * @vue-data {String} errorMessage - Sets message shown at errorRepeat.
   * @vue-data {Boolean} focused - Returns true if textfield is focused.
   * @vue-data {Array} rules - Array of functions that return either True or a String with
     an error message. Used for validation of first textfield.
   * @vue-data {Array} rulesRepeat - Array of functions that return either True or a String with
     an error message. Used for validation of repeat textfield.
   */
  data() {
    return {
      password: 'Password!',
      passwordRepeat: '',
      showRepeat: false,
      placeholder: this.$t('profileSettings.password.password'),
      label: this.$t('profileSettings.password.password'),
      labelRepeat: this.$t('profileSettings.passwordRepeat.passwordRepeat'),
      hint: this.$t('profileSettings.password.hint'),
      hintRepeat: this.$t('profileSettings.passwordRepeat.hint'),
      success: false,
      successMessage: this.$t('profileSettings.password.success'),
      error: false,
      errorMessage: this.$t('profileSettings.password.error'),
      errorRepeat: false,
      errorRepeatMessage: this.$t('profileSettings.password.rules.match'),
      rules: [
        input => !!input || this.$t('registration.rules.password.required'),
        input => (input && input.length >= 8 && input.length <= 32) || this.$t('registration.rules.password.between'),
        input => (input && this.hasUpperCase(input)) || this.$t('registration.rules.password.upper'),
        input => (input && this.hasSpecial(input)) || this.$t('registration.rules.password.special'),
      ],
      rulesRepeat: [
        input => !!input || this.$t('profileSettings.passwordRepeat.rules.required'),
      ],
    };
  },

  methods: {
    /**
     * A special character is either a number or the below list of characters
     */
    hasSpecial(input) {
      return /[!@#$%^&+=]/g.test(input) || /\d/.test(input);
    },

    /**
     * English and German uppercase letters supported
     */
    hasUpperCase(input) {
      return /[A-ZÖÜÄẞ]/g.test(input);
    },

    /**
     * If rules matches focus repeat textfield, if not call unfocus method
     */
    focusRepeat() {
      const val = this.rules.every(rule => typeof rule(this.password) !== 'string');
      if (val) {
        this.$refs.repeatField.$refs.input.focus();
      } else {
        this.unfocus();
      }
    },

    /**
     * Triggers blur event on v-text-field
     * ToDo: Refactoring
     */
    loseFocus() {
      this.$refs.repeatField.$refs.input.blur();
    },

    /**
     * Changes label of first textfield.
     */
    switchLabel() {
      this.label = this.$t('profileSettings.password.newPassword');
      this.password = '';
    },

    /**
     * Checks data for validation and submit data. (Checks if passwords match)
     */
    submit() {
      if (this.password === this.passwordRepeat) {
        this.success = true;
        setTimeout(() => {
          this.success = false;
        }, 1500);

        this.$emit('submit', this.password);

        this.showRepeat = false;
        this.passwordRepeat = '';
        this.password = 'Password!';
        this.label = this.$t('profileSettings.password.password');
      } else {
        this.errorRepeat = true;
        setTimeout(() => {
          this.errorRepeat = false;
        }, 1500);

        this.showRepeat = false;
        this.passwordRepeat = '';
        this.password = 'Password!';
        this.label = this.$t('profileSettings.password.password');
      }
    },

    /**
     * Shows repeat textfield if first textfield matches every rule
     */
    showSecondTextfield() {
      const val = this.rules.every(rule => typeof rule(this.password) !== 'string');
      if (val) {
        this.showRepeat = true;
      } else {
        this.showRepeat = false;
      }
    },

    /**
     * Resets first textfield. Show alert.
     */
    unfocus() {
      const val = this.rules.every(rule => typeof rule(this.password) !== 'string');
      if (!val) {
        this.password = 'Password!';

        // show alert because you dont see a change (due to invisibility of current password)
        this.error = true;
        setTimeout(() => {
          this.error = false;
        }, 1500);
      }
    },
  },
};
</script>
