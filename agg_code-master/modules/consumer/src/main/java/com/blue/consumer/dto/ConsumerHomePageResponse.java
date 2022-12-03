package com.blue.consumer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author DK 2022/3/8 17:20
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "消费者端首页接口返回数据")
public class ConsumerHomePageResponse {

    private List<String> shufflingUrls;

    private BankInfo bankInfo;

    private HealthInfo healthInfo;

    private MerchantInfo merchantInfo;
}
