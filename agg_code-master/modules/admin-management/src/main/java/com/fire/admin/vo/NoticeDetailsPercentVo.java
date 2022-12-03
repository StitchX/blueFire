package com.fire.admin.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 阅读率计算
 * @ClassName: NoticeDetailsPercentVo
 * @Author: liuliu
 * @Date: 2022/3/24 16:34
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeDetailsPercentVo {

    /**
     * 数量
     */
    private Integer num;

    /**
     * 通知编号
     */
    private Long noticeId;

    /**
     * 阅读状态 0 ：未读  1：已读
     */
    private Integer readStatus;
}
