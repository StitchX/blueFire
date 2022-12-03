package com.fire.dto.bank;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @author: admin
 * @Description:
 * @date: 2022-01-04 14:39
 */
@ApiModel("重庆银行表实体")
@Data
@Builder
@TableName("app_chongqing_bank")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChongQingBank {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("本平台商户id")
    private Long mchId;

    @ApiModelProperty("重庆银行商户id")
    private String chongQingMchId;

    @ApiModelProperty("重庆银行的商户秘钥")
    private String chongQingSecret;

    @ApiModelProperty("添加人")
    private String creator;

    @ApiModelProperty("商户添加时间")
    private String createTime;

    @ApiModelProperty("修改人")
    private String updator;

    @ApiModelProperty("修改时间")
    private String updateTime;
}
