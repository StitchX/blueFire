package com.blue.crm.security.config;

import com.blue.crm.security.filter.PreJwtAuthenticationTokenFilter;
import com.blue.crm.security.handle.PreAccessDeniedHandler;
import com.blue.crm.security.handle.PreAuthenticationEntryPointImpl;
import com.blue.crm.security.handle.PreAuthenticationFailureHandler;
import com.blue.crm.security.handle.PreAuthenticationSuccessHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * @author: Cohen
 * @ClassName: PreWebSecurityConfig
 * @Description: Security配置类
 * @date: 2022-02-22 18:08
 */
@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class PreWebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 自定义jwt验证过滤器
     */
    @Autowired
    private PreJwtAuthenticationTokenFilter preJwtAuthenticationTokenFilter;

    /**
     * 自定义认证失败处理类
     */
    @Autowired
    private PreAuthenticationFailureHandler preAuthenticationFailureHandler;

    /**
     * 自定义认证成功处理类
     */
    @Autowired
    private PreAuthenticationSuccessHandler preAuthenticationSuccessHandler;

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 自定义权限不足处理类
     */
    @Autowired
    private PreAccessDeniedHandler preAccessDeniedHandler;

    /**
     *
     */
    @Autowired
    private PreAuthenticationEntryPointImpl preAuthenticationEntryPoint;

    /**
     * 解决 无法直接注入 AuthenticationManager
     *
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 配置策略
     * @param httpSecurity
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.formLogin()
                //自定义登录拦截URI
                .loginProcessingUrl("/auth/login")
                //自定义认证成功处理类
                .successHandler(preAuthenticationSuccessHandler)
                //自定义认证失败处理类
                .failureHandler(preAuthenticationFailureHandler)
                //防止iframe造成跨域
                .and().headers().frameOptions().disable()
                // 解决跨域问题
                .and().cors()
                //token的验证方式不需要开启csrf的防护
                .and().csrf().disable()
                // 自定义匿名用户访问无权限资源时处理
                .exceptionHandling().authenticationEntryPoint(preAuthenticationEntryPoint)
                // 自定义认证用户权限不足处理类
                .accessDeniedHandler(preAccessDeniedHandler)
                // 过滤请求
                .and().authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers("/auth/login").permitAll()
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/v2/**").permitAll()
                .antMatchers("/resources/**").permitAll()
                .antMatchers("/resources/temp/**").permitAll()
                .antMatchers("/downloadFile/**").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .anyRequest().authenticated()
                // 基于token，所以不需要session
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // 配置自己的jwt验证过滤器
        httpSecurity.addFilterBefore(preJwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        // disable page caching
        httpSecurity.headers().cacheControl();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setHideUserNotFoundExceptions(false);
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(new BCryptPasswordEncoder());
        return provider;
    }
}

