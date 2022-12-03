package com.fire.admin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @Description: 监管所列表 包含用户名跟登录账号
 * @ClassName: SupervisionBureauVo
 * @Author: liuliu
 * @Date: 2022/1/27 18:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "监管所账号view object")
public class SupervisionBureauVo {

    @ApiModelProperty(value = "监管所编号")
    private Long supervisionId;

    @ApiModelProperty(value = "监管所名称")
    private String supervisionName;

    @ApiModelProperty(value = "监管所电话")
    private String phone;

    @ApiModelProperty(value = "监管所所属地区行政编码")
    private String addressId;

    @ApiModelProperty(value = "监管所所属地区名称")
    private String addressName;

    @ApiModelProperty(value = "监管所详细地址")
    private String addressDesc;

    @ApiModelProperty(value = "监管所是否删除（未删除：有效，删除：无效）")
    private Integer isDelete;

    private Timestamp createTime;

    @ApiModelProperty(value = "用户编号")
    private Long userId;

    @ApiModelProperty(value = "监管所登录账号名称")
    private String username;

    @ApiModelProperty(value = "省份直辖市")
    private String province;

    @ApiModelProperty(value = "城市")
    private String city;

    @ApiModelProperty(value = "区县")
    private String area;

}
