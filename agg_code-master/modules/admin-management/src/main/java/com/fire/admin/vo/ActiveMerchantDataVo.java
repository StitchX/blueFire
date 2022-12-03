package com.fire.admin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description:
 * @date: 2022-02-10 14:34
 */
@ApiModel("三码合一活跃商户接口数据对象")
@Data
public class ActiveMerchantDataVo extends CommonFieldVo {

    @ApiModelProperty("活跃商户总数")
    private Integer activeMerchantCount;

    @ApiModelProperty("商户总数")
    private Integer totalMerchantCount;

    /*@Builder(toBuilder = true)
    public ActiveMerchantDataVo(Long supervisionId, String supervisionName, Integer activeMerchantCount, Integer totalMerchantCount) {
        super(supervisionId, supervisionName);
        this.activeMerchantCount = activeMerchantCount;
        this.totalMerchantCount = totalMerchantCount;
    }*/
}
