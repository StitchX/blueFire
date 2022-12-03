package com.blue.crm.rest;

import com.blue.crm.request.SupervisionBureauParam;
import com.blue.crm.service.SupervisionBureauService;
import com.fire.dto.response.BaseRestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/supervisionbureau")
@Api(tags = "监管所、监管局")
public class SupervisionBureauController {

    @Autowired
    private SupervisionBureauService supervisionBureauService;

    @ApiOperation("根据区域id查询监管所")
    @PostMapping("/options")
    public BaseRestResponse getSupervisionBureauIds(@RequestBody SupervisionBureauParam param) {
        return new BaseRestResponse(supervisionBureauService.selectByAddressId(param.getAddressId()));
    }
}
