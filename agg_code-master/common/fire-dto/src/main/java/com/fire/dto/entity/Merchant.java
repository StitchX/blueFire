package com.fire.dto.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;

@Data
@TableName("merchant")
@ApiModel("商户")
public class Merchant {
    /**
     * 商户ID
     * 历史数据ID不变，新数据用新的规则，主键自增
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long merchantId;
    /**
     * 经营场所
     */
    @ApiModelProperty("经营场所")
    @Length(max = 128, message = "经营场所字数不能超过128")
    private String businessPlace;
    /**
     * 店头照图片地址
     */
    @ApiModelProperty("店头照图片地址")
    private String shopHeadPhotoUrl;
    /**
     * 公示图片地址
     */
    @ApiModelProperty("公示图片地址")
    private String publicityPictureUrl;
    /**
     * 小程序码图片地址
     */
    @ApiModelProperty("小程序码图片地址")
    private String appletCodeUrl;
    /**
     * 巡查编码
     * ？是否设计为动态变化的，
     * 巡检员用巡检端扫，完成巡检
     */
    @ApiModelProperty("巡查编码")
    private String patrolCode;

    @ApiModelProperty("状态")
    private Integer status;
    /**
     * 餐饮行业类型存的编码未找到
     * 1：餐饮 2：流通
     */
    @ApiModelProperty("餐饮行业类型存的编码未找到")
    private Long industryType;
    /**
     * 监管所名称
     * ，保留冗余字段
     */
    @ApiModelProperty("监管所名称")
    private String supervisionName;
    /**
     * 店铺名称
     */
    @ApiModelProperty("店铺名称")
    @NotBlank(message = "店铺名称不能为空")
    @Length(max = 128, message = "店铺名称字数不能超过128")
    private String storeName;
    /**
     * 经营人姓名
     */
    @ApiModelProperty("经营人姓名")
    @NotBlank(message = "经营人姓名不能为空")
    @Length(max = 32, message = "经营人姓名字数不能超过32")
    private String operatorName;
    /**
     * 经营人手机号
     */
    @ApiModelProperty("经营人手机号")
    @NotBlank(message = "经营人手机号不能为空")
    @Length(max = 16, message = "经营人手机号字数不能超过16")
    private String operatorPhone;
    /**
     * 详细地址
     */
    @ApiModelProperty("详细地址")
    @Length(max = 256, message = "实际地址字数不能超过256")
    private String address;
    /**
     * 地区码
     */
    @ApiModelProperty("地区码")
    private String addressCode;
    /**
     * 信用等级 1:A 2:B 3:C 4:D
     */
    @ApiModelProperty("信用等级")
    private Integer creditLevel;
    /**
     * 银行类型:如1-建行 2-天府银行
     */
    @ApiModelProperty("银行类型")
    private Integer bankType;
    /**
     * 创建者
     */
    @ApiModelProperty("创建者")
    private String creator;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Timestamp createTime;
    /**
     * 修改者
     */
    @ApiModelProperty("修改者")
    @TableField("updater")
    private String updater;
    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Timestamp updateTime;
    /**
     * 监管所id
     */
    @ApiModelProperty("监管所id")
    private Long supervisionId;
    /**
     * 经营人身份证号
     */
    @ApiModelProperty("经营人身份证号")
    private String idCard;
    /**
     * 名片二维码图片地址
     */
    @ApiModelProperty("名片二维码图片地址")
    private String businessCardUrl;
    /**
     * 支付二维码图片地址
     */
    @ApiModelProperty("支付二维码图片地址")
    private String payCodeUrl;
    /**
     * 商户健康码信息(暂时保留)
     */
    @ApiModelProperty("商户健康码信息(暂时保留)")
    private String merchantHealthCode;

    @ApiModelProperty("商铺面积")
    private Integer storeArea;

    @ApiModelProperty("是否距离学校50m内")
    private Boolean awayFromSchool;

    @ApiModelProperty("推荐人")
    @Length(max = 16, message = "推荐人字数不能超过16")
    private String referrer;

    @ApiModelProperty("推荐人电话")
    @Length(max = 16, message = "推荐人电话字数不能超过16")
    private String referrerPhone;

    @ApiModelProperty("是否加入微企标识")
    private Boolean wechatCompanyFlag;

    @ApiModelProperty("业务员ID")
    private Long bdId;

    @ApiModelProperty("食品安全状态:0未开通,1开通中,2已开通")
    private Integer foodSafetyStatus;
    @ApiModelProperty("银行支付状态:0未开通,1开通中,2已开通")
    private Integer payStatus;

    /**
     * 业务员
     */
    @ApiModelProperty("业务员")
    private String bd;
    /**
     * 业务经理
     */
    @ApiModelProperty("业务经理")
    private String bdm;
    /**
     * 是否是测试商户 0:是 1:否
     */
    @ApiModelProperty("是否是测试商户 0:是 1:否")
    private Integer isTest;
    /**
     * 逻辑删除标记 0-正常 1-已删除
     */
    @ApiModelProperty("逻辑删除标记 0-正常 1-已删除")
    @TableLogic(value = "0", delval = "1")
    private Integer isDelete;

    /**
     * 商户第一笔支付时间
     */
    private String firstPayTime;

    /**
     * 银行名称:非该表字段
     */
    @TableField(exist = false)
    private String bankName;
}
