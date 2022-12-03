package com.fire.admin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: admin
 * @Description:
 * @date: 2022-02-11 16:46
 */
@ApiModel("监管通知下达次数接口数据对象")
@Data
public class PolicyPreachDataVo extends CommonFieldVo{

    @ApiModelProperty("政策宣讲数")
    private Integer policyPreachCount;

    @ApiModelProperty("监管提醒数")
    private Integer regulatoryRemindCount;

}
