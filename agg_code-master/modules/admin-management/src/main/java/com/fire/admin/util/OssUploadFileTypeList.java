package com.fire.admin.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description: oss 文件上传格式
 * @ClassName: OssUploadFileTypeList
 * @Author: liuliu
 * @Date: 2022/3/10 11:07
 */
@ConfigurationProperties(prefix = "file")
@Component
@Data
public class OssUploadFileTypeList {

    private List<String> type;

}
