import request from '@/utils/request'

/*
 * 部门管理模块
 */

// 保存
export const saveDept = (data) => {
  return request({
    url: '/supervisionbureau/add',
    method: 'post',
    data: data
  })
}

// 地域查询
export function getArea(data) {
  return request({
    url: '/getArea',
    method: 'post',
    data: data
  })
}
// 获取监管所列表
export const getDept = () => {
  return request({
    url: '/supervisionbureau/tree',
    method: 'get'
  })
}
// 获取部门树
export const getDeptTree = () => {
  return request({
    url: '/dept/tree',
    method: 'get'
  })
}

// 更新部门
export const updateDept = (data) => {
  return request({
    url: '/supervisionbureau/update',
    method: 'post',
    data: data
  })
}
// 删除部门
export const deleteDept = (id) => {
  return request({
    url: '/dept/' + id,
    method: 'delete'
  })
}
