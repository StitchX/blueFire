package com.blue.crm.converter;

import com.blue.crm.entity.AddressInfo;
import com.blue.crm.entity.CategoryInfo;
import com.blue.crm.vo.AddressInfoVo;
import com.blue.crm.vo.CategoryInfoVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryInfoConverter {
    CategoryInfoConverter INSTANCE = Mappers.getMapper(CategoryInfoConverter.class);

    CategoryInfoVo entity2Vo(CategoryInfo categoryInfo);

}
