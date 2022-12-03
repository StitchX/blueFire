import request from '@/utils/request'

// 日志列表
export function getList(data) {
    return request({
      url: '/logInfo/list',
      method: 'post',
      data: data
    })
  }
