import { Vuetify } from 'vuetify';

import { ApolloClient } from 'apollo-client';
import { InMemoryCache } from 'apollo-cache-inmemory';
import { SubscriptionClient } from 'subscriptions-transport-ws';
import { WebSocketLink } from 'apollo-link-ws';
import VueApollo from 'vue-apollo';
import Vue from 'vue';
import localforage from 'localforage';
import 'material-design-icons-iconfont/dist/material-design-icons.css';
import '@mdi/font/css/materialdesignicons.css';
import App from './modules/app/App';
import router from './router';
import './shared/stylus/main.styl';
import { i18n } from './i18n/';
import { ABBREVIATE, CAPITALIZE, UPPERCASE } from './shared/filters';
import { VUETIFY_COMPONENTS, VUETIFY_STYLE } from './config/vuetify-config';

import '../static/fonts/fonts.css';

localforage.config({
  name: 'chatsphere',
});


Vue.use(Vuetify, {
  components: VUETIFY_COMPONENTS,
  theme: VUETIFY_STYLE,
  iconfont: 'mdi',
});
// Install the vue plugin
Vue.use(VueApollo);

Vue.use(require('vue-moment'));

Vue.config.productionTip = false;

const moment = require('moment');
require('moment/locale/de');

Vue.use(require('vue-moment'), {
  moment,
});

// Filter Definitions
Vue.filter(CAPITALIZE.name, CAPITALIZE.handler);
Vue.filter(ABBREVIATE.name, ABBREVIATE.handler);
Vue.filter(UPPERCASE.name, UPPERCASE.handler);

const GRAPHQL_ENDPOINT = document.domain === 'chatsphere.de' ?
  'wss://api.chatsphere.de/graphql' : 'ws://localhost:6060/graphql';


localforage.getItem('token').then(token => {
  const client = new SubscriptionClient(GRAPHQL_ENDPOINT, {
    reconnect: true,
    connectionCallback: ((error) => {
      if (error) {
        localforage.setItem('token', null);
      }
    }),
    connectionParams: {
      token,
    },
  }, WebSocket);


  const link = new WebSocketLink(client);

  // Create the apollo client
  const apolloClient = new ApolloClient({
    link,
    cache: new InMemoryCache(),
    connectToDevTools: true,
  });

  const apolloProvider = new VueApollo({
    defaultClient: apolloClient,
  });

  /* eslint-disable no-new */
  new Vue({
    el: '#app',
    i18n,
    router,
    apolloProvider,
    render: h => h(App),
  });
});
