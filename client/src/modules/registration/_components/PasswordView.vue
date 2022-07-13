<!--
  Step to enter password
-->
<template>
  <v-text-field
    v-model="input"
    :placeholder="placeholder"
    :label="label"
    :rules="rules"
    type="password"
    append-outer-icon="send"
    required
    autofocus
    @click:append-outer="submitPassword"
    @keyup.enter="submitPassword"
  />
</template>

<style scoped>
</style>
<script>

/**
 * @vue-data {String} input - The user's current input in the local text field.
 * @vue-data {String} title - Displayed above the text field.
 * @vue-data {String} placeholder - Grey text displayed in an empty text field.
 * @vue-data {String} label - text above input field to describe its context.
 * @vue-data {Array} rules - An array of functions each having a single `input` argument and.
 *                          return true when rule is passed or a String of the error message if not.
 */
export default {

  data() {
    return {
      input: '',
      title: this.$t('registration.password'),
      placeholder: this.$t('registration.placeholder.password'),
      label: this.$t('registration.label.password'),
      rules: [
        input => !!input || this.$t('registration.rules.password.required'),
        input => (input && input.length >= 8 && input.length <= 32) || this.$t('registration.rules.password.between'),
        input => (input && this.hasUpperCase(input)) || this.$t('registration.rules.password.upper'),
        input => (input && this.hasSpecial(input)) || this.$t('registration.rules.password.special'),
      ],
    };
  },

  computed: {
    valid() {
      return this.rules.every(rule => typeof rule(this.input) !== 'string');
    },
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
     * Submit to parent.
     */
    submitPassword() {
      if (this.valid) {
        this.$emit('next', this.input);
      }
    },
  },
};
</script>
