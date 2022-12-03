package com.fire.admin.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

/**
 * @Description: 通知获取商户的条件
 * @ClassName: NoticemEerchantDto
 * @Author: liuliu
 * @Date: 2022/2/23 18:32
 */
@Data
@Builder
public class NoticemMerchantDto {

    @ApiModelProperty(value = "监管所编号集合")
    private Set<Long> supervisionIds;

    @ApiModelProperty("是否为学校周边50米的商户 true =1 表示是  flase =0 表示否")
    private Boolean awayFromSchool;

    @ApiModelProperty("分页参数")
    private PageDTO pageDTO;

    @ApiModelProperty("通过类别获取子类别")
    private Set<Long> queryCategoryIds;

    @ApiModelProperty(value = "其他查询条件")
    private String otherCondition;

}
