package com.fire.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.fire.admin.service.OssService;
import com.fire.admin.util.OssUploadFileTypeList;
import com.fire.admin.vo.OssUploading;
import com.fire.common.exception.BaseException;
import com.fire.common.tencent.TencentCos;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;

import java.util.Objects;

/**
 * @Description: oss 文件相关的功能
 * @ClassName: OSSServiceImpl
 * @Author: liuliu
 * @Date: 2022/2/23 11:05
 */
@Service
@Slf4j
public class OssServiceImpl implements OssService {

    @Resource
    private TencentCos tencentCos;

    @Value("${oss.noticePath:admin-management/notic/}")
    private String ossNoticePath;

    @Resource
    private OssUploadFileTypeList ossUploadFileTypeList;


    /**
     * @description: 文件上传
     * @return: java.util.Map<java.lang.String, java.lang.String>
     * @author: liuliu
     * @date: 2022-02-23 15:08
     */
    @Override
    public OssUploading ossUpload(MultipartFile multipartFile) {

        File file = transferToFile(multipartFile);
        String path = tencentCos.uploadFile(file, ossNoticePath.concat(file.getName()));
        if (StrUtil.isNotBlank(path)) {
            return OssUploading.builder().fileName(multipartFile.getOriginalFilename()).ossFilePath(path).build();
        }
        return null;
    }

    /**
     * @param multipartFile :传入参数
     * @description: 利用缓存区将 multipartFile 类型转 File类型
     * @return: java.io.File
     * @author: liuliu
     * @date: 2022-02-23 14:58
     */
    private File transferToFile(MultipartFile multipartFile) {
        File file = null;
        try {
            String originalFilename = multipartFile.getOriginalFilename();
            String[] filename = Objects.requireNonNull(originalFilename).split("\\.");
            try {
                if (!ossUploadFileTypeList.getType().contains(filename[1])) {
                    throw new BaseException("不支持的文件格式");
                }
                file = File.createTempFile(filename[0], ".".concat(filename[1]));
            } catch (IllegalArgumentException e) {
                throw new BaseException("文件名称长度不能小于3位");
            }
            multipartFile.transferTo(file);
            file.deleteOnExit();
        } catch (IOException e) {
            log.error("文件上传失败");
            e.printStackTrace();
        }

        return file;
    }
}
