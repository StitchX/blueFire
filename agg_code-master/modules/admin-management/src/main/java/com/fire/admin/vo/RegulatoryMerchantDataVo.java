package com.fire.admin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @date: 2022-02-10 13:34
 */
@ApiModel("纳入监管商户分析接口数据对象")
@Data
@Builder
public class RegulatoryMerchantDataVo {

    @ApiModelProperty("商户总数")
    private Integer totalMerchantCount;

    @ApiModelProperty("商户列表")
    private List<EachMerchant> eachMerchants;

    @ApiModel("商户信息数据对象")
    @Data
    public static class EachMerchant extends CommonFieldVo {

        @ApiModelProperty("商户数")
        private Integer merchantCount;
    }
}
