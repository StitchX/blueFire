package com.blue.crm.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel(value = "区域查询请求参数正文")
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryAreaParam {

    @ApiModelProperty(value = "父级区划代码")
    private String parentCode;

    @ApiModelProperty(value = "区域等级")
    private int level = 1;
}
