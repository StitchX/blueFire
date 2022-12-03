package com.blue.business.service.impl;

import com.blue.business.service.PictureUploadService;
import com.fire.common.exception.BaseException;
import com.fire.common.exception.FileNameLengthLimitExceededException;
import com.fire.common.exception.InvalidExtensionException;
import com.fire.common.tencent.OSSUploadHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileUploadBase;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@Slf4j
@Service
public class PictureUploadServiceImpl implements PictureUploadService {

    //健康证COS路径前缀
    private static final String HEALTH_CERTIFICATE_PATH_PREFIX = "business/healthCertificate";

    //从业证COS路径前缀
    private static final String EMPLOYMENT_CERTIFICATE_PATH_PREFIX = "business/employmentCertificate";

    @Resource
    private OSSUploadHelper ossUploadHelper;

    public String uploadHealthCertificate(MultipartFile file) {
        if (file == null) {
            throw new BaseException("文件不能为空!");
        }

        String imageUrl;

        try {
            imageUrl = ossUploadHelper.upload(file, HEALTH_CERTIFICATE_PATH_PREFIX, OSSUploadHelper.IMAGE_EXTENSION);
            log.info("上传图片到COS，地址为{}", imageUrl);
        } catch (FileUploadBase.FileSizeLimitExceededException e) {
            throw new BaseException("文件大小超出限制!");
        } catch (InvalidExtensionException e) {
            throw new BaseException("文件类型不支持!");
        } catch (FileNameLengthLimitExceededException e) {
            throw new BaseException("文件名大小超出限制!");
        } catch (Exception e) {
            log.error("图片上传失败!", e);
            throw new BaseException("图片上传失败!");
        }

        return imageUrl;
    }

    @Override
    public String uploadEmploymentCertificate(MultipartFile file) {
        if (file == null) {
            throw new BaseException("文件不能为空!");
        }

        String imageUrl;

        try {
            imageUrl = ossUploadHelper.upload(file, EMPLOYMENT_CERTIFICATE_PATH_PREFIX, OSSUploadHelper.IMAGE_EXTENSION);
            log.info("上传图片到COS，地址为{}", imageUrl);
        } catch (FileUploadBase.FileSizeLimitExceededException e) {
            throw new BaseException("文件大小超出限制!");
        } catch (InvalidExtensionException e) {
            throw new BaseException("文件类型不支持!");
        } catch (FileNameLengthLimitExceededException e) {
            throw new BaseException("文件名大小超出限制!");
        } catch (Exception e) {
            log.error("图片上传失败!", e);
            throw new BaseException("图片上传失败!");
        }

        return imageUrl;
    }

}
