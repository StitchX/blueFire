package com.fire.admin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description:
 * @date: 2022-02-11 17:18
 */
@ApiModel("公共字段")
@Data
public class CommonFieldVo {

    @ApiModelProperty("监管所Id")
    private Long supervisionId;

    @ApiModelProperty("监管所名称")
    private String supervisionName;
}
