<!--
  Displays an alert to explain the cause of error.
-->
<template>
  <v-alert
    :value= "error !== ''"
    class="ma-0 text-xs-center alert"
    type= "error"
    origin="center center"
    transition= "scale-transition"
  >
    {{ error }}
  </v-alert>
</template>

<style scoped>
  .alert {
    position: absolute;
    z-index: 2;
    width: 100%;
    top: 0;
  }
</style>
<script>

/**
 * @vue-prop {Boolean} error - whether error is visible or not
 */
export default {

  props: {
    error: {
      type: String,
      required: true,
    },
  },
};
</script>
