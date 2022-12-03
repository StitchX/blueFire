package com.fire.admin.rest;

import com.fire.admin.dto.PageDTO;
import com.fire.admin.dto.SupervisionBureauDTO;
import com.fire.admin.service.SupervisionBureauService;
import com.fire.admin.util.PreSecurityUser;
import com.fire.admin.vo.SupervisionBureauOptionsVo;
import com.fire.dto.response.BaseRestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description:
 * @ClassName: SupervisionBureau
 * @Author: liuliu
 * @Date: 2022/1/25 20:23
 */
@RestController
@RequestMapping("/supervisionbureau")
@Api(tags = "监管所、监管局相关的接口")
@AllArgsConstructor
@Slf4j
public class SupervisionBureauController {

    private SupervisionBureauService supervisionBureauService;

    @ApiOperation("根据用户存的监管所、监管局编号获取子监管所的编号集合")
    @GetMapping("/id")
    public BaseRestResponse getSupervisionBureauIds(Long supervisionId) {
        return new BaseRestResponse(supervisionBureauService.querySupervisionBureau(supervisionId));
    }

    @ApiOperation("获取所有监管局、监管所的树")
    @GetMapping("/tree")
    public BaseRestResponse getSupervisionBureauTree() {
        return new BaseRestResponse(supervisionBureauService.queryAllSupervisionBureauTree());
    }

    @PostMapping("/options")
    @ApiOperation(
            value = "监管所下拉菜单接口",
            notes = "监管所下拉菜单接口",
            response = BaseRestResponse.class)
    public BaseRestResponse supervisionOptions() {
        List<SupervisionBureauOptionsVo> result = supervisionBureauService.listOptions();
        return new BaseRestResponse(result);
    }

    @ApiOperation("监管所账户列表")
    @PostMapping("/account/info")
    public BaseRestResponse getSupervisionBureauAccount(@RequestBody PageDTO page) {
        return new BaseRestResponse(supervisionBureauService.querySupervisionBureauByUser(page));
    }

    @ApiOperation("监管所监管局新增")
    @PostMapping("/add")
    public BaseRestResponse addSupervisionBureau(@RequestBody SupervisionBureauDTO supervisionBureauDTO) {
        return new BaseRestResponse(supervisionBureauService.insertSupervisionBureau(supervisionBureauDTO));
    }

    @ApiOperation("监管所监管局修改")
    @PostMapping("/update")
    public BaseRestResponse modifySupervisionBureau(@RequestBody SupervisionBureauDTO supervisionBureauDTO) {
        return new BaseRestResponse(supervisionBureauService.updateSupervisionBureau(supervisionBureauDTO));
    }


    @ApiOperation("当前账户下的所有监管所")
    @PostMapping("/user/ids")
    public BaseRestResponse getAccountSupervisionBureau() {
        return new BaseRestResponse(supervisionBureauService.accountSupervisionBureau());
    }


}
