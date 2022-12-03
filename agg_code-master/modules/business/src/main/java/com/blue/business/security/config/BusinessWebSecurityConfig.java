package com.blue.business.security.config;

import com.blue.business.security.filter.SmsCodeFilter;
import com.blue.business.security.handler.PreAccessDeineHandler;
import com.blue.business.security.handler.PreAuthenticationFailureHandler;
import com.blue.business.security.handler.PreAuthenticationSuccessHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * @Description: security 配置类
 * @ClassName: BusinessWebSecurityConfig
 * @Author: liuliu
 * @Date: 2022/3/21 14:56
 */
@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class BusinessWebSecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * 短信验证码过滤器
     */
    @Autowired
    private SmsCodeFilter smsCodeFilter;

    @Autowired
    private SmsAuthenticationConfig smsAuthenticationConfig;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 配置策略
     *
     * @param httpSecurity
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // 由于使用的是JWT，我们这里不需要csrf
                .csrf().disable()
                // 短信登录配置
                // 基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                // 过滤请求
                .authorizeRequests()
                // 对于登录login 图标 要允许匿名访问

                .antMatchers("/phone/login/**", "/favicon.ico").anonymous()
                .antMatchers(HttpMethod.GET, "/*.html", "/**/*.html", "/**/*.css", "/**/*.js")
                .permitAll()
                .antMatchers("/code/**").permitAll()
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/resources/**").permitAll()
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .headers().frameOptions().disable()
                .and().apply(smsAuthenticationConfig);


        // 添加自定义异常入口
        httpSecurity
                .exceptionHandling()
                .accessDeniedHandler(new PreAccessDeineHandler());


        // 添加JWT filter 用户名登录
        httpSecurity
                // 添加短信验证码过滤器
                .addFilterBefore(smsCodeFilter, UsernamePasswordAuthenticationFilter.class);

    }


}
