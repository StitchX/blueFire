package com.blue.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blue.business.mapper.FoodBusinessLicenseMapper;
import com.blue.business.service.FoodBusinessLicenseService;
import com.fire.dto.entity.FoodBusinessLicense;
import org.springframework.stereotype.Service;

/**
 * @author : [Cohen]
 * @version : [v1.0]
 * @createTime : [2022/2/24 11:33]
 */
@Service
public class FoodBusinessLicenseServiceImpl extends ServiceImpl<FoodBusinessLicenseMapper, FoodBusinessLicense> implements FoodBusinessLicenseService {
}
