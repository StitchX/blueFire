package com.fire.admin.util;

import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;


/**
 * @author: liuliu
 * @ClassName: PreSecurityUser
 * @Description: 用户手机号和账号密码 身份权限认证类 登陆身份认证
 * @date: 2021-05-13 18:18
 */
@Data
@Setter
@Getter
@Accessors(chain = true)
public class PreSecurityUser implements UserDetails {

    private static final long serialVersionUID = 1L;

    private Long userId;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private Long supervisionId;
    private Set<Long> supervisionIds;

    public PreSecurityUser(Long userId, String username, String password, Collection<? extends GrantedAuthority> authorities, Long supervisionId, Set<Long> supervisionIds) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.supervisionId = supervisionId;
        this.supervisionIds = supervisionIds;
    }


    /**
     * @Description: 返回分配给用户的角色列表
     * @Param: []
     * @return: java.util.Collection<? extends org.springframework.security.core.GrantedAuthority>
     * @Author: liuliu
     * @Date: 2021/5/13 18:23
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }


    /**
     * @Description: 账户是否未过期, 过期无法验证
     * @Param: []
     * @return: boolean
     * @Author: liuliu
     * @Date: 2021/5/13 18:23
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }


    /**
     * @Description: 指定用户是否解锁, 锁定的用户无法进行身份验证
     * @Param: []
     * @return: boolean
     * @Author: liuliu
     * @Date: 2021/5/13 18:23
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }


    /**
     * @Description: 指示是否已过期的用户的凭据(密码), 过期的凭据防止认证
     * @Param: []
     * @return: boolean
     * @Author: liuliu
     * @Date: 2021/5/13 18:23
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * @Description: 是否可用 ,禁用的用户不能身份验证
     * @Param: []
     * @return: boolean
     * @Author: liuliu
     * @Date: 2021/5/13 18:23
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
