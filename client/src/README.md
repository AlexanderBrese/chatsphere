# Welcome to the client side

To generate JSDoc documentation run:
```
yarn run docs
```
And open `client/docs/index.html` in your browser.

# Sample component documentation style
```vue
<!--
This component is for demonstration purposes only.

Using JSDoc to generate pretty documentation via `yarn run docs`
in the client/docs/ folder
-->
<template>
  <v-container fluid>Test</v-container>
</template>

<style scoped>

</style>
<script>
/**
 * @vue-prop {Number} sampleProp - sample property explanation
 * @vue-prop {Number} sampleDefault=1 - sample property with default value
 * @vue-prop {String} [sampleUnrequired] - sample property that is not required
 * @vue-data {Number} sampleData - sample data attribute explanation
 * @vue-computed {String} sampleComputed
 */
export default {
  props: {
    sampleProp: {
      type: Number,
      required: true,
    },
    sampleDefault: {
      type: Number,
      default: 1,
    },
    sampleUnrequired: {
      type: String,
      default: 'Optional',
    },
  },
  data() {
    return {
      sampleData: 0,
    };
  },
  computed: {
    sampleComputed() {
      return `Current value is ${this.sampleData}`;
    },
  },

  methods: {
    /**
     * Sample function
     *
     * @param {Number} arg1 - sample param
     * @param {Number} arg2 - sample param
     * @param {Number} arg3 - sample param
     */
    sampleFunction(arg1, arg2, arg3) {
      return arg1 + arg2 + arg3;
    },
  },
};
</script>
```

# General Style Guide:

https://github.com/pablohpsilva/vuejs-component-style-guide