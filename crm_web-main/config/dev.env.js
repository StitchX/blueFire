// 'use strict'
// const merge = require('webpack-merge')
// const prodEnv = require('./prod.env')

// module.exports = merge(prodEnv, {
//   NODE_ENV: '"development"'
// })
'use strict'
const merge = require('webpack-merge')
const prodEnv = require('./prod.env')

// 获取NODE_ENV参数
const env = process.env.NODE_ENV

module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
  USER_APP: null,
  ENV_CONFIG: '"wacai"',
  PACK_PATH:'"../dida"',
  GET_BACK:'"https%3a%2f%2fapp.bluefire.top%2fyoumi%2f%23%2fresult"',
  // API_ROOT: env === 'development' ? '"http://app.bluefire.top:8020"' : '"/api"' // dev环境配制了服务代理，API_ROOT的api是配制的代理地址
  API_ROOT: env === 'development' ? '"http://139.186.180.210:8102"' : '"/api"' // dev环境配制了服务代理，API_ROOT的api是配制的代理地址
})
