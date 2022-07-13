<!--
  A single message.

  ChatLine also contains the "Content" Component
  as was specified in the Entwurfsdokument.
-->
<template>
  <div
    :class="{'csgrey': alignRight, 'black--text': alignRight, 'white--text': !alignRight}"
    class="message__content pa-3 third">
    {{ value }}
  </div>
</template>

<style scoped>
  .message__content {
    border-radius: 15px;
    min-height: 50px;
    -ms-word-break: break-all;
    word-break: break-all;
    /* Non standard for WebKit */
    word-break: break-word;
    -webkit-hyphens: auto;
    -moz-hyphens: auto;
    hyphens: auto;
    align-items: center;
    display: inline-flex;
    cursor: pointer;
   }
</style>

<script>
/**
 * @vue-prop {Boolean} alignLeft - Foreign, received messages are aligned to the left.
 * @vue-prop {String} text - actual text of the message.
 * @vue-prop {Boolean} time - Timestamp of a sent message.
 * @vue-prop {Boolean} isRead - Whether your own sent message have been read.
 */
export default {
  props: {
    alignRight: {
      type: Boolean,
      required: true,
    },

    value: {
      type: String,
      required: true,
    },

    time: {
      type: Date,
      required: true,
    },

    isRead: {
      type: Boolean,
      required: true,
    },
    avatarUrl: {
      type: String,
      required: true,
    },

  },

  data() {
    return {
    };
  },

  computed: {

  },

  methods: {

  },
};
</script>
