import request from '@/utils/request'

// 顶部--查询数据统计
export function titleData() {
  return request({
    url: '/statistical/titleData',
    method: 'post'
  })
}
// 纳入监管商户分析
export function regulartory(data) {
  return request({
    url: '/statistical/regulatoryMerchantData',
    method: 'post',
    data:data
  })
}
// 信用信息公示接口
export function publicInfo(data) {
  return request({
    url: '/statistical/publicInformationData',
    method: 'post',
    data:data
  })
}
// 三码合一活跃商户接口
export function activeMerch(data) {
  return request({
    url: '/statistical/activeMerchantData',
    method: 'post',
    data:data
  })
}
// 监管通知下达次数
export function policyPreach(data) {
  return request({
    url: '/statistical/policyPreachData',
    method: 'post',
    data:data
  })
}