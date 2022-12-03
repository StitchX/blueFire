package com.fire.dto.cacheDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;


/**
 * 商户信息redis缓存实体类
 * @author DK 2022/3/8 16:07
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RedisMerchantInfo implements Serializable {


    /**
     * 商户id
     */
    private Long merchantId;

    /**
     * 商户名称
     */
    private String merchantName;

    /**
     * 风险等级
     */
    private String riskLevel;

    /**
     * 是否加入食品安全险：0-未加入；1-已加入
     */
    private Integer isInsurance;

    /**
     * 社会信用代码
     */
    private String socialCreditCode;

    /**
     * 法人/负责人姓名
     */
    private String representName;

    /**
     * 联系电话
     */
    private String linkedPhone;

    /**
     * 行业分类
     */
    private String industryClassification;
    private Long industryClassificationId;

    /**
     * 经营地址
     */
    private String businessAddress;

    /**
     * 监管所id
     */
    private Long supervisionId;

    /**
     * 监管所名称
     */
    private String supervisionName;


    /**
     * 营业执照图片url
     */
    private String businessUrl;

    /**
     * 食品许可证图片url
     */
    private String foodLicenseUrl;

    /**
     * 健康码类型：绿码-1；黄码-2；红码-3
     */
    private String healthCodeType;

    /**
     * 银行id：1-天府；2-建行；3-重庆银行；4-农业银行；5-四川农信
     */
    private Integer bankId;

    /**
     * 银行名字
     */
    private String bankName;

    /**
     * 区/县 区域编码
     */
    private String areaId;

    /**
     * 轮播图地址
     */
    private List<String> shufflingUrls;

    /**
     * 商户第一笔支付时间
     */
    private String firstPayTime;

    /**
     * cust_id
     */
    private String bankMerchantNum;

    /**
     * secret_key
     */
    private String secretKey;


    private String addressCode;
}
