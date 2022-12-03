package com.fire.admin.rest;

import com.fire.admin.dto.StatisticalDto;
import cn.hutool.core.util.ObjectUtil;
import com.fire.admin.service.StatisticalService;
import com.fire.admin.util.SecurityUtil;
import com.fire.admin.vo.RegulatoryMerchantDataVo;
import com.fire.dto.response.BaseRestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.compress.utils.Lists;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Description:
 * @date: 2022-02-09 16:13
 */
@Api(tags = "监管所统计接口")
@RestController
@RequestMapping("/statistical")
public class StatisticalController {

    @Resource
    private StatisticalService statisticalService;

    @ApiOperation("顶部抬头数据")
    @PostMapping("/titleData")
    public BaseRestResponse titleData() {
        return new BaseRestResponse(statisticalService.titleData());
    }

    @ApiOperation("纳入监管商户分析")
    @PostMapping("/regulatoryMerchantData")
    public BaseRestResponse regulatoryMerchantData(@RequestBody StatisticalDto param) {
        //应前端要求此处若一条数据都没有就返空数组
        RegulatoryMerchantDataVo vo = statisticalService.regulatoryMerchantData(param);
        if (vo.getTotalMerchantCount().equals(0) || ObjectUtil.isEmpty(SecurityUtil.getUser().getSupervisionIds())) {
            return new BaseRestResponse(Lists.newArrayList());
        }
        return new BaseRestResponse(vo);
    }

    @ApiOperation("信用信息公示")
    @PostMapping("/publicInformationData")
    public BaseRestResponse publicInformationData(@RequestBody StatisticalDto param) {
        return new BaseRestResponse(statisticalService.publicInformationData(param));
    }

    @ApiOperation("三码合一活跃商户")
    @PostMapping("/activeMerchantData")
    public BaseRestResponse activeMerchantData(@RequestBody StatisticalDto param) {
        return new BaseRestResponse(statisticalService.activeMerchantData(param));
    }

    @ApiOperation("监管通知下达次数")
    @PostMapping("/policyPreachData")
    public BaseRestResponse policyPreachData(@RequestBody StatisticalDto param) {
        return new BaseRestResponse(statisticalService.policyPreachData(param));
    }

}
