package com.fire.admin.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fire.admin.dto.NoticeDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description:
 * @date: 2022-02-22 22:38
 */
@ApiModel("通知详情数据对象")
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotiDetaVo {

    @ApiModelProperty("通知编号(主键)")
    private Long noticeId;

    @ApiModelProperty("通知标题")
    private String noticeTitle;

    @ApiModelProperty("是否必读 0:非必读 1：必读")
    private Integer readFlag;

    @ApiModelProperty("阅读率")
    private BigDecimal readPercent;

    @ApiModelProperty("通知类型, 1:政策法规 2：培训学习 3：考试通知 4：开会通知 5：处罚通知 6：信息监管")
    private Integer noticeType;

    @ApiModelProperty("签发单位")
    private String issuingAgency;

    @ApiModelProperty(value = "发送类型 1、立即发送 2、定时发送 默认是立即发送")
    private Integer sendType;

    @ApiModelProperty("通知状态 0:待发 1：已发 2：撤销")
    private Integer status;


    @ApiModelProperty("通知内容")
    private String content;

    @ApiModelProperty("通知发送时间")
    private Timestamp sendTime;

    @ApiModelProperty("附件 k: 文件名称 v:oss地址")
    private List<OssUploading> adjunctFile;

    @ApiModelProperty("监管(局)所ID")
    private Long supervisionId;

    @ApiModelProperty("通知范围 1-全部商家,2-学校周边50米,3-区域、行业选择,4-精准选择")
    private Set<Integer> noticeScope;

    @ApiModelProperty(value = "行业类别编号")
    private List<CategoryNotice> categorys;

    @ApiModelProperty(value = "返回选中的监管所")
    private List<NoticeSupervisionBureau> noticeChooseSupervisionBureau;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CategoryNotice {

        @ApiModelProperty("行业类别编号")
        private Long categoryId;

        @ApiModelProperty("行业类别名称")
        private String categoryName;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NoticeSupervisionBureau {
        @ApiModelProperty("监管所编号")
        private Long supervisionId;
        @ApiModelProperty("监管所名称")
        private String supervisionName;
        @ApiModelProperty(value = "监管所是否选中 true ： 选中 false : 未选择")
        private Boolean flag;
    }


}
