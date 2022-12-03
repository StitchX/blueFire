package com.fire.admin.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fire.admin.entity.SupervisionMan;
import com.fire.admin.vo.SupervisionManVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SupervisionManMapper extends BaseMapper<SupervisionMan> {

    @Select("select * from supervision_man ${ew.customSqlSegment}")
    Page<SupervisionManVO> pageSupervisionVo(Page<SupervisionManVO> query, @Param(Constants.WRAPPER) Wrapper wrapper);


    @Select("select * from supervision_man")
    Page<SupervisionManVO> pageSupervisionVoAll(Page<SupervisionManVO> query);
}
