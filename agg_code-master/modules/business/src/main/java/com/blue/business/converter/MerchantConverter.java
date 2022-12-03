package com.blue.business.converter;

import com.blue.business.vo.IndexMerchantVo;
import com.blue.business.vo.MerchantDetailVo;
import com.fire.dto.cacheDto.RedisMerchantInfo;
import com.fire.dto.entity.Merchant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MerchantConverter {
    MerchantConverter INSTANCE = Mappers.getMapper(MerchantConverter.class);

    MerchantDetailVo entity2Vo(Merchant merchant);

    @Mappings({
            @Mapping(target = "merchantName", source = "storeName"),
            @Mapping(target = "riskLevel", source = "creditLevel"),
            @Mapping(target = "linkedPhone", source = "operatorPhone"),
            @Mapping(target = "businessAddress", source = "address"),
            @Mapping(target = "healthCodeType", source = "merchantHealthCode"),
            @Mapping(target = "bankId", source = "bankType"),
            @Mapping(target = "areaId", source = "addressCode"),
            @Mapping(target = "industryClassificationId", source = "industryType")
    })
    RedisMerchantInfo merchantToRedisMerchantInfo(Merchant merchant);

    List<RedisMerchantInfo> entities2Redis(List<Merchant> merchants);

    IndexMerchantVo entity2IndexMerchantVo(Merchant merchant);
    Merchant merchantDetailVo2Merchant(MerchantDetailVo merchantDetailVo);

}
