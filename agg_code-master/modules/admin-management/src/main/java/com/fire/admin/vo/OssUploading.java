package com.fire.admin.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @Description: oss 文件上传返回的对象
 * @ClassName: OssUploading
 * @Author: liuliu
 * @Date: 2022/3/1 17:03
 */
@Data
@Builder
public class OssUploading {

    @ApiModelProperty("文件名称")
    private String fileName;

    @ApiModelProperty("文件上传地址")
    private String ossFilePath;

}
