<!--
  View to login. Inputted data will
  be validated on the client side and sent to the server afterwards.
  Can navigate to the registration process.
-->
<template>
  <div id="login">
    <LoginHeader/>
    <div class="pa-4">
      <!-- Form part -->
      <LoginForm
        ref="loginForm"
        @onValidate="validate"/>
    </div>
    <v-footer
      :absolute="$vuetify.breakpoint.name !== 'xs'"
      :fixed="$vuetify.breakpoint.name === 'xs'"
      app>
      <v-layout>
        <v-flex>
          <v-btn
            class="no-border ma-0"
            color="secondary"
            block
            depressed
            large
            dark
            @click.native="validate">
            {{ $t('login.signin') | uppercase }}</v-btn>
        </v-flex>
        <v-flex>
          <v-btn
            :to="{name: 'Registration'}"
            class="no-border ma-0"
            color="third"
            depressed
            large
            block
            dark>
            {{ $t('login.signup') | uppercase }}</v-btn>
        </v-flex>
      </v-layout>
    </v-footer>
    <!-- Error alert -->
    <LoginError :error="error"/>
    <div
      v-show="loading"
      class="loading">
      <v-progress-circular
        :width="3"
        :size="80"
        class="loading-indicator"
        indeterminate
        color="blank"
      />
    </div>
  </div>
</template>

<style scoped>
  #login {
    background: white;
    height: 100%;
    position: relative;
  }
  .loading {
    width: 100%;
    height: 100%;
    position: absolute;
    top: 0;
    bottom: 0;
    left: 0;
    right: 0;
    background-color: rgba(0, 0, 0, 0.7);
  }
  .loading-indicator {
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    z-index: 2;
  }
</style>
<script>
import localforage from 'localforage';
import LOGIN from './_queries/Login.graphql';
import LoginHeader from './_components/LoginHeader';
import LoginForm from './_components/LoginForm';
import LoginError from './_components/LoginError';

/**
 * @vue-data {Boolean} error - Bound to LoginError: Uncovers the error when true
 */
export default {
  components: {
    LoginHeader,
    LoginForm,
    LoginError,
  },

  data() {
    return {
      error: '',
      loading: false,
    };
  },

  methods: {

    /**
     * Checks whether the complete form passes all rules
     * (validate() runs all rules one last time to test)
     *
     * Notifies parent afterwards to trigger further action.
     */
    validate() {
      // eslint-disable-next-line prefer-destructuring
      const loginForm = this.$refs.loginForm;
      if (loginForm.$refs.form.validate()) {
        this.submitLogin(loginForm.name, loginForm.password);
      }
    },

    /**
     * Step 1:
     * Send data to server
     *
     * @returns {Boolean} login successful
     */
    submitLogin(username, password) {
      this.loading = true;
      this.$apollo.query({
        query: LOGIN,
        variables: {
          loginInput: {
            username,
            password,
          },
        },
      })
        .then(this.onSuccess)
        .catch(this.onError);
    },

    /**
     * Step 2:
     * Act according to server response
     *
     * @arg {Object} data - contains `login` field that yields true when the login succeedded
     */
    onSuccess({ data }) {
      localforage.setItem('token', data.login.token)
        .then(() => {
          this.$emit('authenticated', data.login.token); // Sent to Auth.vue

          this.loading = false;
          this.$router.push({ name: 'Chat' });
        });
    },

    /**
     * Step 3 (on error):
     * Display alert above form for specific amount of time.
     *
     * To expose detailed logs - for debugging pruposes - uncomment the
     * error argument and console.log as desired.
     */
    onError(error) {
      this.error = error.message;
      this.loading = false;
      setTimeout(() => { this.error = ''; }, 1500);
    },
  },
};
</script>
