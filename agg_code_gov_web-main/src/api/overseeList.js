import request from '@/utils/request'

//index

// 监管所账号
export function adminUserList(data){
  return request({
    url:"/supervisionbureau/account/info",
    method:"post",
    data:data
  })
}

// 监管列表修改密码
export function updateList(data){
  return request({
    url:"/user/modifyPassword",
    method:"post",
    data:data
  })
}



//  admin

// 监管员列表
export function adminGetList(data) {
    return request({
      url: '/supervisionMan/list',
      method: 'post',
      data: data
    })
  }
