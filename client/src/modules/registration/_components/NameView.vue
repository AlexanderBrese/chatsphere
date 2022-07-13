<!--
  Step to enter name
-->
<template>
  <div>
    <v-alert
      :value="isTaken"
      dismissible
      transition="scale-transition"
      origin="center center"
      class="ma-0 text-xs-center"
      type="error"
    >
      {{ $t('registration.rules.username.taken') }}
    </v-alert>

    <v-text-field
      v-model="input"
      :placeholder="placeholder"
      :label="label"
      :rules="rules"
      type="text"
      append-outer-icon="send"
      required
      autofocus
      @click:append-outer="submitName"
      @keyup.enter="submitName"
    />

  </div>
</template>

<style scoped>
</style>
<script>
import USERNAME_TAKEN from '../_queries/UsernameTaken.graphql';

/**
 * @vue-data {String} input - The user's current input in the local text field.
 * @vue-data {String} title - Displayed above the text field.
 * @vue-data {String} placeholder - Grey text displayed in an empty text field.
 * @vue-data {String} label - text above input field to describe its context.
 * @vue-data {Boolean} isTaken - An error message is shown when true.
 * @vue-data {Array} rules - An array of functions each having a single `input` argument and.
 *                          return true when rule is passed or a String of the error message if not.
 */
export default {

  data() {
    return {
      input: '',
      title: this.$t('registration.username'),
      placeholder: this.$t('registration.placeholder.username'),
      label: this.$t('registration.label.username'),
      isTaken: false,
      rules: [
        input => !!input || this.$t('registration.rules.username.required'),
        input => (input && input.length <= 20) || this.$t('registration.rules.username.maxLength'),
        input => (input && input.length >= 4) || this.$t('registration.rules.username.minLength'),
        input => (input && !/\s/g.test(input)) || this.$t('registration.rules.username.whitespace'),
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
    submitName() {
      if (this.valid) {
        // Check if username taken
        this.$apollo.query({
          query: USERNAME_TAKEN,
          variables: {
            username: this.input,
          },
        }).then(({ data }) => {
          // Continue
          if (!data.usernameTaken) {
            this.$emit('next', this.input);
            // Show error
          } else {
            this.isTaken = true;
            setTimeout(() => { this.isTaken = false; }, 1500);
          }
        });
      }
    },
  },
};
</script>
