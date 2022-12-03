const http = uni.$u.http

// 获取商户信息
export const apiMerchant = (params) => http.post('/merchant/indexInfo', params)

// 商户详情
// export const apiDetail = () => http.post('/merchant/detail');
export const apiDetail = (params, config = {}) => http.post('/merchant/detail', params, config)

// 上传许可证
export const apiUpload = (params, config = {}) => http.post('/upload/foodBusinessLicense', params, config)

// 上传营业执照
export const apiUploadConfig = (params, config = {}) => http.post('/upload/businessLicense', params, config)
// 数据更新
export const apiupdate = (params, config = {}) => http.post('/merchant/update', params, config)
// 数据更新
export const apibank = (params, config = {}) => http.post('/bank/options', params, config)

// 行业类型
export const apiOption = (params, config = {}) => http.post('/category/option', params, config)
// get请求，获取菜单，注意：get请求的配置等，都在第二个参数中，详见前面解释
// export const getPic = (data) => http.get('lishi/api.php', data) 

// 查询通行结果
export const getPassResult = (params, config = {}) => http.post('/healthCode/create', params, config)
// 查询通行信息
export const getHealthDetail = (params, config = {}) => http.post('/healthCode/info', params, config)