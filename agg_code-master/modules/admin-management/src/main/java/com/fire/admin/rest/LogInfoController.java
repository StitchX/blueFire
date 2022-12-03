package com.fire.admin.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fire.admin.dto.LogInfoDto;
import com.fire.admin.service.LogInfoService;
import com.fire.dto.response.BaseRestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author: dw
 * @Description:
 * @date: 2022-01-25 10:41
 */
@Api(tags = "操作日志相关接口")
@RestController
@RequestMapping("/logInfo")
public class LogInfoController {

    @Resource
    private LogInfoService logInfoService;

    @PostMapping("/list")
    @ApiOperation(value = "分页获取日志信息", notes = "get_log_info_page.py")
    //@FireOperationLog(description = "查询日志信息", module = LogModule.MERCHANT)
    public BaseRestResponse getLogInfoByCondition(Page page, @RequestBody LogInfoDto param) {
        return new BaseRestResponse(logInfoService.getLogInfoByCondition(page, param));
    }
}
