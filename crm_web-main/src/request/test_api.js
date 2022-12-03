import http from './http.js'
export default {
    //美图秀秀
    //clickid 拿到uuid 和ClickId//价格数据
    clickId(clickId,mockUserId,data) {
        return http.get("/v1/click/api/" + clickId + "/mockUserId/" + mockUserId, data)
    },
    //挖财
    wacai(clickId,data) {
        return http.get("/v1/click/api/"+ clickId, data)
    },
    //获取用户可领的优惠券
    coupon(userId,data){
        return http.get("/v1/coupon/userId/" + userId,data)
    },
    //面额和优惠金额
    price(userId,data){
        return http.get("/v1/coupon/price/userId/" + userId,data)
    },
    //领取优惠卷
    getCoupon(userId,data){
        return http.post("/v1/coupon/getCoupon/"+ userId,data)
    },
    //手机号历史
    onphone(userId,data) {
        return http.get("/v1/order/pnumber/" + userId, data)
    },
    //面额和售价额
    onmoney(data) {
        return http.get("/v1/click/num/price", data)
    },
    //提交充值金额
    onLoad(data) {
        return http.post("/v1/pay/order", data)
    },
    //充值记录
    getUserInfo(page,size,userId,status,data) {
        return http.get("/v1/order/list/page/" + page +"/size/" + size + "/userId/" + userId + "/status/" + status, data)
    },
    //查询订单号
    orderId(data, token) {
        return http.get("/v1/pay/updateOrderFailed", data, token)
    }
}