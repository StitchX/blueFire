package com.blue.business.controller;

import com.blue.business.service.BankService;
import com.fire.dto.response.BaseRestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : [Cohen]
 * @version : [v1.0]
 * @createTime : [2022/1/25 18:25]
 */
@RestController
@RequestMapping("/bank")
@Api(tags = "银行")
public class BankController {

    @Autowired
    private BankService bankService;

    @PostMapping("/options")
    @ApiOperation(
            value = "下拉查询",
            response = BaseRestResponse.class)
    public BaseRestResponse bankOptions() {
        return new BaseRestResponse(bankService.list());
    }
}
