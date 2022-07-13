// http://eslint.org/docs/user-guide/configuring

module.exports = {
  root: true,
  parserOptions: {
    parser: 'babel-eslint',
    sourceType: 'module'
  },
  env: {
    browser: true,
  },

  // https://github.com/airbnb/javascript
  extends: [
    'airbnb-base',
    'plugin:vue/recommended'
  ],

  // required to lint *.vue files
  plugins: [
    'vue',
    'graphql'
  ],

  // add your custom rules here
  rules: {
    // allow paren-less arrow functions
    'arrow-parens': 0,
    // allow async-await
    'generator-star-spacing': 0,
    // allow debugger during development
    'no-debugger': process.env.NODE_ENV === 'production' ? 2 : 0,
    'import/extensions': ['error', 'always', {
      js: 'never',
      mjs: 'never',
      jsx: 'never',
      ts: 'never',
      tsx: 'never',
      vue: 'never'
    }]
  },
  settings: {
    'import/resolver': {
      webpack: {
        'config': 'build/webpack.base.conf.js'
      }
    },
    'import/extensions': ['.js','.mjs','.jsx','.ts','.tsx','.vue']
  }
};
