package com.blue.crm.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class BusinessLicenseVo implements Serializable {

    private String imagesUrl;

    private String storeName;

    private String representName;

    private String socialCreditCode;

    private String businessScope;

    private String detailedAddress;

    private boolean validPermanent;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date validPeriod;

}
