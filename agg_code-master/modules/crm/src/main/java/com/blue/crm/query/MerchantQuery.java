package com.blue.crm.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

/**
 * @author Cohen
 * @version 0.0.1
 * @description MerchantQuery
 * @since 2022/1/24 11:37
 */

@Data
@ApiModel("商户查询参数")
public class MerchantQuery extends PageQuery implements Serializable {
    @ApiModelProperty(value = "注册开始时间", example = "2022-01-01 00:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Timestamp startTime;
    @ApiModelProperty(value = "注册结束时间", example = "2022-01-01 23:59:59")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Timestamp endTime;
    @ApiModelProperty("银行类型")
    private List<Integer> bankType;
    @ApiModelProperty(value = "状态", dataType = "List", notes = "1.已入驻2.已预约3.已开卡4.已使用")
    private List<Integer> status;
    @ApiModelProperty("关键字")
    private String keywords;
    @ApiModelProperty("主键")
    private Long id;
    private List<Long> ids;
}
