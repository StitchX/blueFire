package com.fire.api.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: admin
 * @Description:
 * @date: 2022-01-05 10:32
 */
@ApiModel("密钥列表数据对象")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class ChongQingBankVo {

    @ApiModelProperty("商户id")
    private String businessId;

    @ApiModelProperty("商户名称")
    private String businessName;

    @ApiModelProperty("放心码商户id")
    private Long codeId;

    @ApiModelProperty("商户密钥展示16位")
    private String chongQingSecret;

    @ApiModelProperty("上传时间")
    private String createTime;

    @ApiModelProperty("上传人员")
    private String creator;


}
