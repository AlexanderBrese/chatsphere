/* eslint-disable global-require */
require('babel-register');
const config = require('../../config');

// http://nightwatchjs.org/gettingstarted#settings-file
module.exports = {
  src_folders: ['test/e2e/specs'],
  output_folder: 'test/e2e/reports',
  custom_assertions_path: ['test/e2e/custom-assertions'],

  selenium: {
    start_process: true,
    server_path: require('selenium-server').path,
    host: '127.0.0.1',
    port: 5555,
    cli_args: {
      'webdriver.chrome.driver': require('chromedriver').path,
      "webdriver.gecko.driver" : require('geckodriver').path
    },
  },

  test_settings: {
    default: {
      selenium_port: 5555,
      selenium_host: 'localhost',
      silent: true,
      globals: {
        devServerURL: `http://localhost:${process.env.PORT || config.dev.port}`,
      },
    },

    chrome: {
      desiredCapabilities: {
        browserName: 'chrome',
        javascriptEnabled: true,
        acceptSslCerts: true,
        "chromeOptions" : {
          // NOTE: Chrome requires some Workaround for Headless Mode!
          "args" : ["headless", "disable-gpu","window-size=1200x600"]
          // "binary": "/usr/bin/chrome"
        }
      },
    },

    firefox: {
      desiredCapabilities: {
        browserName: 'firefox',
        javascriptEnabled: true,
        acceptSslCerts: true,
        "moz:firefoxOptions" : {
          "args" : ["-headless"]
          // "bin": "/usr/bin/firefox"
        }
      },
    },
  },
};
