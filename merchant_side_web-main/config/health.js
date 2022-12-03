const http = uni.$u.http

// 获取商户信息
// export const apiMerchant = (params) => http.post('/merchant/indexInfo', params)




// get请求，获取菜单，注意：get请求的配置等，都在第二个参数中，详见前面解释
// export const getPic = (data) => http.get('lishi/api.php', data) // 健康证  列表export const getHealth = (params) => http.post('/health/certificate/list', params)// 健康证  新增export const addHealth = (params) => http.post('/health/certificate/save', params)//健康证 删除    export const delHealth = (params) => http.delete(`/health/certificate/remove/${params}`, params)//健康证  照片export const imgHealth = (params) => http.post('/upload/health/certificate', params)
// 从业证  列表export const getEmployment = (params) => http.post('/employment/certificate/list', params)// 从业证  新增export const addEmployment = (params) => http.post('/employment/certificate/save', params)//从业证 删除export const delEmployment = (params) => http.delete(`/employment/certificate/remove/${params}`, params)//从业证 照片export const imgEmployment = (params) => http.post('/upload/employment/certificate', params)