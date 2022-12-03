package com.fire.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fire.admin.entity.BusinessLicense;
import com.fire.admin.mapper.BusinessLicenseMapper;
import com.fire.admin.service.BusinessLicenseService;
import org.springframework.stereotype.Service;

/**
 * @author : [Cohen]
 * @version : [v1.0]
 * @createTime : [2022/2/11 17:42]
 */
@Service
public class BusinessLicenseServiceImpl extends ServiceImpl<BusinessLicenseMapper, BusinessLicense> implements BusinessLicenseService {
}
