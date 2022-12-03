package com.blue.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blue.business.vo.IndexMerchantVo;
import com.blue.business.vo.MerchantDetailVo;
import com.fire.dto.entity.Merchant;
import com.fire.dto.response.BaseRestResponse;

public interface MerchantService extends IService<Merchant> {

    BaseRestResponse<IndexMerchantVo> indexInfo();

    BaseRestResponse merchantDetail();

    BaseRestResponse editMerchant(MerchantDetailVo merchantDetailVo);
}
