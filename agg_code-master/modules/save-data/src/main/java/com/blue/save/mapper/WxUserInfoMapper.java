package com.blue.save.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fire.dto.entity.WxUserInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author fcq
 * @version v2.0.2.consumer
 * @description 支付宝微信授权信息
 * @date 2022/3/14 10:10
 */
@Mapper
public interface WxUserInfoMapper extends BaseMapper<WxUserInfo> {

    int insert(WxUserInfo wxUserInfo);
}
