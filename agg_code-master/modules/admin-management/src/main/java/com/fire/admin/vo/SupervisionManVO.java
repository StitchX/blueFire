package com.fire.admin.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author : [Cohen]
 * @version : [v1.0]
 * @createTime : [2022/1/24 15:41]
 */
@Data
@Api("监管列表Vo")
public class SupervisionManVO {
    /**
     * 监管员编号
     */
    @ApiModelProperty("监管员编号")
    private Integer supervisionManId;
    /**
     * 监管员名称
     */
    @ApiModelProperty("监管员名称")
    private String supervisionManName;
    /**
     * 监管员电话
     */
    @ApiModelProperty("监管员电话")
    private String phoneNum;
    /**
     * 监管员创建人
     */
    @ApiModelProperty("监管员创建人")
    private String createAuthor;
    /**
     * 监管员创建时间
     */
    @ApiModelProperty("监管员创建时间")
    private Timestamp createdTime;

    /**
     * 监管所id
     */
    @ApiModelProperty("监管所ID")
    private Long supervisionId;
    /**
     * 监管所名称
     */
    @ApiModelProperty("监管所名称")
    private String supervisionName;

}
