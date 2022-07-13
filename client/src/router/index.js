import Vue from 'vue';
import Router from 'vue-router';
import ROUTELIST from './routes';

Vue.use(Router);

export default new Router({
  routes: ROUTELIST,
  mode: 'history',
});
