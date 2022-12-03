package com.blue.pay.rest;


import com.blue.pay.request.MakeBankParam;
import com.blue.pay.response.MakeBankResp;
import com.blue.pay.service.BankService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "银行下单分发模块")
@RestController
@Slf4j
public class BankDistribution {

    @Resource
    private BankService bankService;

    //产生银行支付订单
    @PostMapping("/distribution/makeorder.ws")
    public MakeBankResp makeOrder(@RequestBody MakeBankParam makeBankParam) {
        return bankService.makeOrder(makeBankParam);
    }


}
