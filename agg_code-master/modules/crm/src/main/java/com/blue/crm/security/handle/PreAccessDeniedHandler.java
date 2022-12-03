package com.blue.crm.security.handle;

import cn.hutool.http.HttpStatus;
import com.blue.crm.util.SecurityUtil;
import com.fire.dto.response.BaseRestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @author: Cohen
 * @ClassName: PreAccessDeniedHandler
 * @Description: 用来解决匿名用户访问无权限资源时的异常
 * @date: 2022-02-28 18:08
 */
@Slf4j
@Component
public class PreAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        log.info("请求访问: " + request.getRequestURI() + " 接口， 没有访问权限");
        SecurityUtil.writeJavaScript(new BaseRestResponse(String.valueOf(HttpStatus.HTTP_UNAUTHORIZED), "请求访问:" + request.getRequestURI() + "接口,没有访问权限"), response);
    }
}
