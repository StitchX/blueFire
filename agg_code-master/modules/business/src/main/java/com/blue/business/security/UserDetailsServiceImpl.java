package com.blue.business.security;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.blue.business.domain.MerchantUser;
import com.blue.business.exception.ValidateCodeException;
import com.blue.business.security.util.PreSecurityUser;
import com.blue.business.service.MerchantUserService;
import com.fire.common.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:
 * @ClassName: UserDetailsServiceImpl
 * @Author: liuliu
 * @Date: 2022/3/21 15:00
 */
@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private MerchantUserService merchantUserService;

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        MerchantUser merchantUser = merchantUserService.querySecurityUser(phone);
        if (ObjectUtil.isNull(merchantUser)) {
            log.info("账户：" + phone + " 不存在");
            throw new ValidateCodeException("账户：" + phone + " 不存在");
        }
        if (merchantUser.getLockFlag().equals(9)) {
            log.info("账户：" + phone + " 被锁定");
            throw new ValidateCodeException("账户：" + phone + " 被锁定");
        }
        PreSecurityUser preSecurityUser = new PreSecurityUser(merchantUser.getUsername(), merchantUser.getMerchantId(), mapToGrantedAuthorities(new ArrayList<>()));
        log.info("当前登录的用户为：{}", JSONUtil.parseObj(preSecurityUser));
        return preSecurityUser;
    }


    private static List<GrantedAuthority> mapToGrantedAuthorities(List<String> authorities) {
        return authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
