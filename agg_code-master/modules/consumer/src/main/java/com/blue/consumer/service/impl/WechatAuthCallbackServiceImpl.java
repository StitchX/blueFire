package com.blue.consumer.service.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.blue.consumer.config.WeChatConfig;
import com.blue.consumer.service.AuthCallbackService;
import com.fire.dto.response.BaseRestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fcq
 * @version v2.0.2.consumer
 * @description 微信授权回调
 * @date 2022/3/9 10:11
 */
@Slf4j
@Service("wechatAuthCallbackServiceImpl")
public class WechatAuthCallbackServiceImpl implements AuthCallbackService {
    @Override
    public BaseRestResponse getTokenByCode(String code) {
        Map<String, Object> params = new HashMap<>(4);
        params.put("appid", WeChatConfig.appid);
        params.put("secret", WeChatConfig.secret);
        params.put("code", code);
        params.put("grant_type", "authorization_code");
        String s = HttpUtil.get("https://api.weixin.qq.com/sns/oauth2/access_token", params);
        log.info("微信授权获取token响应,{}", s);
        String openid = JSONUtil.parseObj(s).getStr("openid");
        BaseRestResponse<String> response = new BaseRestResponse<>(openid);
        return response;
    }

    @Override
    public BaseRestResponse getUserinfoByToken(Map<String, Object> params) {
        params.put("lang", "zh_CN");
        String s = HttpUtil.get("https://api.weixin.qq.com/sns/oauth2/access_token", params);
        log.info("微信授权获取用户信息响应,{}", s);
        return null;
    }
}
