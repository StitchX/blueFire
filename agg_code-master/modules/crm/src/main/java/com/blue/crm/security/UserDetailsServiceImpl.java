package com.blue.crm.security;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.blue.crm.dto.LoginUser;
import com.blue.crm.entity.BD;
import com.blue.crm.enums.BDEnum;
import com.blue.crm.service.*;
import com.fire.dto.system.SysRole;
import com.fire.dto.system.SysUser;
import com.fire.dto.system.SysUserRole;
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


@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private BDService bdService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("CLASS::UserDetails::FUNCTION::loadUserByUsername::param is {}", username);
        if(StringUtils.isBlank(username)){
            throw new UsernameNotFoundException("用户名不能为空");
        }
        //通过用户名查询数据库中的用户
        LambdaQueryWrapper sysUserWrapper = new LambdaQueryWrapper<SysUser>()
                .eq(StringUtils.isNotBlank(username), SysUser::getUsername, username);
        SysUser user = sysUserService.getOne(sysUserWrapper);
        log.info("CLASS::UserDetails::FUNCTION::loadUserByUsername::从数据库获取user信息： {}", user);
        if(user == null){
            throw new UsernameNotFoundException("账号或密码错误，请重新输入");
        }

        LambdaQueryWrapper BDWrapper = new LambdaQueryWrapper<BD>()
                .eq(BD::getSysUserId, user.getUserId()).eq(BD::getManagerFlag, BDEnum.BD.getCode());
        BD bd = bdService.getOne(BDWrapper);
        if(ObjectUtil.isNull(bd)){
            throw new UsernameNotFoundException("账号或密码错误，请重新输入");
        }

        List<String> menus = new ArrayList<>();
        LoginUser loginUser = new LoginUser(username, user.getPassword(), mapToGrantedAuthorities(menus),
                user.getUserId(), bd.getId(), bd.getDepartmentId());
        return loginUser;
    }


    //将与用户类一对多的角色类的名称集合转换为 GrantedAuthority 集合
    private static List<GrantedAuthority> mapToGrantedAuthorities(List<String> authorities) {
        return authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
