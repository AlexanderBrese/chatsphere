import Vue from 'vue';

const LongPress = {
  bind(el, binding) {
    // Define variable
    let pressTimer = null;
    let pressed = false;
    // Run Function
    const handler = () => {
      binding.value.method(binding.value.arg, true);
    };


    // Cancel Timeout
    const cancel = (e) => {
      e.preventDefault();
      e.stopPropagation();
      // Check if timer has a value or not
      if (pressTimer !== null) {
        clearTimeout(pressTimer);
        pressTimer = null;
      }
      if (!pressed) {
        binding.value.method(binding.value.arg, false);
      }
    };

    // Define funtion handlers
    // Create timeout ( run function after 300ms )
    const start = (e) => {
      pressed = false;
      e.preventDefault();
      e.stopPropagation();
      if (e.type === 'click' && e.button !== 0) {
        return;
      }
      if (pressTimer === null) {
        pressTimer = setTimeout(() => {
          // Run function
          handler();
          pressed = true;
        }, 500);
      }
    };

    // Add Event listeners
    el.addEventListener('mousedown', start);
    el.addEventListener('touchstart', start);
    // Cancel timeouts if this events happen
    el.addEventListener('click', cancel);
    el.addEventListener('touchend', cancel);
    el.addEventListener('touchcancel', cancel);
  },
};

export default LongPress;

Vue.directive('longpress', LongPress);
