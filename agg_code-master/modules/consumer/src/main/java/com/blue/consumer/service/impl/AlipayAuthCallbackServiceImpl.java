package com.blue.consumer.service.impl;

import cn.hutool.json.JSONUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.blue.consumer.config.AlipayConfig;
import com.blue.consumer.service.AuthCallbackService;
import com.fire.dto.response.BaseRestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author fcq
 * @version v2.0.2.consumer
 * @description 支付宝授权回调
 * @date 2022/3/9 10:07
 */
@Slf4j
@Service("alipayAuthCallbackServiceImpl")
public class AlipayAuthCallbackServiceImpl implements AuthCallbackService {
    @Override
    public BaseRestResponse getTokenByCode(String code) {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", AlipayConfig.appId, AlipayConfig.appPrivateKey, "json", "gbk", AlipayConfig.alipayPublicKey, "RSA2");
        AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
        request.setCode(code);
        request.setGrantType("authorization_code");
        AlipaySystemOauthTokenResponse response = null;
        try {
            response = alipayClient.execute(request);
            log.info("微信授权获取token响应,{}", JSONUtil.toJsonStr(response));
        } catch (AlipayApiException e) {
            log.error("支付宝授权获取token失败,错误码：{},错误信息：{}", e.getErrCode(), e.getErrMsg());
            return BaseRestResponse.error(e.getErrMsg());
        }
        BaseRestResponse<AlipaySystemOauthTokenResponse> restResponse = new BaseRestResponse<>(response);
        return restResponse;
    }

    @Override
    public BaseRestResponse getUserinfoByToken(Map<String, Object> params) {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", AlipayConfig.appId, AlipayConfig.appPrivateKey, "json", "gbk", AlipayConfig.alipayPublicKey, "RSA2");
        AlipayUserInfoShareRequest request = new AlipayUserInfoShareRequest();
        AlipayUserInfoShareResponse response = null;
        try {
            response = alipayClient.execute(request, params.get("token").toString());
        } catch (AlipayApiException e) {
            log.error("支付宝授权获取用户信息失败,错误码：{},错误信息：{}", e.getErrCode(), e.getErrMsg());
            return BaseRestResponse.error(e.getErrMsg());
        }
        BaseRestResponse<AlipayUserInfoShareResponse> restResponse = new BaseRestResponse<>(response);
        return restResponse;
    }
}
