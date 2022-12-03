package com.blue.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blue.business.domain.MerchantUser;
import com.blue.business.mapper.MerchantUserMapper;
import com.blue.business.security.filter.SmsCodeAuthenticationProvider;
import com.blue.business.security.util.JwtUtil;
import com.blue.business.security.util.PreSecurityUser;
import com.blue.business.security.util.SmsCodeAuthenticationToken;
import com.blue.business.service.MerchantUserService;
import com.fire.dto.system.SysDept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @ClassName: SysUserServiceImpl
 * @Author: liuliu
 * @Date: 2022/3/22 14:09
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<MerchantUserMapper, MerchantUser> implements MerchantUserService {

    @Autowired
    private SmsCodeAuthenticationProvider smsCodeAuthenticationProvider;

    @Override
    public MerchantUser querySecurityUser(String phone) {
       return baseMapper.selectOne( Wrappers.<MerchantUser>lambdaQuery()
                        .select(MerchantUser::getUsername, MerchantUser::getMerchantId, MerchantUser::getLockFlag)
                        .eq(MerchantUser::getUsername, phone)
                        .eq(MerchantUser::getDelFlag, 0));
    }

    @Override
    public String login(String phone, String smsCode) {
        Authentication authenticate = smsCodeAuthenticationProvider.authenticate(new SmsCodeAuthenticationToken(phone));
        PreSecurityUser userDetail = (PreSecurityUser)authenticate.getPrincipal();
        return JwtUtil.generateToken(userDetail);
    }
}
