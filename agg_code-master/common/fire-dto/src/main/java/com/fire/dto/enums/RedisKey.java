package com.fire.dto.enums;

public enum RedisKey {

    //系统
    ORDER_ID_INCR("agg.order.id.incr", "自增订单id"),
    ORDER_INFO("agg.order.info.", "订单详情"),

    GLOBAL_ID_INCR("agg.global.id.incr", "全局自增id"),

    MERCHANT_ID_INCR("agg.merchant.id.incr", "商户自增id"),
    MERCHANT_INFO("agg.merchant.info.", "商户信息"),
    
    BUSINESS_LGOIN_INCR("bussiness_login_incr","商户系统手机验证码登录存储数据");

    private final String key;

    private final String desc;

    RedisKey(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    public String key() {
        return key;
    }

    public String desc() {
        return desc;
    }

}
