'use strict'
const path = require('path')

module.exports = {

  dev: {
    assetsSubDirectory: 'static',
    assetsPublicPath: '/',
    proxyTable: {
      '/gov': {
        // target: 'http://124.223.84.170:8101',//后端接口地址
        // target: 'http://dev.fire.bluefire.top:8088/pre',//后端接口地址
        // target: 'http://192.168.1.56:8101',//后端接口地址
        target: 'http://127.0.0.1:8101',//后端接口地址
        changeOrigin: true,  //是否跨域
        pathRewrite: {
          '^/gov': '/',//重写,
        }
      }

    },

    // 本地开发使用
    host: '192.168.1.20',
    port: 9527,
    autoOpenBrowser: true,
    errorOverlay: true,
    notifyOnErrors: false,
    poll: false,
    useEslint: true,
    showEslintErrorsInOverlay: false,
    devtool: 'cheap-source-map',
    cssSourceMap: false
  },

  build: {
    index: path.resolve(__dirname, '../dist/index.html'),
    assetsRoot: path.resolve(__dirname, '../dist'),
    assetsSubDirectory: 'static',
    assetsPublicPath: '/',

    productionSourceMap: false,
    devtool: 'source-map',
    productionGzip: false,
    productionGzipExtensions: ['js', 'css'],
    bundleAnalyzerReport: process.env.npm_config_report || false,
    generateAnalyzerReport: process.env.npm_config_generate_report || false
  }
}
