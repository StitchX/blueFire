package com.blue.crm.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PageQuery {
    @ApiModelProperty(value = "页数", example = "1", dataType = "Integer")
    public Integer page = 1;
    @ApiModelProperty(value = "大小", example = "10")
    public Integer size = 10;
}
