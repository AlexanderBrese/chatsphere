<template>
  <!-- Carry on the authenticated event received from index.vue of
  registration or login on to App.vue -->
  <router-view @authenticated="$emit('authenticated', $event)"/>
</template>

<script>
export default {
  name: 'Auth',
};
</script>

<style scoped>

</style>
