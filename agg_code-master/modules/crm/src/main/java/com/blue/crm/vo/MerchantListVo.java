package com.blue.crm.vo;

import lombok.Data;

@Data
public class MerchantListVo {
    private Long merchantId;

    private String storeName;

    private String operatorName;

    private String createTime;

    private int bankType;

    private int status;
}
