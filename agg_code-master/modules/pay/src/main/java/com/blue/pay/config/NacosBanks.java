package com.blue.pay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.annotation.Reference;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "banks", ignoreUnknownFields = false)
@Component
public class NacosBanks {

    @Reference
    private List<BankSet> bankList;

    @Data
    public static class BankSet {
        /**
         * 银行ID
         */
        private int bankId;

        /**
         * 银行名称
         */
        private String bankName;

        /**
         * 银行接口地址
         */
        private String bankUrl;

    }
}
