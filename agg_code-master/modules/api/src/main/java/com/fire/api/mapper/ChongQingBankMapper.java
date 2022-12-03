package com.fire.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fire.api.vo.ChongQingBankVo;
import com.fire.dto.bank.ChongQingBank;
import org.springframework.stereotype.Repository;

/**
 * @author: admin
 * @Description:
 * @date: 2022-01-04 15:10
 */
@Repository
public interface ChongQingBankMapper extends BaseMapper<ChongQingBank> {

    /**
     * 密钥上传
     */
    int saveSecretKey(ChongQingBank chongQingBank);

    /**
     * 密钥列表带分页
     */
    IPage<ChongQingBankVo> querySecretKeyList(Page page);

    /**
     * 根据商户id获取商户名称
     */
    ChongQingBankVo queryBusinessName(Long id);

    /**
     * 获取已有的商户id
     */
    ChongQingBankVo getExistBusinessId(String businessId);

    /**
     * 获取已有的放心码id
     *
     * @param codeId
     * @return
     */
    ChongQingBankVo getExistCodeId(Long codeId);

}
