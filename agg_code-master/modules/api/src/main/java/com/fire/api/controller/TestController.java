package com.fire.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @ClassName: TestController
 * @Author: liuliu
 * @Date: 2022/1/6 17:02
 */
@Api(tags = "测试")
@RestController
@RequestMapping("/test")
public class TestController {

    @ApiOperation("测试专用")
    @GetMapping()
    public String hello() {
        return "SUCCESS HELLO ";
    }

}
