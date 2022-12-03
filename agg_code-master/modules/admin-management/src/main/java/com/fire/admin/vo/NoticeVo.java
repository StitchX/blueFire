package com.fire.admin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description:
 * @date: 2022-02-21 21:46
 * @Modified By:
 */
@ApiModel("通知列表数据对象")
@Data
public class NoticeVo {

    @ApiModelProperty("通知列表id")
    private Long noticeId;

    @ApiModelProperty("通知标题")
    private String noticeTitle;

    @ApiModelProperty("是否必读 0:非必读 1：必读")
    private Integer readFlag;

    @ApiModelProperty("阅读率")
    private BigDecimal readPercent;

    @ApiModelProperty("通知类型:1-政策法规,2-培训学习,3-考试通知,4-开会通知,5-处罚通知,6-信息监管")
    private Integer noticeType;

    @ApiModelProperty("签发单位")
    private String issuingAgency;

    @ApiModelProperty("通知创建时间")
    private String createTime;

    @ApiModelProperty("0-待发 1-已发 2-撤销")
    private Integer status;

    @ApiModelProperty("发送时间")
    private String sendTime;

}
