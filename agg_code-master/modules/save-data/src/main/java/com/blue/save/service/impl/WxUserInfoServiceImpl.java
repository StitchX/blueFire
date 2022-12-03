package com.blue.save.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fire.dto.entity.WxUserInfo;
import com.blue.save.mapper.WxUserInfoMapper;
import com.blue.save.service.WxUserInfoService;
import org.springframework.stereotype.Service;

/**
 * @author fcq
 * @version v2.0.2.consumer
 * @description 支付宝微信授权用户信息
 * @date 2022/3/14 10:13
 */
@Service
public class WxUserInfoServiceImpl extends ServiceImpl<WxUserInfoMapper, WxUserInfo> implements WxUserInfoService {
    @Override
    public boolean insert(WxUserInfo wxUserInfo) {
        return retBool(baseMapper.insert(wxUserInfo));
    }
}
