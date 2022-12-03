package com.fire.dto.constants;

import lombok.Data;

/**
 * @author : [Cohen]
 * @version : [v1.0]
 * @createTime : [2022/2/11 11:29]
 */
@Data
public class RocketTopicConstant {
    /**
     * 商户topic
     */
    public final static String MERCHANT = "MERCHANT";

    /**
     * 微信topic
     */
    public final static String WECHAT = "WECHAT";

    /**
     * 营业执照topic
     */
    public final static String BUSINESS_LICENSE = "BUSINESS_LICENSE";

    /**
     * 其他证件topic
     */
    public final static String OTHER_CERTIFICATES = "OTHER_CERTIFICATES";

    /**
     * 银行topic
     */
    public final static String BANK = "BANK";

    /**
     * 扫码topic
     */
    public final static String SCAN = "SCAN";


    /**
     * 商户二维码生成
     */
    public static final String MAKE_ER_CODE = "ER_CODE";

    public final static String FOOD_BUSINESS_LICENSE = "FOOD_BUSINESS_LICENSE";
    public final static String SUPERVISION_MAN = "SUPERVISION_MAN";

    public final static String ORDER = "ORDER";

    public static final String ER_CODE = "erCode";

    public final static String WX_USER_INFO = "WX_USER_INFO";

    /**
     * 发送通知topic
     */
    public final static String NOTICE = "NOTICE";

    /**
     * 发送通知详情topic
     */
    public final static String NOTICE_DETAIL = "NOTICE_DETAIL";

    /**
     * 通知反馈topic
     */
    public final static String FEED_BACK_NOTICE = "FEED_BACK_NOTICE";

}
