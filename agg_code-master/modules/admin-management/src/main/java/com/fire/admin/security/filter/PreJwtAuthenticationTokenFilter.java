package com.fire.admin.security.filter;

import cn.hutool.core.util.ObjectUtil;
import com.fire.admin.service.ISysUserService;
import com.fire.admin.util.JwtUtil;
import com.fire.admin.util.PreSecurityUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;


/**
 * @author: liuliu
 * @ClassName: PreJwtAuthenticationTokenFilter
 * @Description: token过滤器来验证token有效性
 * @date: 2021-05-14 18:08
 */
@Slf4j
@Component
public class PreJwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private JwtUtil jwtUtil;

    private ISysUserService userService;

    @Autowired
    public PreJwtAuthenticationTokenFilter(JwtUtil jwtUtil, ISysUserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        PreSecurityUser securityUser = jwtUtil.getUserFromToken(request);
        if (ObjectUtil.isNotNull(securityUser)) {
            Set<String> permissions = userService.findPermsByUserId(securityUser.getUserId());
            String[] strings = permissions.toArray(new String[0]);
            Collection<? extends GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(strings);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(securityUser, null, authorities);
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        chain.doFilter(request, response);
    }
}
