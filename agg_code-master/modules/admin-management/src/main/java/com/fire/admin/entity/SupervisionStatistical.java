package com.fire.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Description:
 * @date: 2022-02-09 16:21
 */
@ApiModel("统计表实体")
@Data
@TableName("supervision_statistical_data")
public class SupervisionStatistical {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("日期(格式：yyyy-mm-dd)")
    private Date statisticalDate;

    @ApiModelProperty("监管所id")
    private Long supervisionId;

    @ApiModelProperty("监管所名称")
    private String supervisionName;

    @ApiModelProperty("监管商户总数")
    private Integer totalRegulatoryMerchantCount;

    @ApiModelProperty("三码合一商户总数")
    private Integer totalRegulatoryPayMerchantCount;

    @ApiModelProperty("活跃商户总数")
    private Integer totalRegulatoryActiveMerchantCount;

    @ApiModelProperty("信用信息公示总数")
    private Long totalPubulicInformationCount;

    @ApiModelProperty("当日监管商户数量")
    private Integer todayRegulatoryMerchantCount;

    @ApiModelProperty("当日三码合一商户总数")
    private Integer todayRegulatoryPayMerchantCount;

    @ApiModelProperty("当日活跃商户数量")
    private Integer todayRegulatoryActiveMerchantCount;

    @ApiModelProperty("当日信息公示数量")
    private Long todayPubulicInformationCount;

    @ApiModelProperty("当日风险场所红码总数")
    private Integer todayRiskRedCount;

    @ApiModelProperty("当日风险场所黄码总数")
    private Integer todayRiskYellowCount;

    @ApiModelProperty("当日风险场所绿码总数")
    private Integer todayRiskGreenCount;

    @ApiModelProperty("本年监管提醒总数")
    private Integer yearRegulatoryRemindCount;

    @ApiModelProperty("本年政策宣讲总数")
    private Integer yearPolicyPreachCount;

    @ApiModelProperty("当日监管提醒数量")
    private Integer todayRegulatoryRemindCount;

    @ApiModelProperty("当日政策宣讲数量")
    private Integer todayPolicyPreachCount;
}
