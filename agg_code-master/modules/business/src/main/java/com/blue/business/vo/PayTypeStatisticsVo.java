package com.blue.business.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author fcq
 * @version v2.0.8.business
 * @description 支付类型VO
 * @date 2022/3/24 16:41
 */
@Data
@AllArgsConstructor
public class PayTypeStatisticsVo {

    private String name;

    private Long value;
}
