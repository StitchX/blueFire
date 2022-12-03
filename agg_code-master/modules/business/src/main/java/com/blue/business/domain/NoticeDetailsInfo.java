package com.blue.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author: admin
 * @Description:
 * @date: 2022-02-21 16:22
 */
@ApiModel("通知详情实体类")
@Data
@TableName("business_notice_details_info")
@Builder
public class NoticeDetailsInfo {

    @ApiModelProperty("通知编号")
    private Long noticeId;

    @ApiModelProperty("通知详情编号")
    @TableId(value = "notice_details_id", type = IdType.AUTO)
    private Long noticeDetailsId;

    @ApiModelProperty("监管(局)所ID")
    private Long supervisionId;

    @ApiModelProperty("商户编号")
    private Long merchantId;

    @ApiModelProperty("阅读状态, 0:未读 1：已读")
    private Integer readStatus;

    @ApiModelProperty("消息发送时间")
    private Timestamp msgSendTime;

    @ApiModelProperty("消息阅读时间")
    private Timestamp msgReadTime;

    @ApiModelProperty("消息送达时间")
    private Timestamp msgDeliveryTime;

}
