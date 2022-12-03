package com.blue.crm.service;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blue.crm.converter.AddressInfoConverter;
import com.blue.crm.entity.AddressInfo;
import com.blue.crm.mapper.AddressInfoMapper;
import com.blue.crm.util.StringUtils;
import com.blue.crm.vo.AddressInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author : [Cohen]
 * @version : [v1.0]
 * @createTime : [2022/3/4 10:48]
 */

@Service
@Slf4j
public class AddressInfoService extends ServiceImpl<AddressInfoMapper, AddressInfo> {

    //获取地区信息
    public Map<Integer, AddressInfoVo> mapAddressInfoByCode(String code, Map<Integer, AddressInfoVo> result) {
        log.info("AddressInfoService::mapAddressInfoByCode: params is");
        Integer level = 99;
        while(!level.equals(1)){
            LambdaQueryWrapper<AddressInfo> wrapper = new LambdaQueryWrapper<AddressInfo>()
                    .eq(StringUtils.isNotBlank(code), AddressInfo::getCode, code);
            AddressInfo addressInfo = this.getOne(wrapper);
            code = addressInfo.getParentCode();
            level = addressInfo.getLevel();
            result.put(level, AddressInfoConverter.INSTANCE.entity2Vo(addressInfo));
        }
        return result;
    }
}
