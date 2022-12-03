package com.fire.admin.security.handle;


import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson.JSON;
import com.fire.admin.service.LogInfoService;
import com.fire.admin.util.DateUtil;
import com.fire.admin.util.JwtUtil;
import com.fire.admin.util.PreSecurityUser;
import com.fire.admin.util.SecurityUtil;
import com.fire.dto.log.LogInfo;
import com.fire.dto.response.BaseRestResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;


/**
 * @author: liuliu
 * @ClassName: PreAuthenticationSuccessHandler
 * @Description: 登录成功处理器
 * @date: 2021-05-14 18:08
 */
@Component
public class PreAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Resource
    private LogInfoService logInfoService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Object principal = authentication.getPrincipal();
        if (principal instanceof PreSecurityUser) {
            PreSecurityUser userDetail = (PreSecurityUser) authentication.getPrincipal();
            //存储认证信息
            SecurityContextHolder.getContext().setAuthentication(authentication);
            //生成token

            response.setContentType("text/json;charset=utf-8");
            BaseRestResponse result = new BaseRestResponse(JwtUtil.generateToken(userDetail));
            String token = JSON.toJSONString(result);
            response.getWriter().write(token);

            StringBuilder desc = new StringBuilder();
            String username = null;
            try {
                username = SecurityUtil.getUser().getUsername();
                desc.append("成功");
            } catch (Throwable throwable) {
                desc.append("失败");
            } finally {
                LogInfo loginLogInfo = LogInfo.builder()
                        //.supervisionId(SecurityUtil.getUser().getSupervisionId())
                        .name(username)
                        .content(username.concat("在" + DateUtil.formatLocalDateTime(LocalDateTime.now())).concat("登录系统" + desc))
                        .ip(ServletUtil.getClientIPByHeader(request, "X-Forwarded-For"))
                        .operateTime(new Timestamp(System.currentTimeMillis()))
                        .module("系统")
                        .build();

                logInfoService.saveLogInfo(loginLogInfo);
            }
        }
    }
}

