<!--
  Basic Form: Username and password textfield.

  Does basic validation. Emits 'submitted' event
  to parent when form inputs are OK.
-->
<template>
  <v-form
    ref="form"
    lazy-validation>
    <v-text-field
      v-model="name"
      :rules="nameRules"
      :placeholder="this.$t('login.username')"
      class="mt-0"
      append-icon="person"
      required
      @keyup.enter="$emit('onValidate')"
    />
    <v-text-field
      v-model="password"
      :rules="passwordRules"
      :placeholder="this.$t('login.password')"
      class="mt-0"
      append-icon="lock"
      type="password"
      required
      @keyup.enter="$emit('onValidate')"
    />
  </v-form>
</template>

<style scoped>

</style>
<script>

/**
 * @vue-data {String} name - bound to username textfield
 * @vue-data {String} password - bound to password textfield
 * @vue-data {Array} nameRules - a single rule inside the array: requires a non-empty textfield
 * @vue-data {Array} passwordRules - a single rule inside the array: requires a non-empty textfield
 */
export default {

  data() {
    return {
      name: '',
      nameRules: [
        v => !!v || this.$t('login.rules.username'),
      ],
      password: '',
      passwordRules: [
        v => !!v || this.$t('login.rules.password'),
      ],
    };
  },
};
</script>
