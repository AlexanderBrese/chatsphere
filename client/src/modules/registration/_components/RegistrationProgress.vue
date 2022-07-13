<!--
  Between the carousel and the input form:
  A progress bar to display the current progress in the registration.
-->
<template>
  <v-stepper-header>
    <v-stepper-step
      v-for="(title,i) in titles"
      :key="i"
      :step="i + 1 "
      :complete="count > i + 1"> <!-- count starts from 1 instead of 0, but i starts at 0 -->
    </v-stepper-step>
  </v-stepper-header>
</template>

<style scoped>

</style>

<script>
/**
 * @vue-prop {Number} count - Required to tick off and show completed steps.
 * @vue-data {Array} titles - An array of titles. Required to display a text to each step.
 */
export default {
  props: {
    count: {
      type: Number,
      required: true,
    },
  },

  data() {
    return {
      titles: [
        this.$t('registration.username'),
        this.$t('registration.password'),
        this.$t('registration.confirm'),
        this.$t('registration.email'),
      ],
    };
  },
};
</script>
