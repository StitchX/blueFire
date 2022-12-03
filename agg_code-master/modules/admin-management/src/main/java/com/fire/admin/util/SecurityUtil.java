package com.fire.admin.util;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.fire.admin.security.UserDetailsServiceImpl;
import com.fire.admin.service.SupervisionBureauService;
import com.fire.common.exception.BaseException;
import com.fire.dto.response.BaseRestResponse;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * @author: liuliu
 * @ClassName: SecurityUtil
 * @Description: 安全服务工具类
 * @date: 2021-05-13 18:18
 */
@UtilityClass
@Slf4j
public class SecurityUtil {

   /* @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private SupervisionBureauService supervisionBureauService;*/

    public void writeJavaScript(BaseRestResponse r, HttpServletResponse response) throws IOException {
        response.setStatus(200);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter printWriter = response.getWriter();
        printWriter.write(JSON.toJSONString(r));
        printWriter.flush();
    }


    /**
     * @Description: 获取Authentication
     * @Param: []
     * @return: org.springframework.security.core.Authentication
     * @Author: liuliu
     * @Date: 2021/5/11 17:59
     */
    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }


    /**
     * @Description: 获取用户
     * @Param: []
     * @return: com.fire.admin.util.PreSecurityUser
     * @Author: liuliu
     * @Date: 2021/5/11 17:58
     */
    public PreSecurityUser getUser() {
        try {
            PreSecurityUser user = (PreSecurityUser) getAuthentication().getPrincipal();
            return user;
        } catch (Exception e) {
            log.error("登录异常：{}", e);
            throw new BaseException(String.valueOf(HttpStatus.UNAUTHORIZED.value()), "登录状态过期");
        }

    }


}
