package com.fire.admin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description:
 * @date: 2022-02-10 14:15
 */
@ApiModel("信用信息公示接口数据对象")
@Data
public class PublicInformationDataVo extends CommonFieldVo{

    @ApiModelProperty("当日信息公示数量")
    private Integer publicInformationCount;
}
