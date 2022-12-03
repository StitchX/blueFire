package com.blue.business.security.filter;

import com.blue.business.security.UserDetailsServiceImpl;
import com.blue.business.security.util.SmsCodeAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Description: 111
 * @ClassName: SmsCodeAuthenticationProvider
 * @Author: liuliu
 * @Date: 2022/3/21 15:25
 */
@Component
public class SmsCodeAuthenticationProvider implements AuthenticationProvider {
    @Resource
    private UserDetailsServiceImpl userDetailService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsCodeAuthenticationToken authenticationToken = (SmsCodeAuthenticationToken) authentication;
        String mobile = (String) authenticationToken.getPrincipal();
        //调用自定义的userDetailsService认证
        UserDetails userDetails = userDetailService.loadUserByUsername(mobile);
        //如果user不为空重新构建SmsCodeAuthenticationToken（已认证）
        SmsCodeAuthenticationToken authenticationResult = new SmsCodeAuthenticationToken(userDetails, userDetails.getAuthorities());
        authenticationResult.setDetails(authenticationToken.getDetails());
        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return SmsCodeAuthenticationToken.class.isAssignableFrom(aClass);
    }

    public UserDetailsServiceImpl getUserDetailService() {
        return userDetailService;
    }

    public void setUserDetailService(UserDetailsServiceImpl userDetailService) {
        this.userDetailService = userDetailService;
    }

}
