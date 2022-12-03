package com.blue.code.entity;

import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@ApiModel(value = "商户表实体")
@Data
@Builder
public class Merchant {

        @ApiModelProperty(value = "商户ID")
        private Long merchantId;
    
        @ApiModelProperty(value = "经营场所")
        private String businessPlace;
    
        @ApiModelProperty(value = "店头照图片地址")
        private String shopHeadPhotoUrl;
    
        @ApiModelProperty(value = "公示图片地址")
        private String publicityPictureUrl;
    
        @ApiModelProperty(value = "小程序码图片地址")
        private String appletCodeUrl;
    
        @ApiModelProperty(value = "巡查编码？是否设计为动态变化的， 巡检员用巡检端扫，完成巡检")
        private String patrolCode;
    
        @ApiModelProperty(value = "状态:1.已入驻2.已预约3.已开卡4.已使用")
        private Integer status;
    
        @ApiModelProperty(value = "餐饮行业类型存的编码未找到 1：餐饮 2：流通")
        private Integer industryType;
    
        @ApiModelProperty(value = "监管所名称，保留冗余字段")
        private String supervisionName;
    
        @ApiModelProperty(value = "店铺名称")
        private String storeName;
    
        @ApiModelProperty(value = "经营人姓名")
        private String operatorName;
    
        @ApiModelProperty(value = "经营人手机号")
        private String operatorPhone;
    
        @ApiModelProperty(value = "详细地址")
        private String address;
    
        @ApiModelProperty(value = "地区码")
        private String addressCode;
    
        @ApiModelProperty(value = "信用等级 1:A 2:B 3:C 4:D")
        private Integer creditLevel;
    
        @ApiModelProperty(value = "银行类型:如1-建行 2-天府银行")
        private Integer bankType;
    
        @ApiModelProperty(value = "创建者")
        private String creator;
    
        @ApiModelProperty(value = "创建时间")
        private Date createTime;
    
        @ApiModelProperty(value = "修改者")
        private String updater;
    
        @ApiModelProperty(value = "修改时间")
        private Date updateTime;
    
        @ApiModelProperty(value = "监管所id")
        private Long supervisionId;
    
        @ApiModelProperty(value = "经营人身份证号")
        private String idCard;
    
        @ApiModelProperty(value = "名片二维码图片地址")
        private String businessCardUrl;
    
        @ApiModelProperty(value = "支付二维码图片地址")
        private String payCodeUrl;
    
        @ApiModelProperty(value = "商户健康码信息(暂时保留)")
        private String merchantHealthCode;
    
        @ApiModelProperty(value = "商铺面积")
        private Integer storeArea;
    
        @ApiModelProperty(value = "距离学校50米附近")
        private Integer awayFromSchool;
    
        @ApiModelProperty(value = "推荐人")
        private String referrer;
    
        @ApiModelProperty(value = "推荐人电话")
        private String referrerPhone;
    
        @ApiModelProperty(value = "是否加入微企标识")
        private Integer wechatCompanyFlag;
    
        @ApiModelProperty(value = "对应BD的ID")
        private Long bdId;
    
        @ApiModelProperty(value = "业务员")
        private String bd;
    
        @ApiModelProperty(value = "业务经理")
        private String bdm;
    
        @ApiModelProperty(value = "第一次支付的时间")
        private Date firstPayTime;
    
        @ApiModelProperty(value = "是否是测试商户 0:是 1:否")
        private Integer isTest;
    
        @ApiModelProperty(value = "逻辑删除标记 0-正常 1-已删除")
        private Integer isDelete;
    


}
