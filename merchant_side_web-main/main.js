import App from './App'
import uView from '@/uni_modules/uview-ui'
import validate from '@/js_sdk/ys-validate.js'

// 挂载到全局
Vue.prototype.$validate = validate
Vue.use(uView)

// #ifndef VUE3
import Vue from 'vue'
Vue.config.productionTip = false
App.mpType = 'app'
const app = new Vue({
    ...App
})
// 引入请求封装，将app参数传递到配置中
require('./config/request')(app)
app.$mount()
// #endif
