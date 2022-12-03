package com.fire.admin.rest;

import com.fire.admin.dto.NoticeDto;
import com.fire.admin.service.NoticeService;
import com.fire.dto.response.BaseRestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author yll
 * @Description:
 * @date: 2022-02-21 22:12
 */
@Api(tags = "通知相关接口")
@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Resource
    private NoticeService noticeService;

    @ApiOperation("通知列表")
    @PostMapping("/list")
    public BaseRestResponse noticeList(@RequestBody NoticeDto noticeDto) {
        return new BaseRestResponse(noticeService.noticeList(noticeDto));
    }

    @ApiOperation(value = "新增通知")
    @PostMapping("/add")
    public BaseRestResponse addNotice(@RequestBody @Valid NoticeDto noticeDto, BindingResult results) {
        return results.hasErrors() ? BaseRestResponse.error(results.getFieldError().getDefaultMessage()) : new BaseRestResponse(noticeService.insertNotice(noticeDto));
    }

    @PostMapping("/noticeId")
    @ApiOperation("根据通知id获取详情")
    public BaseRestResponse getNoticeDetailsById(@RequestBody NoticeDto noticeDto) {
        return new BaseRestResponse(noticeService.queryNoticeDetailsById(noticeDto));
    }

    @ApiOperation("修改通知")
    @PostMapping("/update")
    public BaseRestResponse modifyNotice(@RequestBody @Valid NoticeDto noticeDto, BindingResult results) {
        return results.hasErrors() ? BaseRestResponse.error(results.getFieldError().getDefaultMessage()) : new BaseRestResponse(noticeService.updateNotice(noticeDto));
    }

    @ApiOperation("删除通知")
    @PutMapping("/delete")
    public BaseRestResponse removeNotice(@RequestBody NoticeDto noticeDto) {
        return new BaseRestResponse(noticeService.deleteNoticeById(noticeDto));
    }


}
