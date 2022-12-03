// 此vm参数为页面的实例，可以通过它引用vuex中的变量
module.exports = (vm) => {
  // 初始化请求配置
  uni.$u.http.setConfig((config) => {
    /* config 为默认全局配置*/
    config.baseURL = 'https://businesstest.supervision.bluefire.top'; /* 测试 */
    // config.baseURL = 'https://businessdev.supervision.bluefire.top'; /* 开发 */
    // config.baseURL = 'http://192.168.1.56:8106'; /* 联调 */  //66
    // config.baseURL = 'https://business.supervision.bluefire.top'; //生产
    // config.baseURL = 'http://192.168.3.62:8106'; //me/
    // config.baseURL = 'http://192.168.3.14:8106'; //fcq
	// config.baseURL = 'http://business.vaiwan.com'; //wwx
    return config
  })

  // 请求拦截
  uni.$u.http.interceptors.request.use((config) => {
    urlPath = config.url
    // 可使用async await 做异步操作
    // 初始化请求拦截器时，会执行此方法，此时data为undefined，赋予默认{}
    config.data = config.data || {}
    if (!urlPath.includes("/code") && !urlPath.includes("/phone/login")) {
      let token = uni.getStorageSync('token');
      // 根据custom参数中配置的是否需要token，添加对应的请求头
      config.header.token = "Bearer " + token
    }
    return config
  }, config => { // 可使用async await 做异步操作
    return Promise.reject(config)
  })

  // 响应拦截
  uni.$u.http.interceptors.response.use((response) => {
    /* 对响应成功做点什么 可使用async await 做异步操作*/
    const data = response.data
	console.log(data,"data")
    if (data.status == "401") {
      uni.showModal({
        title: "",
        showCancel: false,
        confirmColor: "#03A9F4",
        content: "当前登录过期，请重新登录。",
        success: function (res) {
          if (res.confirm) {
            uni.redirectTo({
              url: "/pages/login/login",
            });
            return Promise.reject(data)
          }
        },
      });

      // uni.showToast({
      //   title: "当前登录状态过期!",
      //   duration: 3000,
      //   icon: "error"
      // });
      // setTimeout(() => {
      //   uni.redirectTo({
      //     url: "/pages/login/login",
      //   });
      //   return Promise.reject(data)
      // }, 3000)
    }
    if (data.status !== "200" && data.status !== "401") {
      uni.$u.toast(data.message)
      return Promise.reject(data)
    }
    return data === undefined ? {} : data
  }, (response) => {
    // 对响应错误做点什么 （statusCode !== 200）
    uni.$u.toast("网络请求超时，请重新加载！")
    return Promise.reject(response)
  })
}