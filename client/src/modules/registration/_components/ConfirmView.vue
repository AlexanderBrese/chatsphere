<!--
  Step to confirm password
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
    @click:append-outer="submitConfirmation"
    @keyup.enter="submitConfirmation"
  />
</template>

<style scoped>
</style>
<script>

/**
 * @vue-prop {String} password=undefined - The recently entered password to compare to.
 * @vue-data {String} input - The user's current input in the local text field.
 * @vue-data {String} title - Displayed above the text field.
 * @vue-data {String} placeholder - Grey text displayed in an empty text field.
 * @vue-data {String} label - text above input field to describe its context.
 * @vue-data {Array} rules - An array of functions each having a single `input` argument and.
 *                          return true when rule is passed or a String of the error message if not.
 */
export default {

  props: {
    password: {
      type: String,
      default: undefined,
    },
  },

  data() {
    return {
      input: '',
      title: this.$t('registration.confirm'),
      placeholder: this.$t('registration.placeholder.password'),
      label: this.$t('registration.label.confirm'),
      rules: [
        input => !!input || this.$t('registration.rules.password.required'),
        input => (input && input === this.password) || this.$t('registration.rules.password.match'),
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
     * Submit to parent.
     */
    submitConfirmation() {
      if (this.valid) {
        this.$emit('next', this.input);
      }
    },
  },
};
</script>
