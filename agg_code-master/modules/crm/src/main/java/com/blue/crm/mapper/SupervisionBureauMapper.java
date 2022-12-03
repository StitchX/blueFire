package com.blue.crm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blue.crm.entity.SupervisionBureau;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SupervisionBureauMapper extends BaseMapper<SupervisionBureau> {

    List<SupervisionBureau> selectByAddressId(Long addressId);

}
