package com.blue.business.security.handler;

import cn.hutool.http.HttpStatus;
import com.blue.business.security.util.SecurityUtil;
import com.fire.dto.response.BaseRestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description: 认证过的用户访问无权限资源时的异常
 * @ClassName: PreAccessDeineHandler
 * @Author: liuliu
 * @Date: 2022/3/21 15:14
 */
@Slf4j
public class PreAccessDeineHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        log.info("请求访问: " + request.getRequestURI() + " 接口， 没有访问权限");
        SecurityUtil.writeJavaScript(new BaseRestResponse(String.valueOf(HttpStatus.HTTP_UNAUTHORIZED), "请求访问:" + request.getRequestURI() + "接口,没有访问权限"), response);
    }
}
