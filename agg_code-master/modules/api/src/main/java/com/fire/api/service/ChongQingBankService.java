package com.fire.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fire.api.request.ChongQingBankParam;
import com.fire.api.vo.ChongQingBankVo;
import com.fire.dto.bank.ChongQingBank;

/**
 * @author: admin
 * @Description:
 * @date: 2022-01-04 15:06
 */
public interface ChongQingBankService extends IService<ChongQingBank> {

    /**
     * 密钥上传
     * @param param 重庆银行请求数据传输对象
     */
    void saveSecretKey(ChongQingBankParam param);

    /**
     * 密钥列表带分页
     * @param param 重庆银行请求数据传输对象
     * @return 列表分页数据
     */
    IPage<ChongQingBankVo> getSecretKeyList(ChongQingBankParam param);

    /**
     * 获取回显商户名称
     * @param id 放心码商户id
     * @return 回显商户名称
     */
    ChongQingBankVo getBusinessName(Long id);

}
