import request from "@/request";
export function list(data,id) {
  return request({
    url: "/consumer/getHomePage?id=" + id,
    method: "post",
    data:data,
  });
}
// 获取id
export function consumerId(id) {
  return request({
    url: "/consumer/authIdIncr?id="+id,
    method: "post",
  });
}

export function makeCount(data,id){
  return request({
    url:"/consumer/makeCount?id=" + id,
    method:"post",
    data:data
  })
}
