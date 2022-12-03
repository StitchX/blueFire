package com.blue.crm.rest;


import com.blue.crm.request.QueryAreaParam;
import com.blue.crm.service.QueryAreaService;
import com.blue.crm.vo.QueryAreaVo;
import com.fire.dto.response.BaseRestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "地区")
@RestController
@RequestMapping("area")
public class AreaController {

    @Resource
    private QueryAreaService queryAreaService;


    @PostMapping("/list")
    @ApiOperation(value = "下拉查询")
    public BaseRestResponse<List<QueryAreaVo>> makeQueryAreaFromParentCodeInfo(@RequestBody QueryAreaParam areaParam) {
        return queryAreaService.queryAreaByParentCode(areaParam);
    }

}
