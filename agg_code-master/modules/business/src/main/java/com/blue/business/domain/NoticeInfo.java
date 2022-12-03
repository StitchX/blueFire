package com.blue.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author: admin
 * @Description:
 * @date: 2022-02-21 16:08
 */
@ApiModel("通知实体类")
@Data
@TableName("business_notice_info")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoticeInfo {

    @ApiModelProperty("通知编号(主键)")
    @TableId(value = "notice_id", type = IdType.INPUT)
    private Long noticeId;

    @ApiModelProperty("通知标题")
    private String noticeTitle;

    @ApiModelProperty("是否必读 0:非必读 1：必读")
    private Integer readFlag;

    @ApiModelProperty("阅读率")
    private BigDecimal readPercent;

    @ApiModelProperty("通知类型 1:政策法规 2：培训学习 3：考试通知 4：开会通知 5：处罚通知 6：信息监管")
    private Integer noticeType;

    @ApiModelProperty("签发单位")
    private String issuingAgency;

    @ApiModelProperty("通知创建时间")
    private Timestamp createTime;

    @ApiModelProperty("通知创建人")
    private String createAuthor;

    @ApiModelProperty("通知修改时间")
    private Timestamp updateTime;

    @ApiModelProperty("通知修改人")
    private String updateAuthor;

    @ApiModelProperty("通知状态 0:待发 1：已发 2：撤销")
    private Integer status;

    @ApiModelProperty("通知条件")
    private String noticeCondition;

    @ApiModelProperty("通知发送时间")
    private Timestamp sendTime;

    @ApiModelProperty("通知内容")
    private String content;

    @ApiModelProperty("附件")
    private String adjunctFile;

    @ApiModelProperty("监管(局)所ID")
    private Long supervisionId;

    @ApiModelProperty("监管(局)所名称")
    private String supervisionName;

    @ApiModelProperty("是否删除 0-正常 1-删除")
    @TableLogic
    private Integer isDelete;
}
