package com.blue.business.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fire.dto.entity.BankAppointment;
import com.fire.dto.entity.BusinessLicense;
import com.fire.dto.entity.FoodBusinessLicense;
import com.fire.dto.entity.Merchant;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.Map;

/**
 * @author : [Cohen]
 * @version : [v1.0]
 * @createTime : [2022/2/22 14:51]
 */
@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class MerchantDetailVo {

    /**
     * 商户ID
     * 历史数据ID不变，新数据用新的规则，主键自增
     */
    private Long merchantId;

    private Integer status;
    /**
     * 经营场所
     */
    @ApiModelProperty("经营场所")
    @Length(max = 128, message = "经营场所字数不能超过128")
    private String businessPlace;

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
     * 监管所id
     */
    @ApiModelProperty("监管所id")
    private Long supervisionId;

    /**
     * 支付二维码图片地址
     */
    @ApiModelProperty("支付二维码图片地址")
    private String payCodeUrl;

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

    private String bankName;

    private BusinessLicense businessLicense;
    private BankAppointment appointmentBank;
    private FoodBusinessLicense foodBusinessLicense;
    private Map<Integer, AddressInfoVo> addressInfoMap;
    private Map<Integer, CategoryInfoVo> categoryInfoMap;
}
