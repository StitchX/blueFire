package com.blue.business.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.blue.business.dto.NoticeDto;
import com.blue.business.service.NoticeDetailsService;
import com.blue.business.service.NoticeService;
import com.blue.business.vo.NoticeVo;
import com.fire.dto.response.BaseRestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @ClassName: NoticeController
 * @Author: liuliu
 * @Date: 2022/3/23 17:00
 */
@RestController
@RequestMapping("/notice")
@AllArgsConstructor
@Api(tags = "商户通知接口")
public class NoticeController {

    private NoticeService noticeService;

    private NoticeDetailsService noticeDetailsService;

    @ApiOperation("商户通知列表")
    @PostMapping("/page")
    public BaseRestResponse<IPage<NoticeVo>> getNoticePage(@RequestBody NoticeDto noticeDto) {
        return new BaseRestResponse<>(noticeService.selectNoticePageInfo(noticeDto));
    }

    @ApiOperation("通知详情编号")
    @PostMapping("/details/info")
    public BaseRestResponse getNoticeDetails(@RequestBody NoticeDto noticeDto) {
        return new BaseRestResponse(noticeService.selectNoticeDetails(noticeDto));
    }

    @ApiOperation("通知详情改为已读")
    @PostMapping("/details/read")
    public BaseRestResponse getNoticeDetailsRead(@RequestBody NoticeDto noticeDto) {
        return new BaseRestResponse(noticeDetailsService.updateNoticeDetails(noticeDto));
    }
}
