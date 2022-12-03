package com.blue.consumer.service;

import com.fire.dto.response.BaseRestResponse;

import java.util.Map;

/**
 * @author fcq
 * @version v2.0.2.consumer
 * @description 支付宝、微信授权回调
 * @date 2022/3/9 10:01
 */
public interface AuthCallbackService {

    /**
     * @description 通过支付宝回调code获取token和userId
     * @author fcq
     * @date 2022/3/9 10:12
     * @version v2.0.1.crm
     */
    BaseRestResponse getTokenByCode(String code);

    /**
     * @description 通过支付宝token获取用户信息
     * @author fcq
     * @date 2022/3/9 10:30
     * @version v2.0.1.crm
     */
    BaseRestResponse getUserinfoByToken(Map<String, Object> params);
}
