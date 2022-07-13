<template>
  <v-layout>
    <v-flex>
      <!-- success alert shown under textfield -->
      <v-alert
        :value= "success"
        type= "success"
        transition ="scale-transition"
      >
        {{ this.$t('profileSettings.phone.success') }}
      </v-alert>

      <!-- error alert shown under textfield -->
      <v-alert
        :value= "error"
        type= "error"
        transition= "scale-transition"
      >
        {{ this.$t('profileSettings.phone.error') }}
      </v-alert>

      <v-text-field
        ref= "phoneField"
        v-model= "phone"
        :rules= "rules"
        :placeholder= "placeholder"
        :label= "label"
        :hint= "hint"
        type= "text"
        @keyup.enter= "loseFocus()"
        @blur= "submit"
      />
    </v-flex>
  </v-layout>
</template>

<script>

/**
  * @vue-data {String} phone - Phone Number of User/Input value of textfield.
  * @vue-data {String} phoneSaved - Saves input (phone)before user changes input.
  * @vue-data {String} placeholder - Sets the input’s placeholder text.
  * @vue-data {String} label - Sets input label.
  * @vue-data {String} hint - Sets hint text shown under textfield.
  * @vue-data {Boolean} success - Returns true if submit of data succeed.
  * @vue-data {Boolean} error - Returns true if submit of data failed.
  * @vue-data {Boolean} focused - Returns true if textfield is focused.
  * @vue-data {Array} rules - Array of functions that return either True or a String with
    an error message. Used for validation of textfield.
  */
export default {
  name: 'TextfieldPhone',

  props: {
    account: {
      type: Object,
      default() {
        return {};
      },
    },
  },

  data() {
    return {
      phone: this.account.user.phone,
      phoneSaved: '',
      placeholder: this.$t('profileSettings.phone.none'),
      label: this.$t('profileSettings.phone.phone'),
      hint: this.$t('profileSettings.phone.hint'),
      success: false,
      error: false,
      focused: false,
      rules: [
        input => (this.validPhoneNumber(input)) || this.$t('profileSettings.phone.rules.valid'),
      ],
    };
  },

  watch: {
    'account.user.phone': function phone() {
      if (this.phone !== this.account.user.phone) {
        this.phone = this.account.user.phone;
        this.phoneSaved = this.account.user.phone;
      }
    },
  },

  methods: {
    /**
     * https://stackoverflow.com/questions/4338267/validate-phone-number-with-javascript
     */
    validPhoneNumber(input) {
      if (input === '') {
        return true;
      }

      const re = /^[+]*[(]{0,1}[0-9]{1,3}[)]{0,1}[-\s/0-9]*$/;
      return re.test(input);
    },

    /**
     * Triggers blur event on v-text-field
     * ToDo: Refactoring
     */
    loseFocus() {
      this.$refs.phoneField.$refs.input.blur();
    },

    /**
     * Checks data for validation and submit data.
     */
    submit() {
      const val = this.rules.every(rule => typeof rule(this.phone) !== 'string');
      if (val) {
        if (this.phone !== this.phoneSaved) {
          this.success = true;
          setTimeout(() => {
            this.success = false;
          }, 1500);

          this.$emit('submit', this.phone);

          if (this.phone === '') {
            this.placeholder = this.$t('profileSettings.phone.none');
          }
        }
        this.phoneSaved = this.phone;
      } else {
        if (this.phone !== '') {
          this.error = true;
          setTimeout(() => {
            this.error = false;
          }, 1500);
        }

        this.phone = this.phoneSaved;
      }

      this.label = this.$t('profileSettings.phone.phone');
    },
  },
};
</script>
