package com.blue.crm.rest;

import com.blue.crm.service.PictureUploadService;
import com.fire.dto.response.BaseRestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author: fcq
 * @ClassName: PictureUpLoadController
 * @Description:
 * @date: 2022-02-21 14:39
 */
@Slf4j
@Api(tags = "文件上传")
@RestController
@RequestMapping("/picture")
public class PictureUpLoadController {

    @Resource
    private PictureUploadService pictureUploadService;

    @CrossOrigin
    @ApiOperation("营业执照上传")
    @PostMapping("/businessLicense")
    public BaseRestResponse businessLicense(MultipartFile file) throws IOException {
        return pictureUploadService.businessLicense(file);
    }

    @CrossOrigin
    @ApiOperation("食品经营许可证上传")
    @PostMapping("/foodBusinessLicense")
    public BaseRestResponse foodBusinessLicense(MultipartFile file) throws IOException {
        return pictureUploadService.foodBusinessLicense(file);
    }
}
