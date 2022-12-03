package com.fire.admin.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fire.admin.entity.SupervisionBureau;
import com.fire.admin.vo.SupervisionBureauOptionsVo;
import com.fire.admin.vo.SupervisionBureauVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

import java.util.Set;

@Mapper
public interface SupervisionBureauMapper extends BaseMapper<SupervisionBureau> {

    /***
     *@description: 获取当前登录状态下的监管所账户列表
     * @param page
     * @param supervisionIds
     *@return: com.baomidou.mybatisplus.core.metadata.IPage<com.fire.admin.vo.SupervisionBureauVo>
     *@author: liuliu
     *@date: 2022-01-27 18:17
     */
    IPage<SupervisionBureauVo> querySupervisionBureauByUserId(Page page, Set<Long> supervisionIds);


    @Select("select supervision_id, supervision_name, parent_supervision_id from supervision_bureau ")
    List<SupervisionBureauOptionsVo> options();
}
