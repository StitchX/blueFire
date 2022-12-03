package com.fire.admin.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel(value = "区域查询请求参数正文")
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryAreaDto {

    @ApiModelProperty(value = "父级区划代码")
    private String parentCode;


}
