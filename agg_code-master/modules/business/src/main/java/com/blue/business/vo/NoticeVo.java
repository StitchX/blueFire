package com.blue.business.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

/**
 * @Description:
 * @ClassName: NoticeVo
 * @Author: liuliu
 * @Date: 2022/3/23 16:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NoticeVo {
    /**
     * 通知编号
     */
    private Long noticeId;

    /**
     * 通知标题
     */
    private String noticeTitle;

    /**
     * 是否必读, 0:非必读 1：必读
     */
    private Integer readFlag;

    /**
     * 通知类型, 1:政策法规 2：培训学习 3：考试通知 4：开会通知 5：处罚通知 6：信息监管
     */
    private Integer noticeType;

    /**
     * 阅读状态, 0:未读 1：已读
     */
    private Integer readStatus;

    /**
     * 商户通知id
     */
    private Long noticeDetailsId;

    /**
     * 通知发送时间
     */
    private Timestamp msgSendTime;

    /**
     * 通知内容
     */
    private String content;

    /**
     * 签发单位
     */
    private String issuingAgency;

    /**
     * 通知创建时间
     */
    private Timestamp createTime;

    /**
     * 附件
     */
    private String file;

    private List<AdjuncFile> adjunctFile;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AdjuncFile {
        /**
         * 文件名称
         */
        private String fileName;

        /**
         * 文件地址
         */
        private String ossFilePath;
    }
}
