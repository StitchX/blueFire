package com.fire.admin.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @Description:
 * @date: 2022-02-21 22:41
 */
@ApiModel("通知详情请求参数")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeDetailsDto {

    @ApiModelProperty("通知编号")
    private Long noticeId;

    @ApiModelProperty("店铺名称")
    private String storeName;

    @ApiModelProperty("法人")
    private String representName;


    @ApiModelProperty("当前页码")
    private Integer current;

    @ApiModelProperty("每页显示条数")
    private Integer size;

    @ApiModelProperty("阅读状态, 0:未读 1：已读")
    private Integer readStatus;

    @ApiModelProperty("监管所编号集合（要包括自己）")
    private Set<Long> supervisionIds;

}
