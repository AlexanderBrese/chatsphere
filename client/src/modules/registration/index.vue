<!--
  View of registration of user accounts. Each step of the process
  is contained in here.
-->
<template>
  <div class="register">
    <v-icon
      class="back"
      color="blank"
      @click.stop="back()">arrow_back
    </v-icon>
    <v-layout
      column
      fill-height>
      <v-flex fill-height>
        <RegistrationCarousel
          :count="count < 5 ? count : 4"
        />
      </v-flex>
      <v-flex>
        <v-stepper
          v-model="count"
          class="stepper py-2">
          <!--
            Each Step of the registration process
          -->
          <v-stepper-items>
            <v-stepper-content
              class="px-3 pb-1 pt-3"
              step="1">
              <NameView
                @next="next"
              />
            </v-stepper-content>
            <v-stepper-content
              class="px-3 pb-1 pt-3"
              step="2">
              <PasswordView
                @next="next"
              />
            </v-stepper-content>
            <v-stepper-content
              class="px-3 pb-1 pt-3"
              step="3">
              <ConfirmView
                :password="submitted[1]"
                @next="next"
              />
            </v-stepper-content>
            <v-stepper-content
              class="px-3 pb-1 pt-3"
              step="4">
              <EmailView
                @next="submitRegistration"
              />
            </v-stepper-content>
            <v-stepper-content
              class="pa-0"
              step="5">
              <SuccessView
                :done-loading="doneLoading"
                :error="error"
              />
            </v-stepper-content>
          </v-stepper-items>
        </v-stepper>
      </v-flex>
    </v-layout>
  </div>
</template>

<style scoped>
    .back {
        position: absolute;
        top: 16px;
        z-index: 2;
        left: 16px;
        font-size: 30px;
    }
  .register {
    height: 100%;
    background: white;
  }
  .footer{
    height: initial !important;
  }
  .stepper {
  }
</style>
<script>
import localforage from 'localforage';

import CREATE_ACCOUNT from './_queries/CreateAccount.graphql';
import RegistrationProgress from './_components/RegistrationProgress';
import RegistrationCarousel from './_components/RegistrationCarousel';
import NameView from './_components/NameView';
import PasswordView from './_components/PasswordView';
import ConfirmView from './_components/ConfirmView';
import EmailView from './_components/EmailView';
import SuccessView from './_components/SuccessView';

/**
 * @vue-data {Number} count - Counts each completed step in the registration.
 *                            Note: Starting step from "0" will cause type errors with V-Stepper
 *                            because 0 is evaluated as String "0"
 * @vue-data {Array} submitted - Stores all currently inputted data:
 *                             * [0]: username
 *                             * [1]: password
 *                             * [2]: same password
 *                             * [3]: email
 * @vue-data {Number} progress - Artifical timer that reaches 100 before further actions.
 * @vue-data {Number} interval - Number representing the ID value of the timer
 * @vue-data {Boolean} error - true on any graphql errors
 */
export default {

  components: {
    RegistrationProgress,
    RegistrationCarousel,
    NameView,
    PasswordView,
    ConfirmView,
    EmailView,
    SuccessView,
  },

  data() {
    return {
      count: 1,
      submitted: [],
      doneLoading: false,
      interval: 0,
      error: '',
    };
  },

  methods: {

    /**
     * Store inputs and continue.
     */
    next(input) {
      this.submitted.push(input);
      this.count += 1;
    },

    /**
     * Remove previous input and go back a step
     */
    back() {
      if (this.count === 1) {
        this.$router.back();
      }
      this.submitted.pop();
      this.count -= 1;
    },

    /**
     * Send data to server
     */
    submitRegistration(input) {
      this.next(input);
      // Put artifical waiting time (prevents mass account registration)
      this.$apollo.mutate({
        mutation: CREATE_ACCOUNT,
        variables: {
          createAccountInput: {
            username: this.submitted[0],
            password: this.submitted[1],
            email: this.submitted[3],
          },
        },
      })
        .then(this.register)
        .catch(this.displayError);
    },

    register({ data }) {
      this.doneLoading = true;
      localforage.setItem('token', data.createAccount.token).then(() => {
        this.$emit('authenticated', data.createAccount.token); // Sent to Auth.vue
        this.$router.push({ name: 'Chat' });
      });
    },

    displayError(error) {
      this.doneLoading = true;
      this.error = error.message;
      setTimeout(() => {
        setTimeout(() => {
          this.doneLoading = false;
          this.error = '';
        }, 800);
        this.back();
      }, 1500);
    },
  },
};
</script>
