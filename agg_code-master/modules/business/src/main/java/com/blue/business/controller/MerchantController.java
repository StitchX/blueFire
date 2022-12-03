package com.blue.business.controller;

import com.blue.business.service.MerchantService;
import com.blue.business.vo.IndexMerchantVo;
import com.blue.business.vo.MerchantDetailVo;
import com.fire.dto.response.BaseRestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : [Cohen]
 * @version : [v1.0]
 * @createTime : [2022/3/23 15:00]
 */

@RestController
@RequestMapping("/merchant")
@Api("商户相关接口")
public class MerchantController {

    @Autowired
    private MerchantService merchantService;

    @PostMapping("/indexInfo")
    @ApiOperation("首页商户信息")
    public BaseRestResponse<IndexMerchantVo> indexMerchantVo(){
        return merchantService.indexInfo();
    }

    @PostMapping("/detail")
    @ApiOperation("商户详情")
    public BaseRestResponse merchantDetail() {
        return merchantService.merchantDetail();
    }

    @PostMapping("/update")
    public BaseRestResponse editMerchant(@RequestBody MerchantDetailVo merchantDetailVo) {
        return merchantService.editMerchant(merchantDetailVo);
    }

}
