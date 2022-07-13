<!--
  Loading screen
-->
<template>
  <div>
    <div
      v-show="!doneLoading"
      class="pa-3">
      <!--
        On a full revolution (progress=100) wait a small fraction
        before displaying further information
      -->
      <v-progress-linear
        :width="3"
        indeterminate
        color="primary"
      />


    </div>
    <v-alert
      :value="error !== ''"
      class="ma-0 text-xs-center"
      type="error"
      transition="scale-transition"
      origin="center center"
    >
      {{ error }}
    </v-alert>
  </div>

</template>

<style scoped>
</style>
<script>

/**
* @vue-prop {Number} progress - required to display a loading bar
* @vue-prop {Number} error - if error is true an appropriate message will be displayed
*/
export default {

  props: {
    doneLoading: {
      type: Boolean,
      required: true,
    },
    error: {
      type: String,
      default: '',
    },
  },
};
</script>
