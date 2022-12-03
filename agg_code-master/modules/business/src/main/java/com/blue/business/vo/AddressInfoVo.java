package com.blue.business.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author : [Cohen]
 * @version : [v1.0]
 * @createTime : [2022/3/4 11:47]
 */
@ApiModel(value = "地区表实体")
@Data
public class AddressInfoVo {

    @ApiModelProperty(value = "行政区划代码")
    private String code;

    @ApiModelProperty(value = "名称")
    private String name;
}
