import Vue from 'vue';
import VueI18n from 'vue-i18n';
import messages from '@/i18n/lang/de';

Vue.use(VueI18n);

export const i18n = new VueI18n({
  locale: 'de',
  fallbackLocale: 'en',
  messages: {
    de: messages,
  },
});

// our default language that is preloaded
const loadedLanguages = ['de'];

function setI18nLanguage(lang) {
  i18n.locale = lang;
  document.querySelector('html').setAttribute('lang', lang);
  return lang;
}
/**
 * Instead of i18n.locale = '...' use this function to load the language
 * asynchronously.
 *
 * @export function
 * @param {String} lang language to change to (e.g. 'de', 'en')
 */
export function loadLanguageAsync(lang) {
  if (i18n.locale !== lang) {
    if (!loadedLanguages.includes(lang)) {
      return import(/* webpackChunkName: "lang-[request]" */ `@/i18n/lang/${lang}`).then(msgs => {
        i18n.setLocaleMessage(lang, msgs.default);
        loadedLanguages.push(lang);
        return setI18nLanguage(lang);
      });
    }
    return Promise.resolve(setI18nLanguage(lang));
  }
  return Promise.resolve(lang);
}
