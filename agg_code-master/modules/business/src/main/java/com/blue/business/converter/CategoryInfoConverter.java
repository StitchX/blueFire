package com.blue.business.converter;

import com.blue.business.vo.CategoryInfoVo;
import com.fire.dto.entity.CategoryInfo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryInfoConverter {
    CategoryInfoConverter INSTANCE = Mappers.getMapper(CategoryInfoConverter.class);

    CategoryInfoVo entity2Vo(CategoryInfo categoryInfo);

}
