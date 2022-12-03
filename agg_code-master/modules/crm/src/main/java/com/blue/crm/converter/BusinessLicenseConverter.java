package com.blue.crm.converter;

import com.blue.crm.entity.CategoryInfo;
import com.blue.crm.vo.BusinessLicenseVo;
import com.blue.crm.vo.CategoryInfoVo;
import com.fire.dto.entity.BusinessLicense;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author : [Cohen]
 * @version : [v1.0]
 * @createTime : [2022/3/4 12:43]
 */
@Mapper
public interface BusinessLicenseConverter {
    BusinessLicenseConverter INSTANCE = Mappers.getMapper(BusinessLicenseConverter.class);

    BusinessLicenseVo entity2Vo(BusinessLicense businessLicense);
}
