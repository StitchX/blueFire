package com.fire.admin.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fire.admin.query.PageQuery;
import com.fire.admin.service.SupervisionManService;
import com.fire.admin.util.PreSecurityUser;
import com.fire.admin.util.SecurityUtil;
import com.fire.admin.vo.SupervisionManVO;
import com.fire.dto.response.BaseRestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Cohen
 * @version 0.0.1
 * @description SupervisionManController
 * @since 2022/1/24 11:33
 */
@Api(value = "SupervisionManController", tags = "监管员列表")
@RestController
@RequestMapping("/supervisionMan")
public class SupervisionManController {

    @Autowired
    private SupervisionManService supervisionManService;

    @PostMapping("/list")
    @ApiOperation(
            value = "监管员列表",
            notes = "监管员列表",
            response = BaseRestResponse.class)
    public BaseRestResponse supervisionManList(@RequestBody PageQuery query){

        Page<SupervisionManVO> result = supervisionManService.pageSupervisionMan(query);
        return new BaseRestResponse(result);
    }

}
