package com.blue.business.converter;

import com.blue.business.vo.AddressInfoVo;
import com.fire.dto.entity.AddressInfo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AddressInfoConverter {
    AddressInfoConverter INSTANCE = Mappers.getMapper(AddressInfoConverter.class);

    AddressInfoVo entity2Vo(AddressInfo addressInfo);

}
