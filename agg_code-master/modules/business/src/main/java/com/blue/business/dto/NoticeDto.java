package com.blue.business.dto;

import lombok.Data;

/**
 * @Description: 分页参数
 * @ClassName: PageDto
 * @Author: liuliu
 * @Date: 2022/3/23 16:38
 */
@Data
public class NoticeDto {
    /**
     * 当前页记录条数
     */
    private Long size;

    /**
     * 当前页
     */
    private Long current;

    /**
     * 通知编号
     */
    private Long noticeId;

    /**
     * 是否必读, 0:非必读 1：必读
     */
    private Integer readFlag;

    /**
     * 通知详情编号
     */
    private Long noticeDetailsId;
}
