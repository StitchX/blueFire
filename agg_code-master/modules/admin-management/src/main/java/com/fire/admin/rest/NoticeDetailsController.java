package com.fire.admin.rest;

import com.fire.admin.dto.NoticeDetailsDto;
import com.fire.admin.mapper.NoticeDetailsMapper;
import com.fire.admin.service.NoticeDetailsService;
import com.fire.dto.response.BaseRestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @date: 2022-02-21 22:36
 */
@Api(tags = "通知详情相关接口")
@RestController
@RequestMapping("/notice/details")
public class NoticeDetailsController {

    @Resource
    private NoticeDetailsService noticeDetailsService;



    @PostMapping("/list")
    @ApiOperation("通知详情列表")
    public BaseRestResponse noticeDetailsList(@RequestBody NoticeDetailsDto noticeDetailsDto) {
        return new BaseRestResponse(noticeDetailsService.noticeDetailsList(noticeDetailsDto));
    }

    @ApiOperation("成功率计算")
    @PostMapping("/percent")
    public BaseRestResponse noticeDetailsPercent() {
        List<Long> list = new ArrayList<>();
        list.add(1648108770904L);
        return new BaseRestResponse(noticeDetailsService.selectNoticeDetailPercent(list));
    }
}
