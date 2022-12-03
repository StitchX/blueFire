package com.fire.admin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @Description:
 * @date: 2022-02-09 16:42
 */
@ApiModel("顶部抬头数据接口数据对象")
@Data
@Builder
public class TitleDataVo {

    @ApiModelProperty("纳入监管商户总数")
    private Integer totalRegulatoryMerchantCount;

    @ApiModelProperty("当日新纳入监管商户")
    private Integer todayRegulatoryMerchantCount;

    @ApiModelProperty("信用信息公示总数")
    private Integer totalPubulicInformationCount;

    @ApiModelProperty("当日信用信息公示")
    private Integer todayPubilicInformationCount;

    @ApiModelProperty("本年监管提醒")
    private Long yearRegulatoryRemindCount;

    @ApiModelProperty("本年政策宣讲")
    private Integer yearPolicyPreachCount;

    @ApiModelProperty("红码数量")
    private Integer redCount;

    @ApiModelProperty("黄码数量")
    private Integer yellowCount;

    @ApiModelProperty("绿码数量")
    private Integer greenCount;

}
