package com.blue.consumer.rest;

import cn.hutool.json.JSONUtil;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.blue.consumer.service.AuthCallbackService;
import com.fire.dto.response.BaseRestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author fcq
 * @version v2.0.2.consumer
 * @description 支付宝、微信授权回调
 * @date 2022/3/8 12:03
 */
@Slf4j
@RestController
@RequestMapping("callback")
public class AuthCallbackController {

    @Autowired
    @Qualifier("alipayAuthCallbackServiceImpl")
    private AuthCallbackService alipayAuthCallbackService;

    @Autowired
    @Qualifier("wechatAuthCallbackServiceImpl")
    private AuthCallbackService wechatAuthCallbackService;

    @RequestMapping("ali")
    public void ali(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("支付宝授权回调{}", request.getParameterMap());
        String code = request.getParameter("auth_code");
        BaseRestResponse token = alipayAuthCallbackService.getTokenByCode(code);
        AlipaySystemOauthTokenResponse tokenData = (AlipaySystemOauthTokenResponse) token.getData();
        String userId = tokenData.getUserId();
        log.info("支付宝授权返回openid:{}", userId);
//        HashMap<String, Object> params = new HashMap<>();
//        params.put("token", tokenData.getAccessToken());
//        BaseRestResponse userinfo = alipayAuthCallbackService.getUserinfoByToken(params);
        response.sendRedirect("http://192.168.1.20:8081/#/?openid=" + userId);
    }

    @RequestMapping("wechat")
    public void wechat(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("微信授权回调{}", request.getParameterMap());
        String code = request.getParameter("code");
        BaseRestResponse token = wechatAuthCallbackService.getTokenByCode(code);
//        Map<String, Object> params = new HashMap<>();
//        params.put("access_token", "");
//        params.put("openid", "");
//        BaseRestResponse userinfo = wechatAuthCallbackService.getUserinfoByToken(params);
        response.sendRedirect("http://192.168.1.20:8081/#/?openid=" + token.getData().toString());
//        response.getWriter().print(token.getData().toString());
    }

    @RequestMapping("valid")
    public void valid(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String echostr = request.getParameter("echostr");
        response.getWriter().println(echostr);
    }
}
