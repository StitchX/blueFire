package com.blue.business.controller;

import com.blue.business.dto.AuthDto;
import com.blue.business.service.SmsServer;
import com.fire.common.exception.BaseException;
import com.fire.dto.response.BaseRestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @Description:
 * @ClassName: AuthController
 * @Author: liuliu
 * @Date: 2022/3/21 14:45
 */
@RestController
@RequestMapping
@AllArgsConstructor
@Api(tags = "获取手机验证码及退出登录接口")
public class AuthController {

    private SmsServer noteServer;

    @ApiOperation("获取手机验证码")
    @PostMapping("/code")
    public BaseRestResponse makeAuthCode(@RequestBody AuthDto authDto) {
        noteServer.sendMsg(authDto.getPhone());
        return new BaseRestResponse();
    }

    @ApiOperation("退出登录")
    @PostMapping("/login/out")
    public BaseRestResponse loginOut() {
        return new BaseRestResponse("success");
    }

}
