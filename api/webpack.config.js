const webpack = require('webpack');
const path = require('path');
const nodeExternals = require('webpack-node-externals');
const StartServerPlugin = require('start-server-webpack-plugin');

module.exports = {
  entry: ['webpack/hot/poll?1000', './src/index'],
  watch: true,
  target: 'node',
  mode: 'development',
  node: {
    __filename: true,
    __dirname: true,
  },
  externals: [nodeExternals({
    whitelist: ['webpack/hot/poll?1000'],
  })],
  module: {
    rules: [
      {
        enforce: 'pre',
        test: /\.js$/,
        exclude: /node_modules|dist/,
        loader: 'eslint-loader',
      }, {
        test: /\.js?$/,
        use: [{
          loader: 'babel-loader',
          options: {
            babelrc: false,
            presets: [
              ['env', {
                modules: false,
              }], 'stage-0',
            ],
            plugins: ['transform-regenerator', 'transform-runtime'],
          },
        }],
        exclude: /node_modules|dist/,
      },
      {
        test: /\.graphqls?$/,
        use: [{
          loader: 'webpack-graphql-loader',
          options: {
            validate: false,
            removeUnusedFragments: false,
          },
        }],
      },
    ],
  },
  plugins: [
    new StartServerPlugin('server.js'),
    new webpack.NamedModulesPlugin(),
    new webpack.HotModuleReplacementPlugin(),
    new webpack.NoEmitOnErrorsPlugin(),
    new webpack.DefinePlugin({
      'process.env': {
        BUILD_TARGET: JSON.stringify('server'),
      },
    }),
  ],
  output: {
    path: path.join(__dirname, 'dist'),
    filename: 'server.js',
  },
};
