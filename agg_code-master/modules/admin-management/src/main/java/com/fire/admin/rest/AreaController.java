package com.fire.admin.rest;


import com.fire.admin.dto.QueryAreaDto;
import com.fire.admin.service.QueryAreaService;
import com.fire.admin.task.AddressData;
import com.fire.admin.vo.QueryAreaVo;
import com.fire.dto.response.BaseRestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "区域查询接口")
@RestController
public class AreaController {

    @Resource
    private QueryAreaService queryAreaService;


    @PostMapping("/getArea")
    public BaseRestResponse<List<QueryAreaVo>> makeQueryAreaFromParentCodeInfo(@RequestBody QueryAreaDto areaParam) {
        return queryAreaService.queryAreaByParentCode(areaParam);
    }

    @GetMapping("/info/tt")
    @ApiOperation("获取区域信息")
    public BaseRestResponse getAddressInfo() {
        return new BaseRestResponse(AddressData.getAddressInfo("11"));
    }



}
