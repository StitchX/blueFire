package com.blue.crm.security.handle;

import cn.hutool.http.HttpStatus;
import com.blue.crm.util.SecurityUtil;
import com.fire.dto.response.BaseRestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;


/**
 * @author: Cohen
 * @ClassName: PreAuthenticationEntryPointImpl
 * @Description: 用来解决认证过的用户访问无权限资源时的异常
 * @date: 2022-02-28 18:08
 */
@Slf4j
@Component
public class PreAuthenticationEntryPointImpl implements AuthenticationEntryPoint, Serializable {

    private static final long serialVersionUID = -8970718410437077606L;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        log.error("请求访问: " + request.getRequestURI() + " 接口， 经jwt认证失败，无法访问系统资源.");
        SecurityUtil.writeJavaScript(new BaseRestResponse(String.valueOf(HttpStatus.HTTP_UNAUTHORIZED), "请求访问:" + request.getRequestURI() + "接口,经jwt 认证失败,无法访问系统资源"), response);
    }
}

