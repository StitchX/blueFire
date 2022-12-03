package com.fire.admin.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description: 监管所DTO
 * @ClassName: SupervisionBureauDTO
 * @Author: liuliu
 * @Date: 2022/2/9 14:58
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("监管所网络传输对象")
public class SupervisionBureauDTO {

    @ApiModelProperty(value = "监管局编号")
    private Long supervisionId;
    @ApiModelProperty(value = "监管所或者监管局名称")
    private String name;
    private String phone;
    private String addressId;

}
