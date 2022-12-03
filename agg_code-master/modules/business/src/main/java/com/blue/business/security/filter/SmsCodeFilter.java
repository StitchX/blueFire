package com.blue.business.security.filter;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.StrUtil;
import com.blue.business.exception.ValidateCodeException;
import com.blue.business.security.util.JwtUtil;
import com.blue.business.security.util.SmsCodeAuthenticationToken;
import com.blue.business.util.HttpRequestParamUtil;
import com.blue.business.util.RedisUtil;
import com.fire.common.exception.BaseException;
import com.fire.dto.enums.RedisKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Description: 短信验证码过滤器
 * @ClassName: SmsCodeFilter
 * @Author: liuliu
 * @Date: 2022/3/21 15:15
 */
@Component
@Slf4j
public class SmsCodeFilter extends OncePerRequestFilter {

    @Autowired
    private RedisUtil redisUtil;

    @Resource
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    private JwtUtil jwtUtil;

    private Set<String> urls = new HashSet<>();

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();
        // 这里配置需要拦截的地址
        urls.add("/phone/login");
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        boolean action = false;
        for (String url : urls) {
            if (antPathMatcher.match(url, request.getRequestURI())) {
                action = true;
                break;
            }
        }
        if (action) {
            try {
                validate(request);
            } catch (ValidateCodeException e) {
                authenticationFailureHandler.onAuthenticationFailure(request, response, e);
                return;
            }
        }
        String authToken = request.getHeader("Authorization");
        String username = jwtUtil.getUsernameFromToken(authToken);
        //当token中的username不为空时进行验证token是否是有效的token
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            //token中username不为空，并且Context中的认证为空，进行token验证
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(authToken, userDetails.getUsername())) { //如username不为空，并且能够在数据库中查到
                SmsCodeAuthenticationToken authentication =
                        new SmsCodeAuthenticationToken(userDetails, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                //将authentication放入SecurityContextHolder中
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    private void validate(HttpServletRequest request) {
        //短信验证码
        String smsCode = obtainSmsCode(request);
        // 手机号
        String mobile = obtainMobile(request);
        // 判断手机号码的合法性
        if (!PhoneUtil.isMobile(mobile)) {
            throw new ValidateCodeException("手机号码错误");
        }
        Object redisCode = redisUtil.hget(RedisKey.BUSINESS_LGOIN_INCR.key(), mobile);

        if (smsCode == null || smsCode.isEmpty()) {
            throw new ValidateCodeException("短信验证码不能为空");
        }
        if (ObjectUtil.isNull(redisCode)) {
            throw new ValidateCodeException("验证码已失效");
        }
        if (!smsCode.equals(redisCode)) {
            throw new ValidateCodeException("短信验证码错误");
        }
    }

    /**
     * 获取验证码
     *
     * @param request
     * @return
     */
    private String obtainSmsCode(HttpServletRequest request) {
        return request.getParameter("smsCode");
    }

    /**
     * 获取手机号
     *
     * @param request
     * @return
     */
    private String obtainMobile(HttpServletRequest request) {
        return request.getParameter("phone");
    }

}
