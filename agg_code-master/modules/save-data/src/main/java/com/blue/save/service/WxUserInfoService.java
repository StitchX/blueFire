package com.blue.save.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fire.dto.entity.WxUserInfo;

/**
 * @author fcq
 * @version v2.0.2.consumer
 * @description 支付宝微信授权
 * @date 2022/3/14 10:12
 */
public interface WxUserInfoService extends IService<WxUserInfo> {

    boolean insert(WxUserInfo wxUserInfo);
}
