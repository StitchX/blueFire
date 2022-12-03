package com.fire.admin.service;

import com.fire.admin.vo.OssUploading;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @Description:
 * @ClassName: OSSService
 * @Author: liuliu
 * @Date: 2022/2/23 11:04
 */
public interface OssService {

    OssUploading ossUpload(MultipartFile multipartFile) ;

}
