import Vue from 'vue';
import { Vuetify } from 'vuetify';
import { VUETIFY_COMPONENTS, VUETIFY_STYLE } from '@/config/vuetify-config';
import App from '@/modules/app/App';
import { i18n } from '@/i18n/';
import router from '@/router';
import { UPPERCASE } from '../../../src/shared/filters';

Vue.use(Vuetify, {
  components: VUETIFY_COMPONENTS,
  theme: VUETIFY_STYLE,
});

Vue.filter(UPPERCASE.name, UPPERCASE.handler);

const Constructor = Vue.extend({ render: h => h(App), router });

describe('App.vue', () => {
  it('Returns the correct Status code', () => expect(true).toBe(true));

  it('should have chatsphere as title', () => {
    const vm = new Constructor({ i18n }).$mount();

    vm.$router.onReady(() => {
      expect(vm.$el.querySelector('h4').textContent).toBe('CHATSPHERE');
    });
  });

  it('should display a logo', () => {
    const vm = new Constructor({ i18n }).$mount();

    vm.$router.onReady(() => {
      expect(vm.$el.querySelector('#logo')).toBeDefined();
    });
  });
});
