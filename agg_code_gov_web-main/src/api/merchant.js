import request from '@/utils/request'

// 查询商户列表
export function merchList(data) {
  return request({
    url: '/merchant/list',
    method: 'post',
    data: data
  })
}

// 监管所
export function superList(data) {
  return request({
    url: '/supervisionbureau/options',
    method: 'post',
    data: data
  })
}
// 银行
export function bankList(data) {
  return request({
    url: '/bank/options',
    method: 'post',
    data: data
  })
}
