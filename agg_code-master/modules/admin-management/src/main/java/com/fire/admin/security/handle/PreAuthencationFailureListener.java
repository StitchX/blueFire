package com.fire.admin.security.handle;

import cn.hutool.extra.servlet.ServletUtil;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.fire.admin.service.LogInfoService;
import com.fire.admin.util.DateUtil;
import com.fire.common.exception.BaseException;
import com.fire.dto.log.LogInfo;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.LocalDateTime;


/**
 * @author: liuliu
 * @ClassName: PreAuthencationFailureListener
 * @Description: 用户登录失败监听器事件
 * @date: 2021-05-14 18:08
 */
@Component
public class PreAuthencationFailureListener implements ApplicationListener<AbstractAuthenticationFailureEvent> {

    @Resource
    private LogInfoService logInfoService;

    @Resource
    private HttpServletRequest request;

    @Override
    public void onApplicationEvent(AbstractAuthenticationFailureEvent event) {

        if (event instanceof AuthenticationFailureBadCredentialsEvent) {
            // 提供的凭据是错误的，用户名或者密码错误
            Object principal = event.getAuthentication().getPrincipal();

            LogInfo loginLogInfo = LogInfo.builder()
                        .name((String)principal)
                        .content(principal + ("在" + DateUtil.formatLocalDateTime(LocalDateTime.now())).concat("登录系统失败"))
                        .ip(ServletUtil.getClientIPByHeader(request, "X-Forwarded-For"))
                        .operateTime(new Timestamp(System.currentTimeMillis()))
                        .module("系统")
                        .build();

            logInfoService.saveLogInfo(loginLogInfo);
            if (event.getException() instanceof BadCredentialsException) {
                throw new UsernameNotFoundException("密码不正确");
            }
        } else if (event instanceof AuthenticationFailureCredentialsExpiredEvent) {
            //验证通过，但是密码过期
            throw new BaseException("402", "密码过期");
        } else if (event instanceof AuthenticationFailureDisabledEvent) {
            //验证过了但是账户被禁用
            throw new BaseException("402", "账户被禁用");
        } else if (event instanceof AuthenticationFailureExpiredEvent) {
            //验证通过了，但是账号已经过期
            throw new BaseException("402", "账号已经过期");
        } else if (event instanceof AuthenticationFailureLockedEvent) {
            //账户被锁定
            throw new BaseException("402", "账户被锁定");
        } else if (event instanceof AuthenticationFailureProviderNotFoundEvent) {
            //配置错误，没有合适的AuthenticationProvider来处理登录验证
            throw new BaseException("402", "配置错误");
        } else if (event instanceof AuthenticationFailureProxyUntrustedEvent) {
            //代理不受信任，用于Oauth、CAS这类三方验证的情形，多属于配置错误
            throw new BaseException("402", "代理不受信任");
        } else if (event instanceof AuthenticationFailureServiceExceptionEvent) {
            //其他任何在AuthenticationManager中内部发生的异常都会被封装成此类
            throw new BaseException("402", "内部发生错误");
        }
    }

}