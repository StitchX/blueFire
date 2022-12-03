package com.fire.admin.converter;

import com.fire.admin.entity.SupervisionBureau;
import com.fire.admin.vo.SupervisionBureauOptionsVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SupervisionBureauConverter {
    SupervisionBureauConverter INSTANCE = Mappers.getMapper(SupervisionBureauConverter.class);

    @Mapping(source = "supervisionId", target = "supervisionId")
    @Mapping(source = "supervisionName", target = "supervisionName")
    @Mapping(source = "parentSupervisionId", target = "parentSupervisionId")
    List<SupervisionBureauOptionsVo> entities2OptionsVos(List<SupervisionBureau> source);
}
