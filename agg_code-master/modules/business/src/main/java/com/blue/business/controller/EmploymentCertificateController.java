package com.blue.business.controller;

import com.blue.business.dto.EmploymentCertificateDto;
import com.blue.business.service.EmploymentCertificateService;
import com.fire.dto.response.BaseRestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Description:
 * @date: 2022-03-21 10:54
 */
@Api(tags = "从业证接口")
@RestController
@RequestMapping("/employment/certificate")
public class EmploymentCertificateController {

    @Resource
    private EmploymentCertificateService employmentCertificateService;

    @PostMapping("/list")
    @ApiOperation("从业证列表")
    public BaseRestResponse employmentCertificateList(@RequestBody EmploymentCertificateDto dto) {
        return new BaseRestResponse(employmentCertificateService.employmentCertificateList(dto));
    }

    @PostMapping("/save")
    @ApiOperation("新增从业证")
    public BaseRestResponse SaveEmploymentCertificate(@RequestBody EmploymentCertificateDto dto) {
        return new BaseRestResponse(employmentCertificateService.addEmploymentCertificate(dto));
    }

    @DeleteMapping("/remove/{id}")
    @ApiOperation("删除从业证")
    public BaseRestResponse removeEmploymentCertificate(@PathVariable Long id) {
        return new BaseRestResponse(employmentCertificateService.removeEmploymentCertificate(id));
    }
}
