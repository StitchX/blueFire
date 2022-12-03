package com.blue.business.security.handler;

import com.blue.business.security.util.SecurityUtil;
import com.fire.dto.response.BaseRestResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description: 认证失败
 * @ClassName: PreAuthenticationFailureHandler
 * @Author: liuliu
 * @Date: 2022/3/21 15:01
 */
@Component
public class PreAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        BaseRestResponse result = new BaseRestResponse();
        result.setStatus("402");
        result.setMessage(exception.getMessage());
        response.setContentType("application/json;charset=utf-8");
        SecurityUtil.writeJavaScript(result, response);
    }
}
