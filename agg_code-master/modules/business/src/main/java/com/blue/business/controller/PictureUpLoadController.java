package com.blue.business.controller;

import com.blue.business.service.PictureUploadService;
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
 * @Description:
 * @date: 2022-03-23 15:39
 */
@Slf4j
@Api(tags = "文件上传")
@RestController
@RequestMapping("/upload")
public class PictureUpLoadController {

    @Resource
    private PictureUploadService pictureUploadService;

    @CrossOrigin
    @ApiOperation("上传健康证")
    @PostMapping("/health/certificate")
    public BaseRestResponse uploadHealthCertificate(MultipartFile file) throws IOException {
        return new BaseRestResponse(pictureUploadService.uploadHealthCertificate(file));
    }

    @CrossOrigin
    @ApiOperation("上传从业者证")
    @PostMapping("/employment/certificate")
    public BaseRestResponse uploadEmploymentCertificate(MultipartFile file) throws IOException {
        return new BaseRestResponse(pictureUploadService.uploadEmploymentCertificate(file));
    }

}
