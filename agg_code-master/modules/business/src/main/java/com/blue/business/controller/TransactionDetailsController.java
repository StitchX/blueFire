package com.blue.business.controller;

import com.blue.business.dto.TransactionDetailsDto;
import com.blue.business.service.TransactionDetailsService;
import com.fire.dto.response.BaseRestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author: admin
 * @Description:
 * @date: 2022-03-22 14:51
 */
@Api(tags = "交易明细接口")
@RestController
@RequestMapping("/TransactionDetails")
public class TransactionDetailsController {

    @Resource
    private TransactionDetailsService transactionDetailsService;

    @ApiOperation("交易明细查询接口")
    @PostMapping("/list")
    public BaseRestResponse transactionDetails(@RequestBody TransactionDetailsDto dto) {
        return new BaseRestResponse(transactionDetailsService.getTransactionDetails(dto));
    }
}
