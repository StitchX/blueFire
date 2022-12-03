'use strict'
// Template version: 1.3.1
// see http://vuejs-templates.github.io/webpack for documentation.

// const os = require('os')
// let localhost = ''
// try {
//   const network = os.networkInterfaces()
//   localhost = network[Object.keys(network)[0]][1].address
// } catch (e) {
//   localhost = 'localhost';
// }

const path = require('path')
let assetsPublicPath
// if(process.env.USER_APP == undefined){
//     assetsPublicPath = '/'
// }else{
//     assetsPublicPath = '/'+process.env.USER_APP       //   /meitu/        记得打版本号
// }
let index,pack
if(process.env.ENV_CONFIG == 'wacai'){
     assetsPublicPath = '/'
     index='../crm/index.html'
     pack='../crm'
}else{
    assetsPublicPath = '/'
    index='../dist/index.html'
    pack='../dist'
}
 
module.exports = {
    dev: {
        // indeEnv: require('./inde.env'),
        // Paths
        assetsSubDirectory: 'static',
        assetsPublicPath: assetsPublicPath,
        proxyTable: {
            '/api': {
              // target: 'https://crmtest.supervision.bluefire.top/api',
              // target: 'https://crmdev.supervision.bluefire.top/api',  //开发环境
              // target: 'http://139.186.176.126/api',  //测试环境
              // target: 'http://crmblue.vaiwan.com',
              // target: 'http://101.35.81.126:8102',  //cqfw
              // target: 'http://114.117.235.143:36231',
              target: 'http://127.0.0.1:8102',
              changeOrigin: true, // 是否允许跨域
              pathRewrite: {
                '^/api': '/' // 重写   // 开发和测试的时候给去掉/api    连后端本地加上/api
              }
            },
          },
        // Various Dev Server settings   localhost/   192.168.0.126
        // host: '192.168.1.20', // can be overwritten by process.env.HOST
        host: 'localhost', // can be overwritten by process.env.HOST
        port: 8080, // can be overwritten by process.env.PORT, if port is in use, a free one will be determined
        autoOpenBrowser: false,
        errorOverlay: true,
        notifyOnErrors: true,
        poll: false, // https://webpack.js.org/configuration/dev-server/#devserver-watchoptions-


        /**
         * Source Maps
         */

        // https://webpack.js.org/configuration/devtool/#development
        devtool: 'cheap-module-eval-source-map',

        // If you have problems debugging vue-files in devtools,
        // set this to false - it *may* help
        // https://vue-loader.vuejs.org/en/options.html#cachebusting
        cacheBusting: true,

        cssSourceMap: true
    },

    build: {
        devEnv: require('./dev.env'),
        meituEnv: require('./meitu.env'),
        wacaiEnv: require('./wacai.env'),
        prodEnv: require('./prod.env'),
        shansongEnv: require('./shansong.env'),
        zhongtongEnv:require('./zhongtong.env'),
        youmiEnv:require('./youmi.env'),
        didaEnv:require('./dida.env'),
        
        // Template for index.html
        index: path.resolve(__dirname,index),
        

        // Paths
        assetsRoot: path.resolve(__dirname,pack),
        assetsSubDirectory: 'static_h5',
        assetsPublicPath: assetsPublicPath,

        /**
         * Source Maps
         */

        productionSourceMap: true,
        // https://webpack.js.org/configuration/devtool/#production
        devtool: '#source-map',

        // Gzip off by default as many popular static hosts such as
        // Surge or Netlify already gzip all static assets for you.
        // Before setting to `true`, make sure to:
        // npm install --save-dev compression-webpack-plugin
        productionGzip: false,
        productionGzipExtensions: ['js', 'css'],

        // Run the build command with an extra argument to
        // View the bundle analyzer report after build finishes:
        // `npm run build --report`
        // Set to `true` or `false` to always turn it on or off
        bundleAnalyzerReport: process.env.npm_config_report
    }
}
