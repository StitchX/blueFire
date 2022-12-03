package com.blue.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blue.business.domain.MerchantUser;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @ClassName: SysUserService
 * @Author: liuliu
 * @Date: 2022/3/21 15:49
 */

@Service
public interface MerchantUserService extends IService<MerchantUser> {


    /**
     *@description:  通过电话号码查询登录用户
     * @param phone
     *@return: com.blue.business.domain.MerchantUser
     *@author: liuliu
     *@date: 2022-03-22 14:08
    */
    MerchantUser querySecurityUser(String phone);

    String login(String phone, String smsCode);
}
