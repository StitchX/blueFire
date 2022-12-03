const http = uni.$u.http
// -------------------------------------登录相关接口------------------------------//

// 获取手机验证码
export const fetchCode = (params, config = {}) => http.post('/code', params, config)
// 登出
export const logout = (params, config = {}) => http.post('/login/out', params, config)
// 登录提交
export const login = (params, config = {}) => http.post(`/phone/login?phone=${params.phone}&smsCode=${params.smsCode}`, params, config)
// -------------------------------------登录相关接口------------------------------//



// -------------------------------------首页相关接口------------------------------//
// 获取首页商户信息
export const fetchMerchantData = (params, config = {}) => http.post('/merchant/indexInfo', params, config)
// 开通食品安全险
export const fetchFoodSafety = (params, config = {}) => http.post('/merchant/updateFoodSafetyStatus', params, config)
// 开通银行支付
export const fetchPay = (params, config = {}) => http.post('/merchant/updatePayStatus', params, config)
// 交易明细
export const fetchTradeDetail = (params, config = {}) => http.post('/TransactionDetails/list', params, config)
//获取统计数据
export const fetchCountData = (params, config = {}) => http.post('/statistics/count', params, config)
// 交易笔数统计
export const fetchFrequency = (params, config = {}) => http.post('/statistics/frequency', params, config)
// 交易金额统计
export const fetchMoney = (params, config = {}) => http.post('/statistics/money', params, config)
// 交易类型占比统计
export const fetchType = (params, config = {}) => http.post('/statistics/type', params, config)
// 银行入驻短信
export const sendBankMsgBd = (params, config = {}) => http.post('/merchant/sendMessage', params, config)
// 食品安全入驻短信
export const sendSafeMsgBd = (params, config = {}) => http.post('/merchant/sendInsureMessage', params, config)
// get请求，获取菜单，注意：get请求的配置等，都在第二个参数中，详见前面解释
// export const getPic = (data) => http.get('lishi/api.php', data) 

// 溯源删除
export const deleteSource = (params, config = {}) => http.post('/source/delete', params, config)
// 获取溯源列表
export const fetchSourceList = (params, config = {}) => http.post('/source/list', params, config)
// 上传溯源
export const uploadSource = (params, config = {}) => http.post('/upload/merchantSource', params, config)
// 提交溯源图片
export const submitSource = (params, config = {}) => http.post('/source/create', params, config)

// 曝光台
export const platformList = (params, config = {}) => http.post('/exposure/platform/list', params, config)