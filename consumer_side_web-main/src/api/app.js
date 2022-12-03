import request from "@/request";

// 从业证
export function certificateInfo(data,id){
  return request({
    url:"/consumer/getEmploymentCertificateInfo?id="+id,
    method:"post",
    data:data
  })
}

// 健康证
export function healthCertificate(data,id){
  return request({
    url:"/consumer/getHealthCertificateInfo?id=" + id,
    method:"post",
    data:data
  })
}

// 备案证
export function certificates(data,id){
  return request({
    url:"/consumer/other/certificates/?id=" + id,
    method:"post",
    data:data
  })
}

// 溯源照片
export function datalist(data,id) {
  return request({
    url: "/consumer/source/list?id=" +id,
    method: "post",
    data:data,
  });
}

// 首页  商户信息
export function consumerInfo(data,id) {
  return request({
    url: "/consumer/getHomePage?id="+id,
    method: "post",
    data:data
  });
}

// 经营信息查询
export function businessInfo(data,id) {
  return request({
    url: "/consumer/getBusinessInfo?id=" + id,
    method: "post",
    data:data
  });
}

// 许可证查询
export function foodLicenseInfo(data,id) {
  return request({
    url: "/consumer/getFoodLicenseInfo?id="+ id,
    method: "post",
    data:data
  });
}
// 支付
export function makeorder(data,id) {
  return request({
    url: "/distribution/makeorder.ws?id=" + id,
    method: "post",
    data:data
  });
}