package com.blue.business.service;

import com.blue.business.query.StatisticsQuery;
import com.blue.business.vo.ChartStatisticVo;
import com.blue.business.vo.CountStatisticsVo;
import com.blue.business.vo.PayTypeStatisticsVo;

import java.util.List;

/**
 * @author fcq
 * @version v2.0.8.business
 * @description 统计数据
 * @date 2022/3/22 15:28
 */
public interface IStatisticsService {

    /**
     * @description 营业额、交易笔数、扫码量统计
     * @author fcq
     * @date 2022/3/22 23:40
     * @version v2.0.8.business
     */
    CountStatisticsVo count(StatisticsQuery query);

    /**
     * @description 交易类型占比统计
     * @author fcq
     * @date 2022/3/22 15:28
     * @version v2.0.8.business
     */
    List<PayTypeStatisticsVo> type(StatisticsQuery query);

    /**
     * @description 交易金额统计
     * @author fcq
     * @date 2022/3/22 15:28
     * @version v2.0.8.business
     */
    ChartStatisticVo money(StatisticsQuery query);

    /**
     * @description 交易笔数统计
     * @author fcq
     * @date 2022/3/22 15:28
     * @version v2.0.8.business
     */
    ChartStatisticVo frequency(StatisticsQuery query);
}
