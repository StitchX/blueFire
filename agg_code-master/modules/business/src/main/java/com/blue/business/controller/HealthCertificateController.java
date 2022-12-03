package com.blue.business.controller;

import com.blue.business.dto.HealthCertificateDto;
import com.blue.business.service.HealthCertificateService;
import com.fire.dto.response.BaseRestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Description:
 * @date: 2022-03-21 11:06
 */
@Api(tags = "健康证接口")
@RestController
@RequestMapping("/health/certificate")
public class HealthCertificateController {

    @Resource
    private HealthCertificateService healthCertificateService;

    @ApiOperation("健康证列表")
    @PostMapping("/list")
    public BaseRestResponse healthCertificateList(@RequestBody HealthCertificateDto dto) {
        return new BaseRestResponse(healthCertificateService.healthCertificateList(dto));
    }

    @ApiOperation("新增健康证")
    @PostMapping("/save")
    public BaseRestResponse saveHealthCertificate(@RequestBody HealthCertificateDto dto) {
        return new BaseRestResponse(healthCertificateService.addHealthCertificate(dto));
    }

    @ApiOperation("删除健康证")
    @DeleteMapping("/remove/{id}")
    public BaseRestResponse removeHealthCertificate(@PathVariable Long id) {
        return new BaseRestResponse(healthCertificateService.removeHealthCertificate(id));
    }
}
