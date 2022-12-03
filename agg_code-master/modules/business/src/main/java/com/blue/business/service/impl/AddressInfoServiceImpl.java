package com.blue.business.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blue.business.converter.AddressInfoConverter;
import com.blue.business.mapper.AddressInfoMapper;
import com.blue.business.service.AddressInfoService;
import com.blue.business.vo.AddressInfoVo;
import com.fire.dto.entity.AddressInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author : [Cohen]
 * @version : [v1.0]
 * @createTime : [2022/3/4 10:48]
 */

@Service
@Slf4j
public class AddressInfoServiceImpl extends ServiceImpl<AddressInfoMapper, AddressInfo> implements AddressInfoService {

    //获取地区信息
    public Map<Integer, AddressInfoVo> mapAddressInfoByCode(String code, Map<Integer, AddressInfoVo> result) {
        log.info("AddressInfoService::mapAddressInfoByCode: params is");
        Integer level = 99;
        while(!level.equals(1)){
            LambdaQueryWrapper<AddressInfo> wrapper = new LambdaQueryWrapper<AddressInfo>()
                    .eq(StringUtils.isNotBlank(code), AddressInfo::getCode, code);
            AddressInfo addressInfo = this.getOne(wrapper);
            if(ObjectUtil.isNull(addressInfo)){
                return result;
            }
            code = addressInfo.getParentCode();
            level = addressInfo.getLevel();
            result.put(level, AddressInfoConverter.INSTANCE.entity2Vo(addressInfo));
        }
        return result;
    }
}
