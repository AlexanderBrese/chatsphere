/* eslint-disable no-console */
import runtime from 'serviceworker-webpack-plugin/lib/runtime';

function runSW() {
  // Check if desktop notifications are supported
  if (!('showNotification' in ServiceWorkerRegistration.prototype)) {
    console.warn('Notifications aren\'t supported.');
    return;
  }

  // Check if user has disabled notifications
  Notification.requestPermission().then(permission => {
    if (permission === 'denied') {
      console.warn('The user has blocked notifications.');
    }
  });

  if (!('serviceWorker' in navigator)) {
    console.warn('Service Worker API not supported');
    return;
  }

  runtime.register().catch((e) => console.error('Error during service worker registration:', e));
}

export default runSW;
