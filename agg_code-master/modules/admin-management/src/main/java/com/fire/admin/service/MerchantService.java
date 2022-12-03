package com.fire.admin.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fire.admin.dto.NoticemMerchantDto;
import com.fire.admin.entity.Merchant;
import com.fire.admin.query.MerchantQuery;
import org.apache.poi.ss.formula.functions.T;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;

public interface MerchantService {

    Page<Merchant> pageMerchant(MerchantQuery query);

    /**
     * 商户列表导出
     *
     * @param query
     * @param response
     */
    int merchantInfoExport(MerchantQuery query, HttpServletResponse response);

    /**
     * @param noticemMerchantDto: 商户搜索条件对象
     * @description: 根据监管所获取商户
     * @return: java.util.Set<java.lang.Long>
     * @author: liuliu
     * @date: 2022-02-23 16:38
     */
    IPage<Merchant> queryMerchantBySupervisionIds(NoticemMerchantDto noticemMerchantDto);
}
