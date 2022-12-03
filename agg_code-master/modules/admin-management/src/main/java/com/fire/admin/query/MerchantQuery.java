package com.fire.admin.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Cohen
 * @version 0.0.1
 * @description MerchantQuery
 * @since 2022/1/24 11:37
 */

@Data
@ApiModel("商户查询参数")
public class MerchantQuery extends PageQuery {
    @ApiModelProperty("注册开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Timestamp startTime;
    @ApiModelProperty("注册结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Timestamp endTime;
    @ApiModelProperty("店铺名")
    private String storeName;
    @ApiModelProperty("手机号")
    private String operatorPhone;
    @ApiModelProperty("银行类型")
    private Integer bankType;
    @ApiModelProperty("监管所ID")
    private Long supervisionId;
    @ApiModelProperty("地区街道名")
    private String address;
    @ApiModelProperty("行业类型")
    private Integer industryType;
    @ApiModelProperty("id列表")
    private List<Long> merchantIds = new ArrayList<>();
    @ApiModelProperty("经营人")
    private String operatorName;
    @ApiModelProperty("导出记录勾选列表")
    private List<Long> recordIds;
    @ApiModelProperty("账号关联监管局")
    private Set<Long> supervisionIds;
}
