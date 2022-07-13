/* eslint-disable no-restricted-globals */
/**
 * Force activate immediately
 */
self.addEventListener('install', event => {
  event.waitUntil(self.skipWaiting());
});

/**
 * Do not wait until refresh to activate,
 * force Service Worker to be available immediately for
 * communication
 */
self.addEventListener('activate', event => {
  event.waitUntil(self.clients.claim());
});

/**
 * Notifications can be called by the service worker
 * but currently they are not done so - because
 * firefox gets problematic with chrome's postMessage
 */
self.addEventListener('message', event => {
  const title = 'You have a new message!';
  const body = event.data;
  const icon = '/static/img/icons/android-chrome-192x192.png';
  const tag = 'chatsphere-notification';

  self.registration.showNotification(title, {
    body,
    icon,
    tag,
  });
});
