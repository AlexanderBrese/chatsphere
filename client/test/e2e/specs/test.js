// For authoring Nightwatch tests, see
// http://nightwatchjs.org/guide#usage
// automatically uses dev Server port from /config.index.js
// default: http://localhost:3020
// see nightwatch.conf.js

module.exports = {
  'Logo displayed in the span of 5 seconds': function (browser) { // eslint-disable-line func-names
    const devServer = browser.globals.devServerURL;

    browser
      .url(devServer)
      .waitForElementVisible('#app', 60000)
      .assert
      .elementPresent('#logo')
      .end();
  },
};
