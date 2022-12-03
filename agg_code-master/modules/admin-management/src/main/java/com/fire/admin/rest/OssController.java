package com.fire.admin.rest;

import cn.hutool.json.JSONUtil;
import com.fire.admin.service.OssService;
import com.fire.admin.util.OssUploadFileTypeList;
import com.fire.admin.vo.OssUploading;
import com.fire.dto.response.BaseRestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @Description: 文件上传
 * @ClassName: OSSController
 * @Author: liuliu
 * @Date: 2022/2/23 11:01
 */
@RestController
@RequestMapping("/oss")
@Api(tags = "OSS 接口")
@AllArgsConstructor
@Slf4j
public class OssController {

    private final OssService ossService;


    @PostMapping("/up")
    @ApiOperation("文件上传")
    public BaseRestResponse<OssUploading> upFile(MultipartFile multipartFile) {
        return new BaseRestResponse(ossService.ossUpload(multipartFile));
    }




}
