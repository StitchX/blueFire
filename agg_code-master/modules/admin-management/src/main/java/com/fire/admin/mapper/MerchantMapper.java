package com.fire.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fire.admin.dto.NoticemMerchantDto;
import com.fire.admin.entity.Merchant;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

@Mapper
public interface MerchantMapper extends BaseMapper<Merchant> {

    /**
     *@description:  获取商户信息
     * @param page ： 分页对象
     * @param noticemMerchantDto ：通知功能获取商户的查询条件
     *@return: com.baomidou.mybatisplus.core.metadata.IPage<com.fire.admin.entity.Merchant>
     *@author: liuliu
     *@date: 2022-02-24 11:31
    */
    IPage<Merchant> selectMerchantByNotice(Page<Merchant> page, @Param("query") NoticemMerchantDto noticemMerchantDto);


}
