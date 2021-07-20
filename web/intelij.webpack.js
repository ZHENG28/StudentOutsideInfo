// This configuration file is not used anywhere in the code, it's a hack to handle InteliJ relative path imports
// Keep in sync with actual webpack aliases

const path = require('path');
var webpack = require("webpack");

module.exports = {
  resolve: {
    alias: {
      '@': path.resolve(__dirname, 'src')
    }
  },
  plugins: [
    new webpack.optimize.CommonsChunkPlugin('common.js'),
    new webpack.ProvidePlugin({
      jQuery: "jquery",
      $: "jquery"
    })],
};
