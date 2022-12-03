const http = uni.$u.http

// 获取商户通知
export const apinNotice = (params = {}) => http.post('/notice/page', params)

// 通知详情接口
export const apinInfo = (params = {}) => http.post('/notice/details/info', params)
// 必读点击
export const apread = (params = {}) => http.post('/notice/details/read', params)


// 预警消息通知
// 列表
export const apiList = (params = {}) => http.post("/merchantWarning/list",params)
// 必读
export const batchRead = (params = {}) => http.post("/merchantWarning/batchRead",params)


// get请求，获取菜单，注意：get请求的配置等，都在第二个参数中，详见前面解释
export const getPic = (data) => http.get('lishi/api.php', data) 