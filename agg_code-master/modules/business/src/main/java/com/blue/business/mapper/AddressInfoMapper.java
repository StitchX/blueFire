package com.blue.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fire.dto.entity.AddressInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 地区表(AddressInfo)表数据库访问层
 *
 * @author ZJQ  2022-01-26 11:48:16
 */
@Mapper
public interface AddressInfoMapper extends BaseMapper<AddressInfo> {


}

