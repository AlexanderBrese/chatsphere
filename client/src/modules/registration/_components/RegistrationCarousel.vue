<!--
  On each registration step show a picture.
-->
<template>
  <v-carousel
    :value="count - 1"
    :cycle="false"
    class="carousel"
    hide-controls
  >
    <v-carousel-item
      v-for="(slide, index) in slides"
      :key="index"
      reverse-transition="fade"
      transition="fade"
    >
      <v-layout
        class="gradient-bg"
        fill-height
        align-center
        justify-center
        column>
        <v-icon
          color="blank"
          size="164px">{{ slide.icon }}</v-icon>
        <h1 class="display-1 white--text">{{ slide.name | uppercase }}</h1>
      </v-layout>
    </v-carousel-item>
  </v-carousel>
</template>

<style>
  .preview {
    height: 100% !important;
  }
  .carousel {
    height: 100% !important;
  }
  .carousel .v-carousel__controls {
    bottom: 24px;
    background: transparent !important;
  }
  .carousel .v-btn--icon.v-btn--small {
    margin: 0 !important;
  }
</style>
<script>
/**
 * @vue-prop {Number} count - Required to show transition to the next image of each step.
 * @vue-data {Array} images - Array of urls. Because count starts at 1, the 0th element is a filler.
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
      slides: [
        { icon: 'person', name: 'name' },
        { icon: 'https', name: 'password' },
        { icon: 'https', name: 'password' },
        { icon: 'email', name: 'email' },
      ],
    };
  },
};
</script>
