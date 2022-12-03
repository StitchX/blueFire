package com.fire.admin.rest;

import com.fire.admin.util.EnumUtils;
import com.fire.dto.response.BaseRestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: liuliu
 * @ClassName: ProvinceController
 * @Description: 获取省份列表
 * @date: 2021-05-18 15:44
 */
@Api(tags = "省份接口")
@RestController
@RequestMapping("/province")
public class ProvinceController {


    @ApiOperation(("通过省份名称获取省份编号"))
    @GetMapping("/code")
    public String getProvinceCode(String name) {
        return EnumUtils.convert(name);
    }


}
