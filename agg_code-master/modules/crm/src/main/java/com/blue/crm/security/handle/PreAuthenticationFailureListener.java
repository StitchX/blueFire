package com.blue.crm.security.handle;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.event.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * @author: Cohen
 * @ClassName: PreAuthenticationFailureListener
 * @Description: 用户登录失败监听器事件
 * @date: 2022-02-28 18:08
 */
@Component
public class PreAuthenticationFailureListener implements ApplicationListener<AbstractAuthenticationFailureEvent> {

    @Override
    public void onApplicationEvent(AbstractAuthenticationFailureEvent event) {
        if (event instanceof AuthenticationFailureBadCredentialsEvent) {
            if (event.getException() instanceof BadCredentialsException){
                throw new UsernameNotFoundException("账号或密码错误，请重新输入");
            }
        }
    }
}