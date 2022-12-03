package com.blue.consumer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author DK 2022/3/10 11:21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("许可证信息查询返回数据")
public class FoodLicenseInfoResponse {

    @ApiModelProperty(value = "许可证url")
    private String foodLicenseUrl;
}
