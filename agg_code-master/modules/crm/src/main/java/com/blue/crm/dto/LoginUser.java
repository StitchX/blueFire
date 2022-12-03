package com.blue.crm.dto;

import com.blue.crm.entity.BD;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * @author : [Cohen]
 * @version : [v1.0]
 * @createTime : [2022/2/18 15:52]
 */
@Setter
@Getter
@Accessors(chain = true)
public class LoginUser extends User {
    private static final long serialVersionUID = 1L;

    private Long userId;

    private Long bdId;

    private Long deptId;

    public LoginUser(String username, String password, Collection<? extends GrantedAuthority> authorities
            , Long userId, Long bdId, Long deptId) {
        super(username, password, authorities);
        this.userId = userId;
        this.bdId = bdId;
        this.deptId = deptId;
    }
}
