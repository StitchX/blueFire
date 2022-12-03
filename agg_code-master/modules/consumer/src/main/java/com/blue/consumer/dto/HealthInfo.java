package com.blue.consumer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author DK 2022/3/8 17:43
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Api("健康码信息")
public class HealthInfo {

    @ApiModelProperty("健康码logo url")
    private String healthCodeUrl;

    @ApiModelProperty("健康码类型：绿码-1；黄码-2；红码-3；0-不显示健康码")
    private String healthCodeType;


}
