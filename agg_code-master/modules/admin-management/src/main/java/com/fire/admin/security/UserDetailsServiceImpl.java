package com.fire.admin.security;

import cn.hutool.core.util.ObjectUtil;
import com.fire.admin.service.ISysUserService;
import com.fire.admin.util.PreSecurityUser;
import com.fire.dto.system.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.Set;



/**
 * @author: liuliu
 * @ClassName: UserDetailsServiceImpl
 * @Description: 用户身份验证
 * @date: 2021-05-14 18:08
 */
@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private ISysUserService userService;

    @Autowired
    public UserDetailsServiceImpl(ISysUserService userService) {
        this.userService = userService;
    }

    /**
     * 用户名密码登录
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = new SysUser();
        sysUser.setUsername(username);

        SysUser user = userService.findSecurityUserByUser(sysUser);
        if (ObjectUtil.isNull(user)) {
            log.info("登录用户：" + username + " 不存在.");
            throw new UsernameNotFoundException("登录用户：" + username + " 不存在");
        }
        if(user.getLockFlag().equals("9")){
            throw new InternalAuthenticationServiceException("登录用户"+username+"被锁定");
        }
        Set<Long> supervisionBureauIds = userService.findSupervisionBureaus(user.getSupervisionId());
        user.setSupervisionIds(supervisionBureauIds);
        Collection<? extends GrantedAuthority> authorities = getUserAuthorities(user.getUserId());
        PreSecurityUser preSecurityUser = new PreSecurityUser(user.getUserId(), username, user.getPassword(), authorities, user.getSupervisionId(),user.getSupervisionIds());
        return preSecurityUser;
    }


    /**
     * 封装 根据用户Id获取权限
     *
     * @param userId
     * @return
     */
    private Collection<? extends GrantedAuthority> getUserAuthorities(Long userId) {
        // 获取用户拥有的角色
        // 用户权限列表，根据用户拥有的权限标识与如 @PreAuthorize("hasAuthority('sys:menu:view')") 标注的接口对比，决定是否可以调用接口
        // 权限集合
        Set<String> permissions = userService.findPermsByUserId(userId);
        // 角色集合
        Set<String> roleIds = userService.findRoleIdByUserId(userId);
        permissions.addAll(roleIds);
        Collection<? extends GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(permissions.toArray(new String[0]));
        return authorities;
    }
}
