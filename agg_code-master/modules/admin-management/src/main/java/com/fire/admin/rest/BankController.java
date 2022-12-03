package com.fire.admin.rest;

import com.fire.admin.entity.Bank;
import com.fire.admin.service.BankService;
import com.fire.dto.response.BaseRestResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author : [Cohen]
 * @version : [v1.0]
 * @createTime : [2022/1/25 18:25]
 */
@RestController
@RequestMapping("/bank")
public class BankController {

    @Autowired
    private BankService bankService;


    @PostMapping("/options")
    @ApiOperation(
            value = "银行下拉菜单",
            notes = "银行下拉菜单",
            response = BaseRestResponse.class)
    public BaseRestResponse bankOptions() {
        List<Bank> result = bankService.bankOptions();
        return new BaseRestResponse(result);
    }
}
