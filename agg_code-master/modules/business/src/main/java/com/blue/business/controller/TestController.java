package com.blue.business.controller;

import com.blue.business.domain.MerchantUser;
import com.blue.business.service.MerchantUserService;
import com.blue.business.service.SmsServer;
import com.blue.business.util.RedisUtil;
import com.fire.dto.enums.RedisKey;
import com.fire.dto.response.BaseRestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Description:
 * @ClassName: TestController
 * @Author: liuliu
 * @Date: 2022/3/21 11:43
 */
@RestController
@RequestMapping("/test")
@Api(tags = "测试接口")
public class TestController {

    @Resource
    private SmsServer noteServer;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private MerchantUserService iSysUserService;

    @ApiOperation("获取登录短信验证码")
    @GetMapping("/{phone}")
    public BaseRestResponse getNoteCode(@PathVariable String phone) {
        noteServer.sendMsg(phone);
        return new BaseRestResponse();
    }

    @ApiOperation("根据手机号码获取验证码，并检测验证码过期时间")
    @GetMapping("/valid/{phone}")
    public BaseRestResponse getCodeValid(@PathVariable String phone) {
        return new BaseRestResponse(redisUtil.hget(RedisKey.BUSINESS_LGOIN_INCR.key(), phone));
    }

    @ApiOperation("根据手机号码获取登录的用户")
    @GetMapping("/user/{phone}")
    public BaseRestResponse<MerchantUser> getLoginUser(@PathVariable String phone){
        return new BaseRestResponse<MerchantUser>(iSysUserService.querySecurityUser(phone));
    }


}
