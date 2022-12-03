const { resolve } = require('path');
var webpack = require('webpack');
const Timestamp = new Date().getTime();
module.exports = {
  // outputDir: process.env.VUE_APP_DIST,
  // 放置生成的css和js和img和fonts的目录
  assetsDir: process.env.VUE_APP_STATIC,
  productionSourceMap:false,
  // filenameHashing: false,
  chainWebpack: config => {
    config.resolve.alias
      .set('@', resolve('src'))
      .set('assets', resolve('src/assets'))
      .set('components', resolve('src/components'))
      // if(process.env.NODE_ENV == "production"){
      // config.plugin('webpack-bundle-analyzer').use(require('webpack-bundle-analyzer').BundleAnalyzerPlugin)
      // }
      config.plugins.delete('prefetch')
  },
  configureWebpack: (config) => {
      //  externals:{
      //     vant:vant
      //  }
        // 开启分离js
        //   config.optimization = {
        //     runtimeChunk: 'single',
        //     splitChunks: {
        //         chunks: 'all',
        //         maxInitialRequests: Infinity,
        //         minSize: 1000 * 60,
        //         cacheGroups: {
        //             vendor: {
        //                 test: /[\\/]node_modules[\\/]/,
        //                 name(module) {
        //                     // 排除node_modules 然后吧 @ 替换为空 ,考虑到服务器的兼容
        //                     const packageName = module.context.match(/[\\/]node_modules[\\/](.*?)([\\/]|$)/)[1]
        //                     return `npm.${packageName.replace('@', '')}`
        //                 }
        //             }
        //         }
        //     }
        // };
          config.plugins.push(
              ...[
                  // 链接：https://juejin.cn/post/6844904105555525640
                  new webpack.ContextReplacementPlugin(
                      /moment[/\\]locale$/, // 这个参数表明了我们要改变的打包上下文
                      /zh-cn/ // 这个参数表示我们只想打包这个正则匹配的文件
                  )
              ]
          );
      config.mode = 'production';
      config["performance"] = {//打包文件大小配置
        "maxEntrypointSize": 10000000,
        "maxAssetSize": 30000000,
        assetFilter: function (assetFilename) {
          return assetFilename.endsWith('.js');
      }
      }
      // config.output.filename = `${process.env.VUE_APP_STATIC}/js/[name].${Timestamp}.js`
      //   config.output.chunkFilename = `${process.env.VUE_APP_STATIC}/js/[name].${Timestamp}.js`
    },
  devServer: {
    port: 8082,
    proxy: {
      "/consumer": {
        // target: "http://192.168.1.78:8102",
        // target: "https://credit.supervision.bluefire.top:555",
        target: "http://127.0.0.1:31003",
        // target: "http://crmdev.supervision.bluefire.top:31003",
        ws: true,
        changeOrigin: true,  //是否跨域
        // changeOrigin: true,
      },
    },
  },
};
