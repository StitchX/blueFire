package com.fire.admin.rest;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fire.admin.annotation.FireOperationLog;
import com.fire.admin.dto.NoticemMerchantDto;
import com.fire.admin.dto.PageDTO;
import com.fire.admin.entity.Merchant;
import com.fire.admin.query.MerchantQuery;
import com.fire.admin.service.MerchantService;
import com.fire.admin.util.PreSecurityUser;
import com.fire.admin.util.SecurityUtil;
import com.fire.dto.constants.LogModuleConstant;
import com.fire.dto.response.BaseRestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Cohen
 * @version 0.0.1
 * @description MerchantController
 * @since 2022/1/24 11:33
 */
@Api(value = "MerchantController", tags = "商户相关接口")
@RestController
@RequestMapping("/merchant")
public class MerchantController {

    @Autowired
    private MerchantService merchantService;

    @PostMapping("/list")
    @ApiOperation(
            value = "商户列表",
            notes = "商户列表",
            response = BaseRestResponse.class)
    public BaseRestResponse merchantList(@RequestBody MerchantQuery query) {
        PreSecurityUser user = (PreSecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user != null) {
            query.setSupervisionIds(user.getSupervisionIds());
        }

        Page<Merchant> result = merchantService.pageMerchant(query);
        return new BaseRestResponse(result);
    }

    @GetMapping("/export")
    @ApiOperation("导出商户列表")
    @FireOperationLog(description = "导出商户列表", module = LogModuleConstant.MERCHANT)
    public BaseRestResponse merchantInfoExport(MerchantQuery query, HttpServletResponse response) {
        return new BaseRestResponse(merchantService.merchantInfoExport(query, response));
    }

    @PostMapping("/notice/info")
    @ApiOperation("根据账户所在的监管局或者监管所编号获取其所在监管所的商户")
    public BaseRestResponse<IPage<Merchant>> getMerchantListByNotice(@RequestBody NoticemMerchantDto noticemMerchantDto) {
         Set<Long> supervisionIds = SecurityUtil.getUser().getSupervisionIds();
        noticemMerchantDto.setSupervisionIds(supervisionIds);
        return new BaseRestResponse<>(merchantService.queryMerchantBySupervisionIds(noticemMerchantDto));
    }

}
