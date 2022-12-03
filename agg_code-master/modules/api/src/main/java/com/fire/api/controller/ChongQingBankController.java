package com.fire.api.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fire.api.request.ChongQingBankParam;
import com.fire.api.service.ChongQingBankService;
import com.fire.api.vo.ChongQingBankVo;
import com.fire.dto.response.BaseResponse;
import com.fire.dto.response.BaseRestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author: admin
 * @Description:
 * @date: 2022-01-05 9:53
 */
@Api(tags = "重庆银行相关接口")
@RestController
@RequestMapping("/chongqing/bank")
@Slf4j
public class ChongQingBankController {

    @Resource
    private ChongQingBankService chongQingBankService;

    @ApiOperation("密钥上传")
    @PostMapping("/secret/add")
    public BaseResponse saveSecretKey(@RequestBody ChongQingBankParam param) {
        try {
            chongQingBankService.saveSecretKey(param);
        } catch (Exception e) {
            log.error("密钥上传异常！", e);
        }
        return new BaseResponse();
    }

    @ApiOperation("密钥列表")
    @PostMapping("/secret/list")
    public BaseRestResponse querygetSecretKeyList(@RequestBody ChongQingBankParam param) {
        IPage<ChongQingBankVo> page = null;
        try {
            page = chongQingBankService.getSecretKeyList(param);
        } catch (Exception e) {
            log.error("获取密钥列表异常！", e);
        }
        return new BaseRestResponse(page);
    }

    @ApiOperation("获取回显的商户名称")
    @PostMapping("/secret/name")
    public BaseRestResponse queryBusinessName(@RequestBody ChongQingBankParam param) {
        ChongQingBankVo businessName = null;
        try {
            businessName = chongQingBankService.getBusinessName(param.getId());
        } catch (Exception e) {
            log.error("获取回显商户名称异常！", e);
        }
        return new BaseRestResponse(businessName);
    }

}
