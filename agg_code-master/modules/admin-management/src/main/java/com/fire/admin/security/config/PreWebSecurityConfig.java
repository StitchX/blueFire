package com.fire.admin.security.config;


import com.fire.admin.security.UserDetailsServiceImpl;
import com.fire.admin.security.filter.PreJwtAuthenticationTokenFilter;
import com.fire.admin.security.handle.PreAccessDeineHandler;
import com.fire.admin.security.handle.PreAuthenticationEntryPointImpl;
import com.fire.admin.security.handle.PreAuthenticationFailureHandler;
import com.fire.admin.security.handle.PreAuthenticationSuccessHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * @author: liuliu
 * @ClassName: PreWebSecurityConfig
 * @Description: Security配置类
 * @date: 2021-05-14 18:08
 */
@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class PreWebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PreJwtAuthenticationTokenFilter preJwtAuthenticationTokenFilter;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

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
     *
     * @param httpSecurity
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.formLogin()
                .successHandler(preAuthenticationSuccessHandler)
                //自定义认证失败处理类
                .failureHandler(preAuthenticationFailureHandler)
                // 由于使用的是JWT，我们这里不需要csrf
                .and().csrf().disable()
                // 基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                // 过滤请求
                .authorizeRequests()
                // 对于登录login 图标 要允许匿名访问
                // .antMatchers("/login/**", "/favicon.ico").anonymous().antMatchers(HttpMethod.GET, "/*.html", "/**/*.html", "/**/*.css", "/**/*.js").permitAll().antMatchers("/auth/**").anonymous().antMatchers("/sendCode/**").anonymous().antMatchers("/tenant/list").anonymous().antMatchers("/tenant/setting/**").anonymous().antMatchers("/define/deploy/**").anonymous().antMatchers("/file/**").permitAll()

                //dev
                .antMatchers("/**").permitAll()
                // 访问/user 需要拥有admin权限
                //.antMatchers("/**").anonymous()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated().and().headers().frameOptions().disable();


        // 添加自定义异常入口
        httpSecurity.exceptionHandling().authenticationEntryPoint(new PreAuthenticationEntryPointImpl()).accessDeniedHandler(new PreAccessDeineHandler());

        // 添加JWT filter 用户名登录
        httpSecurity
                // 添加JWT验证过滤器
                .addFilterBefore(preJwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
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

