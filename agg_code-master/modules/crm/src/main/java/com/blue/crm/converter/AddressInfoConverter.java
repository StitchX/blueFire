package com.blue.crm.converter;

import com.blue.crm.entity.AddressInfo;
import com.blue.crm.vo.AddressInfoVo;
import com.blue.crm.vo.MerchantListVo;
import com.blue.crm.vo.MerchantVo;
import com.fire.dto.entity.Merchant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface AddressInfoConverter {
    AddressInfoConverter INSTANCE = Mappers.getMapper(AddressInfoConverter.class);

    AddressInfoVo entity2Vo(AddressInfo addressInfo);

}
