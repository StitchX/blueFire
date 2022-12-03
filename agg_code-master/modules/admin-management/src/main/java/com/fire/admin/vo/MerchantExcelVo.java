package com.fire.admin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @author: dw
 * @Description:
 * @date: 2022-01-25 15:19
 */
@ApiModel("商户列表导出数据对象")
@Builder
@Data
public class MerchantExcelVo {

    @ApiModelProperty("商户id不导出，条件判断用")
    private Long merchantId;

    @ApiModelProperty("店铺名称")
    private String storeName;

    @ApiModelProperty("经营人姓名")
    private String operatorName;

    @ApiModelProperty("经营人手机号")
    private String operatorPhone;

    @ApiModelProperty("监管所名称")
    private String supervisionName;

    @ApiModelProperty("经营场所")
    private String businessPlace;

    @ApiModelProperty("行业分类")
    private String industryType;

    @ApiModelProperty("收款银行")
    private String bankType;

    @ApiModelProperty("注册时间")
    private String createTime;

}
