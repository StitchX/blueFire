package com.fire.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName("merchant")
@ApiModel("商户")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Merchant {
    /**
     * 商户ID
     * 历史数据ID不变，新数据用新的规则，主键自增
     */
    @TableId(type = IdType.NONE)
    private Long merchantId;
    /**
     * 经营场所
     */
    @ApiModelProperty("经营场所")
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

    @ApiModelProperty("状态:1.已入驻2.已预约3.已开卡4.已使用")
    private Integer status;

    /**
     * 餐饮行业类型存的编码未找到
     * 1：餐饮 2：流通
     */
    @ApiModelProperty("餐饮行业类型存的编码未找到")
    private Integer industryType;
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
    private String storeName;
    /**
     * 经营人姓名
     */
    @ApiModelProperty("经营人姓名")
    private String operatorName;
    /**
     * 经营人手机号
     */
    @ApiModelProperty("经营人手机号")
    private String operatorPhone;
    /**
     * 详细地址
     */
    @ApiModelProperty("详细地址")
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
    /**
     * 业务员
     */
    @ApiModelProperty("业务员")
    private String bd;

    @ApiModelProperty("业务经理编号")
    private Long bdId;

    @ApiModelProperty("商户面积")
    private Integer storeArea;

    /**
     * 业务经理
     */
    @ApiModelProperty("业务经理名称")
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
     * 银行名称:非该表字段
     */
    @TableField(exist = false)
    private String bankName;

    @ApiModelProperty("是否为学校周边50米的商户 1代表TRUE--表示是,0代表FALSE -- 表示否 ")
    private Boolean awayFromSchool;

    @ApiModelProperty("推荐人")
    private String referrer;

    @ApiModelProperty("推荐人电话")
    private String referrerPhone;

    private Boolean wechatCompanyFlag;

    /**
     * 法人姓名
     */
    @TableField(exist = false)
    private String representName;
}
