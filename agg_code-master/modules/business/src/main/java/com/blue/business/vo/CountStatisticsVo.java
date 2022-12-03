package com.blue.business.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author fcq
 * @version v2.0.8.business
 * @description 总数统计
 * @date 2022/3/24 14:18
 */
@Data
@Builder
public class CountStatisticsVo {

    /**
     * 营业额
     */
    private Double turnover;

    /**
     * 营业额变化
     */
    private Double turnoverChange;

    /**
     * 交易笔数
     */
    private Long transaction;

    /**
     * 交易笔数变化
     */
    private Long transactionChange;

    /**
     * 扫码次数
     */
    private Long scan;

    /**
     * 扫码次数变化
     */
    private Long scanChange;
}
