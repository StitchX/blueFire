package com.fire.admin.dto;

import com.fire.admin.entity.NoticeDetailsInfo;
import com.fire.admin.vo.OssUploading;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description:
 * @date: 2022-02-21 22:09
 * @Modified By:
 */
@ApiModel("通知请求参数")
@Data
public class NoticeDto {


    @ApiModelProperty("通知编号(主键)")
    private Long noticeId;

    @ApiModelProperty("创建开始数据")
    private String startTime;

    @ApiModelProperty("创建结束时间")
    private String endTime;

    @ApiModelProperty("当前页码")
    private Integer current;

    @ApiModelProperty("每页显示条数")
    private Integer size;


    @ApiModelProperty("通知标题")
    @Valid
    @NotNull(message = "通知标题不能为空")
    private String noticeTitle;

    @ApiModelProperty("是否必读 0:非必读 1：必读 默认是非必读的，前端控制")
    private Integer readFlag;

    @ApiModelProperty("通知类型 1-政策法规,2-培训学习,3-考试通知,4-开会通知,5-处罚通知,6-信息监管")
    @Valid
    @NotNull(message = "通知类型不能为空")
    private Integer noticeType;

    @ApiModelProperty("通知范围集合 1-全部商家,2-学校周边50米,3-区域、行业选择,4-精准选择")
    @Valid
    @NotNull(message = "通知范围不能为空")
    private Set<Integer> noticeScope;

    @ApiModelProperty("通知内容")
    private String content;

    @ApiModelProperty(value = "发送类型 1、立即发送 2、定时发送 默认是立即发送")
    @Valid
    @NotNull(message = "通知发送类型不能为空")
    private Integer sendType;

    @ApiModelProperty("是否为学校周边50米的商户 true =1 表示是  flase =0 表示否")
    private Boolean awayFromSchool;

    @ApiModelProperty("类别编号（支持多个类别）用于条件存储")
    private Set<Long> categoryIds;

    @ApiModelProperty("类别编号 (用于查询商户)")
    private Set<Long> queryCateGoryIds;

    @ApiModelProperty("前端需要传递到后台得监管所编号")
    private Set<Long> supervisionIds;

    @ApiModelProperty("商户编号集合，通知范围为4 精准匹配的时候使用")
    private Set<Long> merchantIds;

    @ApiModelProperty("附件")
    private List<OssUploading> adjunctFile;

    @ApiModelProperty("通知发送时间")
    private String sendTime;



    @Data
    @Builder
    public static class NoticeCondition {

        @ApiModelProperty("商户编号集合")
        private Set<Long> merchantIds;

        @ApiModelProperty("通知范围 1-全部商家,2-学校周边50米,3-区域、行业选择,4-精准选择")
        private Set<Integer> noticeScope;

        @ApiModelProperty(value = "监管所编号")
        private Set<Long> supervisionIds;

        @ApiModelProperty(value = "行业类别编号（用于查询商户）")
        private Set<Long> queryCategoryId;

        @ApiModelProperty("行业类别编号（用于条件存储）")
        private Set<Long> categoryId;

        @ApiModelProperty(value = "发送类型 1、立即发送 2、定时发送 默认是立即发送")
        private Integer sendType;

        private List<NoticeDetailsInfo> noticeDetailsInfos;
    }
}