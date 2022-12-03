package com.blue.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blue.business.mapper.BusinessLicenseMapper;
import com.blue.business.service.BusinessLicenseService;
import com.fire.dto.entity.BusinessLicense;
import org.springframework.stereotype.Service;

/**
 * @author : [Cohen]
 * @version : [v1.0]
 * @createTime : [2022/2/23 11:51]
 */
@Service
public class BusinessLicenseServiceImpl extends ServiceImpl<BusinessLicenseMapper, BusinessLicense> implements BusinessLicenseService {
}
