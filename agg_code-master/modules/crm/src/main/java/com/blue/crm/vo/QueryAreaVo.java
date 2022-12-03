package com.blue.crm.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "区域查询返回信息正文")
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryAreaVo {

    @ApiModelProperty(value = "区域行政编码")
    private String code;

    @ApiModelProperty(value = "区域名称")
    private String name;
}
