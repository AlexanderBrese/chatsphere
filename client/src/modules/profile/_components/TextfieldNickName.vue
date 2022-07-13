<template>
  <div id="textfield">
    <!-- success alert shown above textfield -->
    <v-alert
      :value= "success"
      type= "success"
      transition= "scale-transition"
    >
      {{ this.$t('profileOverview.success') }}
    </v-alert>

    <!-- error alert shown above textfield -->
    <v-alert
      :value= "error"
      type= "error"
      transition= "scale-transition"
    >
      {{ this.$t('profileOverview.error') }}
    </v-alert>

    <v-text-field
      ref= "nicknameField"
      v-model= "nickname"
      :rules= "rules"
      :placeholder= "placeholder"
      :label= "label"
      :hint= "hint"
      type= "text"
      dark
      color="white"
      @keyup.enter= "loseFocus()"
      @click= "clearInput"
      @blur= "submit"
    />
  </div>
</template>
<script>
/**
   * @vue-prop {Object} user - User or Contact Object.
   * @vue-data {String} nickname - nickname of User/Input value of textfield .
   * @vue-data {String} nicknameSaved - Saves input (nickname) before user changes input.
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
  name: 'TextfieldNickName',

  props: {
    user: {
      type: Object,
      default() {
        return {};
      },
    },
  },

  data() {
    return {
      nickname: this.user.nickname,
      nicknameSaved: '',
      placeholder: '',
      label: this.$t('profile.nickname.nickname'),
      hint: this.$t('profile.nickname.hint'),
      success: false,
      error: false,
      focused: false,
      rules: [
        input => !!input || this.$t('profile.rules.required'),
        input => (input && input.length <= 20) || this.$t('profile.rules.length'),
      ],
    };
  },

  methods: {
    /**
     * Delete input and set old input as placeholder, if textfield is not focused before.
     */
    clearInput() {
      if (!this.focused) {
        this.label = this.$t('profile.newNickname');
        this.nicknameSaved = this.nickname;
      }

      this.focused = true;
    },

    /**
     * Triggers blur event on v-text-field
     * ToDo: Refactoring
     */
    loseFocus() {
      this.$refs.nicknameField.$refs.input.blur();
    },

    /**
     * Checks data for validation and submit data.
     */
    submit() {
      this.focused = false;
      const val = this.rules.every(rule => typeof rule(this.nickname) !== 'string');
      if (val) {
        if (this.nickname !== this.nicknameSaved) {
          this.success = true;
          setTimeout(() => {
            this.success = false;
          }, 1500);

          this.$emit('updateNickname', this.nickname);
        }
      } else {
        if (this.nickname !== '') {
          this.error = true;
          setTimeout(() => {
            this.error = false;
          }, 1500);
        }

        this.nickname = this.nicknameSaved;
      }
      this.label = this.$t('profile.nickname.nickname');
    },
  },
};
</script>
