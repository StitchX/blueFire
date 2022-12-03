package com.blue.business.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PictureUploadService {

    /**
     * 健康证上传
     * @param file
     * @return
     * @throws IOException
     */
    String uploadHealthCertificate(MultipartFile file) throws IOException;

    /**
     * 从业证上传
     * @param file
     * @return
     * @throws IOException
     */
    String uploadEmploymentCertificate(MultipartFile file) throws IOException;

}
