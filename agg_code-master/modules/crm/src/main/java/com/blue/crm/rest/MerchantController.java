package com.blue.crm.rest;

import com.blue.crm.query.MerchantQuery;
import com.blue.crm.service.MerchantService;
import com.blue.crm.service.RedisLoadDataService;
import com.blue.crm.vo.GenerateEmptyCodeParams;
import com.blue.crm.vo.MerchantVo;
import com.fire.dto.response.BaseRestResponse;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : [Cohen]
 * @version : [v1.0]
 * @createTime : [2022/2/22 14:46]
 */

@RestController
@RequestMapping("/merchant")
@Api(tags = "商户相关接口")
public class MerchantController {

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private RedisLoadDataService redisLoadDataService;

    @PostMapping("/add")
    public BaseRestResponse addMerchant(@Validated @RequestBody MerchantVo merchantVo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            return BaseRestResponse.error(errorMessage);
        }
        return merchantService.addMerchant(merchantVo);
    }

    public BaseRestResponse addEmptyCodeMerchant(@RequestBody GenerateEmptyCodeParams params){
        merchantService.addEmptyCodeMerchant(params);
        return new BaseRestResponse();
    }


    @PostMapping("/update")
    public BaseRestResponse editMerchant(@Validated @RequestBody MerchantVo merchantVo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            return BaseRestResponse.error(errorMessage);
        }
        return merchantService.editMerchant(merchantVo);
    }

    @PostMapping("/list")
    public BaseRestResponse listMerchant(@RequestBody MerchantQuery query) {
        return merchantService.listMerchant(query);
    }

    @PostMapping("/detail")
    public BaseRestResponse merchantDetail(@RequestBody MerchantQuery query) {
        return merchantService.merchantDetail(query);
    }

    @PostMapping("/loadMerchantToRedis")
    public BaseRestResponse loadMerchantToRedis() {
        redisLoadDataService.loadMerchantData();
        return new BaseRestResponse();
    }
}
