package com.blue.crm.security.handle;


import com.blue.crm.util.SecurityUtil;
import com.fire.dto.response.BaseRestResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @author: Cohen
 * @ClassName: PreAuthenticationFailureHandler
 * @Description: 登录失败处理器
 * @date: 2022-02-28 18:08
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


