package com.blue.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blue.business.vo.AddressInfoVo;
import com.fire.dto.entity.AddressInfo;

import java.util.Map;

public interface AddressInfoService extends IService<AddressInfo> {

    Map<Integer, AddressInfoVo> mapAddressInfoByCode(String code, Map<Integer, AddressInfoVo> result);
}
