<!--
  Step to enter email
-->
<template>
  <v-text-field
    v-model="input"
    :placeholder="placeholder"
    :label="label"
    :rules="rules"
    type="text"
    append-outer-icon="send"
    required
    autofocus
    @click:append-outer="submitEmail"
    @keyup.enter="submitEmail"
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
      title: this.$t('registration.email'),
      placeholder: this.$t('registration.placeholder.email'),
      label: this.$t('registration.label.email'),
      rules: [
        input => !!input || this.$t('registration.rules.email.required'),
        input => (input && this.validEmail(input)) || this.$t('registration.rules.email.valid'),
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
     * https://stackoverflow.com/questions/46155/how-to-validate-an-email-address-in-javascript/1373724#1373724
     */
    validEmail(input) {
      const re = /[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9A-Z](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?/;
      return re.test(input);
    },

    /**
     * Submit to parent.
     */
    submitEmail() {
      if (this.valid) {
        this.$emit('next', this.input);
      }
    },
  },
};
</script>
