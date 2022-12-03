package com.blue.business.controller;

import com.blue.business.query.StatisticsQuery;
import com.blue.business.service.IStatisticsService;
import com.fire.dto.response.BaseRestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author fcq
 * @version v2.0.8.business
 * @description 统计数据
 * @date 2022/3/22 15:28
 */
@Api(tags = "统计数据")
@RestController
@RequestMapping("statistics")
public class StatisticController {

    @Resource
    private IStatisticsService statisticsService;

    @ApiOperation("营业额、交易笔数、扫码量统计")
    @PostMapping("/count")
    public BaseRestResponse count(@RequestBody StatisticsQuery query) {
        return new BaseRestResponse(statisticsService.count(query));
    }

    @ApiOperation("交易类型占比统计")
    @PostMapping("type")
    public BaseRestResponse type(@RequestBody StatisticsQuery query) {
        return new BaseRestResponse(statisticsService.type(query));
    }

    @ApiOperation("交易金额统计")
    @PostMapping("money")
    public BaseRestResponse money(@RequestBody StatisticsQuery query) {
        return new BaseRestResponse(statisticsService.money(query));
    }

    @ApiOperation("交易笔数统计")
    @PostMapping("frequency")
    public BaseRestResponse frequency(@RequestBody StatisticsQuery query) {
        return new BaseRestResponse(statisticsService.frequency(query));
    }
}
