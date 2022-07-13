<template>
  <v-layout>
    <v-flex>
      <!-- success alert shown above textfield -->
      <v-alert
        :value= "success"
        type= "success"
        transition= "scale-transition"
      >
        {{ this.$t('profileSettings.email.success') }}
      </v-alert>

      <!-- error alert shown above textfield -->
      <v-alert
        :value= "error"
        type= "error"
        transition= "scale-transition"
      >
        {{ this.$t('profileSettings.email.error') }}
      </v-alert>

      <v-text-field
        ref= "emailField"
        v-model= "email"
        :rules= "rules"
        :placeholder= "placeholder"
        :label= "label"
        :hint= "hint"
        type= "text"
        @keyup.enter= "loseFocus()"
        @blur= "submit"
      />
    </v-flex>
  </v-layout>
</template>

<script>

/**
 * @vue-data {String} email - Email Adress of User/Input value of textfield.
 * @vue-data {String} emailSaved - Saves input (email) before user changes input.
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
  name: 'TextfieldEmail',

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
      email: this.account.email,
      emailSaved: '',
      placeholder: '',
      label: this.$t('profileSettings.email.email'),
      hint: this.$t('profileSettings.email.hint'),
      success: false,
      error: false,
      focused: false,
      rules: [
        input => !!input || this.$t('profileSettings.email.rules.required'),
        input => (input && this.validEmail(input)) || this.$t('profileSettings.email.rules.valid'),
      ],
    };
  },

  watch: {
    'account.email': function email() {
      if (this.email !== this.account.email) {
        this.email = this.account.email;
        this.emailSaved = this.account.email;
      }
    },
  },

  methods: {
    /**
     * Validates an E-Mail adress.
     * See: https://stackoverflow.com/questions/46155/how-to-validate-an-email-address-in-javascript/1373724#1373724
     */
    validEmail(input) {
      const re = /[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9A-Z](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?/;
      return re.test(input);
    },

    /**
     * Triggers blur event on v-text-field
     * ToDo: Refactoring
     */
    loseFocus() {
      this.$refs.emailField.$refs.input.blur();
    },

    /**
     * Checks data for validation and submit data.
     */
    submit() {
      const val = this.rules.every(rule => typeof rule(this.email) !== 'string');
      if (val) {
        if (this.email !== this.emailSaved) {
          this.success = true;
          setTimeout(() => {
            this.success = false;
          }, 1500);

          this.$emit('submit', this.email);
          this.emailSaved = this.email;
        }
      } else {
        if (this.email !== '') {
          this.error = true;
          setTimeout(() => {
            this.error = false;
          }, 1500);
        }

        this.email = this.emailSaved;
      }

      this.label = this.$t('profileSettings.email.email');
    },
  },
};
</script>
